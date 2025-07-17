package me.zhengjie.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ZipUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import me.zhengjie.domain.ColumnInfo;
import me.zhengjie.domain.GenConfig;
import me.zhengjie.domain.vo.TableInfo;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.repository.ColumnInfoRepository;
import me.zhengjie.reqresp.GenPreviewResp;
import me.zhengjie.service.DatabaseTableInfoGather;
import me.zhengjie.service.DatabaseTableInfoGatherFactory;
import me.zhengjie.service.GeneratorService;
import me.zhengjie.utils.GenUtil;
import me.zhengjie.utils.PageResult;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Zheng Jie
 * @since 2019-01-02
 */
@ApplicationScoped
@RequiredArgsConstructor
@SuppressWarnings({"unchecked","all"})
public class GeneratorServiceImpl implements GeneratorService {
    private static final Logger log = LoggerFactory.getLogger(GeneratorServiceImpl.class);
    private final String CONFIG_MESSAGE = "请先配置生成器";

    @Inject
    ColumnInfoRepository columnInfoRepository;
    @Inject
    private EntityManager em;
    @ConfigProperty(name = "generator.base-path")
    private String generatorBasePath;

    private DatabaseTableInfoGatherFactory.DbType dbType = DatabaseTableInfoGatherFactory.DbType.MYSQL;

    @Override
    public Object getTables() {
        DatabaseTableInfoGather choose = DatabaseTableInfoGatherFactory.choose(dbType);
        String sqlForTables = choose.getSqlForTables();
        Query query = em.createNativeQuery(sqlForTables);
        return query.getResultList();
    }

    @Override
    public PageResult<TableInfo> getTables(String name, int[] startEnd) {
        DatabaseTableInfoGather choose = DatabaseTableInfoGatherFactory.choose(dbType);
        String sqlForTablesForQuery = choose.getSqlForTablesForQuery();
        Query query = em.createNativeQuery(sqlForTablesForQuery);
        query.setFirstResult(startEnd[0]);
        query.setMaxResults(startEnd[1] - startEnd[0]);
        query.setParameter("table", StringUtils.isNotBlank(name) ? ("%" + name + "%") : "%%");
        List result = query.getResultList();
        List<TableInfo> tableInfos = new ArrayList<>();
        for (Object obj : result) {
            Object[] arr = (Object[]) obj;
            tableInfos.add(new TableInfo(arr[0], arr[1], arr[2], arr[3], ObjectUtil.isNotEmpty(arr[4]) ? arr[4] : "-"));
        }
        String countSql = "select count(1) from (" + sqlForTablesForQuery + ") tmp";
        Query queryCount = em.createNativeQuery(countSql);
        queryCount.setParameter("table", StringUtils.isNotBlank(name) ? ("%" + name + "%") : "%%");
        BigInteger totalElements = (BigInteger) queryCount.getSingleResult();
        return PageUtil.toPage(tableInfos, totalElements.longValue());
    }

    @Override
    public List<ColumnInfo> getColumns(String tableName) {
        List<ColumnInfo> columnInfos = columnInfoRepository.findByTableNameOrderByIdAsc(tableName);
        if (CollectionUtil.isNotEmpty(columnInfos)) {
            return columnInfos;
        } else {
            columnInfos = query(tableName);
            columnInfoRepository.persist(columnInfos);
            return columnInfos;
        }
    }

    @Override
    public List<ColumnInfo> query(String tableName) {
        DatabaseTableInfoGather choose = DatabaseTableInfoGatherFactory.choose(dbType);
        String sqlForColumns = choose.getSqlForColumns(tableName);
        Query query = em.createNativeQuery(sqlForColumns);
        query.setParameter(1, tableName);
        List result = query.getResultList();
        List<ColumnInfo> columnInfos = new ArrayList<>();
        for (Object obj : result) {
            Object[] arr = (Object[]) obj;
            columnInfos.add(
                    new ColumnInfo(
                            tableName,
                            arr[0].toString(),
                            "NO".equals(arr[1]),
                            arr[2].toString(),
                            ObjectUtil.isNotNull(arr[3]) ? arr[3].toString() : null,
                            ObjectUtil.isNotNull(arr[4]) ? arr[4].toString() : null,
                            ObjectUtil.isNotNull(arr[5]) ? arr[5].toString() : null)
            );
        }
        return columnInfos;
    }

    @Override
    public void sync(List<ColumnInfo> columnInfos, List<ColumnInfo> columnInfoList) {
        // 第一种情况，数据库类字段改变或者新增字段
        for (ColumnInfo columnInfo : columnInfoList) {
            // 根据字段名称查找
            List<ColumnInfo> columns = columnInfos.stream().filter(c -> c.getColumnName().equals(columnInfo.getColumnName())).collect(Collectors.toList());
            // 如果能找到，就修改部分可能被字段
            if (CollectionUtil.isNotEmpty(columns)) {
                ColumnInfo column = columns.get(0);
                column.setColumnType(columnInfo.getColumnType());
                column.setExtra(columnInfo.getExtra());
                column.setKeyType(columnInfo.getKeyType());
                if (StringUtils.isBlank(column.getRemark())) {
                    column.setRemark(columnInfo.getRemark());
                }
                columnInfoRepository.save(column);
            } else {
                // 如果找不到，则保存新字段信息
                columnInfoRepository.save(columnInfo);
            }
        }
        // 第二种情况，数据库字段删除了
        for (ColumnInfo columnInfo : columnInfos) {
            // 根据字段名称查找
            List<ColumnInfo> columns = columnInfoList.stream().filter(c -> c.getColumnName().equals(columnInfo.getColumnName())).collect(Collectors.toList());
            // 如果找不到，就代表字段被删除了，则需要删除该字段
            if (CollectionUtil.isEmpty(columns)) {
                columnInfoRepository.delete(columnInfo);
            }
        }
    }

    @Override
    public void save(List<ColumnInfo> columnInfos) {
        columnInfoRepository.persist(columnInfos);
    }

    @Override
    public void generator(GenConfig genConfig, List<ColumnInfo> columns) {
        if (genConfig.getId() == null) {
            throw new BadRequestException(CONFIG_MESSAGE);
        }
        try {
            GenUtil.generatorCode(columns, genConfig, generatorBasePath);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new BadRequestException("生成失败，请手动处理已生成的文件");
        }
    }

    @Override
    public List<GenPreviewResp> preview(GenConfig genConfig, List<ColumnInfo> columns) {
        if (genConfig.getId() == null) {
            throw new BadRequestException(CONFIG_MESSAGE);
        }
        return GenUtil.preview(columns, genConfig);
    }

    @Override
    public File download(GenConfig genConfig, List<ColumnInfo> columns) {
        if (genConfig.getId() == null) {
            throw new BadRequestException(CONFIG_MESSAGE);
        }
        try {
            File file = new File(GenUtil.download(columns, genConfig));
            String zipPath = file.getPath() + ".zip";
            ZipUtil.zip(file.getPath(), zipPath);
            File zipFile = new File(zipPath);
            return zipFile;
        } catch (IOException e) {
            log.error("error when download()", e);
            throw new BadRequestException("打包失败");
        }
    }
}

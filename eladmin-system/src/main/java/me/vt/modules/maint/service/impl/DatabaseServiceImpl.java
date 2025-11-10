package me.vt.modules.maint.service.impl;

import cn.hutool.core.util.IdUtil;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.vt.modules.maint.domain.Database;
import me.vt.modules.maint.repository.DatabaseRepository;
import me.vt.modules.maint.service.client.DatabaseService;
import me.vt.modules.maint.service.dto.DatabaseDto;
import me.vt.modules.maint.service.dto.DatabaseQueryCriteria;
import me.vt.modules.maint.service.mapstruct.DatabaseMapper;
import me.vt.modules.maint.util.SqlUtils;
import me.vt.utils.FileUtil;
import me.vt.utils.PageResult;
import me.vt.utils.ValidationUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
* @author zhanghouying
 * @since 2019-08-24
*/
@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class DatabaseServiceImpl implements DatabaseService {

    @Inject
    DatabaseRepository databaseRepository;
    @Inject
    DatabaseMapper databaseMapper;

    @Override
    public PageResult<DatabaseDto> queryAll(DatabaseQueryCriteria criteria, Page pageable) {
        // fixme        Page<Database> page = databaseRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        // fixme     return PageUtil.toPage(page.map(databaseMapper::toDto));
        return null;
    }

    @Override
    public List<DatabaseDto> queryAll(DatabaseQueryCriteria criteria){
        // fixme      return databaseMapper.toDto(databaseRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
        return null;
    }

    @Override
    public DatabaseDto findById(String id) {
        Database database = databaseRepository.findById(id);
        ValidationUtil.isNull(database.getId(),"Database","id",id);
        return databaseMapper.toDto(database);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void create(Database resources) {
        resources.setId(IdUtil.simpleUUID());
        databaseRepository.persist(resources);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void update(Database resources) {
        Database database = databaseRepository.findById(resources.getId());
        ValidationUtil.isNull(database.getId(),"Database","id",resources.getId());
        database.copy(resources);
        databaseRepository.persist(database);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void delete(Set<String> ids) {
        for (String id : ids) {
            databaseRepository.deleteById(id);
        }
    }

    @Override
    public boolean testConnection(Database resources) {
        try {
            return SqlUtils.testConnection(resources.getJdbcUrl(), resources.getUserName(), resources.getPwd());
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    @Override
    public File download(List<DatabaseDto> queryAll) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (DatabaseDto databaseDto : queryAll) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("数据库名称", databaseDto.getName());
            map.put("数据库连接地址", databaseDto.getJdbcUrl());
            map.put("用户名", databaseDto.getUserName());
            map.put("创建日期", databaseDto.getCreateTime());
            list.add(map);
        }
        return FileUtil.writeToExcel(list);
    }
}

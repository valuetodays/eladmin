package me.zhengjie.service;

import me.zhengjie.domain.ColumnInfo;
import me.zhengjie.domain.GenConfig;
import me.zhengjie.domain.vo.TableInfo;
import me.zhengjie.reqresp.GenPreviewResp;
import me.zhengjie.utils.PageResult;

import java.io.File;
import java.util.List;

/**
 * @author Zheng Jie
 * @since 2019-01-02
 */
public interface GeneratorService {

    /**
     * 查询数据库元数据
     * @param name 表名
     * @param startEnd 分页参数
     * @return /
     */
    PageResult<TableInfo> getTables(String name, int[] startEnd);

    /**
     * 得到数据表的元数据
     * @param name 表名
     * @return /
     */
    List<ColumnInfo> getColumns(String name);

    /**
     * 同步表数据
     * @param columnInfos /
     * @param columnInfoList /
     */
    void sync(List<ColumnInfo> columnInfos, List<ColumnInfo> columnInfoList);

    /**
     * 保持数据
     * @param columnInfos /
     */
    void save(List<ColumnInfo> columnInfos);

    /**
     * 获取所有table
     * @return /
     */
    Object getTables();

    /**
     * 代码生成
     * @param genConfig 配置信息
     * @param columns 字段信息
     */
    void generator(GenConfig genConfig, List<ColumnInfo> columns);

    /**
     * 预览
     * @param genConfig 配置信息
     * @param columns 字段信息
     * @return /
     */
    List<GenPreviewResp> preview(GenConfig genConfig, List<ColumnInfo> columns);

    /**
     * 打包下载
     * @param genConfig 配置信息
     * @param columns 字段信息
     */
    File download(GenConfig genConfig, List<ColumnInfo> columns);

    /**
     * 查询数据库的表字段数据数据
     * @param table /
     * @return /
     */
    List<ColumnInfo> query(String table);
}

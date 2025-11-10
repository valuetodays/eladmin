package me.vt.service.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import me.vt.domain.GenConfig;
import me.vt.repository.GenConfigRepository;
import me.vt.service.client.GenConfigService;

import java.io.File;
import java.util.Objects;

/**
 * @author Zheng Jie
 * @since 2019-01-14
 */
@ApplicationScoped
@SuppressWarnings({"unchecked","all"})
public class GenConfigServiceImpl implements GenConfigService {

    @Inject
    GenConfigRepository genConfigRepository;

    @Override
    public GenConfig find(String tableName) {
        GenConfig genConfig = genConfigRepository.findByTableName(tableName);
        if(genConfig == null){
            return new GenConfig(tableName);
        }
        return genConfig;
    }

    @Override
    @Transactional
    public GenConfig update(GenConfig genConfig) {
        String separator = File.separator;
        String[] paths;
        String symbol = "\\";
        if (symbol.equals(separator)) {
            paths = genConfig.getPath().split("\\\\");
        } else {
            paths = genConfig.getPath().split(File.separator);
        }
        StringBuilder api = new StringBuilder();
        for (String path : paths) {
            api.append(path);
            api.append(separator);
            if ("src".equals(path)) {
                api.append("api");
                break;
            }
        }
        genConfig.setApiPath(api.toString());
        Long id = genConfig.getId();
        GenConfig old;
        if (Objects.isNull(id)) {
            old = new GenConfig();
        } else {
            old = genConfigRepository.findById(id);
        }
        old.setTableName(genConfig.getTableName());
        old.setApiAlias(genConfig.getApiAlias());
        old.setPack(genConfig.getPack());
        old.setModuleName(genConfig.getModuleName());
        old.setPath(genConfig.getPath());
        old.setApiPath(genConfig.getApiPath());
        old.setAuthor(genConfig.getAuthor());
        old.setPrefix(genConfig.getPrefix());
        old.setCover(genConfig.getCover());
        return genConfigRepository.saveOrUpdate(old);
    }
}

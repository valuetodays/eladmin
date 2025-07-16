
package me.zhengjie.service.impl;

import java.io.File;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import me.zhengjie.domain.GenConfig;
import me.zhengjie.repository.GenConfigRepository;
import me.zhengjie.service.GenConfigService;

/**
 * @author Zheng Jie
 * @date 2019-01-14
 */
@ApplicationScoped
@RequiredArgsConstructor
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
    public GenConfig update(String tableName, GenConfig genConfig) {
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
        return genConfigRepository.save(genConfig);
    }
}

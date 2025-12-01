package me.vt.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * sql字段转java
 *
 * @author Zheng Jie
 * @since 2019-01-03
 */
@Slf4j
public class ColUtil {

    /**
     * 转换mysql数据类型为java数据类型
     *
     * @param type 数据库字段类型
     * @return String
     */
    static String cloToJava(String type) {
        Configuration config = getConfig();
        String javaType = config.getString(type, "unknowType");
        log.info("type={} -> javaType={}", type, javaType);
        return javaType;
    }

    /**
     * 获取配置信息
     */
    public static PropertiesConfiguration getConfig() {
        try {
            return new PropertiesConfiguration("gen.properties");
        } catch (ConfigurationException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
}

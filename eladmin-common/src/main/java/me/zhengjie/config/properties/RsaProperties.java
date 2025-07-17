package me.zhengjie.config.properties;

import jakarta.inject.Inject;
import lombok.Data;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * @author Zheng Jie
 * @website https://eladmin.vip
 * @description
 * @since 2020-05-18
 **/
@Data
public class RsaProperties {

    public static String privateKey;

    @Inject
    @ConfigProperty(name = "rsa.private_key")
    public void setPrivateKey(String privateKey) {
        RsaProperties.privateKey = privateKey;
    }
}
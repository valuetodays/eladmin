
package me.zhengjie.config.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author Zheng Jie
 * @website https://eladmin.vip
 * @description
 * @date 2020-05-18
 **/
@Data
@Component
public class RsaProperties {

    public static String privateKey;

    @Value("${rsa.private_key}")
    public void setPrivateKey(String privateKey) {
        RsaProperties.privateKey = privateKey;
    }
}
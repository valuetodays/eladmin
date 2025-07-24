package me.zhengjie.config.properties;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.Data;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * @author Zheng Jie

 * @description
 * @since 2020-05-18
 **/
@Data
@ApplicationScoped
public class RsaProperties {

    @ConfigProperty(name = "rsa.private-key")
    private String privateKey;

}

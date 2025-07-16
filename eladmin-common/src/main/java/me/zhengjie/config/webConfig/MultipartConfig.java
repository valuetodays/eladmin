
package me.zhengjie.config.webConfig;

import java.io.File;
import javax.servlet.MultipartConfigElement;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @date 2018-12-28
 * @author <a href="https://blog.csdn.net/llibin1024530411/article/details/79474953">...</a>
 */
@Configuration
public class MultipartConfig {

    /**
     * 文件上传临时路径
     */
    @Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        String location = System.getProperty("user.home") + "/.eladmin/file/tmp";
        File tmpFile = new File(location);
        if (!tmpFile.exists()) {
            if (!tmpFile.mkdirs()) {
                System.out.println("create was not successful.");
            }
        }
        factory.setLocation(location);
        return factory.createMultipartConfig();
    }
}
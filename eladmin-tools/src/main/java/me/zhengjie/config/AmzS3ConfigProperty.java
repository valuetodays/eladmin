package me.zhengjie.config;

import io.smallrye.config.ConfigMapping;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Amazon S3 配置接口。
 *
 * @author lei.liu
 * @since 2025-07-17
 */
@ConfigMapping(prefix = "amz.s3")
@ApplicationScoped
public interface AmzS3ConfigProperty {
    /**
     * Amazon S3 的区域配置，例如 "us-west-2"。
     * 该区域决定了 S3 存储桶的地理位置。
     */
    String region();

    /**
     * Amazon S3 的端点 URL。
     * 该端点用于访问 S3 服务。
     */
    String endPoint();

    /**
     * Amazon S3 的域名。
     * 该域名用于构建访问 S3 服务的完整 URL。
     */
    String domain();

    /**
     * Amazon S3 的访问密钥 ID，用于身份验证。
     * 与 secretKey 一起使用来授权对 S3 服务的访问。
     */
    String accessKey();

    /**
     * Amazon S3 的秘密访问密钥，用于身份验证。
     * 与 accessKey 一起使用来授权对 S3 服务的访问。
     */
    String secretKey();

    /**
     * 默认的 S3 存储桶名称。
     * 用于存储上传的文件和数据。
     */
    String defaultBucket();

    /**
     * 文件上传后存储的文件夹格式，默认为 "yyyy-MM"。
     */
    default String timeformat() {
        return "yyyy-MM";
    }
}
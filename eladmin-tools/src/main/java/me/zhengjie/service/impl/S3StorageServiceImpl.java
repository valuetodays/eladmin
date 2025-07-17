/*
*  Copyright 2019-2025 Zheng Jie
*
*  Licensed under the Apache License, Version 2.0 (the "License");
*  you may not use this file except in compliance with the License.
*  You may obtain a copy of the License at
*
*  http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software
*  distributed under the License is distributed on an "AS IS" BASIS,
*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*  See the License for the specific language governing permissions and
*  limitations under the License.
 */
package me.zhengjie.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.config.AmzS3ConfigProperty;
import me.zhengjie.domain.S3Storage;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.repository.S3StorageRepository;
import me.zhengjie.service.S3StorageService;
import me.zhengjie.service.dto.S3StorageQueryCriteria;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.PageResult;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.StringUtils;
import org.apache.commons.io.IOUtils;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.BucketAlreadyOwnedByYouException;
import software.amazon.awssdk.services.s3.model.BucketCannedACL;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.waiters.S3Waiter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
* @description 服务实现
* @author Zheng Jie
 * @since 2025-06-25
**/
@Slf4j
@ApplicationScoped
public class S3StorageServiceImpl implements S3StorageService {

    @Inject
    S3Client s3Client;
    @Inject
    AmzS3ConfigProperty amzS3ConfigProperty;
    @Inject
    S3StorageRepository s3StorageRepository;

    @Override
    public S3Storage getById(Long id) {
        return s3StorageRepository.findById(id);
    }

    @Override
    public PageResult<S3Storage> queryAll(S3StorageQueryCriteria criteria, Page pageable) {
        // fixme:     Page<S3Storage> page = s3StorageRepository.findAll((root, criteriaQuery, criteriaBuilder)
        // fixme:                -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        PanacheQuery<S3Storage> paged = s3StorageRepository.findAll().page(pageable);
        return PageUtil.toPage(paged);
    }

    @Override
    public List<S3Storage> queryAll(S3StorageQueryCriteria criteria){
//     fixme:    return s3StorageRepository.findAll((root, criteriaQuery, criteriaBuilder)
//   fixme:              -> QueryHelp.getPredicate(root,criteria,criteriaBuilder));
        return s3StorageRepository.findAll().list();
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void deleteAll(List<Long> ids) {
        // 检查桶是否存在
        String bucketName = amzS3ConfigProperty.defaultBucket();
        if (!bucketExists(bucketName)) {
            throw new BadRequestException("存储桶不存在，请检查配置或权限。");
        }
        // 遍历 ID 列表，删除对应的文件和数据库记录
        for (Long id : ids) {
            String filePath = s3StorageRepository.selectFilePathById(id);
            if (filePath == null) {
                System.err.println("未找到 ID 为 " + id + " 的文件记录，无法删除。");
                continue;
            }
            try {
                // 创建 DeleteObjectRequest，指定存储桶和文件键
                DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                        .bucket(bucketName)
                        .key(filePath)
                        .build();
                // 调用 deleteObject 方法
                s3Client.deleteObject(deleteObjectRequest);
                // 删除数据库数据
                s3StorageRepository.deleteById(id);
            } catch (S3Exception e) {
                // 处理 AWS 特定的异常
                log.error("从 S3 删除文件时出错: {}", e.awsErrorDetails().errorMessage(), e);
            }
        }
    }

    @Override
    public S3Storage upload(File file) {
        String bucketName = amzS3ConfigProperty.defaultBucket();
        // 检查存储桶是否存在
        if (!bucketExists(bucketName)) {
            log.warn("存储桶 {} 不存在，尝试创建...", bucketName);
            if (createBucket(bucketName)){
                log.info("存储桶 {} 创建成功。", bucketName);
            } else {
                throw new BadRequestException("存储桶创建失败，请检查配置或权限。");
            }
        }
        // fixme:
        String originalName = "fixme:";
        // 获取文件名
//        String originalName = file.getOriginalFilename();
        if (StringUtils.isBlank(originalName)) {
            throw new IllegalArgumentException("文件名不能为空");
        }
        // 生成存储路径和文件名
        String folder = DateUtil.format(new Date(), amzS3ConfigProperty.timeformat());
        String fileName = IdUtil.simpleUUID() + "." + FileUtil.getExtensionName(originalName);
        String filePath = folder + "/" + fileName;
        // 构建上传请求
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(amzS3ConfigProperty.defaultBucket())
                .key(filePath)
                .build();
        // 创建 S3Storage 实例
        S3Storage s3Storage = new S3Storage();
        try {
            // 上传文件到 S3
            s3Client.putObject(putObjectRequest, RequestBody.fromFile(file));
            // 设置 S3Storage 属性
            s3Storage.setFileMimeType(FileUtil.getMimeType(originalName));
            s3Storage.setFileName(originalName);
            s3Storage.setFileRealName(fileName);
            s3Storage.setFileSize(FileUtil.getSize(file.length()));
            s3Storage.setFileType(FileUtil.getExtensionName(originalName));
            s3Storage.setFilePath(filePath);
            // 保存入库
            s3StorageRepository.save(s3Storage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // 设置地址
        return s3Storage;
    }

    @Override
    public File download(List<S3Storage> all) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (S3Storage s3Storage : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("文件名称", s3Storage.getFileName());
            map.put("真实存储的名称", s3Storage.getFileRealName());
            map.put("文件大小", s3Storage.getFileSize());
            map.put("文件MIME 类型", s3Storage.getFileMimeType());
            map.put("文件类型", s3Storage.getFileType());
            map.put("文件路径", s3Storage.getFilePath());
            map.put("创建者", s3Storage.getCreateBy());
            map.put("更新者", s3Storage.getUpdateBy());
            map.put("创建日期", s3Storage.getCreateTime());
            map.put("更新时间", s3Storage.getUpdateTime());
            list.add(map);
        }
        return FileUtil.downloadExcel(list);
    }

    public Map<String, String> privateDownload(Long id) {
        S3Storage storage = s3StorageRepository.findById(id);
        if (storage == null) {
            throw new BadRequestException("文件不存在或已被删除");
        }
        // 创建 GetObjectRequest，指定存储桶和文件键
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(amzS3ConfigProperty.defaultBucket())
                .key(storage.getFilePath())
                .build();
        String base64Data;
        // 使用 try-with-resources 确保流能被自动关闭
        // s3Client.getObject() 返回一个 ResponseInputStream，它是一个包含S3对象数据的输入流
        try (ResponseInputStream<GetObjectResponse> s3InputStream = s3Client.getObject(getObjectRequest)) {
            // 使用 IOUtils.toByteArray 将输入流直接转换为字节数组
            byte[] fileBytes = IOUtils.toByteArray(s3InputStream);
            // 使用 Java 内置的 Base64 编码器将字节数组转换为 Base64 字符串
            base64Data = Base64.getEncoder().encodeToString(fileBytes);
        } catch (S3Exception e) {
            // 处理 AWS 特定的异常
            throw new BadRequestException("从 S3 下载文件时出错: " + e.awsErrorDetails().errorMessage());
        } catch (IOException e) {
            // 处理通用的 IO 异常 (IOUtils.toByteArray 可能会抛出)
            throw new BadRequestException("读取 S3 输入流时出错: " + e.getMessage());
        }
        // 构造返回数据
        Map<String, String> responseData = new HashMap<>();
        // 文件名
        responseData.put("fileName", storage.getFileName());
        // 文件类型
        responseData.put("fileMimeType", storage.getFileMimeType());
        // 文件内容
        responseData.put("base64Data", base64Data);
        return responseData;
    }

    /**
     * 检查云存储桶是否存在
     * @param bucketName 存储桶名称
     */
    @SuppressWarnings({"all"})
    private boolean bucketExists(String bucketName) {
        try {
            HeadBucketRequest headBucketRequest = HeadBucketRequest.builder()
                    .bucket(bucketName)
                    .build();
            s3Client.headBucket(headBucketRequest);
            return true;
        } catch (S3Exception e) {
            // 如果状态码是 404 (Not Found), 说明存储桶不存在
            if (e.statusCode() == 404) {
                log.error("存储桶 '{}' 不存在。", bucketName);
                return false;
            }
            // 其他异常 (如 403 Forbidden) 说明存在问题，但不能断定它不存在
            throw new BadRequestException("检查存储桶时出错: " + e.awsErrorDetails().errorMessage());
        }
    }

    /**
     * 创建云存储桶
     * @param bucketName 存储桶名称
     */
    private boolean createBucket(String bucketName) {
        try {
            // 使用 S3Waiter 等待存储桶创建完成
            S3Waiter s3Waiter = s3Client.waiter();
            CreateBucketRequest bucketRequest = CreateBucketRequest.builder()
                    .bucket(bucketName)
                    .acl(BucketCannedACL.PRIVATE)
                    .build();
            s3Client.createBucket(bucketRequest);
            // 等待直到存储桶创建完成
            HeadBucketRequest bucketRequestWait = HeadBucketRequest.builder()
                    .bucket(bucketName)
                    .build();
            // 使用 WaiterResponse 等待存储桶存在
            WaiterResponse<HeadBucketResponse> waiterResponse = s3Waiter.waitUntilBucketExists(bucketRequestWait);
            waiterResponse.matched().response().ifPresent(response ->
                    log.info("存储桶 '{}' 创建成功，状态: {}", bucketName, response.sdkHttpResponse().statusCode())
            );
        } catch (BucketAlreadyOwnedByYouException e) {
            log.warn("存储桶 '{}' 已经被您拥有，无需重复创建。", bucketName);
        } catch (S3Exception e) {
            throw new BadRequestException("创建存储桶时出错: " + e.awsErrorDetails().errorMessage());
        }
        return true;
    }
}
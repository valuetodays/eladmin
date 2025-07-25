package me.zhengjie;

import cn.vt.auth.AuthUser;
import cn.vt.exception.CommonException;
import cn.vt.util.JsonUtils;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.resteasy.reactive.server.multipart.FileItem;
import org.jboss.resteasy.reactive.server.multipart.FormValue;
import org.jboss.resteasy.reactive.server.multipart.MultipartFormDataInput;
import org.redisson.api.RedissonClient;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * .
 *
 * @author lei.liu
 * @since 2025-07-16
 */
@Slf4j
public abstract class BaseController /*extends BaseCrudController */ {
    private static final String UNKNOWN = "unknown";
    @Context
    HttpHeaders headers;

    protected Response download(File file) {
        //response为HttpServletResponse对象
        String contentType = ("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
//        response.setHeader("Content-Disposition", "attachment;filename=file.xlsx");

        return Response.ok(file, MediaType.valueOf(contentType))
                .header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"")
                .build();
    }

    protected String getFileContentAsString(MultipartFormDataInput dataInput, String fileId, Charset charset) {
        File file = getFileFormItem(dataInput, fileId);
        if (Objects.isNull(file)) {
            return null;
        }
        try {
            return FileUtils.readFileToString(file, charset);
        } catch (IOException e) {
            throw new CommonException(e);
        }
    }

    protected File getFileFormItem(MultipartFormDataInput dataInput, String fileId) {
        Map<String, Collection<FormValue>> values = dataInput.getValues();
        if (MapUtils.isEmpty(values)) {
            return null;
        }
        Collection<FormValue> fileCollection = values.get(fileId);
        FormValue[] fileFormValue = fileCollection.toArray(FormValue[]::new);
        FormValue formValue = fileFormValue[0];
        FileItem fileItem = formValue.getFileItem();
        java.nio.file.Path file = fileItem.getFile();
        return file.toFile();
    }
    @Inject
    RedissonClient redissonClient;

    protected Long getCurrentAccountId() {
        return Long.valueOf(getCurrentAccount().getUserId());
    }

    protected AuthUser getCurrentAccount() {
        String token = headers.getHeaderString("authorization");
        if (StringUtils.isBlank(token)) {
            throw new CommonException("login timeout");
        }
        Object cached = redissonClient.getBucket("login:users:" + token).get();
        if (Objects.isNull(cached)) {
            throw new CommonException("login timeout #2");
        }
        AuthUser authUser = JsonUtils.fromJson(cached.toString(), AuthUser.class);
        return authUser;
    }

    protected void putLoginAccount(AuthUser authUser) {
        redissonClient.getBucket("login:users:" + authUser.getLoginToken()).set(JsonUtils.toJson(authUser), 1, TimeUnit.HOURS);
    }

    /**
     * 获取ip地址
     */
    protected String getIp() {
        String ip = headers.getHeaderString("x-forwarded-for");
        if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = headers.getHeaderString("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = headers.getHeaderString("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
// fixme:            ip = headers.getRemoteAddr();
        }
        String comma = ",";
        String localhost = "127.0.0.1";
        if (ip.contains(comma)) {
            ip = ip.split(",")[0];
        }
        if (localhost.equals(ip)) {
            // 获取本机真正的ip地址
            try {
                ip = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                log.error(e.getMessage(), e);
            }
        }
        return ip;
    }

}

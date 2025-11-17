package me.vt.utils;

import cn.vt.exception.CommonException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;

/**
 * .
 *
 * @author lei.liu
 * @since 2025-11-12
 */
@Slf4j
public class OkhttpUtils {
    private OkhttpUtils() {}

    private static OkHttpClient createHttpClient(String proxyIp, int proxyPort) {


        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (StringUtils.isNotBlank(proxyIp) && proxyPort > 0) {
            // 创建代理
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyIp, proxyPort));
            builder.proxy(proxy);
            //若有鉴权方式：可添加一下配置,反之忽略此步骤
            //            .proxyAuthenticator((route, response) -> {
            //            String credential = Credentials.basic(authKey, password);
            //            return response.request().newBuilder().header("Proxy-Authorization", credential).build();
            //        })
        }
        return builder.build();
    }

    public static String doGet(String url, String proxyIp, int proxyPort) {
        OkHttpClient httpClient = createHttpClient(proxyIp, proxyPort);
        Request.Builder builder = new Request.Builder();
        fillHeaders(builder);
        okhttp3.Request request = builder.url(url).get().build();
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            String json = response.body() != null ? response.body().string() : null;
            log.info("response.body()={}", json);
            return json;
        } catch (IOException e) {
            throw new CommonException(e);
        }
    }

    private static void fillHeaders(Request.Builder builder) {

    }
}

package org.clever.quartz.utils;

import okhttp3.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 封装 OkHttp, Http请求工具类
 * 作者：lizw <br/>
 * 创建时间：2017/6/2 13:53 <br/>
 */
public class HttpClientUtils {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static final OkHttpClient okHttpClient;

    static {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(60, TimeUnit.SECONDS);
        builder.readTimeout(60, TimeUnit.SECONDS);
        builder.writeTimeout(60, TimeUnit.SECONDS);
        builder.retryOnConnectionFailure(true);

        builder.cookieJar(new CookieJar() {
            private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                cookieStore.put(url.host(), cookies);
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                List<Cookie> cookies = cookieStore.get(url.host());
                return cookies != null ? cookies : new ArrayList<>();
            }
        });

        okHttpClient = builder.build();
    }

    /**
     * 获取全局的 OkHttpClient
     */
    public static OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }


}

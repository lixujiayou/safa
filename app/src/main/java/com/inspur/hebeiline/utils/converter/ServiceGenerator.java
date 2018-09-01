package com.inspur.hebeiline.utils.converter;










import com.inspur.hebeiline.core.AllUrl;
import com.inspur.hebeiline.utils.converter.string.StringConverterFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jiang.xu on 2015/11/7.
 */
public class ServiceGenerator {
    public static OkHttpClient sOkHttpClient = new OkHttpClient.Builder().
    connectTimeout(60, TimeUnit.SECONDS).
    readTimeout(60, TimeUnit.SECONDS).
    writeTimeout(60, TimeUnit.SECONDS).build();

    public static Retrofit.Builder mBuilder = new Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())   //加入Rxjava
            .addConverterFactory(GsonConverterFactory.create())
            ;

//    private static Retrofit.Builder mStringBuilder = new Retrofit.Builder()
//            .baseUrl(AllUrl.mainUrl)
//            .addConverterFactory(StringConverterFactory.create());


    public static <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit =mBuilder
                .baseUrl(AllUrl.mainUrl)
                .client(sOkHttpClient)
                .build();
        return retrofit.create(serviceClass);
    }

//    public static <S> S createService2(Class<S> serviceClass) {
//        Retrofit retrofit = mStringBuilder.client(sOkHttpClient).build();
//        return retrofit.create(serviceClass);
//    }

    public static OkHttpClient genericClient() {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Interceptor.Chain chain) throws IOException {
                        Request request = chain.request()
                                .newBuilder()
//                                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                                .addHeader("Content-Type", "application/json;charset=UTF-8")
//                                .addHeader("Accept-Encoding", "gzip, deflate")
//                                .addHeader("Connection", "keep-alive")
//                                .addHeader("Accept", "*/*")
//                                .addHeader("Cookie", "add cookies here")
                                .build();
                        return chain.proceed(request);
                    }

                })
                .build();

        return httpClient;
    }
}

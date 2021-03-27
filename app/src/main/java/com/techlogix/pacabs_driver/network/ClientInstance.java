package com.techlogix.pacabs_driver.network;


import java.security.KeyStore;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.CertificatePinner;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClientInstance {

    private static Retrofit retrofit;
    private static final String BASE_URL = "http://ec2-13-233-196-58.ap-south-1.compute.amazonaws.com:3002/api/v1/";


    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {


            CertificatePinner certificatePinner = new CertificatePinner.Builder()
                    .add(BASE_URL, "sha256/jsLeDvJGTHxNOfo0gVJshskgrVG+go02xJ9d0GNDoLw=")
                    .build();
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
            okHttpClientBuilder.connectTimeout(10, TimeUnit.MINUTES);
            okHttpClientBuilder.readTimeout(10, TimeUnit.MINUTES);
//            okHttpClientBuilder.certificatePinner(certificatePinner);
            okHttpClientBuilder.addInterceptor(interceptor);
            builder.client(okHttpClientBuilder.build());
            retrofit = builder.build();
        }
        return retrofit;
    }

    private static X509TrustManager getTrustManager() throws Exception {
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init((KeyStore) null);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
        }
        X509TrustManager trustManager = (X509TrustManager) trustManagers[0];
// SSLContext sslContext = SSLContext.getInstance("SSL");
// sslContext.init(null, new TrustManager[]{trustManager}, null);
        return trustManager;
    }
}

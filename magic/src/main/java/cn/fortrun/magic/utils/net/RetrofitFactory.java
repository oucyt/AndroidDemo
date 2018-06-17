package cn.fortrun.magic.utils.net;

import android.os.Build;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import cn.fortrun.magic.common.Constants;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * description
 * 获取Retrofit单例
 * 采用静态内部类模式实现
 *
 * @author 87627
 * @create 2018.05.12 16:52
 * @since 1.0.0
 */
public class RetrofitFactory {
    private static final int READ_TIMEOUT = 20 * 1000;//读取超时时间,单位  秒
    private static final int CONN_TIMEOUT = 15 * 1000;//连接超时时间,单位  秒

    private RetrofitFactory() {
    }


    private static class LazyHolder {
        private static final Retrofit INSTANCE = configure();
    }

    public static final Retrofit getInstance() {
        return LazyHolder.INSTANCE;
    }

    /**
     * 配置Retrofit参数
     *
     * @return
     */
    static Retrofit configure() {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder().connectTimeout(30000L, TimeUnit.MILLISECONDS)
                .connectTimeout(60000L, TimeUnit.MILLISECONDS)
                .readTimeout(60000L, TimeUnit.MILLISECONDS)
//                .addNetworkInterceptor(new UserAgentInterceptor("Withyou/" + version + " (android; " + getSystemVersion() + ")"))
                .addInterceptor(new LoggerInterceptor("TAG"));
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            SSLContext sslContext = null;
            try {
                sslContext = SSLContext.getInstance("TLS");
                try {
                    sslContext.init(null, null, null);
                } catch (KeyManagementException e) {
                    // TODO
                    e.printStackTrace();
                }
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            SSLSocketFactory socketFactory = new Tls12SocketFactory(sslContext.getSocketFactory());
            builder.sslSocketFactory(socketFactory, new HttpsUtils.UnSafeTrustManager());
        } else {
            HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
            builder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        }
        OkHttpClient okHttpClient = builder
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        return chain.proceed(chain.request() // originalRequest
                                .newBuilder()
                                .addHeader("Content-Type", "application/json;charset=UTF-8")
                                .build());
                    }
                })
                .build();

        return new Retrofit.Builder()
                .baseUrl(Constants.MAGIC_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

}

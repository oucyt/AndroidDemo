package com.ty.glide.m4;

import android.content.Context;

import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.GenericLoaderFactory;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;

import java.io.InputStream;

import javax.net.ssl.SSLContext;

import okhttp3.OkHttpClient;

/**
 * description
 *
 * @author 87627
 * @create 2018.07.29 17:13
 * @since 1.0.0
 */
public class OkHttpUrlLoader  implements ModelLoader<GlideUrl, InputStream> {

    /**
     * The default factory for {@link OkHttpUrlLoader}s.
     */
    public static class Factory implements ModelLoaderFactory<GlideUrl, InputStream> {
        private static volatile OkHttpClient internalClient;
        private OkHttpClient client;

        private static OkHttpClient getInternalClient() {
            if (internalClient == null) {
                synchronized (Factory.class) {
                    if (internalClient == null) {
                        //让Glide支持HTTPS协议
                        SSLContext sslContext = SSLContextUtil.getDefaultSLLContext();
                        internalClient = new OkHttpClient.Builder()
                                .sslSocketFactory(new NoSSLv3SocketFactory(sslContext.getSocketFactory()), SSLContextUtil.getX509TrustManager())
                                .hostnameVerifier(SSLContextUtil.getHostnameVerifier())
                                .build();
                    }
                }
            }
            return internalClient;
        }

        /**
         * Constructor for a new Factory that runs requests using a static singleton client.
         */
        public Factory() {
            this(getInternalClient());
        }

        /**
         * Constructor for a new Factory that runs requests using given client.
         */
        public Factory(OkHttpClient client) {
            this.client = client;
        }

        @Override
        public ModelLoader<GlideUrl, InputStream> build(Context context, GenericLoaderFactory factories) {
            return new OkHttpUrlLoader(client);
        }

        @Override
        public void teardown() {
            // Do nothing, this instance doesn't own the client.
        }
    }

    private final OkHttpClient client;

    public OkHttpUrlLoader(OkHttpClient client) {
        this.client = client;
    }

    @Override
    public DataFetcher<InputStream> getResourceFetcher(GlideUrl model, int width, int height) {
        return new OkHttpStreamFetcher(client, model);
    }
}
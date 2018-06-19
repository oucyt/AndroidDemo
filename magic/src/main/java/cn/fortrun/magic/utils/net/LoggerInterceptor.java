package cn.fortrun.magic.utils.net;

import android.text.TextUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.fortrun.magic.utils.FileLogUtils;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * Created by zhy on 16/3/1.
 */
public class LoggerInterceptor implements Interceptor {
    public static final String TAG = "OkHttpUtils";
    private static final String DEVIDE = "=========================================";
    private String tag;
    private boolean showResponse;

    public LoggerInterceptor(String tag, boolean showResponse) {
        if (TextUtils.isEmpty(tag)) {
            tag = TAG;
        }
        this.showResponse = showResponse;
        this.tag = tag;
    }

    public LoggerInterceptor(String tag) {
        this(tag, false);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        logForRequest(request);
        Response response = chain.proceed(request);
        return logForResponse(response);
    }

    private Response logForResponse(Response response) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("\n").append(DEVIDE);
            sb.append("\nurl:").append(response.request().url());
            sb.append("\ncode:").append(response.code());
            sb.append("\nprotocol:").append(response.protocol());
            if (!TextUtils.isEmpty(response.message()))
                sb.append("\nmessage:").append(response.message());
            ResponseBody body = response.body();
            if (body != null) {
                sb.append("\ncontentType : " + body.contentType());
                MediaType mediaType = body.contentType();
                if (mediaType != null) {
                    if (isText(mediaType)) {
                        // string()方法不能调用两次，否则抛异常，具体看源码。java.lang.IllegalStateException: closed
                        String content = body.string();
                        sb.append("\ncontent : ").append(content);
                        body = ResponseBody.create(mediaType, content);
                        response = response.newBuilder().body(body).build();
                    } else {
                        sb.append("\ncontent : " + " maybe [file part] , too large too print , ignored!");
                    }
                }
            }
            sb.append("\n").append(DEVIDE);
            FileLogUtils.getInstance().i("http response", sb.toString());


        } catch (Exception e) {
//            e.printStackTrace();
        }
        return response;
    }

    private void logForRequest(Request request) {
        try {
            Headers headers = request.headers();

            StringBuilder sb = new StringBuilder();

            sb.append("\n").append(DEVIDE);
            sb.append("\nmethod : ").append(request.method());
            sb.append("\nurl : ").append(request.url().toString());
            sb.append("\nisHttps: ").append(request.isHttps());
            if (headers != null && headers.size() > 0) {
                sb.append("\nheaders : ").append(headers.toString());
            }
            RequestBody requestBody = request.body();
            if (requestBody != null) {
                MediaType mediaType = requestBody.contentType();
                if (mediaType != null) {
                    sb.append("\nmediaType : ").append(mediaType);
                    if (isText(mediaType)) {
                        sb.append("\ncontent : ").append(bodyToString(request));
                    } else {
                        sb.append("\ncontent : " + " maybe [file part] , too large too print , ignored!");
                    }
                }
            }
            sb.append("\n").append(DEVIDE);
            FileLogUtils.getInstance().i("http request", sb.toString());
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    private boolean isText(MediaType mediaType) {

        if (mediaType.type() != null && mediaType.type().equals("text")) {
            return true;
        }
        List<String> list = new ArrayList<>();
        list.add("json");
        list.add("xml");
        list.add("html");
        list.add("webviewhtml");
        return mediaType.subtype() != null && list.contains(mediaType.subtype());
    }

    private String bodyToString(final Request request) {
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "something error when show requestBody.";
        }
    }
}

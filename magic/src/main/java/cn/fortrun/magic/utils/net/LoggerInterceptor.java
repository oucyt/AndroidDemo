package cn.fortrun.magic.utils.net;

import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import java.io.IOException;

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
            //===>response log

            Response.Builder builder = response.newBuilder();
            Response clone = builder.build();
            StringBuilder sb = new StringBuilder();
            sb.append("\n=========response=========");
            sb.append("\nurl:" + clone.request().url());
            sb.append("\ncode:" + clone.code());
            sb.append("\nprotocol:" + clone.protocol());
            if (!TextUtils.isEmpty(clone.message()))
                sb.append("\nmessage:" + clone.message());
            if (showResponse) {
                ResponseBody body = clone.body();
                if (body != null) {
                    MediaType mediaType = body.contentType();
                    if (mediaType != null) {
                        sb.append("\nresponseBody's contentType : " + mediaType.toString());
                        if (isText(mediaType)) {
                            String resp = body.string();
                            sb.append("\nresponseBody's content : " + resp);

                            body = ResponseBody.create(mediaType, resp);
                            return response.newBuilder().body(body).build();
                        } else {
                            sb.append("\nresponseBody's content : " + " maybe [file part] , too large too print , ignored!");
                        }
                    }
                }
            }
            sb.append("\n=========response end=========");

            FileLogUtils.getInstance().i("返回结果", sb.toString());
            Logger.e(sb.toString());
        } catch (Exception e) {
//            e.printStackTrace();
        }

        return response;
    }

    private void logForRequest(Request request) {
        try {
            String url = request.url().toString();
            Headers headers = request.headers();

            StringBuilder sb = new StringBuilder();

            sb.append("\n========request=======");
            sb.append("\nmethod : " + request.method());
            sb.append("\nurl : " + url);
            if (headers != null && headers.size() > 0) {
                sb.append("\nheaders : " + headers.toString());
            }
            FileLogUtils.getInstance().i("网络请求", sb.toString());
            RequestBody requestBody = request.body();
            if (requestBody != null) {
                MediaType mediaType = requestBody.contentType();
                if (mediaType != null) {
                    sb.append("\nrequestBody's contentType : " + mediaType.toString());
                    if (isText(mediaType)) {
                        sb.append("\nrequestBody's content : " + bodyToString(request));
                    } else {
                        sb.append("\nrequestBody's content : " + " maybe [file part] , too large too print , ignored!");
                    }
                }
            }
            sb.append("\n========request end=======");
            Logger.e(sb.toString());
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    private boolean isText(MediaType mediaType) {
        if (mediaType.type() != null && mediaType.type().equals("text")) {
            return true;
        }
        if (mediaType.subtype() != null) {
            if (mediaType.subtype().equals("json") ||
                    mediaType.subtype().equals("xml") ||
                    mediaType.subtype().equals("html") ||
                    mediaType.subtype().equals("webviewhtml")
                    )
                return true;
        }
        return false;
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

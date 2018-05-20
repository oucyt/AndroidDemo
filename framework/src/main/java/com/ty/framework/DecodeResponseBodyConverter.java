package com.ty.framework;

import com.google.gson.TypeAdapter;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * description
 *
 * @author 87627
 * @create 2018.05.20 15:49
 * @since 1.0.0
 */
public class DecodeResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final TypeAdapter<T> adapter;

    DecodeResponseBodyConverter(TypeAdapter<T> adapter) {
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        //解密字符串
        String temp = new String(value.bytes(), "utf-8");
        String response = value.string();
        com.orhanobut.logger.Logger.i(response);
        return adapter.fromJson(temp);
    }
}

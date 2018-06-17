package com.example.audio;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * description
 * 解析实体的帮助类
 * 解决Response<T>实体解析的问题
 *
 * @author tianyu
 * @create 2018.06.07 下午6:33
 * @since 1.0.0
 */
public class ParameterizedTypeImpl implements ParameterizedType {
    private final Class raw;
    private final Type[] args;

    public ParameterizedTypeImpl(Class raw, Type[] args) {
        this.raw = raw;
        this.args = args != null ? args : new Type[0];
    }

    @Override
    public Type[] getActualTypeArguments() {
        return args;
    }

    @Override
    public Type getRawType() {
        return raw;
    }

    @Override
    public Type getOwnerType() {
        return null;
    }
}
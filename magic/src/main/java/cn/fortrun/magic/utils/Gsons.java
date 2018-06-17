package cn.fortrun.magic.utils;

import com.google.gson.Gson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import cn.fortrun.magic.model.BASEResponse;

/**
 * description
 *
 * @author tianyu
 * @create 2018.06.12 上午9:27
 * @since 1.0.0
 */
public class Gsons {
    /**
     * 底座数据解析
     *
     * @param json
     * @param outer
     * @param inner
     * @param <T>
     * @return
     */
    public static <T> BASEResponse<T> fromJson(String json, Class outer, Class<T> inner) {
        Type type = new ParameterizedTypeImpl(outer, new Class[]{inner});
        return new Gson().fromJson(json, type);
    }

    private static class ParameterizedTypeImpl implements ParameterizedType {
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
}

package cn.fortrun.magic.interfaces;

/**
 * description
 *
 * @author 87627
 * @create 2018.05.20 11:24
 * @since 1.0.0
 */
public interface CallBack3 {
    /**
     * 网络请求成功
     *
     * @param code
     * @param msg
     * @param data
     */
    void onSuccess(String code, String msg, Object data);

    /**
     * 网络请求失败
     */
    void onFailed(Throwable e);
}
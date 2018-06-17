package cn.fortrun.magic.interfaces;

/**
 * description
 *
 * @author 87627
 * @create 2018.05.20 11:24
 * @since 1.0.0
 */
public interface CallBack4 {
    /**
     *
     */
    void onSuccess(Object obj);

    /**
     * 网络请求失败
     */
    void onFailed(Throwable e);
}
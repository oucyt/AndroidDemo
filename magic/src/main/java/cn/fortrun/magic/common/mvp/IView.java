package cn.fortrun.magic.common.mvp;

import android.app.Activity;

/**
 * description
 * mvp架构v层
 *
 * @author 87627
 * @create 2018.05.20 10:15
 * @since 1.0.0
 */
public interface IView {

    /**
     * 某些时候需要调用{@link Activity}的方法
     *
     * @return
     */
     Activity getAc();


    /**
     * 网络请求成功，但是code!=0
     *
     * @param msg
     */
    void onErrorCodeNonZero(String msg);

    /**
     * 网络请求失败，P层完成请求后交给V层做后续处理
     */
    void onFailed(Throwable e);

}

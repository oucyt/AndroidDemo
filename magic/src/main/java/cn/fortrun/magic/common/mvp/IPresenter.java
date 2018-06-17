package cn.fortrun.magic.common.mvp;

/**
 * description
 *
 * @author 87627
 * @create 2018.05.20 10:43
 * @since 1.0.0
 */
public interface IPresenter<V extends IView> {
    /**
     * 实例化Presenter时，需要和对应的V层绑定
     *
     * @param mvpView 目标view
     */
    void attachView(V mvpView);

    /**
     * V层销毁前，解除与V层的绑定，避免内存泄漏
     */
    void detachView();

}

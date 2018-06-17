package cn.fortrun.magic.common.mvp;

import android.text.TextUtils;

/**
 * description
 *
 * @author 87627
 * @create 2018.05.20 10:42
 * @since 1.0.0
 */
public class BasePresenterImpl<V extends IView> implements IPresenter<V> {

    /**
     * P层持有V层的引用
     */
    private V mView;

    /**
     * 实例化Presenter时，需要和对应的V层绑定
     *
     * @param view 目标view
     */

    @Override
    public void attachView(V view) {
        this.mView = view;
    }

    /**
     * V层销毁前，解除与V层的绑定，避免内存泄漏
     */
    @Override
    public void detachView() {
        mView = null;
    }

    /**
     * 返回目标view
     *
     * @return
     */
    public V getView() {
        return mView;
    }


    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    protected boolean isEmpty(String str) {
        return TextUtils.isEmpty(str);
    }


    /**
     * 网络请求成功，但是code!=0
     *
     * @param msg
     */
    public void onErrorCodeNonZero(String msg) {
        if (!isEmpty(msg)) {
//            Intent intent = new Intent(this, ErrorMsgUIActivity.class);
//            intent.putExtra("errmsg", msg);
//            startActivity(intent);
//            finish();
        }
    }
}

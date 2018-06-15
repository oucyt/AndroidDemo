package cn.fortrun.magic.ui.fragment;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.fortrun.magic.R;
import cn.fortrun.magic.common.mvp.IPresenter;
import cn.fortrun.magic.common.mvp.IView;
import cn.fortrun.magic.utils.NetUtils;

/**
 * description
 *
 * @author tianyu
 * @create 2018.06.14 下午6:49
 * @since 1.0.0
 */
public abstract class BaseFragment<P extends IPresenter<V>, V extends IView> extends BaseSupportFragment implements IView {

    protected P mPresenter;

    Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = (P) getPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @LayoutRes
    protected abstract int getLayoutId();

    protected abstract P getPresenter();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    protected boolean isEmpty(String s) {
        return TextUtils.isEmpty(s);
    }

    @Override
    public void onErrorCodeNonZero(String msg) {
        if (!isEmpty(msg)) {
            start(RequestFailedFragment.newInstance());
        }
    }

    @Override
    public void onFailed(Throwable e) {
        if (NetUtils.isNetworkAvailable(_mActivity)) {
            Toast.makeText(_mActivity, getResources().getString(R.string.request_failed), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(_mActivity, getResources().getString(R.string.isNetWork), Toast.LENGTH_SHORT).show();
        }
    }
}

package cn.fortrun.magic.ui.fragment;

import cn.fortrun.magic.R;
import cn.fortrun.magic.common.mvp.IPresenter;

/**
 * description
 * 卡槽已满
 *
 * @author tianyu
 * @create 2018.06.14 上午11:29
 * @since 1.0.0
 */
public class RequestFailedFragment extends BaseFragment {
    public static RequestFailedFragment newInstance() {
        return new RequestFailedFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_request_failed;
    }

    @Override
    protected IPresenter getPresenter() {
        return null;
    }

    @Override
    public void onFailed(Throwable e) {

    }
}

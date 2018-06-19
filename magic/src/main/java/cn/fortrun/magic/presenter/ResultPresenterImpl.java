package cn.fortrun.magic.presenter;

import cn.fortrun.magic.common.mvp.BasePresenterImpl;
import cn.fortrun.magic.model.IResultModel;
import cn.fortrun.magic.model.impl.ResultModelImpl;
import cn.fortrun.magic.ui.ResultView;

/**
 * 主界面
 */
public class ResultPresenterImpl extends BasePresenterImpl<ResultView> {

    private String TAG = ResultPresenterImpl.class.getName();
    private final IResultModel mIResultModel;

    public ResultPresenterImpl() {
        mIResultModel = new ResultModelImpl();
    }


}

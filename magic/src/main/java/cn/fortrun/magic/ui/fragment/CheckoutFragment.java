package cn.fortrun.magic.ui.fragment;

/**
 * description
 * 卡槽已满
 *
 * @author tianyu
 * @create 2018.06.14 上午11:29
 * @since 1.0.0
 */
public class CheckoutFragment /*extends BaseFragment<CheckoutPresenterImpl, CheckoutView> implements CheckoutView*/ {
    public static CheckoutFragment newInstance() {
        return new CheckoutFragment();
    }

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mPresenter.attachView(this);
//        EventBusActivityScope.getDefault(_mActivity).register(this);
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//
//        EventBusActivityScope.getDefault(_mActivity).unregister(this);
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void getRoomCard(RoomCardBean bean) {
//        if (bean == null)
//            return;
//        mPresenter.getCheckoutInfo(bean);
//    }
//
//
//    @Override
//    protected int getLayoutId() {
//        return R.layout.fragment_checkout;
//    }
//
//    @Override
//    protected CheckoutPresenterImpl getPresenter() {
//        return new CheckoutPresenterImpl();
//    }
}
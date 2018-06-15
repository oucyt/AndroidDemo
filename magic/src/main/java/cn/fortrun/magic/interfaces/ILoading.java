package cn.fortrun.magic.interfaces;

/**
 * description
 * 数据加载页面需要实现这个接口
 *
 * @author 87627
 * @create 2018.05.20 10:59
 * @since 1.0.0
 */
public interface ILoading {
    /**
     * 开始加载
     */
    void loading();

    /**
     * 加载结束
     */
    void loadFinished();
}

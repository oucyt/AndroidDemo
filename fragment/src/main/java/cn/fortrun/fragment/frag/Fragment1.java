package cn.fortrun.fragment.frag;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.fortrun.fragment.R;

/**
 * description
 *
 * @author tianyu
 * @create 2018.06.13 下午4:28
 * @since 1.0.0
 */
public class Fragment1 extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_test1, null);
    }
}

package cn.fortrun.magic.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.fortrun.magic.R;

/**
 * description
 * 卡槽已满
 *
 * @author tianyu
 * @create 2018.06.14 上午11:29
 * @since 1.0.0
 */
public class UpdateFragment extends BaseSupportFragment {
    public static UpdateFragment newInstance() {
        return new UpdateFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_update, container, false);
        return view;
    }
}

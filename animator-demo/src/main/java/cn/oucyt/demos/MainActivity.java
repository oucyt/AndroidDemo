package cn.oucyt.demos;

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.blankj.utilcode.util.LogUtils;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends ListActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        // 当前正处在哪个层级
        String path = intent.getStringExtra("cn.fortrun.animator_demo");
        LogUtils.e(" intent.getStringExtra(\"cn.fortrun.animator_demo\") path:" + path);
        if (path == null) {
            // 顶层
            path = "";
        }
        setListAdapter(new SimpleAdapter(this,
                getData(path),
                android.R.layout.simple_list_item_1,
                new String[]{"title"},
                new int[]{android.R.id.text1})
        );
        getListView().setTextFilterEnabled(true);
    }

    /**
     * 通过 Activity 中 Intent.CATEGORY_SAMPLE_CODE 标识，遍历系统中注册该标识的 Activity
     * 根据路径地址，返回封装之后的结果
     *
     * @param levels 当前所处层级
     * @return
     */
    protected List<Map<String, Object>> getData(String levels) {
        List<Map<String, Object>> result = new ArrayList<>();

        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_SAMPLE_CODE);

        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, 0);
        if (null == list) {
            return result;
        }

        String[] nodeArray;
        String nodeString = levels;

        if ("".equals(levels)) {
            nodeArray = null;
        } else {
            // ["APP","Menu"]
            nodeArray = levels.split("/");
            // APP/Menu/
            nodeString = levels + "/";
        }
        // 用以记录下一层级的列表，
        Map<String, Boolean> entries = new HashMap<>();
        LogUtils.e("层级:" + levels);
        LogUtils.e("层级数组:" + Arrays.toString(nodeArray));
        LogUtils.e("层级路径:" + nodeString);
        for (ResolveInfo info : list) {
            // androidManifest中Activity对应的label
            String label = (String) info.loadLabel(getPackageManager());
            LogUtils.e("label:" + label);
            if (label == null) {
                // 如果未设置label,则获取Activity的全限定路径
                label = info.activityInfo.name;
                LogUtils.e("label:" + label);
            }

            if (nodeString.length() == 0 || label.startsWith(nodeString)) {
                // 列举出当前层级下的所有直接子层级
                String[] labelPathArr = label.split("/");
                // 这个label的下一层级
                String nextLabel = nodeArray == null ? labelPathArr[0] : labelPathArr[nodeArray.length];

                if ((nodeArray != null ? nodeArray.length : 0) == labelPathArr.length - 1) {
                    // 如果路径前缀和当前label的父路径一致，则绑定跳转事件；否则进入子层级
                    addItem(result, nextLabel, activityIntent(
                            info.activityInfo.applicationInfo.packageName,
                            info.activityInfo.name));
                } else {
                    if (entries.get(nextLabel) == null) {
                        // 进入下一层级
                        addItem(result, nextLabel, browseIntent("".equals(levels) ? nextLabel : levels + "/" + nextLabel));
                        entries.put(nextLabel, true);
                    }
                }
            }
        }

        Collections.sort(result, new Comparator<Map<String, Object>>() {

            @Override
            public int compare(Map<String, Object> map1, Map<String, Object> map2) {
                return Collator.getInstance().compare(map1.get("title"), map2.get("title"));
            }
        });

        return result;
    }


    protected Intent activityIntent(String pkg, String componentName) {
        LogUtils.e("pkg:" + pkg);
        LogUtils.e("componentName:" + componentName);
        Intent result = new Intent();
        result.setClassName(pkg, componentName);
        return result;
    }

    protected Intent browseIntent(String path) {
        LogUtils.e("path:" + path);
        Intent result = new Intent();
        result.setClass(this, MainActivity.class);
        result.putExtra("cn.fortrun.animator_demo", path);
        return result;
    }

    protected void addItem(List<Map<String, Object>> data, String name, Intent intent) {
        Map<String, Object> temp = new HashMap<>();
        temp.put("title", name);
        temp.put("intent", intent);
        data.add(temp);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Map<String, Object> map = (Map<String, Object>) l.getItemAtPosition(position);

        Intent intent = new Intent((Intent) map.get("intent"));
        intent.addCategory(Intent.CATEGORY_SAMPLE_CODE);
        startActivity(intent);
    }
}

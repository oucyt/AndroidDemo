package cn.fortrun.animator;

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.orhanobut.logger.Logger;

import java.text.Collator;
import java.util.ArrayList;
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
        String path = intent.getStringExtra("com.example.android.apis.Path");
        if (path == null) {
            path = "";
        }
        Logger.d("Demos onCreate path == " + path);
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
     * @param prefix
     * @return
     */
    protected List<Map<String, Object>> getData(String prefix) {
        List<Map<String, Object>> result = new ArrayList<>();

        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_SAMPLE_CODE);

        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, 0);
        if (null == list) {
            return result;
        }

        String[] prefixPath;
        String prefixWithSlash = prefix;

        if ("".equals(prefix)) {
            prefixPath = null;
        } else {
            prefixPath = prefix.split("/");
            prefixWithSlash = prefix + "/";
        }
        Map<String, Boolean> entries = new HashMap<>();

        for (ResolveInfo info : list) {
            // androidManifest设置的label
            String label = (String) info.loadLabel(getPackageManager());
            if (label == null) {
                // Activity的全限定路径
                label = info.activityInfo.name;
            }

            if (prefixWithSlash.length() == 0 || label.startsWith(prefixWithSlash)) {

                String[] labelPath = label.split("/");

                String nextLabel = prefixPath == null ? labelPath[0] : labelPath[prefixPath.length];

                if ((prefixPath != null ? prefixPath.length : 0) == labelPath.length - 1) {
                    addItem(result, nextLabel, activityIntent(
                            info.activityInfo.applicationInfo.packageName,
                            info.activityInfo.name));
                } else {
                    if (entries.get(nextLabel) == null) {
                        addItem(result, nextLabel, browseIntent("".equals(prefix) ? nextLabel : prefix + "/" + nextLabel));
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
        Intent result = new Intent();
        result.setClassName(pkg, componentName);
        return result;
    }

    protected Intent browseIntent(String path) {
        Intent result = new Intent();
        result.setClass(this, MainActivity.class);
        result.putExtra("com.example.android.apis.Path", path);
        return result;
    }

    protected void addItem(List<Map<String, Object>> data, String name, Intent intent) {
        Map<String, Object> temp = new HashMap<>();
        Logger.d("name=%s,intent=%s", name, intent);
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

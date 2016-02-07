package com.yalantis.phoenix.sample;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Oleksii Shliama.
 */
public class BaseRefreshFragment extends Fragment {

    public static final int REFRESH_DELAY = 2000;

    public static final String KEY_ICON = "icon";
    public static final String KEY_COLOR = "color";
    public static final String KEY_ID = "id";

    protected List<Map<String, Integer>> mSampleList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSampleList = new ArrayList<>();
        createSampleList(mSampleList);
    }

    protected void createSampleList(List<Map<String, Integer>> list) {
        Map<String, Integer> map;
        int[] icons = {
                R.drawable.icon_1,
                R.drawable.icon_2,
                R.drawable.icon_3};

        int[] colors = {
                R.color.saffron,
                R.color.eggplant,
                R.color.sienna};

        list.clear();

        for (int i = 0; i < icons.length; i++) {
            map = new HashMap<>();
            map.put(KEY_ID, i);
            map.put(KEY_ICON, icons[i]);
            map.put(KEY_COLOR, colors[i]);
            list.add(map);
        }
    }
}

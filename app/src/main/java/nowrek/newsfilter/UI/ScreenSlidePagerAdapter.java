package nowrek.newsfilter.UI;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> pageFragments = new ArrayList<>();
    private Map<String, Integer> fragmentPositionTagMap = new HashMap<>();
    private Map<Integer, Integer> fragmentPositionTextViewIdMap = new HashMap<>();
    private FragmentManager _fm;

    public ScreenSlidePagerAdapter(FragmentManager fm) {
        super(fm);
        _fm = fm;
        pageFragments.add(0, new SettingsFragment());
    }

    @Override
    public Fragment getItem(int position) {
        return pageFragments.get(position);
    }

    @Override
    public int getCount() {
        return pageFragments.size();
    }

    public void addPage(String fragmentTag, String pageText) {
        int generatedId = View.generateViewId();
        BasicFragment fragment = BasicFragment.newInstance(fragmentTag, pageText, generatedId);
        pageFragments.add(fragment);
        fragmentPositionTagMap.put(fragmentTag, pageFragments.indexOf(fragment));
        fragmentPositionTextViewIdMap.put(pageFragments.indexOf(fragment), generatedId);
    }

    public void addOrReplacePage(String fragmentTag, String pageText){
        if(fragmentPositionTagMap.containsKey(fragmentTag)) {
            Integer position = fragmentPositionTagMap.get(fragmentTag);
            Fragment frag = pageFragments.get(position);
            FragmentTransaction ft = _fm.beginTransaction();
            ft.remove(frag);
            ft.commit();
            pageFragments.remove(position.intValue());

        }
        addPage(fragmentTag, pageText);
    }

    public void clearPages() {
        for (int i = 1; i < pageFragments.size(); ++i)
            pageFragments.remove(i);

        fragmentPositionTagMap.clear();
        fragmentPositionTextViewIdMap.clear();
    }
}

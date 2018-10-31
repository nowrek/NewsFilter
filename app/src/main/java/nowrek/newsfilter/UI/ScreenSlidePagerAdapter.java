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

    private void addPage(String fragmentTag) {
        int generatedId = View.generateViewId();
        BasicFragment fragment = BasicFragment.newInstance(fragmentTag, generatedId);
        pageFragments.add(fragment);
        fragmentPositionTagMap.put(fragmentTag, pageFragments.indexOf(fragment));
        fragmentPositionTextViewIdMap.put(pageFragments.indexOf(fragment), generatedId);
    }

    public void addArticle(String fragmentTag, String articleText){
        if(fragmentPositionTagMap.containsKey(fragmentTag)) {
            Integer position = fragmentPositionTagMap.get(fragmentTag);
            BasicFragment frag = (BasicFragment)pageFragments.get(position);
            frag.addArticle(articleText);
            _fm.beginTransaction().detach(frag).attach(frag).commit();
        }
    }

    public void setPages(List<String> tagList){
        for(String tag : tagList){
            addPage(tag);
        }
        notifyDataSetChanged();
    }

    public void clearPages() {
        FragmentTransaction ft = _fm.beginTransaction();
        for (int i = 1; i < pageFragments.size(); ++i){
            ft.detach(pageFragments.get(i));
            pageFragments.remove(i);
        }
        ft.commit();
        fragmentPositionTagMap.clear();
        fragmentPositionTextViewIdMap.clear();

    }
}

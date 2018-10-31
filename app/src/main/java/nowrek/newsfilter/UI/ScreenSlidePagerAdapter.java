package nowrek.newsfilter.UI;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nowrek.newsfilter.DataStructures.Article;

public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> pageFragments = new ArrayList<>();
    private Map<String, Integer> fragmentPositionTagMap = new HashMap<>();

    public ScreenSlidePagerAdapter(FragmentManager fm) {
        super(fm);
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
        BasicFragment fragment = BasicFragment.newInstance(fragmentTag, generatedId, new ArrayList<Article>());
        pageFragments.add(fragment);
        fragmentPositionTagMap.put(fragmentTag, pageFragments.indexOf(fragment));
    }

    public void addArticle(Article article){
        if(fragmentPositionTagMap.containsKey(article.getArticleOrigin().getUrl())) {
            Integer position = fragmentPositionTagMap.get(article.getArticleOrigin().getUrl());
            BasicFragment frag = (BasicFragment)pageFragments.get(position);
            frag.addArticle(article);
        }
    }

    public void setPages(List<String> tagList){
        for(String tag : tagList){
            if(!fragmentPositionTagMap.keySet().contains(tag)) {
                addPage(tag);
            }
        }
        notifyDataSetChanged();
    }
}

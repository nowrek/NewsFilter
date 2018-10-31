package nowrek.newsfilter.UI;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import nowrek.newsfilter.DataStructures.Article;
import nowrek.newsfilter.R;

public class BasicFragment extends Fragment {
    private String pageTag;
    private ArrayList<Article> articlesList;
    private int pageTextViewId;
    private ListView listView;
    private ArticleListAdapter adapter;

    public static BasicFragment newInstance(String pageTag, int pageTextViewId, ArrayList<Article> articlesList) {
        BasicFragment f = new BasicFragment();
        Bundle b = new Bundle();
        b.putString("pageTag", pageTag);
        b.putInt("pageTextViewId", pageTextViewId);
        b.putParcelableArrayList("articles", articlesList);
        f.setArguments(b);
        return f;
    }

    public void addArticle(Article article){
        if(!articlesList.contains(article)){
            articlesList.add(article);
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        articlesList = getArguments().getParcelableArrayList("articles");
        pageTag = getArguments().getString("pageTag");
        pageTextViewId = getArguments().getInt("pageTextViewId");
        adapter = new ArticleListAdapter(getActivity(),articlesList);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_basic_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = view.findViewById(R.id.article_list_view);
        listView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}

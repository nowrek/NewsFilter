package nowrek.newsfilter.UI;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.LinkedList;

import nowrek.newsfilter.R;

public class BasicFragment extends Fragment {
    private String pageTag;
    private LinkedList<String> articles = new LinkedList<>();
    private int pageTextViewId;

    public static BasicFragment newInstance(String pageTag, int pageTextViewId) {
        BasicFragment f = new BasicFragment();
        Bundle b = new Bundle();
        b.putString("pageTag", pageTag);
        b.putInt("pageTextViewId", pageTextViewId);
        f.setArguments(b);
        return f;
    }

    public void addArticle(String content){
        articles.add(content);
        articles.add("\n------------------------------\n");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.pageTag = getArguments().getString("pageTag");
        this.pageTextViewId = getArguments().getInt("pageTextViewId");
        View rootView = inflater.inflate(R.layout.fragment_basic_layout, container, false);
        TextView textView = rootView.findViewById(R.id.text_view);
        for(String article : articles){
            textView.append(article);
        }
        textView.setMovementMethod(new ScrollingMovementMethod());

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}

package nowrek.newsfilter.UI;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import nowrek.newsfilter.R;

public class BasicFragment extends Fragment implements UpdatableFragments {
    private String pageText;
    private String pageTag;
    private int pageTextViewId;

    public static BasicFragment newInstance(String pageTag, String pageText, int pageTextViewId) {
        BasicFragment f = new BasicFragment();
        Bundle b = new Bundle();
        b.putString("pageTag", pageTag);
        b.putString("pageText", pageText);
        b.putInt("pageTextViewId", pageTextViewId);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.pageTag = getArguments().getString("pageTag");
        this.pageText = getArguments().getString("pageText");
        this.pageTextViewId = getArguments().getInt("pageTextViewId");

        View rootView = inflater.inflate(R.layout.fragment_basic_layout, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.text_view);
        textView.setText(pageText);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void update(String textToUpdate) {
    }
}
package nowrek.newsfilter.UI;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import nowrek.newsfilter.DataStructures.Article;
import nowrek.newsfilter.R;

public class ArticleListAdapter extends ArrayAdapter<Article> {

    public ArticleListAdapter(@NonNull Context context, @NonNull ArrayList<Article> objects) {
        super(context, R.layout.article_layout, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Article article = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.article_layout, parent, false);
            viewHolder.txtTitle = convertView.findViewById(R.id.article_title);
            viewHolder.txtContent = convertView.findViewById(R.id.article_content);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(article != null) {
            viewHolder.txtTitle.setText(article.getTitle());
            viewHolder.txtContent.setText(article.getContent());
        }
        // Return the completed view to render on screen
        return convertView;
    }


    // View lookup cache
    private static class ViewHolder {
        TextView txtTitle;
        TextView txtContent;
    }

}

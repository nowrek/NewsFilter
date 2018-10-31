package nowrek.newsfilter.DataStructures;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class Article implements Parcelable {
    final private URLHandle _articleOrigin;
    final private int _hashCode;
    final private String _title;
    final private String _content;

    private Article(Parcel in){
        String[] array = new String[3];
        in.readStringArray(array);
        _articleOrigin = new URLHandle(array[0]);
        _title = array[1];
        _content = array[2];
        _hashCode = calculateHashCode();
    }

    public Article(URLHandle articleOrigin, String title, String content){
        _articleOrigin = articleOrigin;
        _content = content;
        _title = title;
        _hashCode = calculateHashCode();
    }

    public URLHandle getArticleOrigin(){
        return _articleOrigin;
    }

    public String getTitle() {
        return _title;
    }

    public String getContent() {
        return _content;
    }

    @Override
    public boolean equals(Object o){
        if(Article.class.isInstance(o)){
            Article comparedObject = (Article)o;
            return _title.contentEquals(comparedObject._title) && _content.contentEquals(comparedObject._content);
        }
        return false;
    }

    @Override
    public int hashCode(){
        return _hashCode;
    }

    private int calculateHashCode(){
        return Objects.hash(_content,_title);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        String [] array = {_articleOrigin.getUrl(), _title, _content};
        dest.writeStringArray(array);
    }

    public static final Parcelable.Creator<Article> CREATOR
            = new Parcelable.Creator<Article>() {
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        public Article[] newArray(int size) {
            return new Article[size];
        }
    };
}

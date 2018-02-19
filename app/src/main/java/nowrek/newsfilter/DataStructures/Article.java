package nowrek.newsfilter.DataStructures;

/**
 * Created by jedrzej.nowak on 2018-02-15.
 */

public class Article {
    private URLHandle _articleOrigin;
    private String _content;

    Article(URLHandle inArticleOrigin, String inContent){
        _articleOrigin = inArticleOrigin;
        _content = inContent;
    }

    public String getContent(){
        return _content;
    }

    public URLHandle getArticleOrigin(){
        return _articleOrigin;
    }
}

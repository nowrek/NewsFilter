package nowrek.newsfilter.DataStructures;

public class Article {
    private URLHandle _articleOrigin;
    private String _content;

    public Article(URLHandle articleOrigin, String content){
        _articleOrigin = articleOrigin;
        _content = content;
    }

    public URLHandle getArticleOrigin(){
        return _articleOrigin;
    }

    public String getContent() {
        return _content;
    }
}

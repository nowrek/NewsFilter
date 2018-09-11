package nowrek.newsfilter.DataStructures;

public class Article {
    private URLHandle _articleOrigin;
    private String _content;

    public Article(URLHandle inArticleOrigin, String inContent) {
        _articleOrigin = inArticleOrigin;
        _content = inContent;
    }

    public String getContent() {
        return _content;
    }

    public URLHandle getArticleOrigin() {
        return _articleOrigin;
    }
}

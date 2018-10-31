package nowrek.newsfilter.DataStructures;

import java.util.LinkedList;

public class Page {
    private URLHandle _pageOrigin;
    private LinkedList<Article> _content;

    public Page(URLHandle inPageOrigin, LinkedList<String> inContent) {
        _pageOrigin = inPageOrigin;
        _content = transformStringsToArticles(inContent);
    }

    private String listToString(LinkedList<Article> list) {
       StringBuilder stringBuilder = new StringBuilder();

       for (Article article : list)
           stringBuilder.append(article.getContent());
           stringBuilder.append("\n\n");

       return stringBuilder.toString();
    }

    private LinkedList<Article> transformStringsToArticles(LinkedList<String> inContent){
        LinkedList<Article> result = new LinkedList<>();
        for(String content : inContent){
            result.add(new Article(_pageOrigin, content));
        }
        return result;
    }

    public String getContent() {
        return listToString(_content);
    }

    public LinkedList<Article> getArticleList(){ return _content; }

    public URLHandle getPageOrigin() {
        return _pageOrigin;
    }
}

package nowrek.newsfilter.DataStructures;

import java.util.LinkedList;

public class Page {
    private URLHandle _pageOrigin;
    private LinkedList<String> _content;

    public Page(URLHandle inArticleOrigin, LinkedList<String> inContent) {
        _pageOrigin = inArticleOrigin;
        _content = inContent;
    }

    private String listToString(LinkedList<String> list) {
       StringBuilder stringBuilder = new StringBuilder();

       for (String article : list)
           stringBuilder.append(article + "\n\n");

       return stringBuilder.toString();
    }
    public String getContent() {
        return listToString(_content);
    }

    public URLHandle getPageOrigin() {
        return _pageOrigin;
    }
}

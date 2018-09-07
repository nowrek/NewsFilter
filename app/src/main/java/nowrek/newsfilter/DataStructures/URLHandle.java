package nowrek.newsfilter.DataStructures;

import nowrek.newsfilter.Utils.FilterSet;

public class URLHandle {

    private final String _url;

    public URLHandle(String inUrl) {
        _url = inUrl;
    }

    public FilterSet getFilterSet() {
        // TODO returning functioning filter set for this URL
        return null;
    }

    public String getUrl() {
        return _url;
    }
}

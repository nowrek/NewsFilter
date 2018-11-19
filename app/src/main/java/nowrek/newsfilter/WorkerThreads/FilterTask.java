package nowrek.newsfilter.WorkerThreads;

import android.os.Handler;
import android.util.Log;

import nowrek.newsfilter.DataStructures.Article;

class FilterTask implements Runnable {
    private final Article _article;
    private final Handler _uiHandler;

    FilterTask(Article article, Handler uiHandler) {
        _article = article;
        _uiHandler = uiHandler;
    }

    @Override
    public void run() {
        Log.v(this.getClass().getName(), "RUNNING FILTER TASK FOR: "+_article.getArticleOrigin().getUrl());
        try {
            if (_article.getArticleOrigin().getFilterSet().filterArticle(_article)) {
                _uiHandler.dispatchMessage(_uiHandler.obtainMessage(1, _article));
            }
        } catch (Exception ex) {
            Log.e(this.getClass().getName(), "EXCEPTION IN FILTER TASK FOR: "+_article.getTitle(), ex);
        }
    }
}

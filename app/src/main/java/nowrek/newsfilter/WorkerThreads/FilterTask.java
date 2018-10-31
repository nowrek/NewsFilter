package nowrek.newsfilter.WorkerThreads;

import android.os.Handler;
import android.util.Log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

import nowrek.newsfilter.DataStructures.Article;
import nowrek.newsfilter.DataStructures.Page;
import nowrek.newsfilter.SlidingActivity;

class FilterTask implements Runnable {
    private final Article _article;
    private final Handler _uiHandler;

    FilterTask(Article article, Handler uiHandler) {
        _article = article;
        _uiHandler = uiHandler;
    }

    @Override
    public void run() {
        Log.v("FilterTask", "RUNNING FILTER TASK FOR: "+_article.getArticleOrigin().getUrl());
        try {
            if (_article.getArticleOrigin().getFilterSet().filterArticle(_article)) {
                _uiHandler.dispatchMessage(_uiHandler.obtainMessage(1, _article));
            }
        } catch (Exception ex) {
            Log.e("FilterTask", "EXCEPTION FOR: "+_article.getArticleOrigin().getUrl(), ex);
        }
    }
}

package nowrek.newsfilter.WorkerThreads;

import android.os.Handler;
import android.util.Log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

import nowrek.newsfilter.DataStructures.Page;
import nowrek.newsfilter.SlidingActivity;

class FilterTask implements Runnable {
    private final Page _page;
    private final Handler _uiHandler;

    FilterTask(Page page, Handler uiHandler) {
        _page = page;
        _uiHandler = uiHandler;
    }

    @Override
    public void run() {
        Log.v("FilterTask", "RUNNING FILTER TASK FOR: "+_page.getPageOrigin().getUrl());
        try {
            if (_page.getPageOrigin().getFilterSet().filterArticle(_page)) {
                _uiHandler.dispatchMessage(_uiHandler.obtainMessage(1, _page));
            }
        } catch (Exception ex) {
            //TODO Exception handling
        }
    }
}

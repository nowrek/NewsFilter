package nowrek.newsfilter.WorkerThreads;

import android.os.Handler;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

import nowrek.newsfilter.DataStructures.Page;
import nowrek.newsfilter.SlidingActivity;

class FilterTask implements Callable<Page> {
    private final Page _page;
    private final Handler _uiHandler;

    FilterTask(Page page, Handler inUiHandler) {
        _page = page;
        _uiHandler = inUiHandler;
    }

    @Override
    public Page call() {
        try {
            if (_page.getPageOrigin().getFilterSet().filterArticle(_page)) {
                _uiHandler.dispatchMessage(_uiHandler.obtainMessage(1, _page));
                return _page;
            }
        } catch (Exception ex) {
            //TODO Exception handling
        }
        return null;
    }
}

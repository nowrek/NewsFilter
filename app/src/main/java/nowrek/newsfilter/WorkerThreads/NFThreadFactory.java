package nowrek.newsfilter.WorkerThreads;

import android.os.Handler;

import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import nowrek.newsfilter.DataStructures.Article;
import nowrek.newsfilter.DataStructures.URLHandle;

public class NFThreadFactory extends ThreadPoolExecutor {
    private final Handler _uiHandler;

    public NFThreadFactory(Handler uiHandler, BlockingQueue<Runnable> workQueue, int inThreadNumber) {
        super(inThreadNumber, inThreadNumber, 1000, TimeUnit.MILLISECONDS, workQueue);
        _uiHandler = uiHandler;
    }

    public void executeFilterPageTasks(Collection<Article> articles){
        for(Article art : articles) {
            this.execute(new FilterTask(art, _uiHandler));
        }
    }

    public void executePageDownloadTasks(Collection<URLHandle> urls){
        for(URLHandle url : urls){
            this.execute(new PageDownloadTask(url,this, _uiHandler));
        }
    }
}


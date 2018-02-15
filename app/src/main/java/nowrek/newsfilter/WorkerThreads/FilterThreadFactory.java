package nowrek.newsfilter.WorkerThreads;

import android.os.Handler;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import nowrek.newsfilter.DataStructures.Article;

/**
 * Created by jedrzej.nowak on 2018-02-15.
 */

public class FilterThreadFactory {
    private final BlockingQueue<Article> _inputQueue;
    private final Handler _uiHandler;
    private final ExecutorService _threadPool;
    private final int _threadNumber;

    public FilterThreadFactory(BlockingQueue<Article> inInputQueue, Handler inUiHandler, int inThreadNumber){
        _inputQueue = inInputQueue;
        _uiHandler = inUiHandler;
        _threadPool = Executors.newFixedThreadPool(inThreadNumber);
        _threadNumber = inThreadNumber;
    }

    public void startProcessing() {
        for(int i = 0; i<_threadNumber; i++) {
            _threadPool.submit(new FilterTask(_inputQueue, _uiHandler));
        }
    }
}

class FilterTask implements Runnable{
    private final BlockingQueue<Article> _inputQueue;
    private final Handler _uiHandler;

    FilterTask(BlockingQueue<Article> inInputQueue, Handler inUiHandler) {
        _inputQueue = inInputQueue;
        _uiHandler = inUiHandler;
    }
    @Override
    public void run(){
        boolean interrupted = false;
        while(!interrupted) {
            try {
                Article articleToProcess = _inputQueue.take();
                if (articleToProcess.getArticleOrigin().getFilterSet().filterArticle(articleToProcess)) {
                    _uiHandler.dispatchMessage(_uiHandler.obtainMessage(1, articleToProcess));
                }
            } catch (InterruptedException ex) {
                interrupted = true;
            }
        }
    }
}

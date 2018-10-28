package nowrek.newsfilter.WorkerThreads;

import android.os.Handler;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;

import nowrek.newsfilter.DataStructures.Page;
import nowrek.newsfilter.DataStructures.URLHandle;

public class NFWorkQueue {
    private final Handler _uiHandler;
    private final BlockingQueue<FilterTask> _inputPageQueue;
    private final BlockingQueue<PageDownloadTask> _inputURLQueue;
    private boolean blockedOnPages = false;
    private int _priorityMarker;
    private int _priorityCounter= 0;

    public NFWorkQueue(Handler uiHandler){
        _uiHandler = uiHandler;
        _priorityMarker = 3;
        _inputPageQueue = new LinkedBlockingQueue<>();
        _inputURLQueue = new LinkedBlockingQueue<>();
    }
    public NFWorkQueue(int priorityMarker, Handler uiHandler){
        this(uiHandler);
        _priorityMarker = priorityMarker;
    }

    public NFWorkQueue(Collection<URLHandle> initialURLs, Handler uiHandler){
        this(uiHandler);
        addTasks(initialURLs);
    }

    public NFWorkQueue(Collection<URLHandle> initialURLs, int priorityMarker, Handler uiHandler){
        this(initialURLs, uiHandler);
        _priorityMarker = priorityMarker;

    }

    public void addTask(Page newPage) throws InterruptedException {
        _inputPageQueue.put(new FilterTask(newPage,_uiHandler));
    }
    public void addTask(URLHandle newURL){
        _inputURLQueue.add(new PageDownloadTask(newURL,this));
    }
    public void addTasks(Collection<URLHandle> newURLs){

        for(URLHandle newUrl : newURLs) {
            _inputURLQueue.add(new PageDownloadTask(newUrl, this));
        }
    }

    Callable<Page> getNextTask() throws InterruptedException {
        if(_inputURLQueue.isEmpty() && _inputPageQueue.isEmpty()){
            if(!blockedOnPages) {
                blockedOnPages = true;
                return _inputPageQueue.take();
            } else {
                return _inputURLQueue.take();
            }
        } else if(_inputPageQueue.isEmpty()){
            return _inputURLQueue.take();
        } else if(_inputURLQueue.isEmpty()){
            return _inputPageQueue.take();
        } else {
            return getPrioritizedTask();
        }
    }

    private Callable<Page> getPrioritizedTask ()  throws InterruptedException{
        if (_priorityCounter < _priorityMarker) {
            _priorityCounter++;
            return _inputPageQueue.take();
        } else {
            _priorityCounter = 0;
            return _inputURLQueue.take();
        }
    }
}

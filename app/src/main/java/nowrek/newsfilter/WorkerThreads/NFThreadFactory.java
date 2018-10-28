package nowrek.newsfilter.WorkerThreads;

import android.os.Handler;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import nowrek.newsfilter.DataStructures.Page;
import nowrek.newsfilter.DataStructures.URLHandle;

public class NFThreadFactory {
    private final NFWorkQueue _workQueue;
    private final ExecutorService _threadPool;
    private boolean started = false;
    private final int _threadNumber;

    public NFThreadFactory(NFWorkQueue workQueue, int inThreadNumber) {
        _workQueue = workQueue;
        _threadPool = Executors.newFixedThreadPool(inThreadNumber);
        _threadNumber = inThreadNumber;
    }

    public void startProcessing() {
        if(!started) {
            started = true;
            for (int i = 0; i < _threadNumber; i++) {
                _threadPool.submit(new PoolWorker());
            }
        }
    }

    public void stopProcessing(){
        _threadPool.shutdown();
        started = false;
    }

    private class PoolWorker extends Thread {
        public void run() {
            Callable<Page> task;
            boolean interrupted = false;
            while (!interrupted) {
                try {
                    task = _workQueue.getNextTask();
                    if(task!=null)task.call();
                } catch (Exception iex) {
                    if(InterruptedException.class.isInstance(iex))interrupted = true;
                    //TODO Logging and exp[ection handling
                }
            }
        }
    }
}


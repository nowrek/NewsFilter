package nowrek.newsfilter.WorkerThreads;

import android.support.annotation.NonNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class NFWorkQueue implements BlockingQueue<Runnable>{
    private HashMap<TaskType,BlockingQueue<Runnable>> _taskQueues;
    private boolean blockedOnPages = false;
    private int _priorityMarker;
    private int _priorityCounter= 0;

    private enum TaskType{
        FilterTask,
        PageDownloadTask
    }

    public NFWorkQueue(){
        _priorityMarker = 3;
        _taskQueues = new HashMap<>();
        _taskQueues.put(TaskType.FilterTask, new LinkedBlockingQueue<Runnable>());
        _taskQueues.put(TaskType.PageDownloadTask, new LinkedBlockingQueue<Runnable>());
    }

    private BlockingQueue<Runnable> getCurrentQueue(){
        if(_taskQueues.get(TaskType.PageDownloadTask).isEmpty() && _taskQueues.get(TaskType.FilterTask).isEmpty()){
            if(!blockedOnPages) {
                blockedOnPages = true;
                return _taskQueues.get(TaskType.FilterTask);
            } else {
                return _taskQueues.get(TaskType.PageDownloadTask);
            }
        } else if(_taskQueues.get(TaskType.FilterTask).isEmpty()){
            return _taskQueues.get(TaskType.PageDownloadTask);
        } else if(_taskQueues.get(TaskType.PageDownloadTask).isEmpty()){
            return _taskQueues.get(TaskType.FilterTask);
        } else {
            return getPrioritizedQueue();
        }
    }

    private BlockingQueue<Runnable> getPrioritizedQueue(){
        if (_priorityCounter < _priorityMarker) {
            _priorityCounter++;
            return _taskQueues.get(TaskType.FilterTask);
        } else {
            _priorityCounter = 0;
            return _taskQueues.get(TaskType.PageDownloadTask);
        }
    }

    private BlockingQueue<Runnable> getRelevantQueue(Runnable task){
        if(FilterTask.class.isInstance(task)){
            return _taskQueues.get(TaskType.FilterTask);
        } else if (PageDownloadTask.class.isInstance(task)){
            return _taskQueues.get(TaskType.PageDownloadTask);
        }
        return null;
    }

    private boolean checkAcceptedTypes(Runnable runnable){
        return FilterTask.class.isInstance(runnable) || PageDownloadTask.class.isInstance(runnable);
    }

    private HashMap<TaskType, LinkedList<Runnable>> sortInputCollection(@NonNull Collection<?> c){
        HashMap<TaskType, LinkedList<Runnable>> retVal = new HashMap<>();
        retVal.put(TaskType.FilterTask, new LinkedList<Runnable>());
        retVal.put(TaskType.PageDownloadTask, new LinkedList<Runnable>());
        for(Object task : c){
            if(FilterTask.class.isInstance(task)){
                retVal.get(TaskType.FilterTask).add((Runnable)task);
            }else if(PageDownloadTask.class.isInstance(task)){
                retVal.get(TaskType.PageDownloadTask).add((Runnable)task);
            }
        }

        return retVal;
    }

    @Override
    public boolean add(Runnable runnable) {
        if(checkAcceptedTypes(runnable)) {
            return getRelevantQueue(runnable).add(runnable);
        }
        return false;
    }

    @Override
    public boolean offer(Runnable runnable) {
        if(checkAcceptedTypes(runnable)) {
            return getRelevantQueue(runnable).offer(runnable);
        }
        return false;
    }

    @Override
    public Runnable remove() {
        return getCurrentQueue().remove();
    }

    @Override
    public Runnable poll() {
        return getCurrentQueue().poll();
    }

    @Override
    public Runnable element() {
        return getCurrentQueue().element();
    }

    @Override
    public Runnable peek() {
        return getCurrentQueue().peek();
    }

    @Override
    public void put(@NonNull Runnable runnable) throws InterruptedException {
        if(checkAcceptedTypes(runnable)) {
            getRelevantQueue(runnable).put(runnable);
        }
    }

    @Override
    public boolean offer(Runnable runnable, long timeout, @NonNull TimeUnit unit) throws InterruptedException {
        if(checkAcceptedTypes(runnable)) {
            return getRelevantQueue(runnable).offer(runnable, timeout, unit);
        }
        return false;
    }

    @NonNull
    @Override
    public Runnable take() throws InterruptedException {
        return getPrioritizedQueue().take();
    }

    @Override
    public Runnable poll(long timeout, @NonNull TimeUnit unit) throws InterruptedException {
        return getPrioritizedQueue().poll(timeout, unit);
    }

    @Override
    public int remainingCapacity() {
        return Math.min(_taskQueues.get(TaskType.FilterTask).remainingCapacity(),_taskQueues.get(TaskType.PageDownloadTask).remainingCapacity());
    }

    @Override
    public boolean remove(Object o) {
        return _taskQueues.get(TaskType.FilterTask).remove(o) || _taskQueues.get(TaskType.PageDownloadTask).remove(o);
    }

    @Override
    public boolean containsAll(@NonNull Collection<?> c) {
        for(Object task : c){
            if(!Runnable.class.isInstance(task)) return false;
            if(!this.contains(task)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(@NonNull Collection<? extends Runnable> c) {
        HashMap<TaskType, LinkedList<Runnable>> sortedInput = sortInputCollection(c);
        boolean changed = false;
        for(TaskType key :TaskType.values()){
            changed |= _taskQueues.get(key).addAll(sortedInput.get(key));
        }
        return changed;
    }

    @Override
    public boolean removeAll(@NonNull Collection<?> c) {
        HashMap<TaskType, LinkedList<Runnable>> sortedInput = sortInputCollection(c);
        boolean changed = false;
        for(TaskType key :TaskType.values()){
            changed |= _taskQueues.get(key).removeAll(sortedInput.get(key));
        }
        return changed;
    }

    @Override
    public boolean retainAll(@NonNull Collection<?> c) {
        HashMap<TaskType, LinkedList<Runnable>> sortedInput = sortInputCollection(c);
        boolean changed = false;
        for(TaskType key :TaskType.values()){
            changed |= _taskQueues.get(key).retainAll(sortedInput.get(key));
        }
        return changed;
    }

    @Override
    public void clear() {
        for(TaskType key :TaskType.values()){
            _taskQueues.get(key).clear();
        }
    }

    @Override
    public int size() {
        int size = 0;
        for(TaskType key :TaskType.values()){
            size += _taskQueues.get(key).size();
        }
        return size;
    }

    @Override
    public boolean isEmpty() {
        boolean empty = true;
        for(TaskType key :TaskType.values()){
            empty &= _taskQueues.get(key).isEmpty();
        }

        return empty;
    }

    @Override
    public boolean contains(Object o) {
        boolean contains = false;
        for(TaskType key :TaskType.values()){
            contains |= _taskQueues.get(key).contains(o);
        }

        return contains;
    }

    @NonNull
    @Override
    public Iterator<Runnable> iterator() {
        return null;
    }

    @NonNull
    @Override
    public Object[] toArray() {
        LinkedList<Object> sum = new LinkedList<>();
        for(TaskType key :TaskType.values()){
            sum.addAll(_taskQueues.get(key));
        }
        return sum.toArray();
    }

    @NonNull
    @Override
    public <T> T[] toArray(@NonNull T[] a) {
        LinkedList<T> sum = new LinkedList<>();
        for(TaskType key :TaskType.values()){
            sum.addAll(Arrays.asList(_taskQueues.get(key).toArray(a)));
        }
        return sum.toArray(a);
    }

    @Override
    public int drainTo(@NonNull Collection<? super Runnable> c) {
        int drained = 0;
        for(TaskType key :TaskType.values()){
            drained += _taskQueues.get(key).drainTo(c);
        }
        return drained;
    }

    @Override
    public int drainTo(@NonNull Collection<? super Runnable> c, int maxElements) {
        int drained = 0;
        for(TaskType key :TaskType.values()){
            drained += _taskQueues.get(key).drainTo(c, maxElements-drained);
            if(drained == maxElements){
                return drained;
            }
        }
        return drained;
    }
}

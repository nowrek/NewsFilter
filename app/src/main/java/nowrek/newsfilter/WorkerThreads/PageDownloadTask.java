package nowrek.newsfilter.WorkerThreads;

import android.os.Handler;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.LinkedList;
import java.util.concurrent.ThreadPoolExecutor;

import nowrek.newsfilter.DataStructures.Article;
import nowrek.newsfilter.DataStructures.Page;
import nowrek.newsfilter.DataStructures.URLHandle;
import nowrek.newsfilter.Utils.ArticlesExtractor;

public class PageDownloadTask implements Runnable {
    private final URLHandle _urlHandle;
    private final Handler _uiHandler;
    private final ThreadPoolExecutor _tpe;

    PageDownloadTask(URLHandle urlHandle, ThreadPoolExecutor tpe, Handler uiHandler){
        _urlHandle = urlHandle;
        _tpe = tpe;
        _uiHandler = uiHandler;
    }

    private LinkedList<String> getPageArticles(URLHandle inUrlHandle){
        ArticlesExtractor extractor = new ArticlesExtractor(this);

        try {
            Document doc = Jsoup.connect(inUrlHandle.getUrl()).get();
            return extractor.extractArticlesOneLevelDown(doc, inUrlHandle.getUrl());
        } catch (Exception exception) {
            LinkedList<String> exceptionList = new LinkedList<>();
            exceptionList.add(exception.getMessage());
            return exceptionList;
        }
    }

    public void articleFound(String content){
        _tpe.execute(new FilterTask(new Article(_urlHandle, content), _uiHandler));
    }

    @Override
    public void run(){
        Log.v("PageDownloadTask", "RUNNING PAGE DOWNLOAD TASK FOR: "+_urlHandle.getUrl());
        Page page = new Page(_urlHandle, getPageArticles(_urlHandle));
    }
}

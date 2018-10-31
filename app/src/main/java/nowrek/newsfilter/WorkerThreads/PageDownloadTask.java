package nowrek.newsfilter.WorkerThreads;

import android.os.Handler;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.concurrent.ThreadPoolExecutor;

import nowrek.newsfilter.DataStructures.Article;
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

    private void getPageArticles(URLHandle inUrlHandle){
        ArticlesExtractor extractor = new ArticlesExtractor(this);

        try {
            Document doc = Jsoup.connect(inUrlHandle.getUrl()).get();
            extractor.extractArticlesOneLevelDown(doc, inUrlHandle.getUrl());
        } catch (Exception exception) {
            Log.e(this.getClass().getName(), "EXCEPTION IN PAGE DOWNLOAD TASK FOR: "+_urlHandle.getUrl(),exception);
        }
    }

    public void articleFound(String title, String content){
        _tpe.execute(new FilterTask(new Article(_urlHandle, title, content), _uiHandler));
    }

    @Override
    public void run(){
        Log.v(this.getClass().getName(), "RUNNING PAGE DOWNLOAD TASK FOR: "+_urlHandle.getUrl());
        getPageArticles(_urlHandle);
    }
}

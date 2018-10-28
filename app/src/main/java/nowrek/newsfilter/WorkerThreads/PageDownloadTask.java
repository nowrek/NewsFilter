package nowrek.newsfilter.WorkerThreads;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.Callable;

import nowrek.newsfilter.DataStructures.Page;
import nowrek.newsfilter.DataStructures.URLHandle;
import nowrek.newsfilter.Utils.ArticlesExtractor;

public class PageDownloadTask implements Callable<Page> {
    private final URLHandle _urlHandle;
    private final NFWorkQueue _workQueue;

    PageDownloadTask(URLHandle urlHandle, NFWorkQueue inputQueue){
        _urlHandle = urlHandle;
        _workQueue = inputQueue;
    }

    private LinkedList<String> getPageArticles(URLHandle inUrlHandle){
        ArticlesExtractor extractor = new ArticlesExtractor();

        try {
            Document doc = Jsoup.connect(inUrlHandle.getUrl()).get();
            return extractor.extractArticlesOneLevelDown(doc, inUrlHandle.getUrl());
        } catch (Exception exception) {
            LinkedList<String> exceptionList = new LinkedList<>();
            exceptionList.add(exception.getMessage());
            return exceptionList;
        }
    }

    @Override
    public Page call() throws Exception {
        Page result = new Page(_urlHandle, getPageArticles(_urlHandle));
        _workQueue.addTask(result);
        return result;
    }
}

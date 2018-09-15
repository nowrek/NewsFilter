package nowrek.newsfilter.WorkerThreads;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;

import nowrek.newsfilter.DataStructures.Page;
import nowrek.newsfilter.DataStructures.URLHandle;
import nowrek.newsfilter.SlidingActivity;
import nowrek.newsfilter.Utils.ArticlesExtractor;

public class PageDownloadTask extends AsyncTask<LinkedList<URLHandle>, Integer, LinkedList<Page>> {

    private SlidingActivity activity;

    private String getPageHTML(URLHandle inUrlHandle) throws IOException {
        try {
            Document doc = Jsoup.connect(inUrlHandle.getUrl()).get();
            return doc.html();
        } catch (Exception exception) {
            return exception.getMessage();
        }
    }

    private LinkedList<String> getPageArticles(URLHandle inUrlHandle) throws IOException {
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

    public PageDownloadTask(SlidingActivity activity) {
        this.activity = activity;
    }

    @Override
    protected LinkedList<Page> doInBackground(LinkedList<URLHandle>... urlHandles) {
        LinkedList<URLHandle> urlList = urlHandles[0];
        LinkedList<Page> pages = new LinkedList<>();
        for (int i = 0; i < urlList.size(); ++i) {
            try {
                pages.add(new Page(urlList.get(i), getPageArticles(urlList.get(i))));
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        return pages;
    }

    @Override
    protected void onPostExecute(LinkedList<Page> pages) {
        activity.displayArticles(pages);
    }
}

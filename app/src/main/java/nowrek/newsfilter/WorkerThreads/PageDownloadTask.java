package nowrek.newsfilter.WorkerThreads;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.LinkedList;

import nowrek.newsfilter.DataStructures.Article;
import nowrek.newsfilter.DataStructures.URLHandle;
import nowrek.newsfilter.SlidingActivity;
import nowrek.newsfilter.Utils.ResponseReceiver;

public class PageDownloadTask extends AsyncTask<LinkedList<URLHandle>, Integer, LinkedList<Article>> {

    private SlidingActivity activity;

    private String getPageHTML(URLHandle inUrlHandle) throws IOException {
        Document doc = Jsoup.connect(inUrlHandle.getUrl()).get();
        return doc.html();
    }

    public PageDownloadTask(SlidingActivity activity) {
        this.activity = activity;
    }

    @Override
    protected LinkedList<Article> doInBackground(LinkedList<URLHandle>... urlHandles) {
        LinkedList<URLHandle> urlList = urlHandles[0];
        LinkedList<Article> articles = new LinkedList<>();
        for (int i = 0; i < urlList.size(); ++i) {
            try {
                articles.add(new Article(urlList.get(i), getPageHTML(urlList.get(i))));
            } catch (Exception exception) {
                articles.add(new Article(urlList.get(i), exception.getMessage()));
                exception.printStackTrace();
            }
        }

        return articles;
    }

    @Override
    protected void onPostExecute(LinkedList<Article> articles) {
        activity.displayArticles(articles);
    }
}

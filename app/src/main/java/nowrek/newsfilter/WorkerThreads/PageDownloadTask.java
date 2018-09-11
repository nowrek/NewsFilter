package nowrek.newsfilter.WorkerThreads;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.LinkedList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import nowrek.newsfilter.DataStructures.Article;
import nowrek.newsfilter.DataStructures.URLHandle;
import nowrek.newsfilter.SlidingActivity;

public class PageDownloadTask extends AsyncTask<LinkedList<URLHandle>, Integer, LinkedList<Article>> {

    private SlidingActivity activity;

    private String getPageHTML(URLHandle inUrlHandle) throws IOException {
        try {
            Document doc = Jsoup.connect(inUrlHandle.getUrl()).get();
            return doc.html();
        } catch (Exception exception) {
            return exception.getMessage();
        }
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

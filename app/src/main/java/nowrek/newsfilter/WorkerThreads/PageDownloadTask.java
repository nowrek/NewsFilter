package nowrek.newsfilter.WorkerThreads;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.LinkedList;

import nowrek.newsfilter.DataStructures.URLHandle;

public class PageDownloadTask implements Runnable {

    private final LinkedList<URLHandle> _inputList;

    public PageDownloadTask(LinkedList<URLHandle> inInputList) {
        _inputList = inInputList;
    }

    public void run() {
        for (int i = 0; i < _inputList.size(); ++i) {
            try {
                String htmlPage = getPageHTML(_inputList.get(i));
                Log.d("Page", htmlPage);
            } catch (Exception exception){
                exception.printStackTrace();
            }
        }
    }

    private String getPageHTML(URLHandle inUrlHandle) throws IOException {
        Log.d("Page", inUrlHandle.getUrl());
        Document doc = Jsoup.connect(inUrlHandle.getUrl()).get();

        return doc.html();
    }
}

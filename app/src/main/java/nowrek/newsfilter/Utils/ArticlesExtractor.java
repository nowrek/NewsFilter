package nowrek.newsfilter.Utils;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nowrek.newsfilter.WorkerThreads.PageDownloadTask;

public class ArticlesExtractor {

    private PageDownloadTask _pdt;

    public ArticlesExtractor(PageDownloadTask pdt){
        _pdt = pdt;
    }

    private void extractFirstArticle(Document pageDoc) {
        pageDoc = pageDoc.removeAttr("a").ownerDocument();
        pageDoc = pageDoc.removeAttr("figure").ownerDocument();
        pageDoc = pageDoc.removeAttr("div").ownerDocument();
        pageDoc = pageDoc.removeAttr("aside").ownerDocument();
        pageDoc = pageDoc.removeAttr("figcaption").ownerDocument();
        pageDoc = pageDoc.removeAttr("span").ownerDocument();
        pageDoc = pageDoc.removeAttr("ul").ownerDocument();
        pageDoc = pageDoc.removeAttr("script").ownerDocument();


        Element element = pageDoc.select("article").not("a").not("figure").not("div").not("aside").not("span").not("figcaption").not("ul").not("script").first();
        if (element != null) {
            //TODO wyciagnac naglowek zwykle miedzy tagami h1 lub h2
            _pdt.articleFound(element.baseUri(), element.text());
        }
    }

    private HashSet<String> removeDuplicateLinks(Elements links) {
        HashSet<String> hashSet = new HashSet<>();
/*
        for (Element link : links) {
            if (!hashSet.contains(link.absUrl("href")))
                hashSet.add(link.absUrl("href"));
        }
*/
        hashSet.add(links.first().absUrl("href"));
        return hashSet;
    }

    public void extractArticlesOneLevelDown(Document pageDoc, String mainPageUrl) {
        Elements articles = pageDoc.select("article");

        for (Element article : articles) {
            Elements links = article.select("a[href]");

            for (String link : removeDuplicateLinks(links)) {
                Pattern pattern = Pattern.compile("^.*\\b(" + mainPageUrl + ")\\b.*$");
                Matcher matcher = pattern.matcher(link);

                if (matcher.find()) {
                    try {
                        Document doc = Jsoup.connect(link).get();
                        extractFirstArticle(doc);
                    } catch (Exception exception) {
                        Log.e("ArticleExtractor", "EXCEPTION IN ARTICLE EXTRACTOR",exception);
                    }
                }
            }
        }
    }
}

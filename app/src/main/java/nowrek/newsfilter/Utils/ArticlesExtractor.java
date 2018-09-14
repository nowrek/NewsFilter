package nowrek.newsfilter.Utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArticlesExtractor {

    public LinkedList<String> extractArticles(Document pageDoc) {
        Elements elements = pageDoc.select("article").not("a").not("figure").not("div").not("aside").not("span").not("figcaption").not("ul");

        return new LinkedList(elements.eachText());
    }

    private LinkedList<String> extractFirstArticle(Document pageDoc) {
        pageDoc = pageDoc.removeAttr("a").ownerDocument();
        pageDoc = pageDoc.removeAttr("figure").ownerDocument();
        pageDoc = pageDoc.removeAttr("div").ownerDocument();
        pageDoc = pageDoc.removeAttr("aside").ownerDocument();
        pageDoc = pageDoc.removeAttr("figcaption").ownerDocument();
        pageDoc = pageDoc.removeAttr("span").ownerDocument();
        pageDoc = pageDoc.removeAttr("ul").ownerDocument();
        pageDoc = pageDoc.removeAttr("script").ownerDocument();


        Element element = pageDoc.select("article").not("a").not("figure").not("div").not("aside").not("span").not("figcaption").not("ul").not("script").first();
        LinkedList<String> elementList = new LinkedList<>();
        if (element != null) {elementList.add(element.text());}
        return elementList;
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

    public LinkedList<String> extractArticlesOneLevelDown(Document pageDoc, String mainPageUrl) {
        LinkedList<String> extractedArticles = new LinkedList<>();

        Elements articles = pageDoc.select("article");

        for (Element article : articles) {
            Elements links = article.select("a[href]");

            for (String link : removeDuplicateLinks(links)) {
                Pattern pattern = Pattern.compile("^.*\\b(" + mainPageUrl + ")\\b.*$");
                Matcher matcher = pattern.matcher(link);

                if (matcher.find()) {
                    try {
                        Document doc = Jsoup.connect(link).get();
                        extractedArticles.addAll(extractFirstArticle(doc));
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }
        }

        return extractedArticles;
    }
}

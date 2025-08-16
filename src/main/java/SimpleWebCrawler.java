import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class SimpleWebCrawler {

    public static void main(String[] args) {
        String url = "http://example.com"; // 你想要爬取的网页URL
        try {
            // 连接到网页
            Document doc = Jsoup.connect(url).get();

            // 选择并打印网页的标题
            String title = doc.title();
            System.out.println("Title: " + title);

            // 选择并打印所有的链接
            Elements links = doc.select("a[href]");
            for (Element link : links) {
                String href = link.attr("abs:href");
                System.out.println("Link: " + href);
            }

            // 选择并打印特定的元素，例如id为"footer"的元素
            Element footer = doc.getElementById("footer");
            if (footer != null) {
                System.out.println("Footer: " + footer.text());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
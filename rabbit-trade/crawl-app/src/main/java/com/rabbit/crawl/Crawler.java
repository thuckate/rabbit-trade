package com.rabbit.crawl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;

public class Crawler {
  private HashSet<String> urlLinks;
  private static final int MAX_DEPT = 3;

  public Crawler() {
    urlLinks = new HashSet<>();
  }

  public static void main(String[] args) {
    String html = "<html><head><title>First parse</title></head>"
            + "<body><p>Parsed HTML into a doc.</p></body></html>";
    Document doc = Jsoup.parse(html);
    System.out.println("");
    Crawler crawler = new Crawler();
    crawler.getPageLink("https://24hmoney.vn/recommend/top-foreign", 0);
  }

  public void getPageLink(String URL, int depth) {
    if (!urlLinks.contains(URL) &&  depth < MAX_DEPT) {
      System.out.println(">> Depth: " + depth + " [" + URL + "]");
      try {
        urlLinks.add(URL);
        Document doc = Jsoup.connect(URL).get();
        Elements availableLink = doc.select("a[href]");
        depth++;
        for (Element page : availableLink) {
          getPageLink(page.attr("abs:href"), depth);
        }
      } catch (IOException e) {
        System.out.println("ERROR with " + URL + ": " + e.getMessage());
      }
    }
  }
  public void test() throws IOException {
    Document doc = Jsoup.connect("https://24hmoney.vn/recommend/top-foreign").get();
    System.out.println(doc.title());
    Elements newsHeadlines = doc.select(".main .content");
    for (Element headline : newsHeadlines) {
      System.out.println("%s \t%s".formatted(headline.attr("title"), headline.absUrl("href")));
    }
  }
}

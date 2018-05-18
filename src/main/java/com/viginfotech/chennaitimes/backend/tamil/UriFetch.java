package com.viginfotech.chennaitimes.backend.tamil;


import com.viginfotech.chennaitimes.backend.Constants;
import com.viginfotech.chennaitimes.backend.model.Feed;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by anand on 12/3/15.
 */
public class UriFetch {

    public static String fetchData(String uri) {

        BufferedReader reader = null;
        try {
            URL url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            StringBuilder builder = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line + "\n");
            }
            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
    }

    public static List<Feed> fetchDinakaranData(int categoryId, String uri) {
        Document doc;
        List<Feed> feedList = new ArrayList<>();
        try {
            doc = Jsoup.connect(uri).timeout(10000).get();

            Elements newslist = doc.getElementsByClass("news-list-page");
            if (newslist != null) {

                Elements ul = doc.select("div.news-list-page > ul");
                Elements li = ul.select("li");
                for (int i = 0; i < li.size(); i++) {
                    String title = li.get(i).select("h1 > a").text();
                    String guid = "http://www.dinakaran.com/" + li.get(i).select("h1 > a").attr("href");

                    String summary = li.get(i).select("p").text();
                    String pubDate = li.get(i).select("span").text();
                    Feed feed = new Feed();
                    feed.setTitle(title);

                    feed.setGuid(guid);
                    feed.setSummary(summary);
                    long now = System.currentTimeMillis();
                    feed.setPubDate(now);


                    feed.setCategoryId(categoryId);
                    feed.setSourceId(Constants.SOURCE_DINAKARAN);
                    feedList.add(feed);
                }


            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return feedList;
    }


    public static List<Feed> fetchOneIndiaData(int category, String uri) {
        Document doc;
        List<Feed> feedList = new ArrayList<>();
        try {
            doc = Jsoup.connect(uri).userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21").timeout(10000).get();
            Element element = doc.getElementById("collection-wrapper");
            if (element != null) {
                Elements articles = element.getElementsByTag("article");


                for (Element article : articles) {
                    Feed feed = new Feed();

                    Elements heading = article.getElementsByClass("collection-heading");
                    if (heading != null) {

                        feed.setTitle(heading.get(0).text());
                    }

                    String articleImg = null;
                    Elements articleImgClass = article.getElementsByClass("article-img");
                    if (articleImgClass != null) {
                        feed.setThumbnail(
                                articleImgClass.get(0).getElementsByTag("img").get(0).attr("src"));
                    }


                    String link = article.getElementsByTag("a").get(1).attr("href");

                    feed.setGuid("http://tamil.oneindia.com" + link);
                    feed.setCategoryId(category);
                    feed.setSourceId(Constants.SOURCE_ONEINDIA);

                    long now = System.currentTimeMillis();

                    feed.setPubDate(now);

                    feedList.add(feed);
                }
            }

        } catch (IOException e) {

            e.printStackTrace();
        }
        return feedList;
    }

}

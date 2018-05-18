package com.viginfotech.chennaitimes.backend.tamil;


import com.googlecode.objectify.Key;
import com.viginfotech.chennaitimes.backend.Config;
import com.viginfotech.chennaitimes.backend.Constants;
import com.viginfotech.chennaitimes.backend.model.Feed;
import com.viginfotech.chennaitimes.backend.utils.QueryUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Arrays;
import java.util.List;

import static com.viginfotech.chennaitimes.backend.Constants.*;
import static com.viginfotech.chennaitimes.backend.service.OfyService.ofy;
import static com.viginfotech.chennaitimes.backend.utils.UpdateHelper.removeDuplicates;


/**
 * Created by anand on 1/21/16.
 */
public class Dinamani {
    public Dinamani() {
    }

    public static List<Feed> fetchDinamaniNews(int category) {
        switch (category) {
            case CATEGORY_HEADLINES:
                return DinamaniParser.parseFeed(UriFetch.
                        fetchData(Config.Dinamani.FRONT_PAGE_URI), category);
            case CATEGORY_TAMILNADU:
                return DinamaniParser.parseFeed(UriFetch.fetchData(Config.Dinamani.TAMIL_NADU_PAGE_URI), CATEGORY_TAMILNADU);
            case CATEGORY_INDIA:
                return DinamaniParser.parseFeed(UriFetch.fetchData(Config.Dinamani.INDIA_NEWS_PAGE_URI), CATEGORY_INDIA);
            case CATEGORY_WORLD:
                return DinamaniParser.parseFeed(UriFetch.fetchData(Config.Dinamani.INTERNATIONAL_NEWS_PAGE_URI),
                        CATEGORY_WORLD);
            case CATEGORY_BUSINESS:
                return DinamaniParser.parseFeed(UriFetch.fetchData(Config.Dinamani.BUSINESS_NEWS_PAGE_URI),
                        category);
            case CATEGORY_CINEMA:
                return DinamaniParser.parseFeed(UriFetch.fetchData(Config.Dinamani.CINEMA_NEWS_PAGE_URI),
                        category);
            default:
                return null;

        }
    }

    public static List<Feed> queryDinamaniNews(int category) {
        List<Feed> feedList = null;
        switch (category) {
            case CATEGORY_HEADLINES:
            case CATEGORY_TAMILNADU:
            case CATEGORY_INDIA:
            case CATEGORY_WORLD:
            case CATEGORY_BUSINESS:
            case CATEGORY_CINEMA:

                feedList = QueryUtils.queryCategorySortbyPubDate(SOURCE_DINAMANI, category);
                if (feedList.size() == 0) {
                    System.out.println("Fetching from net " + "headlines" + " Dinamani");
                    feedList = fetchDinamaniNews(category);
                    if (feedList != null) {


                        feedList = removeDuplicates(Constants.SOURCE_DINAMANI, Arrays.asList(category), feedList);
                        System.out.println("filtered size dinamani" + feedList.size());
                        if (feedList.size() > 0) {

                            ofy().save().entities(feedList).now();
                            feedList = QueryUtils.queryCategorySortbyPubDate(SOURCE_DINAMANI, category);
                        } else {
                            feedList = QueryUtils.queryLatest7Feeds(SOURCE_DINAMANI, category);
                        }

                    }
                }
                return feedList;


            default:
                return null;
        }

    }

    public static Feed getDinamaniDetail(String guid) {
        Document doc;
        String descriptionText = null;
        String detailedTitle = null;
        String imgSrc = null;
        StringBuilder builder = new StringBuilder();
        try {
            doc = Jsoup.connect(guid).get();
            Element main = doc.getElementById("main");
            Elements headings = main.getElementsByTag("h1");
            if (headings != null && headings.size() > 0) {
                detailedTitle = headings.get(0).text().trim();
            }

            Element picture = doc.getElementsByClass("relatedContents-picture").first();
            if (picture != null) {
                Elements imgTag = picture.getElementsByTag("img");
                if (imgTag != null) {
                    Element src = imgTag.first();
                    imgSrc = src.attr("src");
                }
            }
            Element body = doc.getElementsByClass("body").first();
            Elements paragraph = body.getElementsByTag("p");
            for (Element p : paragraph) {
                // System.out.println(p.text()+"\n");
                builder.append(p.text().trim() + "\n\n");
            }

            descriptionText = Jsoup.parse(builder.toString()).text();
            descriptionText = descriptionText.replaceAll("\\<.*?>", "");

            Key<Feed> key = Key.create(Feed.class, guid);
            Feed feed = ofy().load().key(key).now();
            if (detailedTitle != null) {
                feed.setDetailedTitle(detailedTitle);
            }
            if (imgSrc != null) {
                feed.setImage(imgSrc);
                if (feed.getThumbnail() == null) feed.setThumbnail(imgSrc);
            }
            feed.setDetailNews(descriptionText);
            ofy().save().entity(feed).now();
            return feed;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}

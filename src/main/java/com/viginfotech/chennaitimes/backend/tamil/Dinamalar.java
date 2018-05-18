package com.viginfotech.chennaitimes.backend.tamil;


import com.googlecode.objectify.Key;
import com.viginfotech.chennaitimes.backend.Config;
import com.viginfotech.chennaitimes.backend.model.Feed;
import com.viginfotech.chennaitimes.backend.utils.QueryUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static com.viginfotech.chennaitimes.backend.Constants.*;
import static com.viginfotech.chennaitimes.backend.service.OfyService.ofy;
import static com.viginfotech.chennaitimes.backend.utils.UpdateHelper.removeDuplicates;


/**
 * Created by anand on 1/3/16.
 */
public class Dinamalar {


    public Dinamalar() {
    }


    public static List<Feed> fetchDinamalarNews(int category) {
        return DinamalarParser.parseFeed(UriFetch.fetchData(getUri(SOURCE_DINAMALAR, category)), category);
    }

    public static List<Feed> queryDinamalarNews(int category) {

        List<Feed> feedList = QueryUtils.queryCategorySortbyPubDate(SOURCE_DINAMALAR, category);
        if (feedList.size() == 0) {
            System.out.println("Fetching from net " + category + " dinamalar");
            feedList = fetchDinamalarNews(category);
            if (feedList != null) {

                feedList = removeDuplicates(SOURCE_DINAMALAR, Arrays.asList(category), feedList);
                System.out.println("filtered size dinamalar" + feedList.size());
                if (feedList.size() > 0) {
                    ofy().save().entities(feedList).now();

                    feedList = QueryUtils.queryCategorySortbyPubDate(SOURCE_DINAMALAR, category);
                } else {
                    feedList = QueryUtils.queryLatest7Feeds(SOURCE_DINAMALAR, category);
                }

            }
        }


        return feedList;
    }


    public static Feed getDetail(String guid, int categroy) {
        switch (categroy) {
            case CATEGORY_BUSINESS:
                return readBusinessNews(guid);
            case CATEGORY_CINEMA:
                return readCinema(guid);
            default:
                return getNews(guid);

        }
    }

    private static Feed getNews(String guid) {
        Document doc;
        StringBuilder builder = new StringBuilder();
        String detailedTitle = null;
        String imgSrc = null;
        try {
            doc = Jsoup.connect(guid).get();


            Elements newsdetbd = doc.getElementsByClass("newsdetbd1");


            if (newsdetbd != null && newsdetbd.size() > 0) {

                detailedTitle = newsdetbd.get(0).text();


                Element description = doc.getElementById("clsclk");
                Elements paragraph = description.getElementsByTag("p");
                for (Element p : paragraph) {
                    if (!((p.text()).isEmpty()))
                        builder.append(p.text() + "\n\n");

                }
                Elements img = description.getElementsByTag("img");
                if (img != null && img.size() > 0) {

                    imgSrc = img.get(0).attr("src");
                }
            } else if (newsdetbd != null && newsdetbd.size() == 0) {
                Elements row = doc.getElementsByClass("row");
                for (Iterator<Element> element = row.iterator(); element.hasNext(); ) {
                    Element next = element.next();
                    builder.append(next.getElementsByTag("p").text());
                    Elements img = next.getElementsByTag("img");
                    if (img != null) {
                        imgSrc = img.attr("src");
                    }
                }
            }
            String detailDescription = builder.toString().trim();

            Key<Feed> key = Key.create(Feed.class, guid);
            Feed feed = ofy().load().key(key).now();
            if (detailedTitle != null) feed.setDetailedTitle(detailedTitle);
            if (imgSrc != null) {
                feed.setImage(imgSrc);
                if (feed.getThumbnail() == null) feed.setThumbnail(imgSrc);
            }
            feed.setDetailNews(detailDescription);
            ofy().save().entity(feed).now();

            return feed;

        } catch (IOException e) {

            e.printStackTrace();
            return null;
        }


    }

    private static Feed readCinema(String guid) {

        Document doc;
        StringBuilder builder = new StringBuilder();
        String detailedTitle = null;
        String imgSrc = null;
        try {
            doc = Jsoup.connect(guid).get();
            Element selImpNews = doc.getElementById("selImpnews");
            Elements titleElements = selImpNews.getElementsByTag("h2");
            if (titleElements != null) {
                detailedTitle = titleElements.text();
            }
            Elements para = selImpNews.getElementsByTag("p");
            for (Element p : para) {
                builder.append(p.text() + "\n\n");
            }
            Elements img = selImpNews.getElementsByTag("img");
            if (img != null && img.size() > 0) {
                imgSrc = img.get(0).attr("src");
            }
            String detailDescription = builder.toString().trim();

            Key<Feed> key = Key.create(Feed.class, guid);
            Feed feed = ofy().load().key(key).now();
            if (detailedTitle != null) feed.setDetailedTitle(detailedTitle);
            feed.setImage(imgSrc);
            feed.setDetailNews(detailDescription);
            ofy().save().entity(feed).now();

            return feed;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

    private static Feed readBusinessNews(String link) {

        Document doc;
        StringBuilder builder = new StringBuilder();
        String detailedTitle = null;
        String imgSrc = null;
        try {
            doc = Jsoup.connect(link).get();
            Elements detailedWd = doc.getElementsByClass("newsdetwd");
            if (detailedWd != null) {
                detailedTitle = detailedWd.get(0).text();
            }
            Element selNews = doc.getElementById("selNews");
            if (selNews != null) {
                Elements paragraph = selNews.getElementsByTag("p");
                if (paragraph.size() > 0) {
                    for (Element p : paragraph) {
                        builder.append(p.text() + "\n\n");
                    }
                }
                Element clsclk = doc.getElementById("clsclk");
                if (clsclk != null) {
                    Elements img = clsclk.getElementsByTag("img");
                    if (img != null && img.size() > 0) {
                        imgSrc = img.get(0).attr("src");
                    }
                }

            } else {
                Element clsclk = doc.getElementById("clsclk");
                Elements all = clsclk.getAllElements();

                for (int i = 10; i < all.size(); i++) {

                    builder.append(all.get(i).text());
                }
                Elements img = clsclk.getElementsByTag("img");
                if (img != null && img.size() > 0) {
                    imgSrc = img.get(0).attr("src");
                }
            }

            String detailDescription = builder.toString().trim();

            Key<Feed> key = Key.create(Feed.class, link);

            Feed feed = ofy().load().key(key).now();
            if (detailedTitle != null)
                feed.setDetailedTitle(detailedTitle);
            feed.setImage(imgSrc);
            feed.setDetailNews(detailDescription);
            ofy().save().entity(feed).now();

            return feed;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private static String getUri(int source, int category) {
        switch (source) {
            case SOURCE_DINAMALAR:
                switch (category) {

                    case CATEGORY_HEADLINES:
                        return Config.Dinamalar.DINAMALAR_HEADLINES_URI;
                    case CATEGORY_TAMILNADU:
                        return Config.Dinamalar.DINAMALAR_TAMILNADU_URI;

                    case CATEGORY_WORLD:
                        return Config.Dinamalar.DINAMALAR_WORLD_URI;
                    case CATEGORY_BUSINESS:
                        return Config.Dinamalar.DINAMALAR_BUSINESS_URI;
                    case CATEGORY_CINEMA:
                        return Config.Dinamalar.DINAMALAR_CINEMA_URI;
                    default:
                        return null;

                }
        }
        return null;
    }
}

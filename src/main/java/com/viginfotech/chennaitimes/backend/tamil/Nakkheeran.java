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
 * Created by anand on 1/22/16.
 */
public class Nakkheeran {

    public Nakkheeran() {
    }

    private static String getUri(int category) {
        switch (category) {
            case CATEGORY_HEADLINES:
                return Config.Nakkheeran.NAKKHEERAN_HEADLINES;
            case CATEGORY_TAMILNADU:
                return Config.Nakkheeran.NAKKHEERAN_TAMILNADU;
            case CATEGORY_INDIA:
                return Config.Nakkheeran.NAKKHEERAN_INDIA;
            case CATEGORY_WORLD:
                return Config.Nakkheeran.NAKKHEERAN_WORLD;
            case CATEGORY_SPORTS:
                return Config.Nakkheeran.NAKKHEERAN_SPORTS;
            default:
                return "";
        }
    }

    public static List<Feed> queryNakkheeranNews(int category) {
        List<Feed> feedList = null;
        switch (category) {
            case CATEGORY_HEADLINES:
            case CATEGORY_TAMILNADU:
            case CATEGORY_INDIA:
            case CATEGORY_WORLD:
            case CATEGORY_SPORTS:


                feedList = QueryUtils.queryCategorySortbyPubDate(SOURCE_NAKKHEERAN, category);
                if (feedList.size() == 0) {
                    System.out.println("fetching from net nakkeran headlines");
                    feedList = fetchNakkheeran(category, getUri(category));
                    if (feedList != null) {

                        feedList = removeDuplicates(Constants.SOURCE_NAKKHEERAN, Arrays.asList(category), feedList);
                        if (feedList.size() > 0) {
                            ofy().save().entities(feedList).now();
                            feedList = QueryUtils.queryCategorySortbyPubDate(SOURCE_NAKKHEERAN, category);
                        } else {
                            feedList = QueryUtils.queryLatest7Feeds(SOURCE_NAKKHEERAN, category);
                        }
                    }

                }
                return feedList;

            default:
                return null;

        }

    }


    public static Feed getNakkheeranDetail(String guid) {
        Document doc;
        try {
            doc = Jsoup.connect(guid).get();
            Element article = doc.getElementById("divCenter");
            String detailedTitle = null;
            Elements heading = article.getElementsByTag("b");
            if (heading != null && heading.size() > 0) {
                detailedTitle = heading.text().trim();

            }
            Elements spanElments = article.getElementsByTag("span");
            StringBuilder builder = new StringBuilder();
            String imgSrc = null;
            String detailedDescription;
            for (int i = 1; i < spanElments.size() - 4; i++) {
                builder.append(spanElments.get(i).text());
                if (spanElments.get(i).childNodeSize() > 1) {
                    Elements image = spanElments.get(i).getElementsByTag("img");
                    if (image.size() > 0) {
                        imgSrc = "http://www.nakkheeran.in" + image.get(0).attr("src");
                    }
                }
            }
            detailedDescription = builder.toString();
            Key<Feed> key = Key.create(Feed.class, guid);
            Feed feed = ofy().load().key(key).now();
            if (detailedTitle != null) feed.setDetailedTitle(detailedTitle);
            if (imgSrc != null) {
                feed.setImage(imgSrc);
                if (feed.getThumbnail() == null) feed.setThumbnail(imgSrc);
            }
            feed.setDetailNews(detailedDescription);
            ofy().save().entity(feed).now();
            return feed;


        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

    public static List<Feed> fetchNakkheeran(int category, String uri) {

        System.out.println("Fetching from net " + category);
        return NakkheeranParser.parseFeed(UriFetch.
                fetchData(uri), category);


    }
}

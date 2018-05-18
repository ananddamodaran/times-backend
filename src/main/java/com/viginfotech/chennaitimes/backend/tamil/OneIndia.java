package com.viginfotech.chennaitimes.backend.tamil;


import com.googlecode.objectify.Key;
import com.viginfotech.chennaitimes.backend.Config;
import com.viginfotech.chennaitimes.backend.Constants;
import com.viginfotech.chennaitimes.backend.model.Feed;
import com.viginfotech.chennaitimes.backend.utils.QueryUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.Arrays;
import java.util.List;

import static com.viginfotech.chennaitimes.backend.Constants.*;
import static com.viginfotech.chennaitimes.backend.service.OfyService.ofy;
import static com.viginfotech.chennaitimes.backend.utils.UpdateHelper.removeDuplicates;


/**
 * Created by anand on 1/21/16.
 */
public class OneIndia {
    public OneIndia() {
    }

    private static String getUri(int category) {
        switch (category) {
            case CATEGORY_TAMILNADU:
                return Config.OneIndia.ONEINDIA_TAMILNADU;
            case CATEGORY_INDIA:
                return Config.OneIndia.ONEINDIA_INDIA;
            case CATEGORY_WORLD:
                return Config.OneIndia.ONEINDIA_WORLD;
            case CATEGORY_BUSINESS:
                return Config.OneIndia.ONEINDIA_BUSINESS;
            default:
                return null;
        }
    }

    public static List<Feed> queryOneIndiaNews(int category) {
        List<Feed> feedList = null;
        switch (category) {
            case CATEGORY_TAMILNADU:
            case CATEGORY_INDIA:
            case CATEGORY_WORLD:
            case CATEGORY_BUSINESS:

                feedList = QueryUtils.queryCategorySortbyPubDate(SOURCE_ONEINDIA, category);
                if (feedList.size() == 0) {
                    System.out.println("Fetching from net " + category);
                    feedList = UriFetch.fetchOneIndiaData(category, getUri(category));

                    if (feedList != null) {

                        feedList = removeDuplicates(Constants.SOURCE_ONEINDIA, Arrays.asList(category), feedList);
                        System.out.println("filtered size oneindia" + feedList.size());

                        if (feedList.size() > 0) {
                            ofy().save().entities(feedList).now();
                            feedList = QueryUtils.queryCategorySortbyPubDate(SOURCE_ONEINDIA, category);
                        } else {
                            feedList = QueryUtils.queryLatest7Feeds(SOURCE_ONEINDIA, category);
                        }
                    }

                }

                return feedList;

            default:
                return null;

        }
    }

    public static Feed getOneIndiaDetail(String guid) {
        Document doc;
        try {
            doc = Jsoup.connect(guid)
                    .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21")
                    .timeout(10000).get();


            Elements articleElements = doc.getElementsByTag("article");
            String imgSrc = null;
            if (articleElements != null && articleElements.size() > 0) {
                Elements bigImage = articleElements.get(0).getElementsByClass("big_center_img");
                if (bigImage != null && bigImage.size() > 0) {
                    imgSrc = "http://tamil.oneindia.com" + bigImage.get(0).getElementsByTag("img").get(0).attr("src");
                }
            }

            Elements headingClass = doc.getElementsByClass("heading");
            String detailedHeading = null;
            if (headingClass != null && headingClass.size() == 0) {
                headingClass = doc.getElementsByClass("articleheading");
            }
            if (headingClass != null)
                detailedHeading = headingClass.text().trim();

            String detailDescription = articleElements.get(0).getElementsByTag("p").text();
            Key<Feed> key = Key.create(Feed.class, guid);
            Feed feed = ofy().load().key(key).now();
            if (detailedHeading != null) {
                feed.setDetailedTitle(detailedHeading);
            }
            if (imgSrc != null) {
                feed.setImage(imgSrc);
                if (feed.getThumbnail() == null) feed.setThumbnail(imgSrc);
            }
            feed.setDetailNews(detailDescription);
            ofy().save().entity(feed).now();
            return feed;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }
}

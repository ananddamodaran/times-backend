package com.viginfotech.chennaitimes.backend.utils;


import com.googlecode.objectify.cmd.Query;
import com.viginfotech.chennaitimes.backend.Constants;
import com.viginfotech.chennaitimes.backend.model.Feed;

import java.util.List;

import static com.viginfotech.chennaitimes.backend.service.OfyService.ofy;
import static com.viginfotech.chennaitimes.backend.utils.TimeUtils.getLast24HoursInMills;
import static com.viginfotech.chennaitimes.backend.utils.TimeUtils.getLast6HoursInMills;


/**
 * Created by anand on 1/21/16.
 */
public class QueryUtils {

    public static List<Feed> queryCategorySortbyPubDate(int sourceId, int category) {
        Query<Feed> q = ofy().load().type(Feed.class);
        q = q.filter("categoryId", category);
        q = q.filter("sourceId", sourceId);
        if (category != Constants.CATEGORY_CINEMA) {
            q = q.filter("pubDate > ", getLast6HoursInMills());
            q = q.limit(15);
        } else {
            q = q.filter("pubDate > ", getLast24HoursInMills());

            q = q.limit(20);
        }
        q = q.order("-pubDate");

        return q.list();
    }

    public static List<Feed> queryLatest7Feeds(int sourceId, int category) {
        Query<Feed> q = ofy().load().type(Feed.class);
        q = q.filter("categoryId", category);
        q = q.filter("sourceId", sourceId);
        q = q.filter("pubDate > ", getLast24HoursInMills());
        q = q.limit(7);
        q = q.order("-pubDate");


        return q.list();
    }
}

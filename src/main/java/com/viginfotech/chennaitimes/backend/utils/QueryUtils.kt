package com.viginfotech.chennaitimes.backend.utils


import com.googlecode.objectify.cmd.Query
import com.viginfotech.chennaitimes.backend.Constants
import com.viginfotech.chennaitimes.backend.model.Feed
import com.viginfotech.chennaitimes.backend.service.OfyService.Companion.ofy


import com.viginfotech.chennaitimes.backend.utils.TimeUtils.last24HoursInMills
import com.viginfotech.chennaitimes.backend.utils.TimeUtils.last6HoursInMills


/**
 * Created by anand on 1/21/16.
 */
object QueryUtils {

    fun queryCategorySortbyPubDate(sourceId: Int, category: Int): List<Feed> {
        var q: Query<Feed> = ofy().load().type(Feed::class.java)
        q = q.filter("categoryId", category)
        q = q.filter("sourceId", sourceId)
        if (category != Constants.CATEGORY_CINEMA) {
            q = q.filter("pubDate > ", last6HoursInMills)
            q = q.limit(15)
        } else {
            q = q.filter("pubDate > ", last24HoursInMills)

            q = q.limit(20)
        }
        q = q.order("-pubDate")

        return q.list()
    }

    fun queryLatest7Feeds(sourceId: Int, category: Int): List<Feed> {
        var q: Query<Feed> = ofy().load().type(Feed::class.java)
        q = q.filter("categoryId", category)
        q = q.filter("sourceId", sourceId)
        q = q.filter("pubDate > ", last24HoursInMills)
        q = q.limit(7)
        q = q.order("-pubDate")


        return q.list()
    }
}

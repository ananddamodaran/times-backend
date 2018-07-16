package com.viginfotech.chennaitimes.backend.tamil


import com.googlecode.objectify.Key
import com.viginfotech.chennaitimes.backend.Config
import com.viginfotech.chennaitimes.backend.Constants.CATEGORY_BUSINESS
import com.viginfotech.chennaitimes.backend.Constants.CATEGORY_INDIA
import com.viginfotech.chennaitimes.backend.Constants.CATEGORY_SPORTS
import com.viginfotech.chennaitimes.backend.Constants.CATEGORY_TAMILNADU
import com.viginfotech.chennaitimes.backend.Constants.CATEGORY_WORLD
import com.viginfotech.chennaitimes.backend.Constants.SOURCE_DINAKARAN
import com.viginfotech.chennaitimes.backend.model.Feed
import com.viginfotech.chennaitimes.backend.service.OfyService.Companion.ofy
import com.viginfotech.chennaitimes.backend.utils.QueryUtils
import com.viginfotech.chennaitimes.backend.utils.UpdateHelper.removeDuplicates
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.util.*


/**
 * Created by anand on 12/6/15.
 */
class Dinakaran {
    companion object {

        private fun getUri(category: Int): String {
            when (category) {
                CATEGORY_TAMILNADU -> return Config.Dinakaran.DINAKARAN_TAMILNADU_URI
                CATEGORY_INDIA -> return Config.Dinakaran.DINAKARAN_INDIA_URI
                CATEGORY_WORLD -> return Config.Dinakaran.DINAKARAN_WORLD_URI
                CATEGORY_BUSINESS -> return Config.Dinakaran.DINAKARAN_BUSINESS_URI
                CATEGORY_SPORTS -> return Config.Dinakaran.DINAKARAN_SPORTS_URI
                else -> return ""
            }
        }

        fun queryDinakaranNews(category: Int): List<Feed>? {
            var feedList: List<Feed>? = null
            when (category) {
                CATEGORY_TAMILNADU, CATEGORY_INDIA, CATEGORY_WORLD, CATEGORY_BUSINESS, CATEGORY_SPORTS -> {

                    feedList = QueryUtils.queryCategorySortbyPubDate(SOURCE_DINAKARAN, category)
                    if (feedList.size == 0) {
                        println("Fetching from net $category")
                        feedList = UriFetch.fetchDinakaranData(category, getUri(category))

                        if (feedList != null) {

                            feedList = removeDuplicates(SOURCE_DINAKARAN, Arrays.asList<Int>(category), feedList)
                            println("filtered size dinakaran" + feedList.size)
                            if (feedList.size > 0) {
                                ofy().save().entities(feedList).now()
                                feedList = QueryUtils.queryCategorySortbyPubDate(SOURCE_DINAKARAN, category)
                            } else {
                                feedList = QueryUtils.queryLatest7Feeds(SOURCE_DINAKARAN, category)

                            }

                        }

                    }

                    return feedList
                }
                else -> return null
            }
        }


        fun getDinakaranDetail(guid: String): Feed? {

            try {
                val doc: Document
                doc = Jsoup.connect(guid).get()
                val main_news = doc.getElementsByClass("main-news")
                var detailedTitle: String? = null
                var imgSrc: String? = null
                var detailDescription: String? = null
                if (main_news != null && main_news.size > 0) {
                    detailedTitle = main_news[0].getElementsByTag("h1").text()
                    imgSrc = main_news[0].getElementsByTag("img").attr("src")
                    detailDescription = main_news[0].getElementsByTag("p").text()


                }
                val leftcolumn = doc.getElementsByClass("leftcolumn")
                if (leftcolumn != null && leftcolumn.size > 0) {
                    detailedTitle = leftcolumn[0].getElementsByTag("h1").text()
                    imgSrc = leftcolumn[0].getElementsByTag("img")[1].attr("src")
                    detailDescription = leftcolumn[0].getElementsByTag("p").text()


                }
                val key = Key.create(Feed::class.java, guid)
                val feed = ofy().load().key(key).now()
                if (detailedTitle != null) feed.detailedTitle = detailedTitle
                if (imgSrc != null) {
                    feed.image = imgSrc
                    if (feed.thumbnail == null) feed.thumbnail = imgSrc

                }
                feed.detailNews = detailDescription
                ofy().save().entity(feed).now()

                return feed
            } catch (ex: Exception) {
                ex.printStackTrace()
                return null
            }

        }
    }

}

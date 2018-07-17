package com.viginfotech.chennaitimes.backend.tamil


import com.googlecode.objectify.Key
import com.viginfotech.chennaitimes.backend.Config
import com.viginfotech.chennaitimes.backend.Constants.CATEGORY_BUSINESS
import com.viginfotech.chennaitimes.backend.Constants.CATEGORY_INDIA
import com.viginfotech.chennaitimes.backend.Constants.CATEGORY_TAMILNADU
import com.viginfotech.chennaitimes.backend.Constants.CATEGORY_WORLD
import com.viginfotech.chennaitimes.backend.Constants.SOURCE_ONEINDIA
import com.viginfotech.chennaitimes.backend.model.Feed
import com.viginfotech.chennaitimes.backend.service.OfyService.Companion.ofy
import com.viginfotech.chennaitimes.backend.utils.QueryUtils
import com.viginfotech.chennaitimes.backend.utils.UpdateHelper.removeDuplicates
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.util.*


/**
 * Created by anand on 1/21/16.
 */
class OneIndia {
    companion object {

        private fun getUri(category: Int): String? {
            return when (category) {
                CATEGORY_TAMILNADU -> Config.OneIndia.ONEINDIA_TAMILNADU
                CATEGORY_INDIA -> Config.OneIndia.ONEINDIA_INDIA
                CATEGORY_WORLD -> Config.OneIndia.ONEINDIA_WORLD
                CATEGORY_BUSINESS -> Config.OneIndia.ONEINDIA_BUSINESS
                else -> null
            }
        }

        fun queryOneIndiaNews(category: Int): List<Feed>? {
            var feedList: List<Feed>? = null
            when (category) {
                CATEGORY_TAMILNADU, CATEGORY_INDIA, CATEGORY_WORLD, CATEGORY_BUSINESS -> {

                    feedList = QueryUtils.queryCategorySortbyPubDate(SOURCE_ONEINDIA, category)
                    if (feedList.isEmpty()) {
                        println("Fetching from net $category")
                        feedList = UriFetch.fetchOneIndiaData(category, getUri(category)!!)

                        if (feedList != null) {

                            feedList = removeDuplicates(SOURCE_ONEINDIA, Arrays.asList<Int>(category), feedList)
                            println("filtered size oneindia" + feedList!!.size)

                            feedList = if (feedList.isNotEmpty()) {
                                ofy().save().entities(feedList).now()
                                QueryUtils.queryCategorySortbyPubDate(SOURCE_ONEINDIA, category)
                            } else {
                                QueryUtils.queryLatest7Feeds(SOURCE_ONEINDIA, category)
                            }
                        }

                    }

                    return feedList
                }

                else -> return null
            }
        }

        fun getOneIndiaDetail(guid: String): Feed? {
            val doc: Document
            try {
                doc = Jsoup.connect(guid)
                        .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21")
                        .timeout(10000).get()


                val articleElements = doc.getElementsByTag("article")
                var imgSrc: String? = null
                if (articleElements != null && articleElements.size > 0) {
                    val bigImage = articleElements[0].getElementsByClass("big_center_img")
                    if (bigImage != null && bigImage.size > 0) {
                        imgSrc = "http://tamil.oneindia.com" + bigImage[0].getElementsByTag("img")[0].attr("src")
                    }
                }

                var headingClass: Elements? = doc.getElementsByClass("heading")
                var detailedHeading: String? = null
                if (headingClass != null && headingClass.size == 0) {
                    headingClass = doc.getElementsByClass("articleheading")
                }
                if (headingClass != null)
                    detailedHeading = headingClass.text().trim { it <= ' ' }

                val detailDescription = articleElements!![0].getElementsByTag("p").text()
                val key = Key.create(Feed::class.java, guid)
                val feed = ofy().load().key(key).now()
                if (detailedHeading != null) {
                    feed.detailedTitle = detailedHeading
                }
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

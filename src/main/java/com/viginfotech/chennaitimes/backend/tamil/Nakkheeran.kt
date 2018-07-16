package com.viginfotech.chennaitimes.backend.tamil


import com.googlecode.objectify.Key
import com.viginfotech.chennaitimes.backend.Config
import com.viginfotech.chennaitimes.backend.Constants.CATEGORY_HEADLINES
import com.viginfotech.chennaitimes.backend.Constants.CATEGORY_INDIA
import com.viginfotech.chennaitimes.backend.Constants.CATEGORY_SPORTS
import com.viginfotech.chennaitimes.backend.Constants.CATEGORY_TAMILNADU
import com.viginfotech.chennaitimes.backend.Constants.CATEGORY_WORLD
import com.viginfotech.chennaitimes.backend.Constants.SOURCE_NAKKHEERAN
import com.viginfotech.chennaitimes.backend.model.Feed
import com.viginfotech.chennaitimes.backend.service.OfyService.Companion.ofy
import com.viginfotech.chennaitimes.backend.utils.QueryUtils
import com.viginfotech.chennaitimes.backend.utils.UpdateHelper.removeDuplicates
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.util.*


/**
 * Created by anand on 1/22/16.
 */
class Nakkheeran {
    companion object {

        private fun getUri(category: Int): String {
            when (category) {
                CATEGORY_HEADLINES -> return Config.Nakkheeran.NAKKHEERAN_HEADLINES
                CATEGORY_TAMILNADU -> return Config.Nakkheeran.NAKKHEERAN_TAMILNADU
                CATEGORY_INDIA -> return Config.Nakkheeran.NAKKHEERAN_INDIA
                CATEGORY_WORLD -> return Config.Nakkheeran.NAKKHEERAN_WORLD
                CATEGORY_SPORTS -> return Config.Nakkheeran.NAKKHEERAN_SPORTS
                else -> return ""
            }
        }

        fun queryNakkheeranNews(category: Int): List<Feed>? {
            var feedList: List<Feed>? = null
            when (category) {
                CATEGORY_HEADLINES, CATEGORY_TAMILNADU, CATEGORY_INDIA, CATEGORY_WORLD, CATEGORY_SPORTS -> {


                    feedList = QueryUtils.queryCategorySortbyPubDate(SOURCE_NAKKHEERAN, category)
                    if (feedList.size == 0) {
                        println("fetching from net nakkeran headlines")
                        feedList = fetchNakkheeran(category, getUri(category))
                        if (feedList != null) {

                            feedList = removeDuplicates(SOURCE_NAKKHEERAN, Arrays.asList<Int>(category), feedList)
                            if (feedList!!.size > 0) {
                                ofy().save().entities(feedList).now()
                                feedList = QueryUtils.queryCategorySortbyPubDate(SOURCE_NAKKHEERAN, category)
                            } else {
                                feedList = QueryUtils.queryLatest7Feeds(SOURCE_NAKKHEERAN, category)
                            }
                        }

                    }
                    return feedList
                }

                else -> return null
            }

        }


        fun getNakkheeranDetail(guid: String): Feed? {
            val doc: Document
            try {
                doc = Jsoup.connect(guid).get()
                val article = doc.getElementById("divCenter")
                var detailedTitle: String? = null
                val heading = article.getElementsByTag("b")
                if (heading != null && heading.size > 0) {
                    detailedTitle = heading.text().trim { it <= ' ' }

                }
                val spanElments = article.getElementsByTag("span")
                val builder = StringBuilder()
                var imgSrc: String? = null
                val detailedDescription: String
                for (i in 1 until spanElments.size - 4) {
                    builder.append(spanElments[i].text())
                    if (spanElments[i].childNodeSize() > 1) {
                        val image = spanElments[i].getElementsByTag("img")
                        if (image.size > 0) {
                            imgSrc = "http://www.nakkheeran.in" + image[0].attr("src")
                        }
                    }
                }
                detailedDescription = builder.toString()
                val key = Key.create(Feed::class.java, guid)
                val feed = ofy().load().key(key).now()
                if (detailedTitle != null) feed.detailedTitle = detailedTitle
                if (imgSrc != null) {
                    feed.image = imgSrc
                    if (feed.thumbnail == null) feed.thumbnail = imgSrc
                }
                feed.detailNews = detailedDescription
                ofy().save().entity(feed).now()
                return feed


            } catch (ex: Exception) {
                ex.printStackTrace()
                return null
            }

        }

        fun fetchNakkheeran(category: Int, uri: String): List<Feed>? {

            println("Fetching from net $category")
            return NakkheeranParser.parseFeed(UriFetch.fetchData(uri), category)


        }
    }
}

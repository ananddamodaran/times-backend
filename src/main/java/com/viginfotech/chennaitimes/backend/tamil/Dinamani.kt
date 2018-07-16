package com.viginfotech.chennaitimes.backend.tamil


import com.googlecode.objectify.Key
import com.viginfotech.chennaitimes.backend.Config
import com.viginfotech.chennaitimes.backend.Constants.CATEGORY_BUSINESS
import com.viginfotech.chennaitimes.backend.Constants.CATEGORY_CINEMA
import com.viginfotech.chennaitimes.backend.Constants.CATEGORY_HEADLINES
import com.viginfotech.chennaitimes.backend.Constants.CATEGORY_INDIA
import com.viginfotech.chennaitimes.backend.Constants.CATEGORY_TAMILNADU
import com.viginfotech.chennaitimes.backend.Constants.CATEGORY_WORLD
import com.viginfotech.chennaitimes.backend.Constants.SOURCE_DINAMANI
import com.viginfotech.chennaitimes.backend.model.Feed
import com.viginfotech.chennaitimes.backend.service.OfyService.Companion.ofy
import com.viginfotech.chennaitimes.backend.utils.QueryUtils
import com.viginfotech.chennaitimes.backend.utils.UpdateHelper.removeDuplicates
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.util.*


/**
 * Created by anand on 1/21/16.
 */
class Dinamani {
    companion object {

        fun fetchDinamaniNews(category: Int): List<Feed>? {
            when (category) {
                CATEGORY_HEADLINES -> return DinamaniParser.parseFeed(UriFetch.fetchData(Config.Dinamani.FRONT_PAGE_URI), category)
                CATEGORY_TAMILNADU -> return DinamaniParser.parseFeed(UriFetch.fetchData(Config.Dinamani.TAMIL_NADU_PAGE_URI), CATEGORY_TAMILNADU)
                CATEGORY_INDIA -> return DinamaniParser.parseFeed(UriFetch.fetchData(Config.Dinamani.INDIA_NEWS_PAGE_URI), CATEGORY_INDIA)
                CATEGORY_WORLD -> return DinamaniParser.parseFeed(UriFetch.fetchData(Config.Dinamani.INTERNATIONAL_NEWS_PAGE_URI),
                        CATEGORY_WORLD)
                CATEGORY_BUSINESS -> return DinamaniParser.parseFeed(UriFetch.fetchData(Config.Dinamani.BUSINESS_NEWS_PAGE_URI),
                        category)
                CATEGORY_CINEMA -> return DinamaniParser.parseFeed(UriFetch.fetchData(Config.Dinamani.CINEMA_NEWS_PAGE_URI),
                        category)
                else -> return null
            }
        }

        fun queryDinamaniNews(category: Int): List<Feed>? {
            var feedList: List<Feed>? = null
            when (category) {
                CATEGORY_HEADLINES, CATEGORY_TAMILNADU, CATEGORY_INDIA, CATEGORY_WORLD, CATEGORY_BUSINESS, CATEGORY_CINEMA -> {

                    feedList = QueryUtils.queryCategorySortbyPubDate(SOURCE_DINAMANI, category)
                    if (feedList.size == 0) {
                        println("Fetching from net " + "headlines" + " Dinamani")
                        feedList = fetchDinamaniNews(category)
                        if (feedList != null) {


                            feedList = removeDuplicates(SOURCE_DINAMANI, Arrays.asList<Int>(category), feedList)
                            println("filtered size dinamani" + feedList!!.size)
                            if (feedList!!.size > 0) {

                                ofy().save().entities(feedList).now()
                                feedList = QueryUtils.queryCategorySortbyPubDate(SOURCE_DINAMANI, category)
                            } else {
                                feedList = QueryUtils.queryLatest7Feeds(SOURCE_DINAMANI, category)
                            }

                        }
                    }
                    return feedList
                }


                else -> return null
            }

        }

        fun getDinamaniDetail(guid: String): Feed? {
            val doc: Document
            var descriptionText: String? = null
            var detailedTitle: String? = null
            var imgSrc: String? = null
            val builder = StringBuilder()
            try {
                doc = Jsoup.connect(guid).get()
                val main = doc.getElementById("main")
                val headings = main.getElementsByTag("h1")
                if (headings != null && headings.size > 0) {
                    detailedTitle = headings[0].text().trim { it <= ' ' }
                }

                val picture = doc.getElementsByClass("relatedContents-picture").first()
                if (picture != null) {
                    val imgTag = picture.getElementsByTag("img")
                    if (imgTag != null) {
                        val src = imgTag.first()
                        imgSrc = src.attr("src")
                    }
                }
                val body = doc.getElementsByClass("body").first()
                val paragraph = body.getElementsByTag("p")
                for (p in paragraph) {
                    // System.out.println(p.text()+"\n");
                    builder.append(p.text().trim { it <= ' ' } + "\n\n")
                }

                descriptionText = Jsoup.parse(builder.toString()).text()
                descriptionText = descriptionText!!.replace("\\<.*?>".toRegex(), "")

                val key = Key.create(Feed::class.java, guid)
                val feed = ofy().load().key(key).now()
                if (detailedTitle != null) {
                    feed.detailedTitle = detailedTitle
                }
                if (imgSrc != null) {
                    feed.image = imgSrc
                    if (feed.thumbnail == null) feed.thumbnail = imgSrc
                }
                feed.detailNews = descriptionText
                ofy().save().entity(feed).now()
                return feed
            } catch (ex: Exception) {
                ex.printStackTrace()
                return null
            }

        }
    }
}

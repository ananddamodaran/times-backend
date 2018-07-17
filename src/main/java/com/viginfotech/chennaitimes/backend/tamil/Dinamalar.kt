package com.viginfotech.chennaitimes.backend.tamil


import com.googlecode.objectify.Key
import com.viginfotech.chennaitimes.backend.Config
import com.viginfotech.chennaitimes.backend.Constants.CATEGORY_BUSINESS
import com.viginfotech.chennaitimes.backend.Constants.CATEGORY_CINEMA
import com.viginfotech.chennaitimes.backend.Constants.CATEGORY_HEADLINES
import com.viginfotech.chennaitimes.backend.Constants.CATEGORY_TAMILNADU
import com.viginfotech.chennaitimes.backend.Constants.CATEGORY_WORLD
import com.viginfotech.chennaitimes.backend.Constants.SOURCE_DINAMALAR
import com.viginfotech.chennaitimes.backend.model.Feed
import com.viginfotech.chennaitimes.backend.service.OfyService.Companion.ofy
import com.viginfotech.chennaitimes.backend.utils.QueryUtils
import com.viginfotech.chennaitimes.backend.utils.UpdateHelper.removeDuplicates
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException
import java.util.*


/**
 * Created by anand on 1/3/16.
 */
class Dinamalar {
    companion object {


        fun fetchDinamalarNews(category: Int): List<Feed>? {
            return DinamalarParser.parseFeed(UriFetch.fetchData(getUri(SOURCE_DINAMALAR, category)!!), category)
        }

        fun queryDinamalarNews(category: Int): List<Feed>? {

            var feedList: List<Feed>? = QueryUtils.queryCategorySortbyPubDate(SOURCE_DINAMALAR, category)
            if (feedList!!.isEmpty()) {
                println("Fetching from net $category dinamalar")
                feedList = fetchDinamalarNews(category)
                if (feedList != null) {

                    feedList = removeDuplicates(SOURCE_DINAMALAR, Arrays.asList<Int>(category), feedList)
                    println("filtered size dinamalar" + feedList!!.size)
                    feedList = if (feedList.isNotEmpty()) {
                        ofy().save().entities(feedList).now()

                        QueryUtils.queryCategorySortbyPubDate(SOURCE_DINAMALAR, category)
                    } else {
                        QueryUtils.queryLatest7Feeds(SOURCE_DINAMALAR, category)
                    }

                }
            }


            return feedList
        }


        fun getDetail(guid: String, categroy: Int): Feed? {
            return when (categroy) {
                CATEGORY_BUSINESS -> readBusinessNews(guid)
                CATEGORY_CINEMA -> readCinema(guid)
                else -> getNews(guid)
            }
        }

        private fun getNews(guid: String): Feed? {
            val doc: Document
            val builder = StringBuilder()
            var detailedTitle: String? = null
            var imgSrc: String? = null
            try {
                doc = Jsoup.connect(guid).get()


                val newsdetbd = doc.getElementsByClass("newsdetbd1")


                if (newsdetbd != null && newsdetbd.size > 0) {

                    detailedTitle = newsdetbd[0].text()


                    val description = doc.getElementById("clsclk")
                    val paragraph = description.getElementsByTag("p")
                    for (p in paragraph) {
                        if (!p.text().isEmpty())
                            builder.append(p.text() + "\n\n")

                    }
                    val img = description.getElementsByTag("img")
                    if (img != null && img.size > 0) {

                        imgSrc = img[0].attr("src")
                    }
                } else if (newsdetbd != null && newsdetbd.size == 0) {
                    val row = doc.getElementsByClass("row")
                    val element = row.iterator()
                    while (element.hasNext()) {
                        val next = element.next()
                        builder.append(next.getElementsByTag("p").text())
                        val img = next.getElementsByTag("img")
                        if (img != null) {
                            imgSrc = img.attr("src")
                        }
                    }
                }
                val detailDescription = builder.toString().trim { it <= ' ' }

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

            } catch (e: IOException) {

                e.printStackTrace()
                return null
            }


        }

        private fun readCinema(guid: String): Feed? {

            val doc: Document
            val builder = StringBuilder()
            var detailedTitle: String? = null
            var imgSrc: String? = null
            try {
                doc = Jsoup.connect(guid).get()
                val selImpNews = doc.getElementById("selImpnews")
                val titleElements = selImpNews.getElementsByTag("h2")
                if (titleElements != null) {
                    detailedTitle = titleElements.text()
                }
                val para = selImpNews.getElementsByTag("p")
                for (p in para) {
                    builder.append(p.text() + "\n\n")
                }
                val img = selImpNews.getElementsByTag("img")
                if (img != null && img.size > 0) {
                    imgSrc = img[0].attr("src")
                }
                val detailDescription = builder.toString().trim { it <= ' ' }

                val key = Key.create(Feed::class.java, guid)
                val feed = ofy().load().key(key).now()
                if (detailedTitle != null) feed.detailedTitle = detailedTitle
                feed.image = imgSrc
                feed.detailNews = detailDescription
                ofy().save().entity(feed).now()

                return feed

            } catch (ex: Exception) {
                ex.printStackTrace()
                return null
            }

        }

        private fun readBusinessNews(link: String): Feed? {

            val doc: Document
            val builder = StringBuilder()
            var detailedTitle: String? = null
            var imgSrc: String? = null
            try {
                doc = Jsoup.connect(link).get()
                val detailedWd = doc.getElementsByClass("newsdetwd")
                if (detailedWd != null) {
                    detailedTitle = detailedWd[0].text()
                }
                val selNews = doc.getElementById("selNews")
                if (selNews != null) {
                    val paragraph = selNews.getElementsByTag("p")
                    if (paragraph.size > 0) {
                        for (p in paragraph) {
                            builder.append(p.text() + "\n\n")
                        }
                    }
                    val clsclk = doc.getElementById("clsclk")
                    if (clsclk != null) {
                        val img = clsclk.getElementsByTag("img")
                        if (img != null && img.size > 0) {
                            imgSrc = img[0].attr("src")
                        }
                    }

                } else {
                    val clsclk = doc.getElementById("clsclk")
                    val all = clsclk.allElements

                    for (i in 10 until all.size) {

                        builder.append(all[i].text())
                    }
                    val img = clsclk.getElementsByTag("img")
                    if (img != null && img.size > 0) {
                        imgSrc = img[0].attr("src")
                    }
                }

                val detailDescription = builder.toString().trim { it <= ' ' }

                val key = Key.create(Feed::class.java, link)

                val feed = ofy().load().key(key).now()
                if (detailedTitle != null)
                    feed.detailedTitle = detailedTitle
                feed.image = imgSrc
                feed.detailNews = detailDescription
                ofy().save().entity(feed).now()

                return feed
            } catch (ex: Exception) {
                ex.printStackTrace()
                return null
            }

        }

        private fun getUri(source: Int, category: Int): String? {
            when (source) {
                SOURCE_DINAMALAR -> when (category) {

                    CATEGORY_HEADLINES -> return Config.Dinamalar.DINAMALAR_HEADLINES_URI
                    CATEGORY_TAMILNADU -> return Config.Dinamalar.DINAMALAR_TAMILNADU_URI

                    CATEGORY_WORLD -> return Config.Dinamalar.DINAMALAR_WORLD_URI
                    CATEGORY_BUSINESS -> return Config.Dinamalar.DINAMALAR_BUSINESS_URI
                    CATEGORY_CINEMA -> return Config.Dinamalar.DINAMALAR_CINEMA_URI
                    else -> return null
                }
            }
            return null
        }
    }
}

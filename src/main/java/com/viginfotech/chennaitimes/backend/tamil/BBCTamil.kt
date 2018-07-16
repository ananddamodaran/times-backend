package com.viginfotech.chennaitimes.backend.tamil


import com.googlecode.objectify.Key
import com.viginfotech.chennaitimes.backend.Config
import com.viginfotech.chennaitimes.backend.Constants
import com.viginfotech.chennaitimes.backend.model.Feed
import com.viginfotech.chennaitimes.backend.utils.QueryUtils
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

import java.util.Arrays

import com.viginfotech.chennaitimes.backend.Constants.CATEGORY_HEADLINES
import com.viginfotech.chennaitimes.backend.Constants.CATEGORY_INDIA
import com.viginfotech.chennaitimes.backend.Constants.CATEGORY_SPORTS
import com.viginfotech.chennaitimes.backend.Constants.CATEGORY_WORLD
import com.viginfotech.chennaitimes.backend.Constants.SOURCE_BBCTAMIL
import com.viginfotech.chennaitimes.backend.service.OfyService.Companion.ofy
import com.viginfotech.chennaitimes.backend.utils.UpdateHelper.removeDuplicates


/**
 * Created by anand on 1/20/16.
 */
class BBCTamil {
    companion object {

        private fun getUri(category: Int): String {
            when (category) {
                CATEGORY_HEADLINES -> return Config.BBCTamil.BBCTAMIL_HEADLINES
                CATEGORY_INDIA -> return Config.BBCTamil.BBCTAMIL_INDIA
                CATEGORY_WORLD -> return Config.BBCTamil.BBCTAMIL_WORLD
                CATEGORY_SPORTS -> return Config.BBCTamil.BBCTAMIL_SPORTS
                else -> return ""
            }
        }

        fun queryBBCNews(category: Int): List<Feed>? {
            var feedList: List<Feed>? = null
            when (category) {
                CATEGORY_HEADLINES, CATEGORY_INDIA, CATEGORY_WORLD, CATEGORY_SPORTS -> {

                    feedList = QueryUtils.queryCategorySortbyPubDate(SOURCE_BBCTAMIL, category)
                    if (feedList.size == 0) {
                        println("Fetching from net $category BBC")
                        feedList = BBCTamilParser.parseFeed(UriFetch.fetchData(getUri(category)), category)
                        if (feedList != null) {
                            feedList = removeDuplicates(SOURCE_BBCTAMIL, Arrays.asList<Int>(category), feedList)
                            println("filtered size oneindia" + feedList.size)
                            if (feedList.size > 0) {
                               ofy().save().entities(feedList).now()
                                feedList = QueryUtils.queryCategorySortbyPubDate(SOURCE_BBCTAMIL, category)
                            } else {
                                feedList = QueryUtils.queryLatest7Feeds(SOURCE_BBCTAMIL, category)
                            }

                        }
                    }
                    return feedList
                }


                else -> return null
            }


        }

        fun fetchBBCNews(category: Int): List<Feed>? {
            var feedList: List<Feed>? = null
            when (category) {
                CATEGORY_HEADLINES -> {
                    System.out.println("Fetching from net " + CATEGORY_HEADLINES)
                    feedList = BBCTamilParser.parseFeed(UriFetch.fetchData(Config.BBCTamil.BBCTAMIL_HEADLINES), CATEGORY_HEADLINES)
                    return feedList
                }

                CATEGORY_INDIA -> {
                    System.out.println("Fetching from net " + CATEGORY_INDIA)
                    feedList = BBCTamilParser.parseFeed(UriFetch.fetchData(Config.BBCTamil.BBCTAMIL_INDIA), category)
                    return feedList
                }

                CATEGORY_WORLD-> {
                    System.out.println("Fetching from net " + CATEGORY_WORLD)
                    feedList = BBCTamilParser.parseFeed(UriFetch.fetchData(Config.BBCTamil.BBCTAMIL_WORLD), category)
                    return feedList
                }

                CATEGORY_SPORTS -> {
                    System.out.println("Fetching from net " + CATEGORY_SPORTS)
                    feedList = BBCTamilParser.parseFeed(UriFetch.fetchData(Config.BBCTamil.BBCTAMIL_SPORTS),
                            category)
                    return feedList
                }


                else -> return null
            }


        }

        fun getDetail(guid: String): Feed? {
            try {
                val doc: Document
                doc = Jsoup.connect(guid).get()
                val story_board = doc.getElementsByClass("story-body")
                var detailDescription: String? = null
                var detailedTitle: String? = null
                var imgSrc: String? = null
                if (story_board != null && story_board.size > 0) {
                    detailedTitle = story_board[0].getElementsByClass("story-body__h1")[0].text().trim { it <= ' ' }
                }
                var story_body: Elements? = doc.getElementsByClass("map-body")
                if (story_body != null && story_body.size > 0) {
                    detailDescription = getStory(story_body)

                } else {
                    story_body = doc.getElementsByClass("story-body__inner")
                    if (story_body != null && story_body.size > 0) {
                        val img = story_body[0].getElementsByTag("img")

                        if (img != null && img.size > 0) {
                            imgSrc = img[0].attr("src")
                        }


                        detailDescription = getStory(story_body)
                    }
                }
                val key = Key.create(Feed::class.java, guid)
                val feed =ofy().load().key(key).now()
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

        private fun getStory(body: Elements): String? {
            if (body.size > 0) {
                val detailText = body[0].getElementsByTag("p")
                if (detailText != null) {
                    val builder = StringBuilder()
                    for (p in detailText) {

                        builder.append(p.text())
                        builder.append("\n")
                    }
                    return builder.toString()
                }
            }
            return null

        }
    }
}





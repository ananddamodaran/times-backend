package com.viginfotech.chennaitimes.backend.tamil


import com.viginfotech.chennaitimes.backend.Constants
import com.viginfotech.chennaitimes.backend.Constants.SOURCE_BBCTAMIL
import com.viginfotech.chennaitimes.backend.model.Feed
import com.viginfotech.chennaitimes.backend.utils.TimeUtils
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory

import java.io.StringReader
import java.util.ArrayList
import java.util.Date

/**
 * Created by anand on 8/30/15.
 */
object BBCTamilParser {
    private val TAG = BBCTamilParser::class.java.simpleName

    fun parseFeed(content: String?, categroy: Int): List<Feed>? {
        var content = content

        content = if (content != null) content.replace("&".toRegex(), "&amp;") else ""
        content = if (content != null) content.replace("%3F".toRegex(), "&#63;") else ""
        val newsList = ArrayList<Feed>()
        try {
            var inDataItemTag = false
            var currentTagName = ""
            var newsItem: Feed? = null


            val factory = XmlPullParserFactory.newInstance()
            val parser = factory.newPullParser()
            parser.setInput(StringReader(content))

            var eventType = parser.eventType
            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        currentTagName = parser.name
                        if (currentTagName == "entry") {
                            inDataItemTag = true
                            newsItem = Feed()
                            newsItem.categoryId = categroy
                            newsItem.sourceId = SOURCE_BBCTAMIL
                            newsList.add(newsItem)
                        }


                        if (currentTagName == "link" && newsItem != null) {

                            if (parser.getAttributeValue(null, "href") != null) {
                                newsItem.guid = parser.getAttributeValue(null, "href")
                            }
                        }

                        if (currentTagName == "media:thumbnail" && newsItem != null) {
                            if (parser.getAttributeValue(null, "url") != null) {
                                newsItem.thumbnail = parser.getAttributeValue(null, "url")
                            }
                        }

                        if (currentTagName == "img" && newsItem != null) {
                            if (parser.getAttributeValue(null, "src") != null) {
                                newsItem.thumbnail = parser.getAttributeValue(null, "src")
                            }
                        }
                    }
                    XmlPullParser.END_TAG -> {
                        if (parser.name == "entry") {
                            inDataItemTag = false
                        }
                        currentTagName = ""
                    }

                    XmlPullParser.TEXT -> if (inDataItemTag && newsItem != null) {
                        when (currentTagName) {
                            "title" -> newsItem.title = parser.text
                            "summary" -> newsItem.summary = parser.text
                            "published" -> {
                                val now = System.currentTimeMillis()
                                val date = TimeUtils.parseTimeStamp(parser.text, SOURCE_BBCTAMIL)
                                if (date != null)
                                    newsItem.pubDate = date.time
                                else
                                    newsItem.pubDate = now
                            }
                            else -> {
                            }
                        }
                    }
                }
                eventType = parser.next()
            }


            return newsList
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

    }


}




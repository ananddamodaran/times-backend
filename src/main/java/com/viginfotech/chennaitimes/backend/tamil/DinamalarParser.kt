package com.viginfotech.chennaitimes.backend.tamil


import com.viginfotech.chennaitimes.backend.Constants
import com.viginfotech.chennaitimes.backend.Constants.CATEGORY_CINEMA
import com.viginfotech.chennaitimes.backend.Constants.SOURCE_DINAMALAR
import com.viginfotech.chennaitimes.backend.model.Feed
import com.viginfotech.chennaitimes.backend.utils.TimeUtils
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.StringReader
import java.util.*

/**
 * Created by anand on 8/30/15.
 */
object DinamalarParser {
    private val TAG = DinamalarParser::class.java.simpleName

    fun parseFeed(content: String?, categroy: Int): List<Feed>? {
        var content = content

        content = if (content != null) content.replace("&".toRegex(), "&amp;") else ""
        content = if (content != null) content.replace("%3F".toRegex(), "&#63;") else ""

        try {
            var inDataItemTag = false
            var currentTagName = ""
            val now = System.currentTimeMillis()
            var newsItem: Feed? = null
            val newsList = ArrayList<Feed>()

            val factory = XmlPullParserFactory.newInstance()
            val parser = factory.newPullParser()
            parser.setInput(StringReader(content))

            var eventType = parser.eventType
            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        currentTagName = parser.name
                        if (currentTagName == "item") {
                            inDataItemTag = true
                            newsItem = Feed()
                            newsItem.categoryId = categroy
                            newsItem.sourceId = SOURCE_DINAMALAR
                            newsList.add(newsItem)
                        }
                    }
                    XmlPullParser.END_TAG -> {
                        if (parser.name == "item") {
                            inDataItemTag = false
                        }
                        currentTagName = ""
                    }

                    XmlPullParser.TEXT -> if (inDataItemTag && newsItem != null) {
                        when (currentTagName) {
                            "title" -> newsItem.title = parser.text
                            "link" -> newsItem.guid = parser.text

                            "description" -> {

                                val description = parser.text
                                newsItem.summary = description
                                var imgSrc: String? = null
                                if (description.contains("src") && description.contains(".jpg")) {
                                    imgSrc = description.substring(description.indexOf("src") + 5, description.indexOf("jpg") + 3)
                                }
                                newsItem.thumbnail = imgSrc
                            }
                            "pubDate" -> {

                                val date = TimeUtils.parseTimeStamp(parser.text, SOURCE_DINAMALAR)
                                if (date != null && categroy != CATEGORY_CINEMA)
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




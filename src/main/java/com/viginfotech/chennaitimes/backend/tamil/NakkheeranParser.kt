package com.viginfotech.chennaitimes.backend.tamil


import com.viginfotech.chennaitimes.backend.Constants.SOURCE_NAKKHEERAN
import com.viginfotech.chennaitimes.backend.model.Feed
import com.viginfotech.chennaitimes.backend.utils.TimeUtils
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.StringReader
import java.util.*

/**
 * Created by anand on 8/30/15.
 */
object NakkheeranParser {
    private val TAG = NakkheeranParser::class.java.simpleName

    fun parseFeed(content: String?, category: Int): List<Feed>? {
        var content = content

        content = if (content != null) content.replace("&".toRegex(), "&amp;") else ""
        content = if (content != null) content.replace("%3F".toRegex(), "&#63;") else ""

        try {
            var inDataItemTag = false
            var currentTagName = ""
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
                            newsItem.categoryId = category
                            newsItem.sourceId = SOURCE_NAKKHEERAN
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
                            "link" -> newsItem.guid = parser.text.trim { it <= ' ' }

                            "pubDate" -> {
                                val now = System.currentTimeMillis()
                                val parserTime = parser.text
                                val date = TimeUtils.parseTimeStamp(parserTime, SOURCE_NAKKHEERAN)
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




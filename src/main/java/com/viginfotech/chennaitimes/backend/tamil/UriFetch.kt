package com.viginfotech.chennaitimes.backend.tamil


import com.viginfotech.chennaitimes.backend.Constants
import com.viginfotech.chennaitimes.backend.model.Feed
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

/**
 * Created by anand on 12/3/15.
 */
object UriFetch {

    fun fetchData(uri: String): String? {

        var reader: BufferedReader? = null
        try {
            val url = URL(uri)
            val con = url.openConnection() as HttpURLConnection
            val builder = StringBuilder()
            reader = BufferedReader(InputStreamReader(con.inputStream, "UTF-8"))

            var line = ""
            while ((line in reader.readLine()) != null) {
                builder.append(line + "\n")
            }
            return builder.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        } finally {
            if (reader != null) {
                try {
                    reader.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                    return null
                }

            }
        }
    }

    fun fetchDinakaranData(categoryId: Int, uri: String): List<Feed> {
        val doc: Document
        val feedList = ArrayList<Feed>()
        try {
            doc = Jsoup.connect(uri).timeout(10000).get()

            val newslist = doc.getElementsByClass("news-list-page")
            if (newslist != null) {

                val ul = doc.select("div.news-list-page > ul")
                val li = ul.select("li")
                for (i in li.indices) {
                    val title = li[i].select("h1 > a").text()
                    val guid = "http://www.dinakaran.com/" + li[i].select("h1 > a").attr("href")

                    val summary = li[i].select("p").text()
                    val pubDate = li[i].select("span").text()
                    val feed = Feed()
                    feed.title = title

                    feed.guid = guid
                    feed.summary = summary
                    val now = System.currentTimeMillis()
                    feed.pubDate = now


                    feed.categoryId = categoryId
                    feed.sourceId = Constants.SOURCE_DINAKARAN
                    feedList.add(feed)
                }


            }


        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return feedList
    }


    fun fetchOneIndiaData(category: Int, uri: String): List<Feed> {
        val doc: Document
        val feedList = ArrayList<Feed>()
        try {
            doc = Jsoup.connect(uri).userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21").timeout(10000).get()
            val element = doc.getElementById("collection-wrapper")
            if (element != null) {
                val articles = element.getElementsByTag("article")


                for (article in articles) {
                    val feed = Feed()

                    val heading = article.getElementsByClass("collection-heading")
                    if (heading != null) {

                        feed.title = heading[0].text()
                    }

                    val articleImg: String? = null
                    val articleImgClass = article.getElementsByClass("article-img")
                    if (articleImgClass != null) {
                        feed.thumbnail = articleImgClass[0].getElementsByTag("img")[0].attr("src")
                    }


                    val link = article.getElementsByTag("a")[1].attr("href")

                    feed.guid = "http://tamil.oneindia.com$link"
                    feed.categoryId = category
                    feed.sourceId = Constants.SOURCE_ONEINDIA

                    val now = System.currentTimeMillis()

                    feed.pubDate = now

                    feedList.add(feed)
                }
            }

        } catch (e: IOException) {

            e.printStackTrace()
        }

        return feedList
    }

}

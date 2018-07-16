package com.viginfotech.chennaitimes.backend.utils


import com.googlecode.objectify.Key
import com.viginfotech.chennaitimes.backend.Config
import com.viginfotech.chennaitimes.backend.Constants.CATEGORY_BUSINESS
import com.viginfotech.chennaitimes.backend.Constants.CATEGORY_CINEMA
import com.viginfotech.chennaitimes.backend.Constants.CATEGORY_HEADLINES
import com.viginfotech.chennaitimes.backend.Constants.CATEGORY_INDIA
import com.viginfotech.chennaitimes.backend.Constants.CATEGORY_SPORTS
import com.viginfotech.chennaitimes.backend.Constants.CATEGORY_TAMILNADU
import com.viginfotech.chennaitimes.backend.Constants.CATEGORY_WORLD
import com.viginfotech.chennaitimes.backend.Constants.SOURCE_DINAMANI
import com.viginfotech.chennaitimes.backend.model.Feed
import com.viginfotech.chennaitimes.backend.service.OfyService.Companion.ofy
import com.viginfotech.chennaitimes.backend.tamil.*
import java.util.*


/**
 * Created by anand on 1/26/16.
 */
object UpdateHelper {

    internal var predicate: FPredicate<Feed> = object : FPredicate<Feed> {
        override fun test(feed: Feed, lastFeedInDB: Feed): Boolean {
            return feed.sourceId == lastFeedInDB.sourceId &&
                    feed.categoryId == lastFeedInDB.categoryId &&
                    feed.pubDate!! > lastFeedInDB.pubDate!! &&
                    feed.guid != lastFeedInDB.guid
        }

        override fun getTlistforThisCatagory(tlist: List<Feed>, category: Int): List<Feed> {
            val feedList = ArrayList<Feed>()

            for (feed in tlist) {
                if (feed.categoryId == category) {
                    feedList.add(feed)
                }
            }
            return feedList

        }
    }

    fun updateNakkheeran(): List<Feed> {
        println("nakkheeran")
        val headLinesFeed = Nakkheeran.fetchNakkheeran(CATEGORY_HEADLINES, Config.Nakkheeran.NAKKHEERAN_HEADLINES)
        val tamilNaduFeed = Nakkheeran.fetchNakkheeran(CATEGORY_TAMILNADU, Config.Nakkheeran.NAKKHEERAN_TAMILNADU)
        val indiaFeed = Nakkheeran.fetchNakkheeran(CATEGORY_INDIA, Config.Nakkheeran.NAKKHEERAN_INDIA)
        val worldFeed = Nakkheeran.fetchNakkheeran(CATEGORY_WORLD, Config.Nakkheeran.NAKKHEERAN_WORLD)
        val sportFeed = Nakkheeran.fetchNakkheeran(CATEGORY_SPORTS, Config.Nakkheeran.NAKKHEERAN_SPORTS)

        val allFeeds = ArrayList<Feed>()
        if (headLinesFeed != null) {

            allFeeds.addAll(headLinesFeed)
        }
        if (tamilNaduFeed != null) {

            allFeeds.addAll(tamilNaduFeed)

        }
        if (indiaFeed != null) {

            allFeeds.addAll(indiaFeed)

        }
        if (worldFeed != null) {

            allFeeds.addAll(worldFeed)
        }
        if (sportFeed != null) {

            allFeeds.addAll(sportFeed)
        }
        return allFeeds

    }

    fun updateOneIndia(): List<Feed> {
        println("OneIndia")
        val feedListtamil = UriFetch.fetchOneIndiaData(CATEGORY_TAMILNADU, Config.OneIndia.ONEINDIA_TAMILNADU)
        val feedListIndia = UriFetch.fetchOneIndiaData(CATEGORY_INDIA, Config.OneIndia.ONEINDIA_INDIA)
        val feedListWorld = UriFetch.fetchOneIndiaData(CATEGORY_WORLD, Config.OneIndia.ONEINDIA_WORLD)
        val feedListBusiness = UriFetch.fetchOneIndiaData(CATEGORY_BUSINESS, Config.OneIndia.ONEINDIA_BUSINESS)

        val allFeeds = ArrayList<Feed>()
        if (feedListtamil != null) {
            println("tamil " + feedListtamil.size)


            allFeeds.addAll(feedListtamil)
        }
        if (feedListIndia != null) {
            println("india " + feedListIndia.size)


            allFeeds.addAll(feedListIndia)
        }
        if (feedListWorld != null) {
            println("world " + feedListWorld.size)


            allFeeds.addAll(feedListWorld)
        }
        if (feedListBusiness != null) {
            println("business " + feedListBusiness.size)


            allFeeds.addAll(feedListBusiness)
        }

        println("oneIndia " + allFeeds.size)
        return allFeeds

    }

    fun updateDinakaran(): List<Feed> {
        println("dinakaran")
        val feedListtamil = UriFetch.fetchDinakaranData(CATEGORY_TAMILNADU, Config.Dinakaran.DINAKARAN_TAMILNADU_URI)
        val feedListIndia = UriFetch.fetchDinakaranData(CATEGORY_INDIA, Config.Dinakaran.DINAKARAN_INDIA_URI)
        val feedListWorld = UriFetch.fetchDinakaranData(CATEGORY_WORLD, Config.Dinakaran.DINAKARAN_WORLD_URI)
        val feedListBusiness = UriFetch.fetchDinakaranData(CATEGORY_BUSINESS, Config.Dinakaran.DINAKARAN_BUSINESS_URI)
        val feedListSports = UriFetch.fetchDinakaranData(CATEGORY_SPORTS, Config.Dinakaran.DINAKARAN_SPORTS_URI)

        val allFeeds = ArrayList<Feed>()
        if (feedListtamil != null) {
            println("tamilnadu " + feedListtamil.size)

            allFeeds.addAll(feedListtamil)

        }
        if (feedListIndia != null) {
            println("india " + feedListIndia.size)

            allFeeds.addAll(feedListIndia)
        }
        if (feedListWorld != null) {
            println("world " + feedListWorld.size)

            allFeeds.addAll(feedListWorld)
        }
        if (feedListBusiness != null) {
            println("business " + feedListtamil.size)

            allFeeds.addAll(feedListBusiness)
        }
        if (feedListSports != null) {
            println("sports " + feedListtamil.size)

            allFeeds.addAll(feedListSports)
        }
        return allFeeds


    }

    fun updateBBCTamil(): List<Feed> {
        println("bbctamil")
        val indiaFeed = BBCTamil.fetchBBCNews(CATEGORY_INDIA)
        val worldFeed = BBCTamil.fetchBBCNews(CATEGORY_WORLD)

        val allFeeds = ArrayList<Feed>()

        if (indiaFeed != null) {
            println("india " + indiaFeed.size)

            allFeeds.addAll(indiaFeed)
        }
        if (worldFeed != null) {
            println("world " + worldFeed.size)


            allFeeds.addAll(worldFeed)
        }

        println("bbcTamil " + allFeeds.size)
        return allFeeds


    }

    private fun addNewFeed(source: Int, category: Int): Feed {
        val feed = Feed()
        feed.title = "NewFeed" + Math.random()
        feed.summary = "summary" + Math.random()
        feed.sourceId = SOURCE_DINAMANI
        feed.categoryId = 0
        feed.guid = "http://www.google.com" + Math.random()
        feed.pubDate = System.currentTimeMillis()
        ofy().save().entity(feed).now()
        return feed
    }

    fun updateDinamani(): List<Feed> {
        println("dinamani")
        val headLinesFeed = Dinamani.fetchDinamaniNews(CATEGORY_HEADLINES)
        val tamilNaduFeed = Dinamani.fetchDinamaniNews(CATEGORY_TAMILNADU)
        val indiaFeed = Dinamani.fetchDinamaniNews(CATEGORY_INDIA)
        val worldFeed = Dinamani.fetchDinamaniNews(CATEGORY_WORLD)
        val businessFeed = Dinamani.fetchDinamaniNews(CATEGORY_BUSINESS)
        val cinemaFeed = Dinamani.fetchDinamaniNews(CATEGORY_CINEMA)

        val allFeeds = ArrayList<Feed>()
        if (headLinesFeed != null) {


            allFeeds.addAll(headLinesFeed)
        }
        if (tamilNaduFeed != null) {
            println("tamilnadu " + tamilNaduFeed.size)

            allFeeds.addAll(tamilNaduFeed)
        }
        if (indiaFeed != null) {
            println("india " + indiaFeed.size)


            allFeeds.addAll(indiaFeed)
        }
        if (worldFeed != null) {
            println("world " + worldFeed.size)

            allFeeds.addAll(worldFeed)
        }
        if (businessFeed != null) {
            println("business " + businessFeed.size)

            allFeeds.addAll(businessFeed)
        }
        if (cinemaFeed != null) {
            println("cinema " + cinemaFeed.size)

            allFeeds.addAll(cinemaFeed)
        }
        println("dinamani " + allFeeds.size)
        return allFeeds
    }


    fun updateDinamalar(): List<Feed> {
        println("dinamalar")
        val headLinesFeed = Dinamalar.fetchDinamalarNews(CATEGORY_HEADLINES)
        val tamilNaduFeed = Dinamalar.fetchDinamalarNews(CATEGORY_TAMILNADU)
        val worldFeed = Dinamalar.fetchDinamalarNews(CATEGORY_WORLD)
        val businessFeed = Dinamalar.fetchDinamalarNews(CATEGORY_BUSINESS)
        val cinemaFeed = Dinamalar.fetchDinamalarNews(CATEGORY_CINEMA)

        val allFeeds = ArrayList<Feed>()
        if (headLinesFeed != null) {
            println("headlines " + headLinesFeed.size)

            allFeeds.addAll(headLinesFeed)

        }
        if (tamilNaduFeed != null) {
            println("tamilnadu " + tamilNaduFeed.size)

            allFeeds.addAll(tamilNaduFeed)
        }
        if (worldFeed != null) {
            println("worldFeed " + worldFeed.size)

            allFeeds.addAll(worldFeed)
        }
        if (businessFeed != null) {
            println("business " + businessFeed.size)

            allFeeds.addAll(businessFeed)
        }
        if (cinemaFeed != null) {
            println("cinema " + cinemaFeed.size)

            allFeeds.addAll(cinemaFeed)
        }


        println("dinamalar " + allFeeds.size)

        return allFeeds


    }


    fun removeDuplicates(source: Int, categoryList: List<Int>, freshFeeds: List<Feed>): List<Feed> {
        val allfeeds = ArrayList<Feed>()
        val reverse = ArrayList<Feed>()

        println("before filter" + source + " " + freshFeeds.size)
        println()
        println(freshFeeds)
        Collections.sort(freshFeeds, Collections.reverseOrder { lhs, rhs -> lhs.pubDate!!.compareTo(rhs.pubDate!!) })
        println()
        println(freshFeeds)
        for (category in categoryList) {
            val thisCategoryFeeds = predicate.getTlistforThisCatagory(freshFeeds, category)
            Collections.sort(thisCategoryFeeds, Collections.reverseOrder { lhs, rhs -> lhs.pubDate!!.compareTo(rhs.pubDate!!) })
            for (f in thisCategoryFeeds) {


                val key = Key.create(Feed::class.java, f.guid)
                val feed = ofy().load().key(key).now()
                if (feed != null) {
                    println("exists$f")
                    break

                } else {
                    println("not exists so add$f")
                    allfeeds.add(f)
                }
            }

        }
        return allfeeds
    }


    /*private fun <T> filter(lastFeedsInDB: List<Feed>, target: MutableList<T>): List<T> {


        val result = ArrayList<T>()
        println("before removing " + target.size)
        if (target.removeAll(lastFeedsInDB)) {
            println("true" + target.size)
        }
        println("after removing " + target.size)
        return target

    }*/


}

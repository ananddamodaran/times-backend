package com.viginfotech.chennaitimes.backend.apis


import com.google.api.server.spi.config.Api
import com.google.api.server.spi.config.ApiMethod
import com.google.api.server.spi.config.ApiNamespace
import com.google.api.server.spi.config.Named
import com.googlecode.objectify.Key
import com.viginfotech.chennaitimes.backend.Constants.API_OWNER
import com.viginfotech.chennaitimes.backend.Constants.API_PACKAGE_PATH
import com.viginfotech.chennaitimes.backend.Constants.CATEGORY_BUSINESS
import com.viginfotech.chennaitimes.backend.Constants.CATEGORY_CINEMA
import com.viginfotech.chennaitimes.backend.Constants.CATEGORY_HEADLINES
import com.viginfotech.chennaitimes.backend.Constants.CATEGORY_INDIA
import com.viginfotech.chennaitimes.backend.Constants.CATEGORY_SPORTS
import com.viginfotech.chennaitimes.backend.Constants.CATEGORY_TAMILNADU
import com.viginfotech.chennaitimes.backend.Constants.CATEGORY_WORLD
import com.viginfotech.chennaitimes.backend.Constants.SOURCE_BBCTAMIL
import com.viginfotech.chennaitimes.backend.Constants.SOURCE_DINAKARAN
import com.viginfotech.chennaitimes.backend.Constants.SOURCE_DINAMALAR
import com.viginfotech.chennaitimes.backend.Constants.SOURCE_DINAMANI
import com.viginfotech.chennaitimes.backend.Constants.SOURCE_NAKKHEERAN
import com.viginfotech.chennaitimes.backend.Constants.SOURCE_ONEINDIA
import com.viginfotech.chennaitimes.backend.model.Feed
import com.viginfotech.chennaitimes.backend.service.OfyService.Companion.ofy
import com.viginfotech.chennaitimes.backend.tamil.*
import java.util.*


/**
 * Created by anand on 1/22/16.
 */
@Api(name = "chennaiTimesApi", version = "v1", namespace = ApiNamespace(ownerDomain = API_OWNER, ownerName = API_OWNER, packagePath = API_PACKAGE_PATH))
class ChennaiTimesEndpoint {
    val headLines: List<Feed>
        @ApiMethod(name = "getHeadLines", path = "digitalHuntHeadLines")
        get() {
            val feedList = ArrayList<Feed>()
            val dinamaniFeeds = Dinamani.queryDinamaniNews(CATEGORY_HEADLINES)
            val dinamalarFeeds = Dinamalar.queryDinamalarNews(CATEGORY_HEADLINES)
            val nakkheeranFeeds = Nakkheeran.queryNakkheeranNews(CATEGORY_HEADLINES)

            if (dinamaniFeeds != null)
                feedList.addAll(dinamaniFeeds)

            if (dinamalarFeeds != null)
                feedList.addAll(dinamalarFeeds)

            if (nakkheeranFeeds != null)
                feedList.addAll(nakkheeranFeeds)


            return feedList
        }

    val tamilNaduFeeds: List<Feed>
        @ApiMethod(name = "getTamilNadu", path = "digitalHuntTamilNadu")
        get() {
            val feedList = ArrayList<Feed>()
            val dinakaranFeeds = Dinakaran.queryDinakaranNews(CATEGORY_TAMILNADU)
            val dinamalarFeeds = Dinamalar.queryDinamalarNews(CATEGORY_TAMILNADU)
            val dinamaniFeeds = Dinamani.queryDinamaniNews(CATEGORY_TAMILNADU)
            val oneIndiaFeeds = OneIndia.queryOneIndiaNews(CATEGORY_TAMILNADU)
            val nakkheeranFeeds = Nakkheeran.queryNakkheeranNews(CATEGORY_TAMILNADU)

            if (dinakaranFeeds != null)
                feedList.addAll(dinakaranFeeds)
            if (dinamalarFeeds != null)
                feedList.addAll(dinamalarFeeds)
            if (dinamaniFeeds != null)
                feedList.addAll(dinamaniFeeds)
            if (oneIndiaFeeds != null) {
                feedList.addAll(oneIndiaFeeds)
            }
            if (nakkheeranFeeds != null)
                feedList.addAll(nakkheeranFeeds)

            return feedList
        }

    val indiaFeeds: List<Feed>
        @ApiMethod(name = "getIndiaFeeds", path = "digitalHuntIndia")
        get() {
            val feedList = ArrayList<Feed>()
            val dinakaranFeeds = Dinakaran.queryDinakaranNews(CATEGORY_INDIA)
            val dinamaniFeeds = Dinamani.queryDinamaniNews(CATEGORY_INDIA)
            val oneIndiaFeeds = OneIndia.queryOneIndiaNews(CATEGORY_INDIA)
            val nakkheeranFeeds = Nakkheeran.queryNakkheeranNews(CATEGORY_INDIA)
            val bbcTamilFeeds = BBCTamil.queryBBCNews(CATEGORY_INDIA)

            if (dinakaranFeeds != null)
                feedList.addAll(dinakaranFeeds)
            if (dinamaniFeeds != null)
                feedList.addAll(dinamaniFeeds)

            if (oneIndiaFeeds != null) {
                feedList.addAll(oneIndiaFeeds)
            }
            if (nakkheeranFeeds != null)
                feedList.addAll(nakkheeranFeeds)

            if (bbcTamilFeeds != null) {
                feedList.addAll(bbcTamilFeeds)
            }
            return feedList
        }

    val worldFeeds: List<Feed>
        @ApiMethod(name = "getWorldFeeds", path = "digitalHuntWorld")
        get() {
            val feedList = ArrayList<Feed>()
            val dinakaranFeeds = Dinakaran.queryDinakaranNews(CATEGORY_WORLD)
            val dinamalarFeeds = Dinamalar.queryDinamalarNews(CATEGORY_WORLD)
            val dinamaniFeeds = Dinamani.queryDinamaniNews(CATEGORY_WORLD)
            val oneIndiaFeeds = OneIndia.queryOneIndiaNews(CATEGORY_WORLD)
            val nakkheeranFeeds = Nakkheeran.queryNakkheeranNews(CATEGORY_WORLD)
            val bbcTamilFeeds = BBCTamil.queryBBCNews(CATEGORY_WORLD)


            if (dinakaranFeeds != null)
                feedList.addAll(dinakaranFeeds)
            if (dinamaniFeeds != null)
                feedList.addAll(dinamaniFeeds)
            if (dinamalarFeeds != null) {
                feedList.addAll(dinamalarFeeds)
            }
            if (oneIndiaFeeds != null) {
                feedList.addAll(oneIndiaFeeds)
            }
            if (nakkheeranFeeds != null)
                feedList.addAll(nakkheeranFeeds)

            if (bbcTamilFeeds != null) {
                feedList.addAll(bbcTamilFeeds)
            }
            return feedList
        }

    val businessFeeds: List<Feed>
        @ApiMethod(name = "getBusinessFeeds", path = "digitalHuntBusiness")
        get() {

            val feedList = ArrayList<Feed>()
            val dinakaranFeeds = Dinakaran.queryDinakaranNews(CATEGORY_BUSINESS)
            val dinamalarFeeds = Dinamalar.queryDinamalarNews(CATEGORY_BUSINESS)
            val dinamaniFeeds = Dinamani.queryDinamaniNews(CATEGORY_BUSINESS)
            val oneIndiaFeeds = OneIndia.queryOneIndiaNews(CATEGORY_BUSINESS)

            if (dinakaranFeeds != null)
                feedList.addAll(dinakaranFeeds)
            if (dinamaniFeeds != null)
                feedList.addAll(dinamaniFeeds)
            if (dinamalarFeeds != null) {
                feedList.addAll(dinamalarFeeds)
            }
            if (oneIndiaFeeds != null) {
                feedList.addAll(oneIndiaFeeds)
            }

            return feedList
        }

    val sportseeds: List<Feed>
        @ApiMethod(name = "getSportsFeeds", path = "digitalHuntSports")
        get() {
            val feedList = ArrayList<Feed>()
            val dinakaranFeeds = Dinakaran.queryDinakaranNews(CATEGORY_SPORTS)
            val nakkheeranFeeds = Nakkheeran.queryNakkheeranNews(CATEGORY_SPORTS)


            if (dinakaranFeeds != null)
                feedList.addAll(dinakaranFeeds)

            if (nakkheeranFeeds != null)
                feedList.addAll(nakkheeranFeeds)


            return feedList
        }

    val cinemaFeeds: List<Feed>
        @ApiMethod(name = "getCinemaFeeds", path = "digitalHuntCinema")
        get() {
            val feedList = ArrayList<Feed>()
            val dinamalarFeeds = Dinamalar.queryDinamalarNews(CATEGORY_CINEMA)
            val dinamaniFeeds = Dinamani.queryDinamaniNews(CATEGORY_CINEMA)


            if (dinamalarFeeds != null)
                feedList.addAll(dinamalarFeeds)

            if (dinamaniFeeds != null)
                feedList.addAll(dinamaniFeeds)


            return feedList
        }

    val shortURL: Feed?
        @ApiMethod(name = "getShortURL")
        get() = null


    @ApiMethod(name = "getNewsDetail", path = "digitalHunt")
    fun getNewsDetail(@Named("source") source: Int, @Named("category") category: Int, @Named("guid") guid: String): Feed? {
        val key = Key.create(Feed::class.java, guid)
        val feed = ofy().load().key(key).now()
        return if (feed.detailNews != null)
            feed
        else {
            when (source) {
                SOURCE_DINAKARAN -> Dinakaran.getDinakaranDetail(guid)
                SOURCE_DINAMALAR -> Dinamalar.getDetail(guid, category)
                SOURCE_BBCTAMIL -> BBCTamil.getDetail(guid)
                SOURCE_DINAMANI -> Dinamani.getDinamaniDetail(guid)
                SOURCE_ONEINDIA -> OneIndia.getOneIndiaDetail(guid)
                SOURCE_NAKKHEERAN -> Nakkheeran.getNakkheeranDetail(guid)
                else -> null
            }
        }

    }
}

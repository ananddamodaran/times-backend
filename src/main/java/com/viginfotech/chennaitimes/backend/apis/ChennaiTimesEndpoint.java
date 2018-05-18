package com.viginfotech.chennaitimes.backend.apis;


import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.googlecode.objectify.Key;
import com.viginfotech.chennaitimes.backend.Constants;
import com.viginfotech.chennaitimes.backend.model.Feed;
import com.viginfotech.chennaitimes.backend.tamil.*;


import java.util.ArrayList;
import java.util.List;

import static com.viginfotech.chennaitimes.backend.Constants.*;
import static com.viginfotech.chennaitimes.backend.service.OfyService.ofy;


/**
 * Created by anand on 1/22/16.
 */
@Api(
        name = "chennaiTimesApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = Constants.API_OWNER,
                ownerName = Constants.API_OWNER,
                packagePath = Constants.API_PACKAGE_PATH
        ))
public class ChennaiTimesEndpoint {
    @ApiMethod(name = "getHeadLines", path = "digitalHuntHeadLines")
    public List<Feed> getHeadLines() {
        List<Feed> feedList = new ArrayList<>();
        List<Feed> dinamaniFeeds = Dinamani.queryDinamaniNews(Constants.CATEGORY_HEADLINES);
        List<Feed> dinamalarFeeds = Dinamalar.queryDinamalarNews(Constants.CATEGORY_HEADLINES);
        List<Feed> nakkheeranFeeds = Nakkheeran.queryNakkheeranNews(Constants.CATEGORY_HEADLINES);

        if (dinamaniFeeds != null)
            feedList.addAll(dinamaniFeeds);

        if (dinamalarFeeds != null)
            feedList.addAll(dinamalarFeeds);

        if (nakkheeranFeeds != null)
            feedList.addAll(nakkheeranFeeds);


        return feedList;
    }

    @ApiMethod(name = "getTamilNadu", path = "digitalHuntTamilNadu")
    public List<Feed> getTamilNaduFeeds() {
        List<Feed> feedList = new ArrayList<>();
        List<Feed> dinakaranFeeds = Dinakaran.queryDinakaranNews(Constants.CATEGORY_TAMILNADU);
        List<Feed> dinamalarFeeds = Dinamalar.queryDinamalarNews(Constants.CATEGORY_TAMILNADU);
        List<Feed> dinamaniFeeds = Dinamani.queryDinamaniNews(Constants.CATEGORY_TAMILNADU);
        List<Feed> oneIndiaFeeds = OneIndia.queryOneIndiaNews(Constants.CATEGORY_TAMILNADU);
        List<Feed> nakkheeranFeeds = Nakkheeran.queryNakkheeranNews(Constants.CATEGORY_TAMILNADU);

        if (dinakaranFeeds != null)
            feedList.addAll(dinakaranFeeds);
        if (dinamalarFeeds != null)
            feedList.addAll(dinamalarFeeds);
        if (dinamaniFeeds != null)
            feedList.addAll(dinamaniFeeds);
        if (oneIndiaFeeds != null) {
            feedList.addAll(oneIndiaFeeds);
        }
        if (nakkheeranFeeds != null)
            feedList.addAll(nakkheeranFeeds);

        return feedList;
    }

    @ApiMethod(name = "getIndiaFeeds", path = "digitalHuntIndia")
    public List<Feed> getIndiaFeeds() {
        List<Feed> feedList = new ArrayList<>();
        List<Feed> dinakaranFeeds = Dinakaran.queryDinakaranNews(Constants.CATEGORY_INDIA);
        List<Feed> dinamaniFeeds = Dinamani.queryDinamaniNews(Constants.CATEGORY_INDIA);
        List<Feed> oneIndiaFeeds = OneIndia.queryOneIndiaNews(Constants.CATEGORY_INDIA);
        List<Feed> nakkheeranFeeds = Nakkheeran.queryNakkheeranNews(Constants.CATEGORY_INDIA);
        List<Feed> bbcTamilFeeds = BBCTamil.queryBBCNews(Constants.CATEGORY_INDIA);

        if (dinakaranFeeds != null)
            feedList.addAll(dinakaranFeeds);
        if (dinamaniFeeds != null)
            feedList.addAll(dinamaniFeeds);

        if (oneIndiaFeeds != null) {
            feedList.addAll(oneIndiaFeeds);
        }
        if (nakkheeranFeeds != null)
            feedList.addAll(nakkheeranFeeds);

        if (bbcTamilFeeds != null) {
            feedList.addAll(bbcTamilFeeds);
        }
        return feedList;
    }

    @ApiMethod(name = "getWorldFeeds", path = "digitalHuntWorld")
    public List<Feed> getWorldFeeds() {
        List<Feed> feedList = new ArrayList<>();
        List<Feed> dinakaranFeeds = Dinakaran.queryDinakaranNews(Constants.CATEGORY_WORLD);
        List<Feed> dinamalarFeeds = Dinamalar.queryDinamalarNews(Constants.CATEGORY_WORLD);
        List<Feed> dinamaniFeeds = Dinamani.queryDinamaniNews(Constants.CATEGORY_WORLD);
        List<Feed> oneIndiaFeeds = OneIndia.queryOneIndiaNews(Constants.CATEGORY_WORLD);
        List<Feed> nakkheeranFeeds = Nakkheeran.queryNakkheeranNews(Constants.CATEGORY_WORLD);
        List<Feed> bbcTamilFeeds = BBCTamil.queryBBCNews(Constants.CATEGORY_WORLD);


        if (dinakaranFeeds != null)
            feedList.addAll(dinakaranFeeds);
        if (dinamaniFeeds != null)
            feedList.addAll(dinamaniFeeds);
        if (dinamalarFeeds != null) {
            feedList.addAll(dinamalarFeeds);
        }
        if (oneIndiaFeeds != null) {
            feedList.addAll(oneIndiaFeeds);
        }
        if (nakkheeranFeeds != null)
            feedList.addAll(nakkheeranFeeds);

        if (bbcTamilFeeds != null) {
            feedList.addAll(bbcTamilFeeds);
        }
        return feedList;
    }

    @ApiMethod(name = "getBusinessFeeds", path = "digitalHuntBusiness")
    public List<Feed> getBusinessFeeds() {

        List<Feed> feedList = new ArrayList<>();
        List<Feed> dinakaranFeeds = Dinakaran.queryDinakaranNews(Constants.CATEGORY_BUSINESS);
        List<Feed> dinamalarFeeds = Dinamalar.queryDinamalarNews(Constants.CATEGORY_BUSINESS);
        List<Feed> dinamaniFeeds = Dinamani.queryDinamaniNews(Constants.CATEGORY_BUSINESS);
        List<Feed> oneIndiaFeeds = OneIndia.queryOneIndiaNews(Constants.CATEGORY_BUSINESS);

        if (dinakaranFeeds != null)
            feedList.addAll(dinakaranFeeds);
        if (dinamaniFeeds != null)
            feedList.addAll(dinamaniFeeds);
        if (dinamalarFeeds != null) {
            feedList.addAll(dinamalarFeeds);
        }
        if (oneIndiaFeeds != null) {
            feedList.addAll(oneIndiaFeeds);
        }

        return feedList;
    }

    @ApiMethod(name = "getSportsFeeds", path = "digitalHuntSports")
    public List<Feed> getSportseeds() {
        List<Feed> feedList = new ArrayList<>();
        List<Feed> dinakaranFeeds = Dinakaran.queryDinakaranNews(Constants.CATEGORY_SPORTS);
        List<Feed> nakkheeranFeeds = Nakkheeran.queryNakkheeranNews(Constants.CATEGORY_SPORTS);


        if (dinakaranFeeds != null)
            feedList.addAll(dinakaranFeeds);

        if (nakkheeranFeeds != null)
            feedList.addAll(nakkheeranFeeds);


        return feedList;
    }

    @ApiMethod(name = "getCinemaFeeds", path = "digitalHuntCinema")
    public List<Feed> getCinemaFeeds() {
        List<Feed> feedList = new ArrayList<>();
        List<Feed> dinamalarFeeds = Dinamalar.queryDinamalarNews(Constants.CATEGORY_CINEMA);
        List<Feed> dinamaniFeeds = Dinamani.queryDinamaniNews(Constants.CATEGORY_CINEMA);


        if (dinamalarFeeds != null)
            feedList.addAll(dinamalarFeeds);

        if (dinamaniFeeds != null)
            feedList.addAll(dinamaniFeeds);


        return feedList;
    }


    @ApiMethod(name = "getNewsDetail", path = "digitalHunt")
    public Feed getNewsDetail(@Named("source") int source, @Named("category") int category, @Named("guid") String guid) {
        Key<Feed> key = Key.create(Feed.class, guid);
        Feed feed = ofy().load().key(key).now();
        if (feed.getDetailNews() != null) return feed;
        else {
            switch (source) {
                case SOURCE_DINAKARAN:
                    return Dinakaran.getDinakaranDetail(guid);
                case SOURCE_DINAMALAR:
                    return Dinamalar.getDetail(guid, category);
                case SOURCE_BBCTAMIL:
                    return BBCTamil.getDetail(guid);
                case SOURCE_DINAMANI:
                    return Dinamani.getDinamaniDetail(guid);
                case SOURCE_ONEINDIA:
                    return OneIndia.getOneIndiaDetail(guid);
                case SOURCE_NAKKHEERAN:
                    return Nakkheeran.getNakkheeranDetail(guid);
                default:
                    return null;
            }
        }

    }

    @ApiMethod(name = "getShortURL")
    public Feed getShortURL() {
        return null;
    }
}

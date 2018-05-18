package com.viginfotech.chennaitimes.backend.servlet;


import com.viginfotech.chennaitimes.backend.Constants;
import com.viginfotech.chennaitimes.backend.model.Feed;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import static com.viginfotech.chennaitimes.backend.service.OfyService.ofy;
import static com.viginfotech.chennaitimes.backend.utils.UpdateHelper.*;


/**
 * Created by anand on 12/21/15.
 */
public class UpdateServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(UpdateServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        logger.info("updating by cron");
        List<Feed> dinakaranFeeds = updateDinakaran();
        List<Feed> dinamalarFeeds = updateDinamalar();
        List<Feed> bbcTamilFeeds = updateBBCTamil();
        List<Feed> dinamaniFeeds = updateDinamani();
        List<Feed> oneIndiaFeeds = updateOneIndia();
        List<Feed> nakkheeranFeeds = updateNakkheeran();

        List<Feed> allFeeds = new ArrayList<>();


        List<Integer> dinakaranCategory = Arrays.asList(
                Constants.CATEGORY_TAMILNADU,
                Constants.CATEGORY_INDIA,
                Constants.CATEGORY_WORLD,
                Constants.CATEGORY_BUSINESS,
                Constants.CATEGORY_SPORTS);
        List<Integer> dinamalarCategory = Arrays.asList(
                Constants.CATEGORY_HEADLINES,
                Constants.CATEGORY_TAMILNADU,
                Constants.CATEGORY_WORLD,
                Constants.CATEGORY_BUSINESS,
                Constants.CATEGORY_CINEMA);
        List<Integer> bbcCategory = Arrays.asList(
                Constants.CATEGORY_INDIA,
                Constants.CATEGORY_WORLD
        );
        List<Integer> dinamanaiCategory = Arrays.asList(
                Constants.CATEGORY_HEADLINES,
                Constants.CATEGORY_TAMILNADU,
                Constants.CATEGORY_INDIA,
                Constants.CATEGORY_WORLD,
                Constants.CATEGORY_BUSINESS,
                Constants.CATEGORY_SPORTS,
                Constants.CATEGORY_CINEMA
        );

        List<Integer> oneIndiaCategory = Arrays.asList(
                Constants.CATEGORY_TAMILNADU,
                Constants.CATEGORY_INDIA,
                Constants.CATEGORY_WORLD,
                Constants.CATEGORY_BUSINESS
        );

        List<Integer> nakkheeranCategory = Arrays.asList(Constants.CATEGORY_HEADLINES,
                Constants.CATEGORY_TAMILNADU,
                Constants.CATEGORY_INDIA,
                Constants.CATEGORY_WORLD,
                Constants.CATEGORY_SPORTS);
        dinakaranFeeds = removeDuplicates(Constants.SOURCE_DINAKARAN, dinakaranCategory, dinakaranFeeds);
        dinamalarFeeds = removeDuplicates(Constants.SOURCE_DINAMALAR, dinamalarCategory, dinamalarFeeds);
        bbcTamilFeeds = removeDuplicates(Constants.SOURCE_BBCTAMIL, bbcCategory, bbcTamilFeeds);
        dinamaniFeeds = removeDuplicates(Constants.SOURCE_DINAMANI, dinamanaiCategory, dinamaniFeeds);
        oneIndiaFeeds = removeDuplicates(Constants.SOURCE_ONEINDIA, oneIndiaCategory, oneIndiaFeeds);
        nakkheeranFeeds = removeDuplicates(Constants.SOURCE_NAKKHEERAN, nakkheeranCategory, nakkheeranFeeds);

        allFeeds.addAll(dinakaranFeeds);
        allFeeds.addAll(dinamaniFeeds);
        allFeeds.addAll(dinamalarFeeds);
        allFeeds.addAll(oneIndiaFeeds);
        allFeeds.addAll(nakkheeranFeeds);
        allFeeds.addAll(bbcTamilFeeds);

        ofy().save().entities(allFeeds).now();


        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);


    }


}

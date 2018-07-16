package com.viginfotech.chennaitimes.backend.servlet


import com.viginfotech.chennaitimes.backend.Constants
import com.viginfotech.chennaitimes.backend.model.Feed
import com.viginfotech.chennaitimes.backend.service.OfyService.Companion.ofy
import com.viginfotech.chennaitimes.backend.utils.UpdateHelper.removeDuplicates
import com.viginfotech.chennaitimes.backend.utils.UpdateHelper.updateBBCTamil
import com.viginfotech.chennaitimes.backend.utils.UpdateHelper.updateDinakaran
import com.viginfotech.chennaitimes.backend.utils.UpdateHelper.updateDinamalar
import com.viginfotech.chennaitimes.backend.utils.UpdateHelper.updateDinamani
import com.viginfotech.chennaitimes.backend.utils.UpdateHelper.updateNakkheeran
import com.viginfotech.chennaitimes.backend.utils.UpdateHelper.updateOneIndia
import java.io.IOException
import java.util.*
import java.util.logging.Logger
import javax.servlet.ServletException
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


/**
 * Created by anand on 12/21/15.
 */
class UpdateServlet : HttpServlet() {

    @Throws(ServletException::class, IOException::class)
    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {

        logger.info("updating by cron")
        var dinakaranFeeds = updateDinakaran()
        var dinamalarFeeds = updateDinamalar()
        var bbcTamilFeeds = updateBBCTamil()
        var dinamaniFeeds = updateDinamani()
        var oneIndiaFeeds = updateOneIndia()
        var nakkheeranFeeds = updateNakkheeran()

        val allFeeds = ArrayList<Feed>()


        val dinakaranCategory = Arrays.asList(
                Constants.CATEGORY_TAMILNADU,
                Constants.CATEGORY_INDIA,
                Constants.CATEGORY_WORLD,
                Constants.CATEGORY_BUSINESS,
                Constants.CATEGORY_SPORTS)
        val dinamalarCategory = Arrays.asList(
                Constants.CATEGORY_HEADLINES,
                Constants.CATEGORY_TAMILNADU,
                Constants.CATEGORY_WORLD,
                Constants.CATEGORY_BUSINESS,
                Constants.CATEGORY_CINEMA)
        val bbcCategory = Arrays.asList(
                Constants.CATEGORY_INDIA,
                Constants.CATEGORY_WORLD
        )
        val dinamanaiCategory = Arrays.asList(
                Constants.CATEGORY_HEADLINES,
                Constants.CATEGORY_TAMILNADU,
                Constants.CATEGORY_INDIA,
                Constants.CATEGORY_WORLD,
                Constants.CATEGORY_BUSINESS,
                Constants.CATEGORY_SPORTS,
                Constants.CATEGORY_CINEMA
        )

        val oneIndiaCategory = Arrays.asList(
                Constants.CATEGORY_TAMILNADU,
                Constants.CATEGORY_INDIA,
                Constants.CATEGORY_WORLD,
                Constants.CATEGORY_BUSINESS
        )

        val nakkheeranCategory = Arrays.asList(Constants.CATEGORY_HEADLINES,
                Constants.CATEGORY_TAMILNADU,
                Constants.CATEGORY_INDIA,
                Constants.CATEGORY_WORLD,
                Constants.CATEGORY_SPORTS)
        dinakaranFeeds = removeDuplicates(Constants.SOURCE_DINAKARAN, dinakaranCategory, dinakaranFeeds)
        dinamalarFeeds = removeDuplicates(Constants.SOURCE_DINAMALAR, dinamalarCategory, dinamalarFeeds)
        bbcTamilFeeds = removeDuplicates(Constants.SOURCE_BBCTAMIL, bbcCategory, bbcTamilFeeds)
        dinamaniFeeds = removeDuplicates(Constants.SOURCE_DINAMANI, dinamanaiCategory, dinamaniFeeds)
        oneIndiaFeeds = removeDuplicates(Constants.SOURCE_ONEINDIA, oneIndiaCategory, oneIndiaFeeds)
        nakkheeranFeeds = removeDuplicates(Constants.SOURCE_NAKKHEERAN, nakkheeranCategory, nakkheeranFeeds)

        allFeeds.addAll(dinakaranFeeds)
        allFeeds.addAll(dinamaniFeeds)
        allFeeds.addAll(dinamalarFeeds)
        allFeeds.addAll(oneIndiaFeeds)
        allFeeds.addAll(nakkheeranFeeds)
        allFeeds.addAll(bbcTamilFeeds)

        ofy().save().entities(allFeeds).now()


        resp.setStatus(HttpServletResponse.SC_NO_CONTENT)


    }

    companion object {
        private val logger = Logger.getLogger(UpdateServlet::class.java.name)
    }


}

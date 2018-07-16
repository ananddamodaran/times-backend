/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package com.viginfotech.chennaitimes.backend.apis


import com.google.api.server.spi.config.Api
import com.google.api.server.spi.config.ApiMethod
import com.google.api.server.spi.config.ApiNamespace
import com.google.api.server.spi.config.Named
import com.viginfotech.chennaitimes.backend.Constants.API_OWNER
import com.viginfotech.chennaitimes.backend.Constants.API_PACKAGE_PATH
import com.viginfotech.chennaitimes.backend.Constants.CATEGORY_BUSINESS
import com.viginfotech.chennaitimes.backend.Constants.CATEGORY_INDIA
import com.viginfotech.chennaitimes.backend.Constants.CATEGORY_SPORTS
import com.viginfotech.chennaitimes.backend.Constants.CATEGORY_TAMILNADU
import com.viginfotech.chennaitimes.backend.Constants.CATEGORY_WORLD
import com.viginfotech.chennaitimes.backend.model.Feed
import com.viginfotech.chennaitimes.backend.tamil.Dinakaran


/**
 * An endpoint class we are exposing
 */
@Api(name = "chennaiTimesApi", version = "v1", namespace = ApiNamespace(ownerDomain = API_OWNER, ownerName = API_OWNER, packagePath = API_PACKAGE_PATH))
class DinakaranEndpoint {


    @ApiMethod(name = "getDinakaranFeedList", path = "dinakaran")
    fun getDinakaranFeedList(@Named("categoryId") categoryId: Int): List<Feed>? {

        when (categoryId) {

            CATEGORY_TAMILNADU -> return Dinakaran.queryDinakaranNews(CATEGORY_TAMILNADU)
            CATEGORY_INDIA -> return Dinakaran.queryDinakaranNews(CATEGORY_INDIA)
            CATEGORY_WORLD -> return Dinakaran.queryDinakaranNews(CATEGORY_WORLD)
            CATEGORY_BUSINESS -> return Dinakaran.queryDinakaranNews(CATEGORY_BUSINESS)
            CATEGORY_SPORTS -> return Dinakaran.queryDinakaranNews(CATEGORY_SPORTS)
        }


        return null


    }

    @ApiMethod(name = "getDinakaranDetail", path = "dinakaran/detail")
    fun getDinakaranDetail(@Named("guid") guid: String): Feed? {

        // TODO: 2/8/16 detail from db if null then get from server
        return Dinakaran.getDinakaranDetail(guid)

    }
}








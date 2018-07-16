package com.viginfotech.chennaitimes.backend.apis


import com.google.api.server.spi.config.Api
import com.google.api.server.spi.config.ApiMethod
import com.google.api.server.spi.config.ApiNamespace
import com.google.api.server.spi.config.Named
import com.viginfotech.chennaitimes.backend.Constants
import com.viginfotech.chennaitimes.backend.model.Feed
import com.viginfotech.chennaitimes.backend.tamil.BBCTamil


/**
 * Created by anand on 1/20/16.
 */
@Api(name = "chennaiTimesApi", version = "v1", namespace = ApiNamespace(ownerDomain = Constants.API_OWNER,
        ownerName = Constants.API_OWNER, packagePath = Constants.API_PACKAGE_PATH))
class BBCTamilEndpoint {

    @ApiMethod(name = "getBBCTamilFeedList", path = "bbctamil")
    fun getBBCTamilFeedList(@Named("category") category: Int): List<Feed>? {
        return BBCTamil.queryBBCNews(category)
    }

    @ApiMethod(name = "getBBCTamilDetail", path = "bbctamil/detail")
    fun getBBCTamilDetail(@Named("guid") guid: String): Feed? {
        return BBCTamil.getDetail(guid)
    }
}

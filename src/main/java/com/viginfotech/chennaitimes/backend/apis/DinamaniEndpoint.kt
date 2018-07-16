package com.viginfotech.chennaitimes.backend.apis


import com.google.api.server.spi.config.Api
import com.google.api.server.spi.config.ApiMethod
import com.google.api.server.spi.config.ApiNamespace
import com.google.api.server.spi.config.Named
import com.viginfotech.chennaitimes.backend.Constants.API_OWNER
import com.viginfotech.chennaitimes.backend.Constants.API_PACKAGE_PATH
import com.viginfotech.chennaitimes.backend.model.Feed
import com.viginfotech.chennaitimes.backend.tamil.Dinamani


/**
 * Created by anand on 1/21/16.
 */
@Api(name = "chennaiTimesApi", version = "v1", namespace = ApiNamespace(ownerDomain = API_OWNER, ownerName = API_OWNER, packagePath = API_PACKAGE_PATH))
class DinamaniEndpoint {

    @ApiMethod(name = "getDinamaniFeeds", path = "dinamani")
    fun getDinamaniFeeds(@Named("category") category: Int): List<Feed>? {
        return Dinamani.queryDinamaniNews(category)
    }

    @ApiMethod(name = "getDinamaniDetail", path = "dinamani/detail")
    fun getDinamaniDetail(@Named("guid") guid: String): Feed? {
        return Dinamani.getDinamaniDetail(guid)
    }
}

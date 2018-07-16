package com.viginfotech.chennaitimes.backend.apis


import com.google.api.server.spi.config.Api
import com.google.api.server.spi.config.ApiMethod
import com.google.api.server.spi.config.ApiNamespace
import com.google.api.server.spi.config.Named
import com.viginfotech.chennaitimes.backend.Constants.API_OWNER
import com.viginfotech.chennaitimes.backend.Constants.API_PACKAGE_PATH
import com.viginfotech.chennaitimes.backend.model.Feed
import com.viginfotech.chennaitimes.backend.tamil.OneIndia


/**
 * Created by anand on 1/21/16.
 */
@Api(name = "chennaiTimesApi", version = "v1", namespace = ApiNamespace(ownerDomain = API_OWNER, ownerName = API_OWNER, packagePath = API_PACKAGE_PATH))
class OneIndiaEndpoint {

    @ApiMethod(name = "getOneIndiaFeedList", path = "oneindia")
    fun getOneIndiaFeeds(@Named("category") category: Int): List<Feed>? {
        return OneIndia.queryOneIndiaNews(category)
    }

    @ApiMethod(name = "getOneIndiaDetail", path = "oneindia/detail")
    fun getOneIndiaDetail(@Named("guid") guid: String): Feed? {
        return OneIndia.getOneIndiaDetail(guid)
    }


}

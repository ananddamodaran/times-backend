package com.viginfotech.chennaitimes.backend.apis;


import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.viginfotech.chennaitimes.backend.Constants;
import com.viginfotech.chennaitimes.backend.model.Feed;
import com.viginfotech.chennaitimes.backend.tamil.BBCTamil;

import java.util.List;


/**
 * Created by anand on 1/20/16.
 */
@Api(
        name = "chennaiTimesApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = Constants.API_OWNER,
                ownerName = Constants.API_OWNER,
                packagePath = Constants.API_PACKAGE_PATH
        ))
public class BBCTamilEndpoint {

    @ApiMethod(name = "getBBCTamilFeedList", path = "bbctamil")
    public List<Feed> getBBCTamilFeedList(@Named("category") int category) {
        return BBCTamil.queryBBCNews(category);
    }

    @ApiMethod(name = "getBBCTamilDetail", path = "bbctamil/detail")
    public Feed getBBCTamilDetail(@Named("guid") String guid) {
        return BBCTamil.getDetail(guid);
    }
}

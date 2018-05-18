package com.viginfotech.chennaitimes.backend.apis;


import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.viginfotech.chennaitimes.backend.Constants;
import com.viginfotech.chennaitimes.backend.model.Feed;
import com.viginfotech.chennaitimes.backend.tamil.OneIndia;


import java.util.List;


/**
 * Created by anand on 1/21/16.
 */
@Api(
        name = "chennaiTimesApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = Constants.API_OWNER,
                ownerName = Constants.API_OWNER,
                packagePath = Constants.API_PACKAGE_PATH
        ))
public class OneIndiaEndpoint {

    @ApiMethod(name = "getOneIndiaFeedList", path = "oneindia")
    public List<Feed> getOneIndiaFeeds(@Named("category") int category) {
        return OneIndia.queryOneIndiaNews(category);
    }

    @ApiMethod(name = "getOneIndiaDetail", path = "oneindia/detail")
    public Feed getOneIndiaDetail(@Named("guid") String guid) {
        return OneIndia.getOneIndiaDetail(guid);
    }


}

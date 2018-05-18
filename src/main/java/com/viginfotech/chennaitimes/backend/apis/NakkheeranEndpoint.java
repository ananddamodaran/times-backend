package com.viginfotech.chennaitimes.backend.apis;


import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.viginfotech.chennaitimes.backend.Constants;
import com.viginfotech.chennaitimes.backend.model.Feed;
import com.viginfotech.chennaitimes.backend.tamil.Nakkheeran;


import java.util.List;

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
public class NakkheeranEndpoint {

    @ApiMethod(name = "getNakkheeranFeedList", path = "nakkheeran")
    public List<Feed> getNakkheeranFeedList(@Named("category") int category) {
        return Nakkheeran.queryNakkheeranNews(category);
    }

    @ApiMethod(name = "getNakkheeranDetail", path = "nakkheeran/detail")
    public Feed getNakkheeranDetail(@Named("guid") String guid) {
        return Nakkheeran.getNakkheeranDetail(guid);
    }

}

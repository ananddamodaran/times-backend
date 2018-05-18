package com.viginfotech.chennaitimes.backend.apis;


import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.viginfotech.chennaitimes.backend.Constants;
import com.viginfotech.chennaitimes.backend.model.Feed;
import com.viginfotech.chennaitimes.backend.tamil.Dinamalar;


import java.util.List;

import static com.viginfotech.chennaitimes.backend.Constants.*;


/**
 * Created by anand on 1/17/16.
 */
@Api(
        name = "chennaiTimesApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = Constants.API_OWNER,
                ownerName = Constants.API_OWNER,
                packagePath = Constants.API_PACKAGE_PATH
        ))
public class DinamalarEndpoint {

    @ApiMethod(name = "getDinamalarFeedList", path = "dinamalar")
    public List<Feed> getDinamalarFeedList(@Named("category") int category) {

        switch (category) {
            case CATEGORY_HEADLINES:
            case CATEGORY_TAMILNADU:
            case CATEGORY_WORLD:
            case CATEGORY_BUSINESS:
            case CATEGORY_CINEMA:
                return Dinamalar.queryDinamalarNews(category);
            default:
                return null;
        }


    }

    @ApiMethod(name = "getDinamalarDetail", path = "dinamalar/detail")
    public Feed getDinamalarDetail(@Named("guid") String guid, @Named("source") int sourceId,
                                   @Named("category") int category) {

        switch (sourceId) {
            case SOURCE_DINAMALAR:
                return Dinamalar.getDetail(guid, category);

            default:
                return null;
        }
    }

}

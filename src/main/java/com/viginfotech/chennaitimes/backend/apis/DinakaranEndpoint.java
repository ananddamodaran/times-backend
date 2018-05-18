/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package com.viginfotech.chennaitimes.backend.apis;


import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.viginfotech.chennaitimes.backend.Constants;
import com.viginfotech.chennaitimes.backend.model.Feed;
import com.viginfotech.chennaitimes.backend.tamil.Dinakaran;


import java.util.List;

import static com.viginfotech.chennaitimes.backend.Constants.*;


/**
 * An endpoint class we are exposing
 */
@Api(
        name = "chennaiTimesApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = Constants.API_OWNER,
                ownerName = Constants.API_OWNER,
                packagePath = Constants.API_PACKAGE_PATH
        )
)
public class DinakaranEndpoint {


    @ApiMethod(name = "getDinakaranFeedList", path = "dinakaran")
    public List<Feed> getDinakaranFeedList(@Named("categoryId") int categoryId) {

        switch (categoryId) {

            case CATEGORY_TAMILNADU:
                return Dinakaran.queryDinakaranNews(CATEGORY_TAMILNADU);
            case CATEGORY_INDIA:
                return Dinakaran.queryDinakaranNews(CATEGORY_INDIA);
            case CATEGORY_WORLD:
                return Dinakaran.queryDinakaranNews(CATEGORY_WORLD);
            case CATEGORY_BUSINESS:
                return Dinakaran.queryDinakaranNews(CATEGORY_BUSINESS);
            case CATEGORY_SPORTS:
                return Dinakaran.queryDinakaranNews(CATEGORY_SPORTS);

        }


        return null;


    }

    @ApiMethod(name = "getDinakaranDetail", path = "dinakaran/detail")
    public Feed getDinakaranDetail(@Named("guid") String guid) {

        // TODO: 2/8/16 detail from db if null then get from server
        return Dinakaran.getDinakaranDetail(guid);

    }
}








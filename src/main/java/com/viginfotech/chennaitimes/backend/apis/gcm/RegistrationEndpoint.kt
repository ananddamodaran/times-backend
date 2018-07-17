/**
 * For step-by-step instructions on connecting your Android application to this backend module,
 * see "App Engine Backend with Google Cloud Messaging" template documentation at
 * https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/GcmEndpoints
 */


package com.viginfotech.chennaitimes.backend.apis.gcm


import com.google.api.server.spi.config.Api
import com.google.api.server.spi.config.ApiMethod
import com.google.api.server.spi.config.ApiNamespace
import com.google.api.server.spi.config.Named
import com.google.api.server.spi.response.CollectionResponse
import com.viginfotech.chennaitimes.backend.Constants
import com.viginfotech.chennaitimes.backend.model.RegistrationRecord
import com.viginfotech.chennaitimes.backend.service.OfyService.Companion.ofy
import java.util.logging.Logger




@Api(name = "chennaiTimesApi", version = "v1", namespace = ApiNamespace(ownerDomain = Constants.API_OWNER, ownerName = Constants.API_OWNER, packagePath = Constants.API_PACKAGE_PATH))
class RegistrationEndpoint {

    @ApiMethod(name = "register")
    fun registerDevice(@Named("regId") regId: String) {
        if (findRecord(regId) != null) {
            log.info("Device $regId already registered, skipping register")
            return
        }
        val record = RegistrationRecord()
        record.regId = regId
        ofy().save().entity(record).now()
    }

    @ApiMethod(name = "unregister")
    fun unregisterDevice(@Named("regId") regId: String) {
        val record = findRecord(regId)
        if (record == null) {
            log.info("Device $regId not registered, skipping unregister")
            return
        }
        ofy().delete().entity(record).now()
    }

    @ApiMethod(name = "listDevices")
    fun listDevices(@Named("count") count: Int): CollectionResponse<RegistrationRecord> {
        val records = ofy().load().type(RegistrationRecord::class.java).limit(count).list()
        return CollectionResponse.builder<RegistrationRecord>().setItems(records).build()
    }

    private fun findRecord(regId: String): RegistrationRecord? {
        return ofy().load().type(RegistrationRecord::class.java).filter("regId", regId).first().now()
    }

    companion object {

        private val log = Logger.getLogger(RegistrationEndpoint::class.java.name)
    }

}


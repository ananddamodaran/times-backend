/**
 * For step-by-step instructions on connecting your Android application to this backend module,
 * see "App Engine Backend with Google Cloud Messaging" template documentation at
 * https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/GcmEndpoints
 */


package com.viginfotech.chennaitimes.backend.apis.gcm


import com.google.android.gcm.server.Message
import com.google.android.gcm.server.Sender
import com.google.api.server.spi.config.Api
import com.google.api.server.spi.config.ApiNamespace
import com.google.api.server.spi.config.Named
import com.viginfotech.chennaitimes.backend.Constants
import com.viginfotech.chennaitimes.backend.model.RegistrationRecord
import com.viginfotech.chennaitimes.backend.service.OfyService
import com.viginfotech.chennaitimes.backend.service.OfyService.Companion.ofy
import java.io.IOException
import java.util.logging.Logger


/**
 * An endpoint to send messages to devices registered with the backend
 *
 * For more information, see
 * https://developers.google.com/appengine/docs/java/endpoints/
 *
 * NOTE: This endpoint does not use any form of authorization or
 * authentication! If this app is deployed, anyone can access this endpoint! If
 * you'd like to add authentication, take a look at the documentation.
 */

@Api(name = "chennaiTimesApi", version = "v1", namespace = ApiNamespace(ownerDomain = Constants.API_OWNER, ownerName = Constants.API_OWNER,
        packagePath = Constants.API_PACKAGE_PATH))
class MessagingEndpoint {


    /**
     * Send to the first 10 devices (You can modify this to send to any number of devices or a specific device)
     *
     * @param message The message to send
     */

    @Throws(IOException::class)
    fun sendMessage(@Named("message") message: String?) {
        var message = message
        if (message == null || message.trim { it <= ' ' }.length == 0) {
            log.warning("Not sending message because it is empty")
            return
        }
        // crop longer messages
        if (message.length > 1000) {
            message = message.substring(0, 1000) + "[...]"
        }
        val sender = Sender(API_KEY)
        val msg = Message.Builder().addData("message", message).build()
        val records = ofy().load().type(RegistrationRecord::class.java).list()
        for (record in records) {
            val result = sender.send(msg, record.regId, 5)
            if (result.getMessageId() != null) {
                log.info("Message sent to " + record.regId!!)
                val canonicalRegId = result.getCanonicalRegistrationId()
                if (canonicalRegId != null) {
                    // if the regId changed, we have to update the datastore
                    log.info("Registration Id changed for " + record.regId + " updating to " + canonicalRegId)
                    record.regId = canonicalRegId
                    ofy().save().entity(record).now()
                }
            } else {
                val error = result.getErrorCodeName()
                if (error == com.google.android.gcm.server.Constants.ERROR_NOT_REGISTERED) {
                    log.warning("Registration Id " + record.regId + " no longer registered with GCM, removing from datastore")
                    // if the device is no longer registered with Gcm, remove it from the datastore
                    OfyService.ofy().delete().entity(record).now()
                } else {
                    log.warning("Error when sending message : $error")
                }
            }
        }
    }

    companion object {
        private val log = Logger.getLogger(MessagingEndpoint::class.java.name)


        /** Api Keys can be obtained from the google cloud console  */

        //private static final String API_KEY = System.getProperty("gcm.api.key");
        private val API_KEY = "AIzaSyAlMkvyu5OFe_-_ohS2jbnWHqsxi7HJp_Y"
    }
}


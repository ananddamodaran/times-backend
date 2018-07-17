package com.viginfotech.chennaitimes.backend.service


import com.googlecode.objectify.Objectify
import com.googlecode.objectify.ObjectifyFactory
import com.googlecode.objectify.ObjectifyService
import com.viginfotech.chennaitimes.backend.model.Feed
import com.viginfotech.chennaitimes.backend.model.RegistrationRecord

/**
 * Created by anand on 10/23/15.
 */
class OfyService {
    companion object {
        init {
            factory().register(Feed::class.java)
            factory().register(RegistrationRecord::class.java)
        }



       fun ofy(): Objectify {
           return ObjectifyService.ofy()
       }

       fun factory(): ObjectifyFactory {
           return ObjectifyService.factory()
       }
   }
}

package com.viginfotech.chennaitimes.backend.service;


import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.viginfotech.chennaitimes.backend.model.Feed;
import com.viginfotech.chennaitimes.backend.model.RegistrationRecord;

/**
 * Created by anand on 10/23/15.
 */
public class OfyService {
    static {
        factory().register(Feed.class);
        factory().register(RegistrationRecord.class);


    }

    public static Objectify ofy() {
        return ObjectifyService.ofy();
    }

    public static ObjectifyFactory factory() {
        return ObjectifyService.factory();
    }
}

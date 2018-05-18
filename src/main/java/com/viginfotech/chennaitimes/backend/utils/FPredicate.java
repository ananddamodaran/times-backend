package com.viginfotech.chennaitimes.backend.utils;


import com.viginfotech.chennaitimes.backend.model.Feed;

import java.util.List;

/**
 * Created by anand on 2/1/16.
 */
public interface FPredicate<T> {
    boolean test(T type, Feed t);

    List<T> getTlistforThisCatagory(List<T> tlist, int category);
}

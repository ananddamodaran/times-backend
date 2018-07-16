package com.viginfotech.chennaitimes.backend.utils


import com.viginfotech.chennaitimes.backend.model.Feed

/**
 * Created by anand on 2/1/16.
 */
interface FPredicate<T> {
    fun test(type: T, t: Feed): Boolean

    fun getTlistforThisCatagory(tlist: List<T>, category: Int): List<T>
}

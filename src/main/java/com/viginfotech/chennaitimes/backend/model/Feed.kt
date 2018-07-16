package com.viginfotech.chennaitimes.backend.model

import com.googlecode.objectify.annotation.*

@Entity
@Cache
class Feed {

    @Unindex
    var title: String? = null
    @Unindex
    var detailedTitle: String? = null
    @Unindex
    var summary: String? = null
    @Index
    var pubDate: Long? = null
    @Id
    var guid: String? = null
    @Unindex
    var thumbnail: String? = null
    @Unindex
    var image: String? = null
    @Unindex
    var detailNews: String? = null
    @Index
    var categoryId: Int = 0
    @Index
    var sourceId: Int = 0


    override fun equals(obj: Any?): Boolean {
        return obj is Feed && guid == obj.guid
    }

    override fun toString(): String {
        return title!! + "\n"
    }
}

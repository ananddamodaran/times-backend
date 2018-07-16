package com.viginfotech.chennaitimes.backend.utils


import com.viginfotech.chennaitimes.backend.Constants
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {

    val ONE_SECOND_IN_MILLIS: Long = 1000
    val ONE_MINUTE_IN_MILLIS = 60 * ONE_SECOND_IN_MILLIS
    val ONE_HOUR_IN_MILLIS = 60 * ONE_MINUTE_IN_MILLIS
    val ONE_DAY_IN_MILLIS = 24 * ONE_HOUR_IN_MILLIS
    private val ACCEPTED_TIMESTAMP_FORMATS = arrayOf(SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US), SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US), SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+00:00", Locale.US), SimpleDateFormat("MM/dd/yy", Locale.US))


    //return new Date(now -(6*ONE_HOUR_IN_MILLIS)).getTime();
    val last6HoursInMills: Long
        get() {
            val now = System.currentTimeMillis()

            val calendar = Calendar.getInstance()
            calendar.timeZone = TimeZone.getTimeZone("IST")
            calendar.add(Calendar.HOUR_OF_DAY, -6)

            println("time " + calendar.time)
            return calendar.timeInMillis
        }

    //return new Date(now -(6*ONE_HOUR_IN_MILLIS)).getTime();
    val last24HoursInMills: Long
        get() {


            val calendar = Calendar.getInstance()
            calendar.timeZone = TimeZone.getTimeZone("IST")
            calendar.add(Calendar.HOUR_OF_DAY, -24)

            println("time " + calendar.time)
            return calendar.timeInMillis
        }

    val lastTwoHourInMills: Long
        get() {
            val now = System.currentTimeMillis()

            val calendar = Calendar.getInstance()
            calendar.add(Calendar.HOUR_OF_DAY, -2)

            println("2 hours back " + calendar.timeInMillis)

            return calendar.timeInMillis
        }

    fun parseTimeStamp(timestamp: String, source: Int): Date? {
        var i = 0
        for (format in ACCEPTED_TIMESTAMP_FORMATS) {

            try {

                val data = format.parse(timestamp)
                if (i == 0 && source == Constants.SOURCE_NAKKHEERAN) {
                    return Date(data.time - (5 * ONE_HOUR_IN_MILLIS + 30 * ONE_MINUTE_IN_MILLIS))
                }
                i++
                return data


            } catch (ex: ParseException) {
                continue
            }

        }


        return null


    }
}

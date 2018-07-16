package com.viginfotech.chennaitimes.backend

/**
 * Created by anand on 12/3/15.
 */
class Config {
    interface Dinakaran {
        companion object {
            val DINAKARAN_TAMILNADU_URI = "http://www.dinakaran.com/News_Main.asp?Id=10"
            val DINAKARAN_INDIA_URI = "http://www.dinakaran.com/News_Main.asp?Id=26"
            val DINAKARAN_WORLD_URI = "http://www.dinakaran.com/News_Main.asp?Id=13"
            val DINAKARAN_BUSINESS_URI = "http://www.dinakaran.com/News_Main.asp?Id=15"
            val DINAKARAN_SPORTS_URI = "http://www.dinakaran.com/News_Main.asp?Id=16"
        }
    }

    interface Dinamalar {
        companion object {
            val DINAMALAR_HEADLINES_URI = "http://feeds.feedburner.com/dinamalar/Front_page_news?format=xml"
            val DINAMALAR_TAMILNADU_URI = "http://rss.dinamalar.com/tamilnadunews.asp"

            val DINAMALAR_WORLD_URI = "http://rss.dinamalar.com/?cat=INL1"
            val DINAMALAR_BUSINESS_URI = "http://rss.dinamalar.com/?cat=business1"
            val DINAMALAR_SPORTS_URI = "http://cinema.dinamalar.com/rss.php"
            val DINAMALAR_CINEMA_URI = "http://cinema.dinamalar.com/rss.php"
        }

    }

    interface BBCTamil {
        companion object {
            val BBCTAMIL_HEADLINES = "http://www.bbc.co.uk/tamil/index.xml"
            val BBCTAMIL_WORLD = "http://www.bbc.co.uk/tamil/global/index.xml"
            val BBCTAMIL_INDIA = "http://www.bbc.co.uk/tamil/india/index.xml"
            val BBCTAMIL_SPORTS = "http://www.bbc.com/tamil/sport/index.xml"
        }
    }

    interface Dinamani {
        companion object {
            val FRONT_PAGE_URI = "http://www.dinamani.com/latest_news/?widgetName=rssfeed&widgetId=1162902&getXmlFeed=true"
            val TAMIL_NADU_PAGE_URI = "http://www.dinamani.com/tamilnadu/?widgetName=rssfeed&widgetId=1162902&getXmlFeed=true"
            val INDIA_NEWS_PAGE_URI = "http://www.dinamani.com/india/?widgetName=rssfeed&widgetId=1162902&getXmlFeed=true"
            val INTERNATIONAL_NEWS_PAGE_URI = "http://www.dinamani.com/world/?widgetName=rssfeed&widgetId=1162902&getXmlFeed=true"
            val BUSINESS_NEWS_PAGE_URI = "http://www.dinamani.com/business/?widgetName=rssfeed&widgetId=1162902&getXmlFeed=true"
            val CINEMA_NEWS_PAGE_URI = "http://www.dinamani.com/cinema/?widgetName=rssfeed&widgetId=1162902&getXmlFeed=true"
        }

    }

    interface OneIndia {
        companion object {
            val ONEINDIA_TAMILNADU = "http://tamil.oneindia.com/news/tamilnadu/"
            val ONEINDIA_INDIA = "http://tamil.oneindia.com/news/india/"
            val ONEINDIA_WORLD = "http://tamil.oneindia.com/news/world/"
            val ONEINDIA_BUSINESS = "http://tamil.oneindia.com/news/business/"
        }
    }

    interface Nakkheeran {
        companion object {
            val NAKKHEERAN_HEADLINES = "http://www.nakkheeran.in/rss/Top_rss.xml"
            val NAKKHEERAN_TAMILNADU = "http://www.nakkheeran.in/rss/tamilnadu_rss.xml"
            val NAKKHEERAN_INDIA = "http://www.nakkheeran.in/rss/india_rss.xml"
            val NAKKHEERAN_WORLD = "http://www.nakkheeran.in/rss/world_rss.xml"
            val NAKKHEERAN_SPORTS = "http://www.nakkheeran.in/rss/sports_rss.xml"
        }
    }

}

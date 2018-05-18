package com.viginfotech.chennaitimes.backend;

/**
 * Created by anand on 12/3/15.
 */
public class Config {
    public interface Dinakaran {
        String DINAKARAN_TAMILNADU_URI = "http://www.dinakaran.com/News_Main.asp?Id=10";
        String DINAKARAN_INDIA_URI = "http://www.dinakaran.com/News_Main.asp?Id=26";
        String DINAKARAN_WORLD_URI = "http://www.dinakaran.com/News_Main.asp?Id=13";
        String DINAKARAN_BUSINESS_URI = "http://www.dinakaran.com/News_Main.asp?Id=15";
        String DINAKARAN_SPORTS_URI = "http://www.dinakaran.com/News_Main.asp?Id=16";
    }

    public interface Dinamalar {
        String DINAMALAR_HEADLINES_URI = "http://feeds.feedburner.com/dinamalar/Front_page_news?format=xml";
        String DINAMALAR_TAMILNADU_URI = "http://rss.dinamalar.com/tamilnadunews.asp";

        String DINAMALAR_WORLD_URI = "http://rss.dinamalar.com/?cat=INL1";
        String DINAMALAR_BUSINESS_URI = "http://rss.dinamalar.com/?cat=business1";
        String DINAMALAR_SPORTS_URI = "http://cinema.dinamalar.com/rss.php";
        String DINAMALAR_CINEMA_URI = "http://cinema.dinamalar.com/rss.php";

    }

    public interface BBCTamil {
        String BBCTAMIL_HEADLINES = "http://www.bbc.co.uk/tamil/index.xml";
        String BBCTAMIL_WORLD = "http://www.bbc.co.uk/tamil/global/index.xml";
        String BBCTAMIL_INDIA = "http://www.bbc.co.uk/tamil/india/index.xml";
        String BBCTAMIL_SPORTS = "http://www.bbc.com/tamil/sport/index.xml";
    }

    public interface Dinamani {
        String FRONT_PAGE_URI = "http://www.dinamani.com/latest_news/?widgetName=rssfeed&widgetId=1162902&getXmlFeed=true";
        String TAMIL_NADU_PAGE_URI = "http://www.dinamani.com/tamilnadu/?widgetName=rssfeed&widgetId=1162902&getXmlFeed=true";
        String INDIA_NEWS_PAGE_URI = "http://www.dinamani.com/india/?widgetName=rssfeed&widgetId=1162902&getXmlFeed=true";
        String INTERNATIONAL_NEWS_PAGE_URI = "http://www.dinamani.com/world/?widgetName=rssfeed&widgetId=1162902&getXmlFeed=true";
        String BUSINESS_NEWS_PAGE_URI = "http://www.dinamani.com/business/?widgetName=rssfeed&widgetId=1162902&getXmlFeed=true";
        String CINEMA_NEWS_PAGE_URI = "http://www.dinamani.com/cinema/?widgetName=rssfeed&widgetId=1162902&getXmlFeed=true";

    }

    public interface OneIndia {
        String ONEINDIA_TAMILNADU = "http://tamil.oneindia.com/news/tamilnadu/";
        String ONEINDIA_INDIA = "http://tamil.oneindia.com/news/india/";
        String ONEINDIA_WORLD = "http://tamil.oneindia.com/news/world/";
        String ONEINDIA_BUSINESS = "http://tamil.oneindia.com/news/business/";
    }

    public interface Nakkheeran {
        String NAKKHEERAN_HEADLINES = "http://www.nakkheeran.in/rss/Top_rss.xml";
        String NAKKHEERAN_TAMILNADU = "http://www.nakkheeran.in/rss/tamilnadu_rss.xml";
        String NAKKHEERAN_INDIA = "http://www.nakkheeran.in/rss/india_rss.xml";
        String NAKKHEERAN_WORLD = "http://www.nakkheeran.in/rss/world_rss.xml";
        String NAKKHEERAN_SPORTS = "http://www.nakkheeran.in/rss/sports_rss.xml";
    }

}

package com.viginfotech.chennaitimes.backend.tamil;


import com.viginfotech.chennaitimes.backend.Constants;
import com.viginfotech.chennaitimes.backend.model.Feed;
import com.viginfotech.chennaitimes.backend.utils.TimeUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by anand on 8/30/15.
 */
public class BBCTamilParser {
    private static final String TAG = BBCTamilParser.class.getSimpleName();

    public static List<Feed> parseFeed(String content, int categroy) {

        content = content != null ? content.replaceAll("&", "&amp;") : "";
        content = content != null ? content.replaceAll("%3F", "&#63;") : "";
        List<Feed> newsList = new ArrayList<>();
        try {
            boolean inDataItemTag = false;
            String currentTagName = "";
            Feed newsItem = null;


            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(content));

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        currentTagName = parser.getName();
                        if (currentTagName.equals("entry")) {
                            inDataItemTag = true;
                            newsItem = new Feed();
                            newsItem.setCategoryId(categroy);
                            newsItem.setSourceId(Constants.SOURCE_BBCTAMIL);
                            newsList.add(newsItem);
                        }


                        if (currentTagName.equals("link") && newsItem != null) {

                            if (parser.getAttributeValue(null, "href") != null) {
                                newsItem.setGuid(parser.getAttributeValue(null, "href"));
                            }
                        }

                        if (currentTagName.equals("media:thumbnail") && newsItem != null) {
                            if (parser.getAttributeValue(null, "url") != null) {
                                newsItem.setThumbnail(parser.getAttributeValue(null, "url"));
                            }
                        }

                        if (currentTagName.equals("img") && newsItem != null) {
                            if (parser.getAttributeValue(null, "src") != null) {
                                newsItem.setThumbnail(parser.getAttributeValue(null, "src"));
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("entry")) {
                            inDataItemTag = false;
                        }
                        currentTagName = "";
                        break;

                    case XmlPullParser.TEXT:
                        if (inDataItemTag && newsItem != null) {
                            switch (currentTagName) {
                                case "title":
                                    newsItem.setTitle(parser.getText());
                                    break;
                                case "summary":
                                    newsItem.setSummary(parser.getText());
                                    break;
                                case "published":
                                    long now = System.currentTimeMillis();
                                    Date date = TimeUtils.parseTimeStamp(parser.getText(), Constants.SOURCE_BBCTAMIL);
                                    if (date != null)
                                        newsItem.setPubDate(date.getTime());
                                    else newsItem.setPubDate(now);

                                    break;
                                default:
                                    break;
                            }
                        }
                        break;
                }
                eventType = parser.next();
            }


            return newsList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}




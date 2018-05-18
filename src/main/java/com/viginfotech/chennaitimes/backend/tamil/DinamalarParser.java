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
public class DinamalarParser {
    private static final String TAG = DinamalarParser.class.getSimpleName();

    public static List<Feed> parseFeed(String content, int categroy) {

        content = content != null ? content.replaceAll("&", "&amp;") : "";
        content = content != null ? content.replaceAll("%3F", "&#63;") : "";

        try {
            boolean inDataItemTag = false;
            String currentTagName = "";
            long now = System.currentTimeMillis();
            Feed newsItem = null;
            List<Feed> newsList = new ArrayList<>();

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(content));

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        currentTagName = parser.getName();
                        if (currentTagName.equals("item")) {
                            inDataItemTag = true;
                            newsItem = new Feed();
                            newsItem.setCategoryId(categroy);
                            newsItem.setSourceId(Constants.SOURCE_DINAMALAR);
                            newsList.add(newsItem);
                        }

                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("item")) {
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
                                case "link":
                                    newsItem.setGuid(parser.getText());
                                    break;

                                case "description":

                                    String description = parser.getText();
                                    newsItem.setSummary(description);
                                    String imgSrc = null;
                                    if (description.contains("src") && description.contains(".jpg")) {
                                        imgSrc = description.substring(description.indexOf("src") + 5, description.indexOf("jpg") + 3);
                                    }
                                    newsItem.setThumbnail(imgSrc);

                                    break;
                                case "pubDate":

                                    Date date = TimeUtils.parseTimeStamp(parser.getText(), Constants.SOURCE_DINAMALAR);
                                    if (date != null && categroy != Constants.CATEGORY_CINEMA)
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




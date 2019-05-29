package com.example.focusstartrssreader.parser;

import android.util.Log;
import android.util.Xml;

import com.example.focusstartrssreader.tools.contract.Contract;
import com.example.focusstartrssreader.tools.converter.DateConverter;
import com.example.focusstartrssreader.RssFeedApp;
import com.example.focusstartrssreader.domain.model.RssFeedModel;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RssFeedParser {

    //Парсинг RSS ленты
    public List<RssFeedModel> parseFeed(InputStream inputStream, String channelTitle) throws IOException {

        String title = null;
        String link = null;
        String description = null;
        String pubDate = null;
        boolean isItem = false;
        List<RssFeedModel> items = new ArrayList<>();

        try {
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(inputStream, null);

            while (xmlPullParser.next() != xmlPullParser.END_DOCUMENT) {
                // получаем тип текущего элемента (START_TAG, END_TAG, TEXT)
                int eventType = xmlPullParser.getEventType();

                // получаем имя текущего элемента
                String name = xmlPullParser.getName();
                if (name == null)
                    continue;

                if(eventType == XmlPullParser.END_TAG) {
                    if(name.equalsIgnoreCase("item")) {
                        isItem = false;
                    }
                    continue;
                }

                if (eventType == XmlPullParser.START_TAG) {
                    if (name.equalsIgnoreCase("item")) {
                        isItem = true;
                        continue;
                    }
                }


                String result = "";
                if (xmlPullParser.next() == XmlPullParser.TEXT) {
                    result = xmlPullParser.getText();
                    xmlPullParser.nextTag();
                }

                if (name.equalsIgnoreCase("title") && isItem) {
                    title = result;
                } else if (name.equalsIgnoreCase("link") && isItem) {
                    link = result;
                } else if (name.equalsIgnoreCase("description") && isItem) {
                    description = result.replaceAll("\\<.*?\\>", "").replaceAll("\n", " ");
                } else if (name.equalsIgnoreCase("pubDate") && isItem) {
                    pubDate = result;
                }

                if (title != null && link != null && description != null && pubDate != null){
                        if (!(RssFeedApp.getInstance().getFeedRepository()
                                .findDuplicateNewsInDatabase(link) > 0)) {

                            long millis = DateConverter.dateToTime(pubDate);
                            RssFeedModel item = new RssFeedModel(channelTitle, title, link, description, millis);
                            items.add(item);
                        }

                         title = null;
                         link = null;
                         description = null;
                         pubDate = null;
                         isItem = false;
                }
            }
        }
        catch (XmlPullParserException ex) {
            Log.d(Contract.Exception.XML_PULL_PARSER_EXCEPTION, ex.getMessage());
        }
        finally {
            inputStream.close();
        }
        return items;
    }

    public String parseFeedTitle(InputStream inputStream) throws IOException{

        String feedTitle = null;

        try {
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(inputStream, null);

            xmlPullParser.next();
            if (!(xmlPullParser.getName().equalsIgnoreCase("rss"))) throw new XmlPullParserException("It is not a news feed");

            boolean flag = true;
            while ( flag ) {
                xmlPullParser.next();
                if (xmlPullParser.getEventType() == XmlPullParser.START_TAG && xmlPullParser.getName().equals("title")) {
                    feedTitle = xmlPullParser.nextText();
                    flag = false;
                }
            }
        } catch (XmlPullParserException ex) {
            Log.d(Contract.Exception.XML_PULL_PARSER_EXCEPTION, ex.getMessage());
        }
        finally {
            inputStream.close();
        }
        return feedTitle;
    }
}

package com.example.focusstartrssreader.parser;

import android.util.Log;
import android.util.Xml;

import com.example.focusstartrssreader.domain.model.RssFeedModel;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RssFeedParser {

    private static final String XML_PULL_PARSER_EXCEPTION = "XmlPullParserException";

    //Парсинг RSS ленты
    public List<RssFeedModel> parseFeed(InputStream inputStream) throws IOException {

        String title = null;
        String link = null;
        String description = null;
        String pubDate = null;
        boolean isItem = false;
        List<RssFeedModel> items = new ArrayList<>();

        try {
            XmlPullParser xmlPullParser = Xml.newPullParser();
            // FEATURE_PROCESS_NAMESPACES определяет, как будет парситься пространство имен XML
            // Если задано false, пространства имен XML не будут обрабатываться
            // и будут рассматриваться как обычные атрибуты
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(inputStream, null);

            // nextTag() вызывает next() и возвращает элемент,
            // если это START_TAG или END_TAG, иначе вызывает исключение
            // next() возвращает след элемент
            //xmlPullParser.nextTag();

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

                //Log.d("XmlParser", "Parsing name = " + name);

                String result = "";
                if (xmlPullParser.next() == XmlPullParser.TEXT) {
                    result = xmlPullParser.getText();
                    xmlPullParser.nextTag();
                }

                if (name.equalsIgnoreCase("title")) {
                    title = result;
                } else if (name.equalsIgnoreCase("link")) {
                    link = result;
                } else if (name.equalsIgnoreCase("description")) {
                    description = result.replaceAll("\\<.*?\\>", "").replaceAll("\n", " ");
                } else if (name.equalsIgnoreCase("pubDate")) {
                    pubDate = result;
                }

                if (title != null && link != null && description != null && pubDate != null) {

                    if (isItem) {
                        RssFeedModel item = new RssFeedModel(title, link, description, pubDate);
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
            Log.d(XML_PULL_PARSER_EXCEPTION, ex.getMessage());
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

            boolean flag = true;
            while ( flag ) {
                xmlPullParser.next();
                if (xmlPullParser.getEventType() == XmlPullParser.START_TAG && xmlPullParser.getName().equals("title")) {
                    feedTitle = xmlPullParser.nextText();
                    flag = false;
                }
            }
        } catch (XmlPullParserException ex) {
            Log.d(XML_PULL_PARSER_EXCEPTION, ex.getMessage());
        }
        finally {
            inputStream.close();
        }
        return feedTitle;
    }
}

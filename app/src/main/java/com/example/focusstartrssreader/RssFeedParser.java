package com.example.focusstartrssreader;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class RssFeedParser {

    private static final String XML_PULL_PARSER_EXCEPTION = "XmlPullParserException";
    private static final String MALFORMED_URL_EXCEPTION = "MalformedUrlException";
    private static final String IO_EXCEPTION = "IOException";

    private String urlString;


    public RssFeedParser(String urlString) {
        if(!urlString.startsWith("http://") && !urlString.startsWith("https://"))
            urlString = "http://" + urlString;
        this.urlString = urlString;
    }

    //Парсинг RSS ленты
    List<RssFeedModel> parseFeed(InputStream inputStream) throws XmlPullParserException, IOException {

        String title = null;
        String link = null;
        String description = null;
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
            xmlPullParser.nextTag();

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
                    description = result;
                }

                if (title != null && link != null && description != null) {
                    if (isItem) {
                        RssFeedModel item = new RssFeedModel(title, link, description);
                        items.add(item);
                    }
                    title = null;
                    link = null;
                    description = null;
                    isItem = false;
                }
            }
            return items;
        }
        finally {
            inputStream.close();
        }
    }

    // используем HttpURLConnection объект, чтобы открыть HTTP соединение с помощью URL
    public boolean fetchFeed() {

        boolean wasDoParsing = false; // флаг, сигнализирующий о том, успешно ли бы произведен парсинг
        HttpURLConnection httpConn = null;

        try {
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();

            if (!(conn instanceof HttpURLConnection))
                throw new IOException("Not an HTTP connection");

            httpConn = (HttpURLConnection) conn;
            httpConn.setRequestMethod("GET");
            httpConn.connect(); // попытка соединения с сервером
            int response = httpConn.getResponseCode(); // код ответа от сервера
            if (response == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = httpConn.getInputStream();
                parseFeed(inputStream);
                wasDoParsing = true; // ислючений нет, парсинг прошел успешно
            }
        }
        catch (MalformedURLException ex) {
            Log.d(MALFORMED_URL_EXCEPTION, ex.getMessage());
        }
        catch (IOException ex) {
            Log.d(IO_EXCEPTION, ex.getMessage());
        }
        catch (XmlPullParserException ex) {
            Log.d(XML_PULL_PARSER_EXCEPTION, ex.getMessage());
        }
        finally {
            httpConn.disconnect();
            return wasDoParsing;
        }
    }

}

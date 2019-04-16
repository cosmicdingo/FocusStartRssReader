package com.example.focusstartrssreader;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FetchFeedService extends Service {

    private static final String TAG = "FetchFeedService";
    private ExecutorService executorService;

    public int onStartCommand(Intent intent, int flags, int startId) {

        String url = intent.getStringExtra(MainActivity.FEED_URL);
        executorService = Executors.newFixedThreadPool(1);
        FetchFeedRun fetchFeedRun = new FetchFeedRun(startId, url);
        executorService.execute(fetchFeedRun);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    class FetchFeedRun implements Runnable {

        String urlLink;
        int startId;
        List<RssFeedModel> feedModelList;

        public FetchFeedRun(int startId, String urlLink) {
            this.startId = startId;
            this.urlLink = urlLink;
        }
        @Override
        public void run() {

            Intent intent = new Intent(MainActivity.BROADCAST_ACTION);
            if (fetchFeedTask()) {
                intent.putExtra(MainActivity.STATUS, MainActivity.SUCCESS);
                sendBroadcast(intent);
            }
            else {
                intent.putExtra(MainActivity.STATUS, MainActivity.FAILURE);
                sendBroadcast(intent);
            }
            stop(startId);
        }

        boolean fetchFeedTask () {
            if(TextUtils.isEmpty(urlLink))
                return false;

            try {
                if(!urlLink.startsWith("http://") && !urlLink.startsWith("https://"))
                    urlLink = "http://" + urlLink;

                URL url = new URL(urlLink);
                InputStream inputStream = url.openConnection().getInputStream();
                feedModelList = parseFeed(inputStream);
                    return true;
            }
            catch (IOException e) {
                    Log.e(TAG, "Error", e);
            }
            catch (XmlPullParserException e) {
                    Log.e(TAG, "Error", e);
            }
            return false;
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

        void stop(int startId) {
            stopSelf(startId);
        }
    }
}

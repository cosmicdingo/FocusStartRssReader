package com.example.focusstartrssreader;

import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private RecyclerView recyclerView;
    private EditText rssFeedLinkEditText;
    private Button rssFeedButton;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView feedTitleTextView;
    private TextView feedDescriptionTextView;
    private TextView feedLinkTextView;

    private List<RssFeedModel> feedModelList;
    private String feedTitle;
    private String feedDescription;
    private String feedLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        rssFeedLinkEditText = (EditText) findViewById(R.id.rssFeedLinkEditText);
        rssFeedButton = (Button) findViewById(R.id.rssFeedButton);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        feedTitleTextView = (TextView) findViewById(R.id.feedTitle);
        feedDescriptionTextView = (TextView) findViewById(R.id.feedDescription);
        feedLinkTextView = (TextView) findViewById(R.id.feedLink);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        rssFeedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FetchFeedTask().execute((Void) null);
            }
        });
    }

    //Парсинг RSS ленты
    public List<RssFeedModel> parseFeed(InputStream inputStream) throws XmlPullParserException, IOException {

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

                if (title != null && link != null & description != null) {
                    if (isItem) {
                        RssFeedModel item = new RssFeedModel(title, link, description);
                        items.add(item);
                    }
                    else {
                        feedTitle = title;
                        feedLink = link;
                        feedDescription = description;
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


    private class FetchFeedTask extends AsyncTask<Void, Void, Boolean> {

        private String urlLink;

        @Override
        protected void onPreExecute() {
            swipeRefreshLayout.setRefreshing(true);
            urlLink = rssFeedLinkEditText.getText().toString();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
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

        @Override
        protected void onPostExecute(Boolean success) {
            swipeRefreshLayout.setRefreshing(false);

            if(success) {
                feedTitleTextView.setText("Feed title: " + feedTitle);
                feedDescriptionTextView.setText("Feed description: " + feedDescription);
                feedLinkTextView.setText("Feed link: " + feedLink);

                //заполняем RecyclerView
                //recyclerView.setAdapter();
            }
            else {
                Toast.makeText(MainActivity.this, "Invalid rss feed url", Toast.LENGTH_LONG).show();
            }
        }
    }
}

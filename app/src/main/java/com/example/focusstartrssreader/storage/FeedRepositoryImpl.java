package com.example.focusstartrssreader.storage;

import android.util.Log;

import com.example.focusstartrssreader.domain.model.RssFeedModel;
import com.example.focusstartrssreader.domain.repository.FeedRepository;
import com.example.focusstartrssreader.network.NetworkConnection;
import com.example.focusstartrssreader.parser.RssFeedParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;

public class FeedRepositoryImpl implements FeedRepository {

    private static final String IO_EXCEPTION = "IOException";

    private NetworkConnection connection;
    private RssFeedParser parser;
    private RssFeedDatabase database;

    private List<RssFeedModel> rssFeedModels;

    public FeedRepositoryImpl(NetworkConnection connection, RssFeedParser parser, RssFeedDatabase database) {
        this.connection = connection;
        this.parser = parser;
        this.database = database;
        rssFeedModels = null;
    }

    @Override
    public String getFeedTitle(String urlString) {

        String feedTitle = null;
        Log.d("RssFeedRepositoryImpl", "on RssFeedRepositoryImpl: getFeedTitle");
        HttpURLConnection httpConn = connection.getHttpConnection(urlString);

        try {
            //HttpURLConnection httpConn = connection.getHttpConnection(urlString);
            httpConn.connect(); // попытка соединения с сервером
            int response = httpConn.getResponseCode(); // код ответа от сервера
            if (response == HttpURLConnection.HTTP_OK) {

                InputStream is = httpConn.getInputStream();
                feedTitle = parser.parseFeedTitle(is);
            }
        } catch (IOException ex) {
            Log.d(IO_EXCEPTION, ex.getMessage());
        }
        return feedTitle;
    }

    @Override
    public boolean uploadData(String urlString) {

        boolean connIsEstablished = false;

        Log.d("RssFeedRepositoryImpl", "on RssFeedRepositoryImpl: uploadData");
        HttpURLConnection httpConn = connection.getHttpConnection(urlString);

        try {
            //HttpURLConnection httpConn = connection.getHttpConnection(urlString);
            httpConn.connect(); // попытка соединения с сервером
            int response = httpConn.getResponseCode(); // код ответа от сервера
            if (response == HttpURLConnection.HTTP_OK) {

                InputStream is = httpConn.getInputStream();
                // парсим ленту
                rssFeedModels = parser.parseFeed(is);

                // Из Database объекта получаем Dao
                RssFeedModelDao feedModelDao = database.rssFeedModelDao();

                // записываем ленту в бд
                feedModelDao.insert(rssFeedModels);

                // соединение было установлено, данные добавлены в бд
                connIsEstablished = true;

                List<RssFeedModel> rssFeedModelList = database.rssFeedModelDao().getAll();
                for (RssFeedModel feedModel : rssFeedModelList)
                    Log.d("RssFeedModel", "title " + feedModel.getTitle() + "\n");
            }
        }
        catch (IOException ex) {
                Log.d(IO_EXCEPTION, ex.getMessage());
                connIsEstablished = false;
        }
        finally {
            httpConn.disconnect();
        }
        return connIsEstablished;
    }


    // получаем данные из бд
    public List<RssFeedModel> getData() {

        Log.d("RssFeedRepositoryImpl", "RssFeedRepositoryImpl: getData");

        rssFeedModels = null;
        new Thread(new Runnable() {
            @Override
            public void run() {
                rssFeedModels = database.rssFeedModelDao().getAll();
            }
        }).start();
       return rssFeedModels;
    }

    private void setResult(List<RssFeedModel> rssFeedModels) {
        this.rssFeedModels = rssFeedModels;
    }

    public List<RssFeedModel> getRssFeedModels() {
        return rssFeedModels;
    }
}

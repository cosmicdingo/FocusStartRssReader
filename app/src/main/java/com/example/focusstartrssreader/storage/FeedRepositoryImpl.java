package com.example.focusstartrssreader.storage;

import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.example.focusstartrssreader.domain.model.Channel;
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


    /**********************************************************************/
    /******* методы для работы с сервисом и загрузки данных в бд***********/
    /**********************************************************************/
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
    public boolean uploadData(String channelTitle, String urlString) {

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
                rssFeedModels = parser.parseFeed(is, channelTitle);

                // Из Database объекта получаем Dao
                RssFeedModelDao feedModelDao = database.rssFeedModelDao();

                // записываем ленту в бд
                feedModelDao.insert(rssFeedModels);

                // соединение было установлено, данные добавлены в бд
                connIsEstablished = true;
                // добавляем канал в бд
                insertChannelInDatabase(channelTitle, urlString);

                List<RssFeedModel> rssFeedModelList = feedModelDao.getAll();
                for (RssFeedModel feedModel : rssFeedModelList) {

                    Log.d("uploadData", "*********************");
                    Log.d("uploadData", "model.channel_title: " + feedModel.getChannelTitle());
                    Log.d("uploadData", "model.news_title: " + feedModel.getTitle());
                    Log.d("uploadData", "*********************");
                }
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

    /**********************************************************************/
    /*******       методы для работы со списком каналов          ***********/
    /**********************************************************************/
    // добавляем канал в бд
    public void insertChannelInDatabase(String title, String urlString) {
        // создаем объект канала
        Channel channel = new Channel(title, urlString);
        // Из Database объекта получаем RssFeedChannelDao
        RssFeedChannelDao channelDao = database.rssFeedChannelDao();
        // записываем канал в бд
        channelDao.insertChannel(channel);
    }

    public LiveData<List<Channel>> getChannelsLiveData() {
        // Из Database объекта получаем RssFeedChannelDao
        RssFeedChannelDao channelDao = database.rssFeedChannelDao();
        return channelDao.getAll();
    }

    public int deleteChannel(Channel channel) {

        // удаляем все новости из бд, связанные с удаляемым каналом
        String channelTitle = channel.getChannelTitle();
        RssFeedModelDao feedModelDao = database.rssFeedModelDao();
        feedModelDao.deleteChannelNewsFeed(channelTitle);

        // удаляем сам канал
        RssFeedChannelDao channelDao = database.rssFeedChannelDao();
        channelDao.delete(channel);
        return 0;
    }

    public List<Channel> getAllChannelList() {
        return database.rssFeedChannelDao().getList();
    }


    /**********************************************************************/
    /*******      метод для работы со списком новостей канала   ***********/
    /**********************************************************************/
    public LiveData<List<RssFeedModel>> getChannelNewsFeedLiveData(String channelTitle) {
        RssFeedModelDao feedModelDao = database.rssFeedModelDao();
        return feedModelDao.getChannelNewsFeed(channelTitle);
    }


}

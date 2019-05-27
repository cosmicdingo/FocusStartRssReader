package com.example.focusstartrssreader.storage;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.example.focusstartrssreader.domain.model.Channel;
import com.example.focusstartrssreader.domain.model.RssFeedModel;
import com.example.focusstartrssreader.domain.model.SelectedNews;
import com.example.focusstartrssreader.domain.repository.FeedRepository;
import com.example.focusstartrssreader.network.NetworkConnection;
import com.example.focusstartrssreader.parser.RssFeedParser;
import com.example.focusstartrssreader.storage.dao.RssFeedChannelDao;
import com.example.focusstartrssreader.storage.dao.RssFeedModelDao;
import com.example.focusstartrssreader.storage.db.RssFeedDatabase;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.concurrent.ExecutorService;

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
    public void getFeedTitle(String urlString, ExecutorService executor, MutableLiveData<String> liveData) {

        executor.execute(() -> {
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
            liveData.postValue(feedTitle);
        });
    }

    @Override
    public boolean uploadFeed(String channelTitle, String urlString) {

        boolean wasAddFeedInDatabase = false;
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
                wasAddFeedInDatabase = true;
            }
        }
        catch (IOException ex) {
                Log.d(IO_EXCEPTION, ex.getMessage());
                wasAddFeedInDatabase = false;
        }
        finally {
            httpConn.disconnect();
        }
        return wasAddFeedInDatabase;
    }

    /**********************************************************************/
    /*******       методы для работы со списком каналов          ***********/
    /**********************************************************************/
    // добавляем канал в бд
    @Override
    public void insertChannelInDatabase(String title, String urlString, ExecutorService executor, MutableLiveData<Boolean> liveData) {

        executor.execute(() -> {
            if (findDuplicateChannelInDatabase(title) > 0)
                liveData.postValue(false);
            else {
                // если данные лента была была добвлена в бд
                // необходимо добавить канал в бд
                if (uploadFeed(title, urlString)) {
                    // создаем объект канала
                    Channel channel = new Channel(title, urlString);
                    // записываем канал в бд
                    database.rssFeedChannelDao().insertChannel(channel);
                    liveData.postValue(true);
                } else liveData.postValue(false);
            }
        });
    }

    @Override
    public LiveData<List<Channel>> getChannelsLiveData() {
        // Из Database объекта получаем RssFeedChannelDao
        RssFeedChannelDao channelDao = database.rssFeedChannelDao();
        return channelDao.getAll();
    }

    @Override
    public void deleteChannel(final Channel channel) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // удаляем все новости из бд, связанные с удаляемым каналом
                String channelTitle = channel.getChannelTitle();
                RssFeedModelDao feedModelDao = database.rssFeedModelDao();
                feedModelDao.deleteChannelNewsFeed(channelTitle);
                // удаляем сам канал
                RssFeedChannelDao channelDao = database.rssFeedChannelDao();
                channelDao.delete(channel);
            }
        });
        thread.start();
    }

    @Override
    public List<Channel> getAllChannelList() {
        return database.rssFeedChannelDao().getList();
    }

    @Override
    public boolean doSync(String title) {
        return uploadFeed(title, database.rssFeedChannelDao().getChannelLink(title));
    }


    /**********************************************************************/
    /*******      метод для работы со списком новостей канала   ***********/
    /**********************************************************************/
    @Override
    public LiveData<List<RssFeedModel>> getChannelNewsFeedLiveData(String channelTitle) {
        RssFeedModelDao feedModelDao = database.rssFeedModelDao();
        return feedModelDao.getChannelNewsFeed(channelTitle);
    }

    @Override
    // используется для отображения описания выбранной новости
    public LiveData<SelectedNews> getSelectedNews(long ID) {
        return database.rssFeedModelDao().getSelectedNews(ID);
    }

    @Override
    public long getNumberNews() {
        return database.rssFeedModelDao().getNumberNews();
    }


    // позволяет выявить в бд повторяющуюся новость
    // возвращает 0 при отсуствии в бд записи со ссылкой link,
    // иначе - количеств совпадений
    @Override
    public long findDuplicateNewsInDatabase(String link) {
        return database.rssFeedModelDao().findDuplicateRecordsInDatabase(link);
    }

    @Override
    public int findDuplicateChannelInDatabase(String title) {
        return database.rssFeedChannelDao().findDuplicateChannelInDatabase(title);
    }
}

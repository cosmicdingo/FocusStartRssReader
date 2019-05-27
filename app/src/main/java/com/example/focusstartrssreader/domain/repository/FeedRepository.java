package com.example.focusstartrssreader.domain.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.example.focusstartrssreader.domain.model.Channel;
import com.example.focusstartrssreader.domain.model.RssFeedModel;
import com.example.focusstartrssreader.domain.model.SelectedNews;

import java.util.List;
import java.util.concurrent.ExecutorService;

public interface FeedRepository {


    /**********************************************************************/
    /******* методы для работы с сервисом и загрузки данных в бд***********/
    /**********************************************************************/
    void getFeedTitle(String urlString, ExecutorService executor, MutableLiveData<String> liveData);

    // выполняем подключение к сети, парсинг и
    // загружаем данные в бд
    boolean uploadFeed(String channelTitle, String urlString);

    /**********************************************************************/
    /*******       методы для работы со списком каналов          ***********/
    /**********************************************************************/

    void insertChannelInDatabase(String title, String urlString, ExecutorService executor, MutableLiveData<Boolean> liveData);

    List<Channel> getAllChannelList();

    LiveData<List<Channel>> getChannelsLiveData();

    void deleteChannel(Channel channel);

    boolean doSync(String title);

    /**********************************************************************/
    /*******      метод для работы со списком новостей канала   ***********/
    /**********************************************************************/
    LiveData<List<RssFeedModel>> getChannelNewsFeedLiveData(String channelTitle);

    LiveData<SelectedNews> getSelectedNews(long ID);

    /**********************************************************************/
    /*******      метод для подсчета количества новостей в бд   ***********/
    /**********************************************************************/
    long getNumberNews();

    long findDuplicateNewsInDatabase(String link);

    int findDuplicateChannelInDatabase(String title);

}

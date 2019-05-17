package com.example.focusstartrssreader.domain.repository;

import android.arch.lifecycle.LiveData;

import com.example.focusstartrssreader.domain.model.Channel;
import com.example.focusstartrssreader.domain.model.RssFeedModel;

import java.util.List;

public interface FeedRepository {


    /**********************************************************************/
    /******* методы для работы с сервисом и загрузки данных в бд***********/
    /**********************************************************************/
    String getFeedTitle(String urlString);

    // выполняем подключение к сети, парсинг и
    // загружаем данные в бд
    boolean uploadData(String channelTitle, String urlString);

    /**********************************************************************/
    /*******       методы для работы со списком каналов          ***********/
    /**********************************************************************/

    void insertChannelInDatabase(String title, String urlString);

    List<Channel> getAllChannelList();

    LiveData<List<Channel>> getChannelsLiveData();

    int deleteChannel(Channel channel);

    /**********************************************************************/
    /*******      метод для работы со списком новостей канала   ***********/
    /**********************************************************************/
    LiveData<List<RssFeedModel>> getChannelNewsFeedLiveData(String channelTitle);

}

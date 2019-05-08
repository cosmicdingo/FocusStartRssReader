package com.example.focusstartrssreader.domain.repository;

import com.example.focusstartrssreader.domain.model.RssFeedModel;

import java.util.List;

public interface FeedRepository {


    String getFeedTitle(String urlString);

    // выполняем подключение к сети, парсинг и
    // загружаем данные в бд
    boolean uploadData(String urlString);

    // получаем данные из бд
    List<RssFeedModel> getData();
}

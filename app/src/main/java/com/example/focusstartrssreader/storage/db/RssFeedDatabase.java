package com.example.focusstartrssreader.storage.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.focusstartrssreader.domain.model.Channel;
import com.example.focusstartrssreader.domain.model.RssFeedModel;
import com.example.focusstartrssreader.storage.dao.RssFeedChannelDao;
import com.example.focusstartrssreader.storage.dao.RssFeedModelDao;

// В классе описываем абстрактные методы для получения
// Dao объектов, которые нам понадобятся
@Database(entities = {RssFeedModel.class, Channel.class}, version = 1, exportSchema = false)
public abstract class RssFeedDatabase extends RoomDatabase {
    public abstract RssFeedModelDao rssFeedModelDao();
    public abstract RssFeedChannelDao rssFeedChannelDao();
}

package com.example.focusstartrssreader.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.focusstartrssreader.Entity.RssFeedModel;
import com.example.focusstartrssreader.RssFeedModelDao;

// В классе описываем абстрактные методы для получения
// Dao объектов, которые нам понадобятся
@Database(entities = {RssFeedModel.class}, version = 1)
public abstract class RssFeedDatabase extends RoomDatabase {
    public abstract RssFeedModelDao rssFeedModelDao();
}

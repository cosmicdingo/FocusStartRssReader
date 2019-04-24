package com.example.focusstartrssreader;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.example.focusstartrssreader.storage.RssFeedDatabase;

// Application класс предназначен для создания
// и хранения RssFeedDatabase. Используем один
// экземпляр для всех операций

public class RssFeedApp extends Application {

    public static RssFeedApp instance;

    private RssFeedDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this, RssFeedDatabase.class, "RssFeedDatabase").build();
    }

    public static RssFeedApp getInstance() {
        return instance;
    }

    public RssFeedDatabase getDatabase() {
        return database;
    }
}

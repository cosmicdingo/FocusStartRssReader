package com.example.focusstartrssreader.application;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.example.focusstartrssreader.database.RssFeedDatabase;

// Application класс предназначен для создания
// и хранения RssFeedDatabase. Используем один
// экземпляр для всех операций

public class RssFeedApp extends Application {

    public static RssFeedApp application;

    private RssFeedDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        database = Room.databaseBuilder(this, RssFeedDatabase.class, "RssFeedDatabase").build();
    }

    public static RssFeedApp getApplication() {
        return application;
    }

    public RssFeedDatabase getDatabase() {
        return database;
    }
}

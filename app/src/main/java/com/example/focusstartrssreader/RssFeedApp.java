package com.example.focusstartrssreader;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.example.focusstartrssreader.domain.repository.FeedRepository;
import com.example.focusstartrssreader.network.NetworkConnection;
import com.example.focusstartrssreader.parser.RssFeedParser;
import com.example.focusstartrssreader.storage.FeedRepositoryImpl;
import com.example.focusstartrssreader.storage.RssFeedDatabase;

// Application класс предназначен для создания
// и хранения RssFeedDatabase. Используем один
// экземпляр для всех операций

public class RssFeedApp extends Application {

    private static RssFeedApp instance;

    private FeedRepositoryImpl repository;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        NetworkConnection connection = new NetworkConnection();
        RssFeedParser parser = new RssFeedParser();
        RssFeedDatabase database = Room.databaseBuilder(this, RssFeedDatabase.class, "RssFeedDatabase").build();

        repository = new FeedRepositoryImpl(connection, parser, database);
    }

    public static RssFeedApp getInstance() {
        return instance;
    }

    public FeedRepository getFeedRepository() {
        return repository;
    }
}

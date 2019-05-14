package com.example.focusstartrssreader.storage;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.focusstartrssreader.domain.model.Channel;
import com.example.focusstartrssreader.domain.model.RssFeedModel;

import java.util.List;

@Dao
public interface RssFeedModelDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(RssFeedModel rssFeedModel);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(List<RssFeedModel> rssFeedModels);

    @Update
    void update(RssFeedModel rssFeedModel);

    @Delete
    void delete(RssFeedModel rssFeedModel);

    @Query("DELETE FROM RssFeedModel WHERE channelTitle = :chTitle")
    int deleteChannelNewsFeed(String chTitle);

    @Query("SELECT * FROM RssFeedModel")
    List<RssFeedModel> getAll();

    @Query("SELECT * FROM RssFeedModel WHERE channelTitle = :chTitle")
    LiveData<List<RssFeedModel>> getChannelNewsFeed(String chTitle);
}

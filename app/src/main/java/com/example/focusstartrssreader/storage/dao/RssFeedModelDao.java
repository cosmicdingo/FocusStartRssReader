package com.example.focusstartrssreader.storage.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.focusstartrssreader.domain.model.SelectedNews;
import com.example.focusstartrssreader.domain.model.RssFeedModel;

import java.util.List;

@Dao
public interface RssFeedModelDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(List<RssFeedModel> rssFeedModels);

    @Query("SELECT COUNT(*) FROM RssFeedModel WHERE link = :link")
    int findDuplicateRecordsInDatabase(String link);

    @Query("SELECT COUNT(ID) FROM RssFeedModel")
    long getNumberNews();

    @Query("DELETE FROM RssFeedModel WHERE channelTitle = :chTitle")
    void deleteChannelNewsFeed(String chTitle);

    @Query("SELECT title, description, millis FROM RssFeedModel WHERE id = :ID")
    LiveData<SelectedNews> getSelectedNews(long ID);

    @Query("SELECT * FROM RssFeedModel WHERE channelTitle = :chTitle ORDER BY millis DESC")
    LiveData<List<RssFeedModel>> getChannelNewsFeed(String chTitle);

    /*@Query("SELECT * FROM RssFeedModel")
    List<RssFeedModel> getAll();*/
}

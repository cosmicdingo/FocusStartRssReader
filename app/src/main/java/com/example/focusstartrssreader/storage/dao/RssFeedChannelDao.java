package com.example.focusstartrssreader.storage.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.focusstartrssreader.domain.model.Channel;

import java.util.List;

@Dao
public interface RssFeedChannelDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertChannel(Channel channel);

    @Query("SELECT COUNT(ID) FROM Channel WHERE channelTitle = :title")
    int findDuplicateChannelInDatabase(String title);

    @Delete
    void delete(Channel channel);

    @Query("SELECT * FROM Channel")
    List<Channel> getList();

    @Query("SELECT * FROM Channel")
    LiveData<List<Channel>> getAll();

    @Query("SELECT channelLink FROM Channel WHERE channelTitle = :chTitle")
    String getChannelLink(String chTitle);
}

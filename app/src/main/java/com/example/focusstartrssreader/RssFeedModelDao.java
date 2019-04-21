package com.example.focusstartrssreader;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Update;

import com.example.focusstartrssreader.Entity.RssFeedModel;

@Dao
public interface RssFeedModelDao {

    @Insert
    void insert(RssFeedModel rssFeedModel);

    @Update
    void update(RssFeedModel rssFeedModel);

    @Delete
    void delete(RssFeedModel rssFeedModel);
}

package com.example.focusstartrssreader.storage;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Update;

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
}

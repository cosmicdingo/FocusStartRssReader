package com.example.focusstartrssreader.domain.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Channel {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String channelTitle;
    private String channelLink;

    public Channel(String channelTitle, String channelLink) {
        this.channelTitle = channelTitle;
        this.channelLink = channelLink;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getChannelLink() {
        return channelLink;
    }

    public String getChannelTitle() {
        return channelTitle;
    }
}

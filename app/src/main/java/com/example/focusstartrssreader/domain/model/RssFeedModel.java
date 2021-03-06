package com.example.focusstartrssreader.domain.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

// класс, содержащий заголовок, описание и ссылку элемента новостной ленты
@Entity
public class RssFeedModel {


    @PrimaryKey(autoGenerate = true)
    private long id;

    private String channelTitle;
    private String title;
    private String link;
    private String description;
    private long millis;

    public RssFeedModel(String channelTitle, String title, String link, String description, long millis) {
        this.channelTitle = channelTitle;
        this.title = title;
        this.link = link;
        this.description = description;
        this.millis = millis;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public String getChannelTitle() {
        return channelTitle;
    }

    public long getMillis() {
        return millis;
    }
}

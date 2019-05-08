package com.example.focusstartrssreader.domain.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

// Вспомогательный класс, содержащий заголовок, описание,
// и ссылку элемента новостной ленты
@Entity
public class RssFeedModel {


    @PrimaryKey(autoGenerate = true)
    private long id;

    private String title;
    private String link;
    private String description;
    private String pubDate;

    public RssFeedModel(String title, String link, String description, String pubDate) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.pubDate = pubDate;
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

    public String getPubDate() {
        return pubDate;
    }
}

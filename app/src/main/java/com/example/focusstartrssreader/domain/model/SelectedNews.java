package com.example.focusstartrssreader.domain.model;


// Используем этот класс в методе запроса RssFeedModelDao.
// чтобы не тащить все поля из таблицы
public class SelectedNews {

    private String title;
    private String description;
    private String pubDate;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }
}

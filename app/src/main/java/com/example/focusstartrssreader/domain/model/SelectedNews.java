package com.example.focusstartrssreader.domain.model;


// Используем этот класс в методе запроса RssFeedModelDao.
// чтобы не тащить все поля из таблицы
public class SelectedNews {

    private String title;
    private String description;
    private long millis;

    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public long getMillis() {
        return millis;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setMillis(long millis) {
        this.millis = millis;
    }

}

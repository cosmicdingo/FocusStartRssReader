package com.example.focusstartrssreader;

// Вспомогательный класс, содержащий заголовок, описание,
// и ссылку элемента новостной ленты
public class RssFeedModel {

    private String title;
    private String link;
    private String description;

    public RssFeedModel(String title, String link, String description) {
        this.title = title;
        this.link = link;
        this.description = description;
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
}

package com.example.focusstartrssreader;

// Вспомогательный класс, содержащий заголовок, описание,
// и ссылку элемента новостной ленты
public class RssFeedModel {

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

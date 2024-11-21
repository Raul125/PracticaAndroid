package com.raulrh.practicaandroid.ui.news.data;

import java.util.List;

public class News {
    private long id;
    private String title;
    private String summary;
    private String dateCreated;
    private String description;
    private List<Category> category;
    private List<Image> image;

    public News() {

    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public String getDescription() {
        return description;
    }

    public List<Category> getCategory() {
        return category;
    }

    public List<Image> getImage() {
        return image;
    }
}

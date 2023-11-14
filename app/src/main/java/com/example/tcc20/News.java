package com.example.tcc20;

public class News {
    private String title;
    private String description;
    private String imageUrl;



    private String newsUrl;

    public News(String title, String description, String imageUrl, String newsUrl) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.newsUrl = newsUrl;
    }


    public String getNewsUrl() {
        return newsUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    // Se desejar, você pode adicionar mais métodos ou atributos conforme necessário
}

package com.example.newsapplication;

public class News {
    private String title;
    private String date;
    private String section;
    private String url;
    private String author;
    private String imgUrl;

    public News(String title,String date,String section,String url,String author){
        this.date=date;
        this.title = title;
        this.section = section;
        this.url = url;
        this.author = author;
    }
    public News(String title,String date,String section,String url,String author,String imgUrl){
        this.date=date;
        this.title = title;
        this.section = section;
        this.url = url;
        this.author = author;
        this.imgUrl = imgUrl;
    }

    public String getDate() {
        return date;
    }

    public String getSection() {
        return section;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getAuthor() { return author; }

    public String getImgUrl(){return imgUrl;}
}


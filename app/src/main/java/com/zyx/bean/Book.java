package com.zyx.bean;

import java.io.Serializable;
import java.util.List;

public class Book implements Serializable {
    private int id;
    private String name;

    public Book() {
        this.name = "";
        this.author = "";
        this.translator = "";
        this.cover = "";
        this.publisher = "";
        this.publish_year = "";
        this.publish_month = "";
        this.isbn = "";
        this.status = "";
        this.shelfId = 0;
        this.tags = null;
        this.link = "";
        this.note = "";
    }

    private String author;
    private String translator;
    private String cover;
    private String publisher;
    private String publish_year;
    private String publish_month;
    private String isbn;
    private String status;
    private int shelfId;
    private List<Integer> tags;
    private String link;
    private String note;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublish_year() {
        return publish_year;
    }

    public void setPublish_year(String publish_year) {
        this.publish_year = publish_year;
    }

    public String getPublish_month() {
        return publish_month;
    }

    public void setPublish_month(String publish_month) {
        this.publish_month = publish_month;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getShelfId() {
        return shelfId;
    }

    public void setShelfId(int shelfId) {
        this.shelfId = shelfId;
    }

    public List<Integer> getTags() {
        return tags;
    }

    public void setTags(List<Integer> tags) {
        this.tags = tags;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setTranslator(String translator) {
        this.translator = translator;
    }

    public String getTranslator() {
        return translator;
    }
}

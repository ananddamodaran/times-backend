package com.viginfotech.chennaitimes.backend.model;

import com.googlecode.objectify.annotation.*;

@Entity
@Cache
public class Feed {

    @Unindex
    private String title;
    @Unindex
    private String detailedTitle;
    @Unindex
    private String summary;
    @Index
    private Long pubDate;
    @Id
    private String guid;
    @Unindex
    private String thumbnail;
    @Unindex
    private String image;
    @Unindex
    private String detailNews;
    @Index
    private int categoryId;
    @Index
    private int sourceId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetailedTitle() {
        return detailedTitle;
    }

    public void setDetailedTitle(String detailedTitle) {
        this.detailedTitle = detailedTitle;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Long getPubDate() {
        return pubDate;
    }

    public void setPubDate(Long pubDate) {
        this.pubDate = pubDate;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getSourceId() {
        return sourceId;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    public String getDetailNews() {
        return detailNews;
    }

    public void setDetailNews(String detailNews) {
        this.detailNews = detailNews;
    }


    @Override
    public boolean equals(Object obj) {
        return obj instanceof Feed && getGuid().equals(((Feed) obj).getGuid());
    }

    @Override
    public String toString() {
        return title + "\n";
    }
}

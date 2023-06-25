package com.mobile.diary;

public class DiaryBean {
    private String date;
    private String location;
    private String photoPath;
    private String content;

    @Override
    public String toString() {
        return "DiaryBean{" +
                "Date='" + date + '\'' +
                ", Location='" + location + '\'' +
                ", PhotoPath='" + photoPath + '\'' +
                ", Content='" + content + '\'' +
                '}';
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

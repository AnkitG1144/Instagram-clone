package com.example.instagramclone.modal;

public class PostModal implements Comparable<PostModal>{

    public PostModal(){

    }

    private String fileName;
    private String dataTime;
    private String caption;
    private String downloadUrl;
    private String userName;

    public PostModal(String fileName, String dataTime, String caption, String downloadUrl, String userName) {
        this.fileName = fileName;
        this.dataTime = dataTime;
        this.caption = caption;
        this.downloadUrl = downloadUrl;
        this.userName = userName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDataTime() {
        return dataTime;
    }

    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public int compareTo(PostModal postModal) {
        return postModal.dataTime.compareTo(this.dataTime);
    }
}

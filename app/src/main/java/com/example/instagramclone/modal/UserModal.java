package com.example.instagramclone.modal;

import java.util.List;

public class UserModal {

    public UserModal(){

    }

    private String name;
    private String email;
    private String mobile;
    private String profile_img;
    private List<PostModal> posts;

    public List<PostModal> getPosts() {
        return posts;
    }

    public void setPosts(List<PostModal> posts) {
        this.posts = posts;
    }

    public String getProfile_img() {
        return profile_img;
    }

    public void setProfile_img(String profile_img) {
        this.profile_img = profile_img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}

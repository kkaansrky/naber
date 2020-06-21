package com.example.naber.Model;

public class User {
    private String id;
    private String username;
    private String imageURL;
    private String status;



    public User() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

}

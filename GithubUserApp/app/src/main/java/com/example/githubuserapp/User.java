package com.example.githubuserapp;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private int photo;
    private String name;
    private String userCompany;
    private String userLocation;
    private String username;
    private String repo;
    private String following;
    private String followers;

    public User(){
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserCompany() {
        return userCompany;
    }

    public void setUserCompany(String userCompany) {
        this.userCompany = userCompany;
    }

    public String getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(String userLocation) {
        this.userLocation = userLocation;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRepo() {
        return repo;
    }

    public void setRepo(String repo) {
        this.repo = repo;
    }

    public String getFollowing() {
        return following;
    }

    public void setFollowing(String following) {
        this.following = following;
    }

    public String getFollowers() {
        return followers;
    }

    public void setFollowers(String followers) {
        this.followers = followers;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected User(Parcel in) {
        photo = in.readInt();
        name = in.readString();
        userCompany = in.readString();
        userLocation = in.readString();
        username = in.readString();
        repo = in.readString();
        following = in.readString();
        followers = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(photo);
        dest.writeString(name);
        dest.writeString(userCompany);
        dest.writeString(userLocation);
        dest.writeString(username);
        dest.writeString(repo);
        dest.writeString(following);
        dest.writeString(followers);
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }
        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}

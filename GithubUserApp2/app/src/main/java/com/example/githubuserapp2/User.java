package com.example.githubuserapp2;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private String photo;
    private String name;
    private String userCompany;
    private String userLocation;
    private String username;
    private String repo;

    public User(){
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
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


    @Override
    public int describeContents() {
        return 0;
    }

    protected User(Parcel in) {
        photo = in.readString();
        name = in.readString();
        userCompany = in.readString();
        userLocation = in.readString();
        username = in.readString();
        repo = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(photo);
        dest.writeString(name);
        dest.writeString(userCompany);
        dest.writeString(userLocation);
        dest.writeString(username);
        dest.writeString(repo);
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
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
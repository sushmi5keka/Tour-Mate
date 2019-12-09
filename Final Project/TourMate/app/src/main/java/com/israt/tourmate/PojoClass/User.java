package com.israt.tourmate.PojoClass;

public class User {

    private String firstName;
    private String lastName;
    private String email;
    private String picture;
    private String uid;

    public User() {
    }

    public User(String firstName, String lastName, String email, String picture, String uid) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.picture = picture;
        this.uid = uid;
    }

//    public User(String firstName, String email, String picture) {
//        this.firstName = firstName;
//
//        this.email = email;
//        this.picture = picture;
//    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPicture() {
        return picture;
    }

    public String getUid() {
        return uid;
    }
}

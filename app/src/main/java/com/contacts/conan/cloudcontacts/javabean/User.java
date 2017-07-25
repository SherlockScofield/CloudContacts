package com.contacts.conan.cloudcontacts.javabean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Conan on 2016/11/9.
 */

public class User extends BmobObject {
    private String user_name;
    private String phone;
    private String email;
    private String password;
    private String partition_id;
    private String photo;
    private String photo_id;
    private String user_sign;
    private String state;

    public void setState(String state) {
        this.state = state;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPartition_id(String partition_id) {
        this.partition_id = partition_id;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setPhoto_id(String photo_id) {
        this.photo_id = photo_id;
    }

    public void setUser_sign(String user_sign) {
        this.user_sign = user_sign;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPartition_id() {
        return partition_id;
    }

    public String getPhoto() {
        return photo;
    }

    public String getPhoto_id() {
        return photo_id;
    }

    public String getUser_sign() {
        return user_sign;
    }

    public String getState() {
        return state;
    }
}

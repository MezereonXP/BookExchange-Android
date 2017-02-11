package com.example.mezereon.bookexchange.Module;

/**
 * Created by Mezereon on 2017/2/10.
 */

public class User {
    private int id;
    private String sex;
    private String phone;
    private String email;
    private String username;
    private String password;
    private String signatrue;
    private String src;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSignatrue() {
        return signatrue;
    }

    public void setSignatrue(String signatrue) {
        this.signatrue = signatrue;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }
}

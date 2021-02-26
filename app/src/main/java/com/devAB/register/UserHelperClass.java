package com.devAB.register;

public class UserHelperClass {

    String name, username, email, phoneno;

    public UserHelperClass() {
    }

    public UserHelperClass(String name, String username, String email, String phoneno) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.phoneno = phoneno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }
    
}

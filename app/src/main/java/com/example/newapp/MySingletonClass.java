package com.example.newapp;

public class MySingletonClass {

    private static MySingletonClass instance;

    public static MySingletonClass getInstance() {
        if (instance == null)
            instance = new MySingletonClass();
        return instance;
    }

    private MySingletonClass() {
    }

    private String username;
    private int userid;
    private boolean debug = false;


    public String getUsername() {
        return username;
    }
    public int getUserid() { return userid;   }
    public boolean getDebug() {
        return debug;
    }

    public void setUsername(String value) {
        this.username = value;
    }
    public void setUserid(int value) {
        this.userid = value;
    }
}
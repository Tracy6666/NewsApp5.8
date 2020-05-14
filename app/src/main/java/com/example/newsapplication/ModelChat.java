package com.example.newsapplication;

import com.parse.ParseUser;

import java.util.Date;

public class ModelChat {
    private String message, receiver,sender;

    private Date date;

    public static final int STATUS_SENDING = 0;

    public  static final int STATUS_SENT = 1;

    public static final int STATUS_FAILED = 2;

    private int status = STATUS_SENT;

    private final String currentUser = ParseUser.getCurrentUser().getString("name");


    public ModelChat() {
    }

    public ModelChat(String message, Date date, String sender) {
        this.message = message;
        this.date = date;
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public  int getStatus(){
        return status;
    }

    public void setStatus(int status){
        this.status = status;
    }

    public boolean isSent() {
        return  currentUser.equals(sender);
    }
}

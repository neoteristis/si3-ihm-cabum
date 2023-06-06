package com.example.ihm_cabum.presenter.notification;

import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Observable;

public class Message extends Observable {
    private static RemoteMessage message;
    private static Message instance;

    public static Message getInstance(){
        if (instance==null) instance=new Message();
        return instance;
    }

    private Message(){
        super();
    }

    public void set(RemoteMessage message) {
        Message.message = message;
        setChanged();
        notifyObservers(message);
    }

    public String getFrom() {
        return message.getFrom();
    }

    public Map<String, String> getData() {
        return message.getData();
    }

    public boolean isNull() {
        return message.getNotification()==null;
    }

    public String getTitle() {
        return message.getNotification()==null ? null : message.getNotification().getTitle();
    }

    public String getBody() {
        return message.getNotification()==null ? null : message.getNotification().getBody();
    }
}

package com.scanba.hibuddy.models;


import com.facebook.Profile;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

public class Chat {

    private String message;
    private String author;
    private long sentAt;

    public Chat() {

    }

    public Chat(String message, String author, long sentAt) {
        this.message = message;
        this.author = author;
        this.sentAt = sentAt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getSentAt() {
        return sentAt;
    }

    public void setSentAt(long sentAt) {
        this.sentAt = sentAt;
    }

    public static void save(DatabaseReference fireBaseDatabase, Profile profile, String message) {
        String key = fireBaseDatabase.child("chat").push().getKey();
        Chat chat = new Chat(message, profile.getName(), System.currentTimeMillis());
        Map<String, Object> chats = new HashMap<>();
        chats.put("/chat/" + key, chat);
        fireBaseDatabase.updateChildren(chats);
    }
}

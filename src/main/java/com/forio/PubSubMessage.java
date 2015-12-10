package com.forio;

import java.util.Date;

public class PubSubMessage {

    private String id;
    private String message;
    private Date timestamp;

    public PubSubMessage(String messageOnTopic) {
        this.id = "1234";//UUID.randomUUID().toString();
        this.message =  messageOnTopic;
        this.timestamp = new Date();
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }


}

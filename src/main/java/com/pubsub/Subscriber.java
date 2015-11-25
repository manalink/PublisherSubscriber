package com.pubsub;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Subscriber {

    private String id;
    private Map<String, List<Message>> messagesPerTopic; //there could be multiple topics for a given subscriber
    private boolean isActive;

    public Subscriber() {
        messagesPerTopic = new HashMap<String, List<Message>>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Message> getMessagesForSingleTopic(String topic) {
        return messagesPerTopic.get(topic);
    }

    public Map<String, List<Message>> getMessagesForAllTopic() {
        return messagesPerTopic;
    }

    public void setMessagesPerTopic(Map<String, List<Message>> messagesPerTopic) {
        this.messagesPerTopic = messagesPerTopic;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }



}

package com.pubsub;

import java.util.*;

public class Topic {

    private String topic;
    private Map<String, Boolean> subscribers; //map of subscriber < id, isActive>
    private List<Message> messages; //master list of messages for a given topic


//    private Status status;

    public Topic(String topic) {
        this.topic = topic;
        subscribers = new HashMap<String, Boolean>();
        messages = new ArrayList<Message>();
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Map<String, Boolean> getSubscribers() {
        return subscribers;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }


}

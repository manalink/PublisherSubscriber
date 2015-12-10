package com.pubsub;


public interface PubSub {

    public void publish(String topicName, String newMessage);

    public String subscribe(String topicName);

    public void unsubscribe(String topicName, String id);

    public void resubscribe(String topicName, String id);
}

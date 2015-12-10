package com.forio;


import org.apache.log4j.Logger;

import java.util.concurrent.locks.ReentrantLock;


public class Publisher {
    private  MessageQueue messageQueue;


    public Publisher() {
        messageQueue = new MessageQueue();
    }

    public  void publish(String topicName, String messageOnTopic) {
        System.out.println("Publishing " + messageOnTopic + " onto topic " + topicName);
        PubSubMessage pubSubMessage = new PubSubMessage(messageOnTopic);
        messageQueue.setMessageQueue(topicName, pubSubMessage);
    }
}

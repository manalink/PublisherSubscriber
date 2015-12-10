package com.forio;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class Topic {

    private String topicName;
    private String topicId;
    private static Map<String, Topic> availableTopics;

    private Topic(String topicName) {
        synchronized (this) {
            this.topicName = topicName;
            this.topicId = "1234";
            this.availableTopics =  new HashMap<String, Topic>();
        }
    }

    public String getTopicId() {
        return topicId;
    }

    public String getTopicName() {
        return topicName;
    }



    public synchronized static Topic getInstance(String topicName) {

        if(availableTopics!= null && availableTopics.keySet().contains(topicName)) {
            return availableTopics.get(topicName);
        }
        else {
//            ReentrantLock reentrantLock = new ReentrantLock();
            try {
//                reentrantLock.lock();
                Topic topic = new Topic(topicName);
                availableTopics.put(topicName,topic);
                return topic;
            } finally {
//                reentrantLock.unlock();
            }


        }
    }

    public synchronized static Topic getTopicById(String topicId) {
        for(Topic topic : availableTopics.values()) {
            if(topic.getTopicId().equalsIgnoreCase(topicId)) {
                return topic;
            }
        }
        return null;
    }


}

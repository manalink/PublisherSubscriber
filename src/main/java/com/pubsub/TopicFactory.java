package com.pubsub;

import java.util.HashMap;
import java.util.Map;


public class TopicFactory {

    private final static Map<String, Topic> availableTopics = new HashMap<String, Topic>();

    public static Topic getInstance(String topicName) {
       if(availableTopics.keySet().contains(topicName)) {
           return availableTopics.get(topicName);
       }
        else {
           Topic topic = new Topic(topicName);
           availableTopics.put(topicName,topic);
           return topic;
       }
    }
}

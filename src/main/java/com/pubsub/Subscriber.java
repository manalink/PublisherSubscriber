package com.pubsub;

import java.util.*;

public class Subscriber {

    private String id;



    private Map<String,TopicLifeCycle> messagesPerTopic; //topic->subscriber's topic lifecycle mapping

    private class TopicLifeCycle {
        List<String> messageIds;
        boolean isActive;
        Date timeOfOriginalSubscription;
        Date timeOfUnsubscription;
        Date timeOfResubscription;
    }
    private TopicLifeCycle topicLifeCycle;

    public Subscriber() {
        messagesPerTopic = new HashMap<String, TopicLifeCycle>();
    }
    public Date getTimeofOriginalSubscription() {
        return topicLifeCycle.timeOfOriginalSubscription;
    }

    public Date getTimeOfUnscbscription() {
        return topicLifeCycle.timeOfUnsubscription;
    }

    public Date getTimeOfResubscription() {
        return topicLifeCycle.timeOfResubscription;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getMessagesForSingleTopic(String topic) {
        return messagesPerTopic.get(topic).messageIds;
    }

//    public Map<String, List<String>> getMessagesForAllTopic() {
//        return messagesPerTopic;
//    }

//    public void setMessagesPerTopic(Map<String, List<String>> messagesPerTopic) {
//        this.messagesPerTopic = messagesPerTopic;
//    }

    public void appendListOfMsgsForTopic(String topic, String messageId) {
        TopicLifeCycle topicTopicLifeCycle = messagesPerTopic.get(topic);
        if (topicTopicLifeCycle.messageIds == null) {
            topicTopicLifeCycle.messageIds = new ArrayList<String>();
        }
             topicTopicLifeCycle.messageIds.add(messageId);
    }

    public boolean isActive() {
        return topicLifeCycle.isActive;
    }

    public void setIsActive(boolean isActive) {
        this.topicLifeCycle.isActive = isActive;
    }

    public Map<String, TopicLifeCycle> getMessagesPerTopic() {
        return messagesPerTopic;
    }

    public void setMessagesPerTopic(Map<String, TopicLifeCycle> messagesPerTopic) {
        this.messagesPerTopic = messagesPerTopic;
    }

    public void updateStatus(String topicName, String action){

        if(action.equalsIgnoreCase("subscribe")) {
            topicLifeCycle = new TopicLifeCycle();
            topicLifeCycle.timeOfOriginalSubscription = new Date();
            topicLifeCycle.isActive = true;
            topicLifeCycle.messageIds = new ArrayList<String>();
            messagesPerTopic.put(topicName, topicLifeCycle);
        } else if(action.equalsIgnoreCase("unsubscribe")) {
            TopicLifeCycle topicLifeCycle = messagesPerTopic.get(topicName);
            topicLifeCycle.timeOfUnsubscription = new Date();
            topicLifeCycle.isActive = false;
        } else if(action.equalsIgnoreCase("resubscribe")) {
            TopicLifeCycle topicLifeCycle = messagesPerTopic.get(topicName);
            topicLifeCycle.timeOfResubscription = new Date();
            topicLifeCycle.isActive = true;
        }
        //update topicLifeCycle in Topic hashmap
        Topic topic = TopicFactory.getInstance(topicName);
        topic.getSubscribers().put(this.getId(), this.topicLifeCycle.isActive);
    }

}

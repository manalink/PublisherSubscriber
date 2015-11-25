package com.pubsub;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class PubSub {

    private Map<String, List<Subscriber>> subscribersPerTopic;

    public PubSub() {
        subscribersPerTopic = new HashMap<String, List<Subscriber>>();
    }

    public void publish(String topicName, String newMessage) {
        Topic topic = TopicFactory.getInstance(topicName);
        List<Message> messages = topic.getMessages();
        Message message = new Message();
        message.setMessage(newMessage);
        message.setTimestamp(new Date());
        messages.add(message);
        topic.setMessages(messages); //master list is populated
        for (String id : topic.getSubscribers().keySet()) {
            if (topic.getSubscribers().get(id)) { //active subscriber
                Subscriber subscriber = getSubscriber(topicName, id);
                Map<String, List<Message>> messagesForTopic = new HashMap<String, List<Message>>();
                messagesForTopic.put(topicName, messages);
                subscriber.setMessagesPerTopic(messagesForTopic);
            }
        }
    }

    public String subscribe(String topicName) {
        //update pubsub map
        String id = UUID.randomUUID().toString();
        List<Subscriber> availableSubscribers;
        if (subscribersPerTopic.get(topicName) == null) {
            availableSubscribers = new ArrayList<Subscriber>();
        } else {
            availableSubscribers = subscribersPerTopic.get(topicName);
        }

        Subscriber subscriber = new Subscriber();
        subscriber.setId(id);
        availableSubscribers.add(subscriber);
        subscribersPerTopic.put(topicName, availableSubscribers); //update the map

        //update topic map
        Topic topic = TopicFactory.getInstance(topicName);
        Map<String, Boolean> subscribersForTopic = topic.getSubscribers();
        subscribersForTopic.put(id, true); //set active subscribers for a given topic

        return id;
    }

    public void unsubscribe(String topic, String id) {
    }

    public void resubscribe(String id) {
    }

    public Subscriber getSubscriber(String topic, String id) {
        List<Subscriber> subscribers = subscribersPerTopic.get(topic);
        for (Subscriber subscriber : subscribers) {
            if (subscriber.getId().equals(id)) {
                return subscriber;
            }
        }
        return null;
    }

}

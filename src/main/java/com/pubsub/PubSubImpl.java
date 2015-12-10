package com.pubsub;

import org.apache.log4j.Logger;

import java.util.*;

public class PubSubImpl implements PubSub {

    private volatile Map<String, List<Subscriber>> subscribersPerTopic;
    private final static Logger logger = Logger.getLogger(PubSubImpl.class);

    public PubSubImpl() {
        subscribersPerTopic = new HashMap<String, List<Subscriber>>();
    }

    public void publish(String topicName, String newMessage) {
        logger.info("Now publishing " + newMessage + "->" + topicName);
        Topic topic = TopicFactory.getInstance(topicName);
        List<Message> messages = topic.getMessages();

        String messageId = UUID.randomUUID().toString();
        Message message = MessageFactory.getInstance(messageId);
        message.setMessage(newMessage);
        messages.add(message);
        topic.setMessages(messages); //master list is populated

        //now for a given topic, get all active subscribers and append new message for them
        for (String id : topic.getSubscribers().keySet()) {
            if (topic.getSubscribers().get(id) == true) { //active subscriber
                Subscriber subscriber = getSubscriber(topicName, id);
                subscriber.appendListOfMsgsForTopic(topicName, messageId);
            }
        }
    }

    public String subscribe(String topicName) {
        //update pubsub map
        logger.info("Subscribing " + topicName);
        String id = UUID.randomUUID().toString();
        List<Subscriber> availableSubscribers;
        if (subscribersPerTopic.get(topicName) == null) {
            availableSubscribers = new ArrayList<Subscriber>();
        } else {
            availableSubscribers = subscribersPerTopic.get(topicName);
        }

        Subscriber subscriber = new Subscriber();
        subscriber.setId(id);
        subscriber.updateStatus(topicName, "subscribe");
        availableSubscribers.add(subscriber);
        subscribersPerTopic.put(topicName, availableSubscribers); //update the map

//        //update master list of messages - update topic map
//        Topic topic = TopicFactory.getInstance(topicName);
//        Map<String, Boolean> subscribersForTopic = topic.getSubscribers();
//        subscribersForTopic.put(id, true); //set active subscribers for a given topic

        return id;
    }

    public void unsubscribe(String topicName, String id) {
        Subscriber subscriber = getSubscriber(topicName, id);
        subscriber.updateStatus(topicName, "unsubscribe");
        logger.info("Unsubscribed");
    }

    public void resubscribe(String topicName, String id) {
        Subscriber subscriber = getSubscriber(topicName, id);
        logger.info("Resubsribing");

        subscriber.updateStatus(topicName, "resubscribe");
        Date timeOfUnsubscription = subscriber.getTimeOfUnscbscription();
        Date timeofResubscription = subscriber.getTimeOfResubscription();
        //now fetch the delta;
        Topic topic = TopicFactory.getInstance(topicName);

        for (Message message : topic.getMessages()) {
            if (message.getTimestamp().after(timeOfUnsubscription)
                    && message.getTimestamp().before(timeofResubscription)) {
                subscriber.appendListOfMsgsForTopic(topicName, message.getId());
            }
        }
    }

    public Subscriber getSubscriber(String topic, String subscriberId) {
        List<Subscriber> subscribers = subscribersPerTopic.get(topic);
        for (Subscriber subscriber : subscribers) {
            if (subscriber.getId().equals(subscriberId)) {
                return subscriber;
            }
        }
       throw new RuntimeException("Couldn't find subscriber for "+topic + " and  subscriberId "+ subscriberId);
    }

}

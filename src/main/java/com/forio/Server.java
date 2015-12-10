package com.forio;


import org.apache.log4j.Logger;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;


public class Server extends Observable implements Observer {

    private Map<String, List<PubSubMessage>> masterQueue;
    private Map<String, List<Subscriber>> subscribersPerTopic;
    private static Server server = null;
    private Logger logger;

    private Server() { //singleton server;
        masterQueue = new HashMap<String, List<PubSubMessage>>();
        subscribersPerTopic = new HashMap<String, List<Subscriber>>();
    }

    public synchronized static Server getInstance() {
        if (server == null) {
            server = new Server();
        }
        return server;
    }


    public Map<String, List<PubSubMessage>> getMasterQueue() {
        return masterQueue;
    }

    public void setMasterQueue(Map<String, List<PubSubMessage>> masterQueue) {
        this.masterQueue = masterQueue;
    }

    public Map<String, List<Subscriber>> getSubscribersPerTopic() {
        return subscribersPerTopic;
    }

    public void setSubscribersPerTopic(Map<String, List<Subscriber>> subscribersPerTopic) {
        this.subscribersPerTopic = subscribersPerTopic;
    }


    private static Boolean isServerRunning = true;


    //syncronize this block
    public void update(Observable o, Object arg) {
        Object[] arr = (Object[]) arg;
        String topicName = (String) arr[0];
        PubSubMessage pubSubMessage = (PubSubMessage) arr[1];
        ReentrantLock reentrantLock = new ReentrantLock();
        reentrantLock.lock();
        //update master list
        List<PubSubMessage> masterListOfMsgs = masterQueue.get(topicName);
        if (masterListOfMsgs == null) {
            masterListOfMsgs = new LinkedList<PubSubMessage>();
        }
        masterListOfMsgs.add(pubSubMessage);
        masterQueue.put(topicName, masterListOfMsgs);

        //refactor this and split into methods
        //publish to ONLY subscribers associated with the topic
        List<Subscriber> availableSubscribers = subscribersPerTopic.get(topicName);
        if (availableSubscribers != null) {
            for (Subscriber subscriber : availableSubscribers) {
                this.addObserver(subscriber);
            }
            setChanged();
            Object[] objects = {topicName, pubSubMessage};
            notifyObservers(objects);
        }
        this.deleteObservers(); //flush subscribers
        reentrantLock.unlock();
    }

//    private ReentrantLock reentrantLock = new ReentrantLock();

    public synchronized String subscribe(String topicName, Subscriber subscriber) {

        Topic topic = Topic.getInstance(topicName);
        List<Subscriber> availableSubscribers = subscribersPerTopic.get(topicName);
        if (availableSubscribers == null) {
            availableSubscribers = new LinkedList<Subscriber>();
        }
        availableSubscribers.add(subscriber);
        subscribersPerTopic.put(topicName, availableSubscribers);
        return topic.getTopicId();
    }

    public List<PubSubMessage> resubscribe(String topicId, Subscriber subscriber) {
        //check for last message for subscriber
        //then check for master list
        //return msgs from master which are >last msg received by subscriber && <= resubscribe time
        Date resubscribeTime = new Date();
        Topic topic = Topic.getTopicById(topicId);
        List<PubSubMessage> pubSubMessageList = masterQueue.get(topic.getTopicName());
        Map<String, List<PubSubMessage>> subscriberMessegesForTopics = subscriber.getMessegesForTopics();
        List<PubSubMessage> subscriberMsgs = subscriberMessegesForTopics.get(topic.getTopicName());
        PubSubMessage subscribersLastMsg = subscriberMsgs.get(subscriberMsgs.size() - 1);
        Date lastMessageTime = subscribersLastMsg.getTimestamp();
        List<PubSubMessage> missedPubSubMessages = new LinkedList<PubSubMessage>();
        List<PubSubMessage> masterPubSubMessages = masterQueue.get(topic.getTopicName());
        for (PubSubMessage pubSubMessage : masterPubSubMessages) {
            if (pubSubMessage.getTimestamp().after(lastMessageTime) && pubSubMessage.getTimestamp().before(resubscribeTime)) {
                missedPubSubMessages.add(pubSubMessage);
            }
        }
        return missedPubSubMessages;
    }
}

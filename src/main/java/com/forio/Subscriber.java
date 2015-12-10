package com.forio;


import org.apache.log4j.Logger;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class Subscriber implements Observer {

    private Map<String, List<PubSubMessage>> messegesForTopics;
    private Server server = Server.getInstance();

//    private Logger logger;



    public Subscriber() {
        messegesForTopics = new HashMap<String, List<PubSubMessage>>();
//        synchronized (this) {
//            logger = Logger.getLogger(Subscriber.class);
//        }
    }

    public String subscribe(String topicName) {
//        logger.info("Now subscribing to "+topicName);
        return server.subscribe(topicName, this);
    }

    public List<PubSubMessage> resubscribe(String id) {
        List<PubSubMessage> missedList =  server.resubscribe(id, this);
        Topic topic = Topic.getTopicById(id);
        messegesForTopics.get(topic.getTopicName()).addAll(missedList);
        return missedList;
    }

    public void update(Observable o, Object arg) {

        Object[] arr = (Object[]) arg;
        String topicName = (String) arr[0];
        PubSubMessage pubSubMessage = (PubSubMessage) arr[1];
        ReentrantLock reentrantLock = new ReentrantLock();
        //update list of messages for a given topic
        reentrantLock.lock();
//        logger.info("Subscriber received "+pubSubMessage.getMessage());
        List<PubSubMessage> updatedListOfMsgsForATopic = messegesForTopics.get(topicName);
        if (updatedListOfMsgsForATopic == null) {
            updatedListOfMsgsForATopic = new LinkedList<PubSubMessage>();
        }
        updatedListOfMsgsForATopic.add(pubSubMessage);
        messegesForTopics.put(topicName, updatedListOfMsgsForATopic);
        reentrantLock.unlock();
    }

    public Map<String, List<PubSubMessage>> getMessegesForTopics() {
        return messegesForTopics;
    }
    public String getLastMessageForTopic(String topicName) {
        List<PubSubMessage> pubSubMessageList = messegesForTopics.get(topicName);
        return pubSubMessageList.get(pubSubMessageList.size()-1).getMessage();
    }

    public List<PubSubMessage> getMessagesForaTopic(String topicName) {
        return messegesForTopics.get(topicName);
    }
}

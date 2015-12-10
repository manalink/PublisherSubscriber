package com.forio;


import java.util.Observable;

public class MessageQueue extends Observable {


    public MessageQueue() {
        Server server = Server.getInstance();
        this.addObserver(server);
    }

    public void setMessageQueue(String topicName, PubSubMessage pubSubMessage) {
        Object[] objects = {topicName, pubSubMessage};
        setChanged();
        notifyObservers(objects);
    }

}

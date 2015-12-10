package com.pubsub;


import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Unit test for simple App.
 */
public class AppTest
{
    private final static Logger logger = Logger.getLogger(PubSubImpl.class);

    //Create a publisher/topic and publish
    @Test
    public void test() {

        PubSubImpl pubSubImpl = new PubSubImpl();
        String subscriberId = pubSubImpl.subscribe("topic1");
        pubSubImpl.publish("topic1", "hello world");
        Subscriber subscriber = pubSubImpl.getSubscriber("topic1", subscriberId);
        List<String> messageList = subscriber.getMessagesForSingleTopic("topic1");
        Assert.assertEquals(1, messageList.size());
        Message message = MessageFactory.getInstance(messageList.get(0));
        System.out.println(message.getMessage());
        Assert.assertEquals("hello world", message.getMessage());
    }

    @Test
    public void test2() {
        PubSubImpl pubSubImpl = new PubSubImpl();
        String subscriberId = pubSubImpl.subscribe("topic1");
        pubSubImpl.publish("topic1", "hello world");
        pubSubImpl.publish("topic1", "ola");
        pubSubImpl.publish("topic1", "namaskar");
        Subscriber subscriber = pubSubImpl.getSubscriber("topic1", subscriberId);
        List<String> messageList = subscriber.getMessagesForSingleTopic("topic1");
        Assert.assertEquals(3, messageList.size());
        Message message = MessageFactory.getInstance(messageList.get(2));
        System.out.println(message.getMessage());
        Assert.assertEquals("namaskar", message.getMessage());
    }

    @Test
    public void resubscribeTest() {
        PubSubImpl pubSubImpl = new PubSubImpl();
        String subscriberId = pubSubImpl.subscribe("topic1");
        pubSubImpl.publish("topic1", "hello world");
        pubSubImpl.publish("topic1", "ola");
        pubSubImpl.unsubscribe("topic1",subscriberId); //he goes silent
        pubSubImpl.publish("topic1", "hello new world");
        pubSubImpl.publish("topic1", "ola new world");
        //messages before resubscribing
        Subscriber subscriber = pubSubImpl.getSubscriber("topic1", subscriberId);
        List<String> messageList = subscriber.getMessagesForSingleTopic("topic1");
        logger.info("----------------------------------------------");
        for(String messageId : messageList) {
            Message message = MessageFactory.getInstance(messageId);
            logger.info(message.getMessage());
        }
        logger.info("----------------------------------------------");
        pubSubImpl.resubscribe("topic1",subscriberId);
//        Subscriber subscriber = pubSubImpl.getSubscriber("topic1", subscriberId);
        messageList = subscriber.getMessagesForSingleTopic("topic1");

        logger.info("----------------------------------------------");
       for(String messageId : messageList) {
           Message message = MessageFactory.getInstance(messageId);
           logger.info(message.getMessage());
       }
        Assert.assertEquals(4, messageList.size());
    }
}

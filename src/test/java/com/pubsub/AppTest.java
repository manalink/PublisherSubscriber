package com.pubsub;


import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Unit test for simple App.
 */
public class AppTest
{
    //Create a publisher/topic and publish
    @Test
    public void test() {

        PubSub pubSub = new PubSub();
        String id = pubSub.subscribe("topic1");
        pubSub.publish("topic1", "hello world");
        Subscriber subscriber = pubSub.getSubscriber("topic1", id);
        List<Message> messageList = subscriber.getMessagesForSingleTopic("topic1");
        Assert.assertEquals(1, messageList.size());
        System.out.println(messageList.get(0).getMessage());
        Assert.assertEquals("hello world", messageList.get(0).getMessage());
    }
}

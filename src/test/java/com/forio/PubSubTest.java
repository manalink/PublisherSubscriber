package com.forio;


import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;

public class PubSubTest {


    @Test
    public void onePubOneSub() {
        Subscriber subscriber = new Subscriber();
        String topicName = "topic1";
        String id = subscriber.subscribe(topicName);
        Publisher publisher = new Publisher();
        publisher.publish(topicName, "Hello World");

        assertEquals("Hello World", subscriber.getLastMessageForTopic(topicName));
    }

    @Test
    public void multiPublishesOneSub() {
        Subscriber subscriber = new Subscriber();
        String topicName = "topic1";
        String id = subscriber.subscribe(topicName);
        Publisher publisher = new Publisher();
        publisher.publish(topicName, "Hello World");
        publisher.publish(topicName, "Ola");
        publisher.publish(topicName, "Namaskar");
        assertEquals(3, subscriber.getMessagesForaTopic(topicName).size());
        assertEquals("Namaskar", subscriber.getLastMessageForTopic(topicName));
    }

    @Test
    public void multiPubsOneSub() {

        Subscriber subscriber = new Subscriber();
        String topicName = "topic1";
        String id = subscriber.subscribe(topicName);

        Publisher publisher1 = new Publisher();
        publisher1.publish(topicName, "Hello World");
        publisher1.publish(topicName, "Ola");

        Publisher publisher2 = new Publisher();
        publisher2.publish(topicName, "Namaskar");

        Publisher publisher3 = new Publisher();
        publisher3.publish("TopicThatNobodySubscribedTo", "I Should not show up");


        assertEquals(3, subscriber.getMessagesForaTopic(topicName).size());
        assertEquals("Namaskar", subscriber.getLastMessageForTopic(topicName));
    }

    @Test
    public void onePubMultipleSub() {

        String topicName = "topic1";

        Subscriber subscriber1 = new Subscriber();
        subscriber1.subscribe(topicName);


        Publisher publisher = new Publisher();
        publisher.publish(topicName, "Hello World");
        publisher.publish(topicName, "Ola");

        Subscriber subscriber2 = new Subscriber();
        subscriber2.subscribe(topicName);

        publisher.publish(topicName, "ola again");
        publisher.publish(topicName, "signing off guys");


        assertEquals(4, subscriber1.getMessagesForaTopic(topicName).size());
        assertEquals(2, subscriber2.getMessagesForaTopic(topicName).size());
        assertEquals("signing off guys", subscriber2.getLastMessageForTopic(topicName));
    }

    @Test
    public void multiPubSub() {

        String topic1 = "topic1";
        String topic2 = "topic2";

        Subscriber subscriber1 = new Subscriber();
        subscriber1.subscribe(topic1);


        Publisher publisher1 = new Publisher();
        publisher1.publish(topic1, "Hello World");
        publisher1.publish(topic1, "Ola");

        Subscriber subscriber2 = new Subscriber();
        subscriber2.subscribe(topic2);

        Publisher publisher2 = new Publisher();
        publisher2.publish(topic2, "Hello World");
        publisher2.publish(topic2, "Ola");

        assertEquals(2, subscriber1.getMessagesForaTopic(topic1).size());
        assertEquals(2, subscriber2.getMessagesForaTopic(topic2).size());

        subscriber2.subscribe(topic1);
        publisher2.publish(topic1, "Hello new World");
        publisher2.publish(topic1, "Ola new world");

        assertEquals(2, subscriber2.getMessagesForaTopic(topic1).size());
        assertEquals(4, subscriber1.getMessagesForaTopic(topic1).size());

    }

    @Test
    public void resubscribe() {
//        final Subscriber subscriber = new Subscriber();
//        final Publisher publisher = new Publisher();
        final String topicName = "topic1";

        ExecutorService executorService = Executors.newFixedThreadPool(1);

//        executorService.execute(new Runnable() {
//            Subscriber subscriber = new Subscriber();
//            public void run() {
//                logger.info("I'm trying to subscribe");
//                String id = subscriber.subscribe(topicName);
//                try {
//                    logger.info("Now sleeping");
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                logger.info(">>>>>>>> " + subscriber.getMessagesForaTopic(topicName).size());

//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                List<PubSubMessage> missedMessages = subscriber.resubscribe(id);

//            }
//        });

        executorService.execute(new Runnable() {
            Publisher publisher = new Publisher();
            public void run() {

                publisher.publish(topicName, "ello");
//                    String[] messagesToPublish = {"hello", "ola", "namaste"};
//                    for (String message : messagesToPublish) {
//                        try {
//                            publisher.publish(topicName, message);
////                        Thread.sleep(500);
//                        } catch (Throwable e) {
//                            e.printStackTrace();
//                        }
//                    }
                }
        });
//        ExecutorService executorService2 = Executors.newFixedThreadPool(10);

//        executorService.shutdown();
//        executorService2.shutdown();
//        Thread publisherThread = new Thread() {
//            public void run() {
//                String[] messagesToPublish = {"hello", "ola", "namaste"};
//                System.out.println("publisher thread started");
//                for (String message : messagesToPublish) {
//                    try {
//                        System.out.println(message);
////                        publisher.publish(topicName, message);
//                    } catch (Throwable e) {
//                        e.printStackTrace();
//                    }
////                    try {
//////                        Thread.sleep(500);
////                    } catch (InterruptedException e) {
////                        e.printStackTrace();
////                    }
//                }
//            }
//        };

//        Thread subscriberThread = new Thread() {
//            public void run() {
//                System.out.println("subscriber thread started");
//                String id = subscriber.subscribe(topicName);
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                List<PubSubMessage> missedMessages = subscriber.resubscribe(id);
//                logger.info(missedMessages.size());
//            }
//        };
//        publisherThread.start();
//        subscriberThread.start();

    }


}

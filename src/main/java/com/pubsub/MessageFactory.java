package com.pubsub;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MessageFactory {

    private static final Map<String,Message> messageMap = new HashMap<String, Message>();

    public static Message getInstance(String messageId) {
        if(messageMap.get(messageId)!=null) {
            return messageMap.get(messageId);
        } else {
            Message message = new Message();
            message.setId(messageId);
            message.setTimestamp(new Date());
            messageMap.put(messageId,message);
            return message;
        }
    }
}

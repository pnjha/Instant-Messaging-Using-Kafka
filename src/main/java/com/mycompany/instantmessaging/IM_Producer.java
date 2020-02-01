/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.instantmessaging;
import java.util.*;
import org.apache.kafka.clients.producer.*;

/**
 *
 * @author prakashjha
 */
public class IM_Producer {

    Properties props;
    String myId;
    
    public IM_Producer(String myId, String socket){
        
        this.myId = myId;
        this.props = new Properties();
        this.props.put("bootstrap.servers", socket);
        this.props.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
        this.props.put("value.serializer", "com.mycompany.instantmessaging.IM_Serializer");
    }
    
    public boolean produceMessage(String msgId, String topicName, String msg){
        try{
            Producer<String, Message> producer = new KafkaProducer <>(this.props);
            Message sp1 = new Message(msgId,topicName,msg);
            producer.send(new ProducerRecord<String,Message>(this.myId,"SUP",sp1)).get();
            producer.close();
            return true;
        }catch(Exception e){
            System.out.println("Error occured in produceMessage method");
            e.printStackTrace();
            return false;
        }
    }        
}

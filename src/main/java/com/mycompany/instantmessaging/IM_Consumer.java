/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.instantmessaging;
import java.util.*;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 *
 * @author prakashjha

*/

public class IM_Consumer {

        public int maxAttempts;
        public String myID;
        public Properties props;
        public ArrayList<String> messages;
        public ArrayList<String> topics;
        
        public IM_Consumer(String myID, String socket, int maxAttempts){

                this.maxAttempts = maxAttempts;
                this.myID = myID;
                this.props = new Properties();
                this.props.put("bootstrap.servers", socket);
                this.props.put("group.id", "com.mycompany");
                this.props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
                this.props.put("value.deserializer", "com.mycompany.instantmessaging.IM_Deserializer");
        }
        
        public ArrayList<String> getMessages(){
                return this.messages;
        }
        
        public void setMessages(){
                this.messages = new ArrayList<String>();
        }
        
        public ArrayList<String> getTopics(){
                setTopics();
                KafkaConsumer<String, Message> consumer = new KafkaConsumer<>(this.props);
                for(String key : consumer.listTopics().keySet()){
                    this.topics.add(key);
                }
                return this.topics;
        }
        
        public void setTopics(){
                this.topics = new ArrayList<String>();
        }
        
        public boolean consumeMessage(String topicName){
                try{
                    setMessages();
                    
                    KafkaConsumer<String, Message> consumer = new KafkaConsumer<>(this.props);
                    consumer.subscribe(Arrays.asList(topicName));
                    
                    int temp = this.maxAttempts;
                    boolean flag = false;
                    
                    while(temp>0){
                        ConsumerRecords<String, Message> records = consumer.poll(100);
                        for (ConsumerRecord<String, Message> record : records){

                                String messageID = String.valueOf(record.value().getMessageID());
                                String ReceiverID = String.valueOf(record.value().getReceiverID());
                                String Message = String.valueOf(record.value().getMessage());
                                if(ReceiverID.equals(this.myID)==true){
                                    System.out.println("Message ID= " + messageID + " Receiver ID = " + ReceiverID + " Message = " + Message);
                                    flag = true;
                                }
                              
                                this.messages.add(messageID+","+Message);
                        }
                        if(flag) break;
                        temp--;
                    }
                    if(flag==false){
                        System.out.println("No messages to read");
                    }
                    consumer.close();                 
                    return true;
                }
                catch(Exception e){
                    System.out.println("Error in consumeData method");
                    e.printStackTrace();
                    return false;
                }
        }
  
}


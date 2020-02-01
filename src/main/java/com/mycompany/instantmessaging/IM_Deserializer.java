/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.instantmessaging;
import java.nio.ByteBuffer;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 *
 * @author prakashjha
 */
public class IM_Deserializer  implements Deserializer<Message> {
    private String encoding = "UTF8";

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
                //Nothing to configure
        }

    @Override
    public Message deserialize(String topic, byte[] data) {

        try {
            if (data == null){
                System.out.println("Null recieved at deserialize");
                                return null;
                                }
            ByteBuffer buf = ByteBuffer.wrap(data);
            
            int sizeOfMessageID = buf.getInt();
            byte[] serializedMessageID = new byte[sizeOfMessageID];
            buf.get(serializedMessageID);
            String deserializedMessageID = new String(serializedMessageID, encoding);
            
            int sizeOfReceiverID = buf.getInt();
            byte[] serializedReceiverID = new byte[sizeOfReceiverID];
            buf.get(serializedReceiverID);
            String deserializedReceiverID = new String(serializedReceiverID, encoding);
            
            int sizeOfMessage = buf.getInt();
            byte[] serializedMessage = new byte[sizeOfMessage];
            buf.get(serializedMessage);
            String deserializedMessage = new String(serializedMessage, encoding);

         
            return new Message(deserializedMessageID,deserializedReceiverID,deserializedMessage);



        } catch (Exception e) {
            throw new SerializationException("Error when deserializing byte[] to Supplier");
        }
    }

    @Override
    public void close() {
        // nothing to do
    }    
}


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.instantmessaging;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.errors.SerializationException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.nio.ByteBuffer;

/**
 *
 * @author prakashjha
 */
public class IM_Serializer implements Serializer<Message> {
    private String encoding = "UTF8";

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
                // nothing to configure
    }

    @Override
    public byte[] serialize(String topic, Message data) {

                int sizeOfMessageID;
                int sizeOfReceiverID;
                int sizeOfMessage;
                byte[] serializedMessageID;
                byte[] serializedReceiverID;
                byte[] serializedMessage;

        try {
            if (data == null) return null;
            
            serializedMessageID = data.getMessageID().getBytes(encoding);
            sizeOfMessageID = serializedMessageID.length;
            serializedReceiverID = data.getReceiverID().getBytes(encoding);
            sizeOfReceiverID = serializedReceiverID.length;
            serializedMessage = data.getMessage().getBytes(encoding);
            sizeOfMessage = serializedMessage.length;
            
            ByteBuffer buf = ByteBuffer.allocate(4+sizeOfMessageID+4+sizeOfReceiverID+4+sizeOfMessage);
            
            buf.putInt(sizeOfMessageID);
            buf.put(serializedMessageID);
            buf.putInt(sizeOfReceiverID);
            buf.put(serializedReceiverID);
            buf.putInt(sizeOfMessage);
            buf.put(serializedMessage);
            
            return buf.array();

        } catch (Exception e) {
            throw new SerializationException("Error when serializing Supplier to byte[]");
        }
    }

    @Override
    public void close() {
        // nothing to do
    }    
}


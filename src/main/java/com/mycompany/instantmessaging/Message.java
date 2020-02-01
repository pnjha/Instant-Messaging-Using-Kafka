/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.instantmessaging;

/**
 *
 * @author prakashjha
 */
import java.util.Date;

public class Message {
    private String messageId;
    private String receiverID;
    private String msg;

    public Message(String messageId, String receiverID, String msg){
        this.messageId = messageId;
        this.receiverID = receiverID;
        this.msg = msg;
    }

    public String getMessageID(){
        return this.messageId;
    }

    public String getReceiverID(){
            return this.receiverID;
    }

    public String getMessage(){
            return this.msg;
    }
}


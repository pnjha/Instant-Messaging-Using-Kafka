/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.instantmessaging;
import java.util.*;
import java.io.*;

/**
 *
 * @author prakashjha
 */
public class InstantMessaging {
    
    public static boolean writeToCSV(String fileName, String data){
    
        try{
            FileWriter csvWriter = new FileWriter(fileName,true);
            PrintWriter printWriter = new PrintWriter(csvWriter);
            printWriter.println(data);
            printWriter.close();
            return true;
        }
        catch(Exception e){
            
            e.printStackTrace();
            System.out.println("Error writing to csv file");
            return false;
        }
    }
    
    public static boolean chechFileExist(String fileName){
        try{
            File csvFile = new File(fileName);
            if (!csvFile.isFile()) {
                System.out.println("New user created");
                FileWriter csvWriter = new FileWriter(fileName);
                csvWriter.flush();
                csvWriter.close();
            }
            return true;
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("Error in chechFileExist method");
            return false;
        }
    }
    
    public static boolean getAllMessages(String userId, String topicName){
        try{
            String fileName = userId+".csv", row;
            File csvFile = new File(fileName);
            if (csvFile.isFile()) {
                BufferedReader csvReader = new BufferedReader(new FileReader(fileName));
                while ((row = csvReader.readLine()) != null) {
                    String[] data = row.split(",",4);
                    if(data[2].equals(topicName)==true){
                        if(data[1].equals("read")==true){
                            System.out.println("Received: "+data[3]);
                        }else{
                            System.out.println("Sent: "+data[3]);
                        }
                    }
                }
                csvReader.close();
            }
            return true;
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean storeTopicName(String userId, String topicName){
        try{
            
            Set<String> topicsSet = new LinkedHashSet<>();
            
            String fileName = userId+"contact.csv",row;
            
            chechFileExist(fileName);
            
            File csvFile = new File(fileName);
            if (csvFile.isFile()) {
                BufferedReader csvReader = new BufferedReader(new FileReader(fileName));
                while ((row = csvReader.readLine()) != null) {
                    String[] data = row.split(",");
                    topicsSet.add(data[0].trim());
                }
                csvReader.close();
            }
            
            topicsSet.add(topicName.trim());
            
            for(String topic : topicsSet){
                writeToCSV(fileName,topic);
             }
            return true;
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean readTopicsName(String userId){
        try{
            String fileName = userId+"contact.csv",row;
            File csvFile = new File(fileName);
            if (csvFile.isFile()) {
                BufferedReader csvReader = new BufferedReader(new FileReader(fileName));
                while ((row = csvReader.readLine()) != null) {
                    String[] data = row.split(",");
                    System.out.println(data[0]);
                }
                csvReader.close();
            }
            return true;
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("Error in readTopicsName method");
            return false;
        }
    }
    
    public static void main(String[] args) throws Exception{
        
        
        if(args.length != 3){
            System.err.println("Usage: java client <Server IP> <Server port no.> <Max Retry Attempts>");
            return;
        }

        String serverIp = args[0];
        String serverPort = args[1];
        int maxRetry = 0, uid = 1;
                
        try {
            int t = Integer.parseInt(args[1]);
            maxRetry = Integer.parseInt(args[2]);
        } catch (Exception e) {
            System.err.println("Invalid port number");
            System.exit(0);
        }
        
        String socket = serverIp+":"+serverPort;
        
        String userId, cmd, topicName, msg, msgId, pathToCsv, fileEntry;
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your username: ");
        userId = sc.nextLine();
        pathToCsv = userId+".csv";
                 
        chechFileExist(pathToCsv);
        
        while(true){
        
            cmd = sc.nextLine();
            if(cmd.equals("contact_list")==true){
                readTopicsName(userId);
                
            }else if(cmd.equals("get_topics")==true){
                
                IM_Consumer consumer = new IM_Consumer(userId,socket,maxRetry);
                ArrayList<String> topics = consumer.getTopics();
                for(int i = 0;i<topics.size();i++){
                    System.out.println(topics.get(i));
                    storeTopicName(userId,topics.get(i));
                }
                
            }else if(cmd.equals("send")==true){
                System.out.println("Enter user name whom you want to send this message: ");
                topicName = sc.nextLine();
                
                storeTopicName(userId,topicName);
                getAllMessages(userId,topicName);
                
                msgId = uid + "_" + userId;
                uid += 1;
                
                System.out.println("Enter your message: ");
                msg = sc.nextLine();
                
                IM_Producer producer = new IM_Producer(userId,socket);
                
                if(producer.produceMessage(msgId, topicName, msg)){
                    System.out.println("Message delivered successfully");
                    
                    //msgid,type,from/to,msg
                    fileEntry = msgId+","+"send,"+topicName+","+msg;
                    writeToCSV(pathToCsv,fileEntry);
                    producer = null;
                }else{
                    System.out.println("Unable to send messages");
                }
                
            }else if(cmd.equals("read")==true){
                
                System.out.println("Enter user name whose message you want to read: ");
                topicName = sc.nextLine();
                
                storeTopicName(userId,topicName);
                getAllMessages(userId,topicName);
                
                IM_Consumer consumer = new IM_Consumer(userId,socket,maxRetry);
                
                if(consumer.consumeMessage(topicName)){
                    
                    System.out.println("All messages read successfully");
                    
                    ArrayList<String> messages = consumer.getMessages();
                    consumer.setMessages();
                    
                    for (int i=0; i<messages.size(); i++){
                        
                        String[] tokens = messages.get(i).split(",", 2);                        
                        fileEntry = tokens[0] +","+"read,"+topicName+","+tokens[1];
                 
                        writeToCSV(pathToCsv,fileEntry);
                    }
                    consumer = null;
                    
                }else{
                    System.out.println("Unable to read messages");
                }
            }else if(cmd.equals("exit")==true){
                System.exit(0);
            }
        }
    }
}
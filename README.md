# Instant-Messaging-Using-Kafka

To run zookepper<br>
bin/zookeeper-server-start.sh config/zookeeper.properties<br>

To run Kafka<br>
bin/kafka-server-start.sh config/server.properties<br>

To Send message<br>
Command: send<br>

To read message<br>
Command: read<br>

To display contact list<br>
Command: contact_list<br>

To display active topics<br>
Command: get_topics<br>  

To run from terminal<br>
mvn install dependency:copy-dependencies<br>
sudo java -cp "target/InstantMessaging-1.0.jar:target/dependency/*" com.mycompany.instantmessaging.InstantMessaging 127.0.0.1 9092 10
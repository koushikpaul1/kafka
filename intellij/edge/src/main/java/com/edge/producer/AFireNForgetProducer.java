package com.edge.producer;
import java.util.*;
import java.util.concurrent.Future;

import org.apache.kafka.clients.producer.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AFireNForgetProducer {

    private static final Logger logger = LogManager.getLogger();

   public static void main(String[] args) throws Exception{


      String topicName = "hello-producer-topic";
	  String key = "Key-";
	  String value = "Value-";
      
      Properties props = new Properties();
      props.put("bootstrap.servers", "localhost:9092,localhost:9093,localhost:9094");
      props.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");         
      props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
       logger.info("Creating Kafka Producer...");
      Producer<String, String> producer = new KafkaProducer <>(props);
	
	  ProducerRecord<String, String> record = new ProducerRecord<>(topicName,key,value);
	 // producer.send(record);
       for (int i = 1; i <= 100; i++) {
           producer.send(new ProducerRecord<>(topicName,key, "Simple Message In order-" + i));
           producer.send(new ProducerRecord<>(topicName,key+i, "Simple Message-" + i));
       }
      producer.close();
       logger.info("Closing Kafka Producer...");
	  System.out.println("SimpleProducer Completed.");
   }
}
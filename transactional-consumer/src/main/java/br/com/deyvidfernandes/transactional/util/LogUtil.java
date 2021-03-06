package br.com.deyvidfernandes.transactional.util;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public class LogUtil {
    public static void showLogTopic(ConsumerRecord<String, String> record) {
        System.out.println("------------------------------------------");
        System.out.println("------------------ LOG -------------------");
        System.out.println("------------------------------------------");
        System.out.println("topic: " + record.topic());
        System.out.println("partition: " + record.partition());
        System.out.println("offset: " + record.offset());
        System.out.println("key: " + record.key());
        System.out.println("value: " + record.value());
    }
}

package br.com.deyvidfernandes.transactional.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

@Service
public class TransactionalRetryDlqProducer {
    @Autowired
    private KafkaProducer<String, String> producer;

    public Future<RecordMetadata> producer(String topic, String value){
        ProducerRecord<String, String> recordProducer = new ProducerRecord<String, String>(topic, value);
        return producer.send(recordProducer);
    }
}
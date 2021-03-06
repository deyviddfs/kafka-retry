package br.com.deyvidfernandes.transactional.consumer;

import br.com.deyvidfernandes.transactional.business.object.TransactionalBO;
import br.com.deyvidfernandes.transactional.util.Constants;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

@Service
public class TransactionalRetryConsumer {

    @Value("${spring.kafka.consumer.poll.duration}")
    private long pollDuration;
    @Value("${spring.kafka.consumer.delay}")
    private long massageDelay;
    @Value("${spring.kafka.consumer.sleep}")
    private long processSleep;
    @Autowired
    private KafkaConsumer<String, String> consumer;
    @Autowired
    TransactionalBO transactionalBO;

    @Scheduled(fixedRate=10000)
    public void subscribe() {
        try {
            process(Arrays.asList(Constants.TOPIC_NAME_RETRY1, Constants.TOPIC_NAME_RETRY2, Constants.TOPIC_NAME_RETRY3));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void process(List<String> topicName) throws InterruptedException {
        consumer.subscribe(topicName);

        while(true) {

            if(!consumer.paused().isEmpty()){
                consumer.resume(consumer.paused());
            }

            var records = consumer.poll(Duration.ofMillis(pollDuration));

            if (!records.isEmpty()) {
                for (var record : records) {
                    LocalDateTime dateTimeProcess = LocalDateTime.now().minusMinutes(massageDelay);
                    LocalDateTime dateTimeMessage = LocalDateTime.ofInstant(Instant.ofEpochMilli(record.timestamp()), TimeZone.getDefault().toZoneId());
                    System.out.println("Process if " +dateTimeProcess +" >="+ dateTimeMessage);

                    var topicPartition = new TopicPartition(record.topic(), record.partition());

                    if(dateTimeProcess.isBefore(dateTimeMessage)) {
                        System.out.println("Wait " + processSleep + " millis");
                        consumer.pause(consumer.assignment());
                        consumer.seek(topicPartition, record.offset());
                        Thread.sleep(processSleep);
                    }else {
                        prepare(record);
                        processAndCommitOffset(record, topicPartition);
                    }
                }
            }
        }
    }

    private void prepare(ConsumerRecord<String, String> record) {
        switch (record.topic()){
            case Constants.TOPIC_NAME_RETRY1: {
                System.out.println("Try again, if fail, producer retry 2");
                transactionalBO.trySendIfFailProduce(record, Constants.TOPIC_NAME_RETRY2);
                break;
            }
            case Constants.TOPIC_NAME_RETRY2: {
                System.out.println("Try again, if fail, producer retry 3");
                transactionalBO.trySendIfFailProduce(record, Constants.TOPIC_NAME_RETRY3);
                break;
            }
            case Constants.TOPIC_NAME_RETRY3: {
                System.out.println("Try again, if fail, producer DLQ");
                transactionalBO.trySendIfFailProduce(record, Constants.TOPIC_NAME_DLQ);
                break;
            }
            default:{
                System.out.println("Topic Not Found");
            }
        }
    }

    protected void processAndCommitOffset(ConsumerRecord<String, String> record, TopicPartition topicPartition) {
        consumer.commitSync(Collections.singletonMap(topicPartition, new OffsetAndMetadata(record.offset() + 1)));
    }
}

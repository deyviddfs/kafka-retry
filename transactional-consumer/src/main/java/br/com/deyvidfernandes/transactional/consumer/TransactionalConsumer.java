package br.com.deyvidfernandes.transactional.consumer;

import br.com.deyvidfernandes.transactional.business.object.TransactionalBO;
import br.com.deyvidfernandes.transactional.util.Constants;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class TransactionalConsumer {
    @Autowired
    TransactionalBO transactionalBO;

    @KafkaListener(topics = Constants.TOPIC_NAME, groupId = "${spring.kafka.consumer.group-id}")
    private void process(ConsumerRecord<String, String> record){
        System.out.println("TransactionalConsumer");
        transactionalBO.trySendIfFailProduce(record, Constants.TOPIC_NAME_RETRY1);
    }
}

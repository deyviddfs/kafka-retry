package br.com.deyvidfernandes.transactional.business.object;

import br.com.deyvidfernandes.transactional.producer.TransactionalRetryDlqProducer;
import br.com.deyvidfernandes.transactional.util.LogUtil;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TransactionalBO {
    @Value("${gateway.url}")
    private String gatewayUrl;
    @Autowired
    TransactionalRetryDlqProducer transactionalRetryDlqProducer;

    public void trySendIfFailProduce(ConsumerRecord<String, String> record, String topic) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            LogUtil.showLogTopic(record);
            ResponseEntity<String> response = restTemplate.getForEntity(gatewayUrl, String.class);
            if(!response.getStatusCode().is2xxSuccessful()){
                System.out.println("Fail");
                producer(record, topic);
            }
            else{
                System.out.println("Success");
            }
        }catch (Exception e){
            producer(record, topic);
        }
    }

    private void producer(ConsumerRecord<String, String> record, String topic) {
        transactionalRetryDlqProducer.producer(topic, record.value());
    }
}

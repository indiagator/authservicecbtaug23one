package com.cbt.authservicecbtaug23one;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class Producer
{
    private static final Logger logger = LoggerFactory.getLogger(Producer.class);
    private static final String TOPIC = "analytics";
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String message)
    {
        logger.info(String.format("#### -> Producing message -> %s", message));
        this.kafkaTemplate.send(TOPIC, message);
    }

    public void sendAnalyticPayload(String type, String message, Object payload) throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();

        AnalyticsDatum datum = new AnalyticsDatum();
        datum.setType(type);
        datum.setMessage(message);
        String payloadJson =  mapper.writeValueAsString(payload);
        datum.setPayload(payloadJson);
        String outmessage =  mapper.writeValueAsString(datum);

        sendMessage(outmessage);
    }
}

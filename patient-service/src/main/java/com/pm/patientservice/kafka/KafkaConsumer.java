package com.pm.patientservice.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;

@Service
public class KafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = "patient", groupId = "patient-service-group")
    public void consume(ConsumerRecord<String, byte[]> record) {
        try {
            PatientEvent event = PatientEvent.parseFrom(record.value());

            log.info("Received Patient Event from Kafka: [id={}, name={}, email={}, type={}]",
                    event.getPatientId(),
                    event.getName(),
                    event.getEmail(),
                    event.getEventType()
            );


        } catch (Exception e) {
            log.error("Failed to parse PatientEvent from Kafka", e);
        }
    }
}

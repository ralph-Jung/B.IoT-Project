package ac.gachon.iot.mqtt;

import ac.gachon.iot.dto.MqttSensorPayload;
import ac.gachon.iot.exception.MqttMessageProcessingException;
import ac.gachon.iot.service.SensorDataIngestionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MqttMessageHandler {

    private final SensorDataIngestionService sensorDataIngestionService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void handle(Message<String> message) {
        String topic = message.getHeaders().get("mqtt_receivedTopic", String.class);
        String payload = message.getPayload();

        log.debug("MQTT message received. topic={}, payload={}", topic, payload);

        try {
            String identifier = extractIdentifier(topic);
            MqttSensorPayload sensorPayload = objectMapper.readValue(payload, MqttSensorPayload.class);
            sensorDataIngestionService.ingest(identifier, sensorPayload);
        } catch (Exception e) {
            log.error("Failed to process MQTT message. topic={}, payload={}", topic, payload, e);
        }
    }

    private String extractIdentifier(String topic) {
        String[] parts = topic.split("/");
        if (parts.length < 3) {
            throw new MqttMessageProcessingException("Invalid topic format: " + topic);
        }
        return parts[1];
    }
}

package ac.gachon.iot.exception;

public class MqttMessageProcessingException extends RuntimeException {
    public MqttMessageProcessingException(String message) {
        super(message);
    }
}

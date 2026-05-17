package ac.gachon.iot.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class TimeZoneProperties {

    @Value("${timezone.identifier}")
    private String timeZoneId;

}

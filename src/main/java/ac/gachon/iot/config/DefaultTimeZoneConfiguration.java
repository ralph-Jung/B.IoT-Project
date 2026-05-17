package ac.gachon.iot.config;

import ac.gachon.iot.properties.TimeZoneProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

@Configuration
@RequiredArgsConstructor
public class DefaultTimeZoneConfiguration {
    private final TimeZoneProperties timeZoneProperties;

    @PostConstruct
    public void defaultTimeZoneInit() {
        // defaultTimeZone에 대해 전달받은 TimeZoneId로 설정한다
        // 즉 , Asia/Seoul로 DefaultTimeZone을 설정한다면 , LocalDateTime.now() 할 때 한국 시간으로 출력된다.
        TimeZone.setDefault(TimeZone.getTimeZone(timeZoneProperties.getTimeZoneId()));
    }


}

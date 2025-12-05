package ma.aza.sgtm.logistics.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "synchronization")
public class SynchronizationProperties {
    private Cron cron = new Cron();

    @Data
    public static class Cron {
        private String devices = "0 0 1 * * *";
        private String dayReport = "0 5 0 * * *";
    }
}

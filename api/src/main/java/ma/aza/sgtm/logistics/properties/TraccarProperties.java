package ma.aza.sgtm.logistics.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "traccar")
public class TraccarProperties {
    private String baseUrl;
    private String email;
    private String password;
}

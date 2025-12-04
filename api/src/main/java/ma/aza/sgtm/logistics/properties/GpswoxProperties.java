package ma.aza.sgtm.logistics.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "gpswox")
public class GpswoxProperties {
    private String baseUrl;
    private String email;
    private String password;
    private String userApiHash;
}

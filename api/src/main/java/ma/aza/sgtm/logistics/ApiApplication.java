package ma.aza.sgtm.logistics;

import ma.aza.sgtm.logistics.config.RsaKeysConfig;
import ma.aza.sgtm.logistics.properties.GpsProviderProperties;
import ma.aza.sgtm.logistics.properties.SynchronizationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@Configuration
@EnableConfigurationProperties({RsaKeysConfig.class, GpsProviderProperties.class, SynchronizationProperties.class})
@EnableScheduling
public class ApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }

}

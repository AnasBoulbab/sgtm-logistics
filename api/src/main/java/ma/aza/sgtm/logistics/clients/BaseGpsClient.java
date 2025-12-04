package ma.aza.sgtm.logistics.clients;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ma.aza.sgtm.logistics.properties.GpsProviderProperties;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Getter
@RequiredArgsConstructor
public abstract class BaseGpsClient {

    private final RestTemplate restTemplate;
    private final GpsProviderProperties properties;

    protected UriComponentsBuilder baseUriBuilder(String relativePath) {
        return UriComponentsBuilder
                .fromHttpUrl(properties.getBaseUrl())
                .path(relativePath);
    }
}

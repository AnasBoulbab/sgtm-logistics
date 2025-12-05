package ma.aza.sgtm.logistics.clients;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ma.aza.sgtm.logistics.properties.GpsProviderProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

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

    protected GpsProviderProperties properties() {
        return properties;
    }

    protected abstract void authenticate(HttpHeaders headers, UriComponentsBuilder uriBuilder);

    public <T> ResponseEntity<T> exchange(
            HttpMethod method,
            String relativePath,
            Map<String, ?> queryParams,
            Object body,
            Class<T> responseType
    ) {
        UriComponentsBuilder builder = baseUriBuilder(relativePath);

        if (queryParams != null) {
            queryParams.forEach((k, v) -> {
                if (v != null) {
                    builder.queryParam(k, v);
                }
            });
        }

        HttpHeaders headers = new HttpHeaders();
        if (method == HttpMethod.POST || method == HttpMethod.PUT || method == HttpMethod.PATCH) {
            headers.setContentType(MediaType.APPLICATION_JSON);
        }

        authenticate(headers, builder);

        URI uri = builder.build(true).toUri();
        HttpEntity<Object> entity = new HttpEntity<>(body, headers);

        try {
            return restTemplate.exchange(uri, method, entity, responseType);
        } catch (HttpStatusCodeException ex) {
            return ResponseEntity
                    .status(ex.getStatusCode())
                    .headers(ex.getResponseHeaders() != null ? ex.getResponseHeaders() : new HttpHeaders())
                    .body(null);
        }
    }
}

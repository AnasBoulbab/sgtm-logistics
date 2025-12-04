package ma.aza.sgtm.logistics.clients;

import ma.aza.sgtm.logistics.properties.GpsProviderProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@Service
@ConditionalOnProperty(prefix = "gps-provider", name = "name", havingValue = "traccar")
public class TraccarClient extends BaseGpsClient {

    public TraccarClient(RestTemplate restTemplate, GpsProviderProperties traccarProperties) {
        super(restTemplate, traccarProperties);
    }

    private String authenticateAndGetSessionCookie() {
        URI uri = UriComponentsBuilder
                .fromHttpUrl(properties().getBaseUrl())
                .path("/session")
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("email", properties().getEmail());
        form.add("password", properties().getPassword());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(form, headers);

        ResponseEntity<String> response = getRestTemplate().postForEntity(uri, request, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Login failed, status: " + response.getStatusCode());
        }

        String cookie = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        if (cookie == null) {
            throw new RuntimeException("Login failed — no session cookie returned");
        }

        // Keep only the session part
        return cookie.split(";", 2)[0];
    }

    public <T> ResponseEntity<T> exchange(
            HttpMethod method,
            String relativePath,           // e.g. "/devices"
            Object body,
            Class<T> responseType,
            Map<String, ?> queryParams     // NEW
    ) {
        String sessionCookie = authenticateAndGetSessionCookie();

        UriComponentsBuilder builder = baseUriBuilder(relativePath);

        // Add query parameters if provided
        if (queryParams != null) {
            queryParams.forEach(builder::queryParam);
        }

        URI uri = builder.build().toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.COOKIE, sessionCookie);

        // Set content type for methods with request body
        if (method == HttpMethod.POST || method == HttpMethod.PUT || method == HttpMethod.PATCH) {
            headers.setContentType(MediaType.APPLICATION_JSON);
        }

        HttpEntity<Object> entity = new HttpEntity<>(body, headers);

        try {
            return getRestTemplate().exchange(uri, method, entity, responseType);
        } catch (HttpStatusCodeException ex) {
            return ResponseEntity
                    .status(ex.getStatusCode())
                    .headers(ex.getResponseHeaders() != null ? ex.getResponseHeaders() : new HttpHeaders())
                    .body(null);
        }
    }

}

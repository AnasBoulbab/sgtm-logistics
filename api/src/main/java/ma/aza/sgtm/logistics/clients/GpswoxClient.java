package ma.aza.sgtm.logistics.clients;

import ma.aza.sgtm.logistics.properties.GpswoxProperties;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class GpswoxClient {

    private final RestTemplate restTemplate;
    private final GpswoxProperties properties;

    /**
     * This is the user_api_hash that MUST be sent as query param
     * on every request.
     */
    private volatile String userApiHash;

    // You can keep the login endpoint here or move to properties
    private static final String LOGIN_PATH = "/login"; // adjust to the real path

    public GpswoxClient(RestTemplate restTemplate, GpswoxProperties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

    // ---------------- AUTH ----------------

    private synchronized void ensureUserApiHash() {
        if (userApiHash != null) {
            return; // already resolved
        }

        // 1) Prefer value from configuration (copied from UI)
        if (properties.getUserApiHash() != null && !properties.getUserApiHash().isBlank()) {
            this.userApiHash = properties.getUserApiHash();
            return;
        }

        // 2) Otherwise, login via API to get user_api_hash in response body
        if (properties.getEmail() == null || properties.getPassword() == null) {
            throw new IllegalStateException("GPSWOX user_api_hash not configured and no login credentials provided");
        }

        String loginUrl = UriComponentsBuilder
                .fromHttpUrl(properties.getBaseUrl())
                .path(LOGIN_PATH)
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // If docs say form-url-encoded, change this to MultiValueMap<String,String>
        Map<String, String> loginBody = Map.of(
                "email", properties.getEmail(),
                "password", properties.getPassword()
        );

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(loginBody, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                loginUrl,
                HttpMethod.POST,
                entity,
                Map.class
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new IllegalStateException("GPSWOX login failed: " + response.getStatusCode());
        }

        Map body = response.getBody();
        if (body == null || !body.containsKey("user_api_hash")) {
            throw new IllegalStateException("GPSWOX login did not return user_api_hash");
        }

        this.userApiHash = String.valueOf(body.get("user_api_hash"));
    }

    // ---------------- GENERIC EXCHANGE ----------------

    public <T> ResponseEntity<T> exchange(
            HttpMethod method,
            String relativePath,
            Map<String, ?> queryParams,
            Object body,
            Class<T> responseType
    ) {
        ensureUserApiHash();

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(properties.getBaseUrl())
                .path(relativePath);

        // Add user_api_hash to ALL requests
        builder.queryParam("user_api_hash", userApiHash);

        // Add other query params
        if (queryParams != null) {
            queryParams.forEach((k, v) -> {
                if (v != null && !"user_api_hash".equals(k)) { // avoid duplicates
                    builder.queryParam(k, v);
                }
            });
        }

        URI uri = builder.build(true).toUri(); // true -> encode

        HttpHeaders headers = new HttpHeaders();
        if (method == HttpMethod.POST || method == HttpMethod.PUT || method == HttpMethod.PATCH) {
            headers.setContentType(MediaType.APPLICATION_JSON);
        }

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

    // ---------------- CONVENIENCE METHODS ----------------

    /**
     * GET devices.
     * Adjust the path ("/devices") to the one from:
     *   https://gpswox.stoplight.io/docs/tracking-software/39odi2xl27a8w-list-get-devices
     */
    public <T> ResponseEntity<T> getDevices(Class<T> responseType) {
        return exchange(
                HttpMethod.GET,
                "/get_devices",    // e.g. "/devices" or "/objects" – check docs
                null,
                null,
                responseType
        );
    }

    public <T> ResponseEntity<T> getHistory(
            long deviceId,
            LocalDateTime from,
            LocalDateTime to,
            Class<T> responseType
    ) {

        // Extract date and time separately in the required formats
        String fromDate = from.toLocalDate().toString(); // yyyy-MM-dd
        String fromTime = from.toLocalTime().toString(); // HH:mm:ss or HH:mm

        String toDate = to.toLocalDate().toString();
        String toTime = to.toLocalTime().toString();

        Map<String, Object> params = new HashMap<>();
        params.put("device_id", deviceId);
        params.put("from_date", fromDate);
        params.put("from_time", fromTime);
        params.put("to_date", toDate);
        params.put("to_time", toTime);

        return exchange(
                HttpMethod.GET,
                "/get_history",
                params,      // GET request → body is null
                null,    // your exchange method should turn this into query parameters
                responseType
        );
    }

}

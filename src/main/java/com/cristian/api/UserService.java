package com.cristian.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final ConcurrentHashMap<String, User> cache = new ConcurrentHashMap<>();
    private final String baseUrl;

    public UserService(String baseUrl) {
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length()-1) : baseUrl;
        this.objectMapper = new ObjectMapper();
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
    }

    public Optional<User> getUser(String id) {
        try {
            if (cache.containsKey(id)) {
                log.debug("Cache hit for {}", id);
                return Optional.of(cache.get(id));
            }

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/users/" + id))
                    .timeout(Duration.ofSeconds(5))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                log.warn("Unexpected status fetching user {}: {}", id, response.statusCode());
                return Optional.empty();
            }

            User user = objectMapper.readValue(response.body(), User.class);
            cache.put(id, user);
            return Optional.of(user);

        } catch (Exception e) {
            log.error("Error fetching user {}", id, e);
            return Optional.empty();
        }
    }

    public void invalidateCache(String id) {
        cache.remove(id);
    }
}

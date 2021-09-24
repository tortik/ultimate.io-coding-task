package com.ultimate.webservertask.http;

import com.ultimate.webservertask.core.model.Intent;
import com.ultimate.webservertask.core.service.IntentService;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class IntentHttpService implements IntentService {

  private String intentServiceUrl;
  private HttpHeaders headers;
  private RestTemplate restTemplate;

  @Autowired
  public IntentHttpService(@Value("${application.intent.url}") String intentServiceUrl,
                           @Value("${application.intent.apiKey}") String apiKey,
                           RestTemplate restTemplate) {
    this.intentServiceUrl = intentServiceUrl;
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.add("authorization", apiKey);
    this.headers = headers;
    this.restTemplate = restTemplate;
  }

  @Override
  public List<Intent> intents(String botId, String message) {
    Map<String, String> body = Map.of("botId", botId, "message", message);
    log.info("Calling intent service. Url: {}", intentServiceUrl);
    log.debug("Calling intent service with body={}", body);

    ResponseEntity<IntentResponse> response = restTemplate.exchange(intentServiceUrl,
        HttpMethod.POST, new HttpEntity<>(body, headers), IntentResponse.class);

    log.info("Successful response received");
    log.debug("Service response is {}", response.getBody());
    return Optional.ofNullable(response.getBody()).map(IntentResponse::getIntents).orElse(Collections.emptyList());
  }

  @Getter
  @Setter
  @ToString
  public static class IntentResponse {
    private List<Intent> intents = new ArrayList<>();
  }

}

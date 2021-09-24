package com.ultimate.webservertask.http;

import com.ultimate.webservertask.core.model.Intent;
import com.ultimate.webservertask.core.service.IntentService;
import lombok.Getter;
import lombok.Setter;
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

@Service
public class IntentHttpService implements IntentService {

  private String intentServiceUrl;
  private HttpHeaders headers;
  private RestTemplate restTemplate;

  @Autowired
  public IntentHttpService(@Value("${service.intent.url}") String intentServiceUrl,
                           @Value("${service.intent.apiKey}") String apiKey,
                           RestTemplate restTemplate) {
    this.intentServiceUrl = intentServiceUrl;
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.add("authentication", apiKey);
    this.headers = headers;
    this.restTemplate = restTemplate;
  }

  @Override
  public List<Intent> intents(String botId, String message) {
    Map<String, String> body = Map.of("botId", botId, "message", message);

    ResponseEntity<IntentResponse> response = restTemplate.exchange(intentServiceUrl,
        HttpMethod.POST, new HttpEntity<>(body, headers), IntentResponse.class);

    return response.getBody().getIntents();
  }

  @Getter
  @Setter
  public class IntentResponse {
    private List<Intent> intents = new ArrayList<>();
  }

}

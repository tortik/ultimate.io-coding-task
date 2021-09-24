package com.ultimate.webservertask.http;

import org.apache.commons.codec.Charsets;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;

@Configuration
public class HttpConfig {

  @Bean
  public CloseableHttpClient httpClient() {
    return HttpClients.createDefault();
  }

  @Bean
  public HttpComponentsClientHttpRequestFactory clientHttpRequestFactory(CloseableHttpClient httpClient) {
    HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
    clientHttpRequestFactory.setHttpClient(httpClient);
    return clientHttpRequestFactory;
  }

  @Bean
  public RestTemplate restTemplate(HttpComponentsClientHttpRequestFactory clientHttpRequestFactory,
                                   RestTemplateResponseErrorHandler restTemplateResponseErrorHandler) {
    return new RestTemplateBuilder()
        .requestFactory(() -> clientHttpRequestFactory)
        //.errorHandler(restTemplateResponseErrorHandler)
        .build();
  }

  @Component
  public class RestTemplateResponseErrorHandler
      implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse httpResponse)
        throws IOException {
      return (httpResponse.getStatusCode().series() == CLIENT_ERROR
              || httpResponse.getStatusCode().series() == SERVER_ERROR);
    }

    @Override
    public void handleError(ClientHttpResponse httpResponse)
        throws IOException {
      String body = new String(httpResponse.getBody().readAllBytes(), StandardCharsets.UTF_8);
      if (httpResponse.getStatusCode().series() == SERVER_ERROR) {
        throw new HttpServerErrorException(httpResponse.getStatusCode(), httpResponse.getStatusText(), body.getBytes(), StandardCharsets.UTF_8);
      } else if (httpResponse.getStatusCode().series() == CLIENT_ERROR) {
        throw new HttpClientErrorException(httpResponse.getStatusCode(), httpResponse.getStatusText(), body.getBytes(), StandardCharsets.UTF_8);
      }
    }
  }

}

package com.ultimate.webservertask.core.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Intent {
  private final String name;
  private final double confidence;

  @JsonCreator
  public Intent(@JsonProperty("name") String name, @JsonProperty("confidence") double confidence) {
    this.name = name;
    this.confidence = confidence;
  }
}

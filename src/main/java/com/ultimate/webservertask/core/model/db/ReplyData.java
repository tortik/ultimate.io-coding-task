package com.ultimate.webservertask.core.model.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@AllArgsConstructor
public class ReplyData {
  @Indexed(unique = true)
  private String intent;
  private String reply;
  private double threshold;
}

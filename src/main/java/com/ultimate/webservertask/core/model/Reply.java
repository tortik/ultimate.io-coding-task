package com.ultimate.webservertask.core.model;

import lombok.Value;

@Value
public class Reply {
  public static final Reply DEFAULT = new Reply("AI could not give the correct answer");

  String reply;
}

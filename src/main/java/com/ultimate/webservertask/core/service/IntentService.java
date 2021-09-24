package com.ultimate.webservertask.core.service;

import com.ultimate.webservertask.core.model.Intent;

import java.util.List;

public interface IntentService {

  List<Intent> intents(String botId, String message);
}

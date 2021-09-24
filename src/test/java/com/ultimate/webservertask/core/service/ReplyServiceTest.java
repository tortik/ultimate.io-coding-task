package com.ultimate.webservertask.core.service;

import com.ultimate.webservertask.core.dao.ReplyDataDao;
import com.ultimate.webservertask.core.model.Intent;
import com.ultimate.webservertask.core.model.Reply;
import com.ultimate.webservertask.core.model.db.ReplyData;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class ReplyServiceTest {

  private IntentService intentService = Mockito.mock(IntentService.class);
  private ReplyDataDao replyDataDao = Mockito.mock(ReplyDataDao.class);
  private ReplyService replyService = new ReplyService(intentService, replyDataDao);

  @Test
  void shouldReplyWithGreetings() {
    when(replyDataDao.findByIntentAndThresholdLessThanEqual("greetings", 0.98))
        .thenReturn(Optional.of(new ReplyData("greetings", "Hello", 0.1)));

    when(intentService.intents("test", "Hi")).thenReturn(Arrays.asList(new Intent("Greetings", 0.98)));

    Reply reply = replyService.reply("test", "Hi");

    assertThat(reply.getReply()).isEqualTo("Hello");
  }

  @Test
  void shouldSortAndReplyWithHigherConfidence() {
    when(replyDataDao.findByIntentAndThresholdLessThanEqual("greetings", 0.99))
        .thenReturn(Optional.of(new ReplyData("greetings", "Hello", 0.1)));
    when(replyDataDao.findByIntentAndThresholdLessThanEqual("thank you", 0.2))
        .thenReturn(Optional.of(new ReplyData("thank you", "Thank you too!", 0.1)));

    when(intentService.intents("test", "Hi")).thenReturn(Arrays.asList(
        new Intent("Thank you", 0.2),
        new Intent("Greetings", 0.99)));

    Reply reply = replyService.reply("test", "Hi");

    assertThat(reply.getReply()).isEqualTo("Hello");
  }

  @Test
  void shouldReplyWithHigherConfidenceAndPassReplyThreshold() {
    when(replyDataDao.findByIntentAndThresholdLessThanEqual("thank you", 0.2))
        .thenReturn(Optional.of(new ReplyData("thank you", "Thank you too!", 0.1)));

    when(intentService.intents("test", "Hi")).thenReturn(Arrays.asList(
        new Intent("Thank you", 0.2),
        new Intent("Greetings", 0.9)));

    Reply reply = replyService.reply("test", "Hi");

    assertThat(reply.getReply()).isEqualTo("Thank you too!");
  }

  @Test
  void shouldReplyWithDefaultReply() {
    when(replyDataDao.findByIntentAndThresholdLessThanEqual("thank you", 0.2))
        .thenReturn(Optional.empty());

    when(intentService.intents("test", "Hi")).thenReturn(Arrays.asList(
        new Intent("Thank you", 0.2),
        new Intent("Greetings", 0.9)));

    Reply reply = replyService.reply("test", "Hi");

    assertThat(reply.getReply()).isEqualTo(Reply.DEFAULT.getReply());
  }

  @Test
  void shouldTrimIntentName() {
    when(replyDataDao.findByIntentAndThresholdLessThanEqual("greetings", 0.99))
        .thenReturn(Optional.of(new ReplyData("greetings", "Hello", 0.1)));

    when(intentService.intents("test", "Hi")).thenReturn(Arrays.asList(
        new Intent("Greetings    ", 0.99)));

    Reply reply = replyService.reply("test", "Hi");

    assertThat(reply.getReply()).isEqualTo("Hello");
  }

}
package com.ultimate.webservertask.core.service;

import com.ultimate.webservertask.core.dao.ReplyDataDao;
import com.ultimate.webservertask.core.model.Intent;
import com.ultimate.webservertask.core.model.Reply;
import com.ultimate.webservertask.core.model.db.ReplyData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ReplyService {
  public static final Comparator<Intent> CONFIDENCE_COMPARATOR = Comparator.comparingDouble(Intent::getConfidence).reversed();

  private final IntentService intentService;
  private final ReplyDataDao replyDataDao;

  public Reply reply(String botId, String message) {
    List<Intent> intents = intentService.intents(botId, message);

    Optional<ReplyData> replyData = intents.stream().sorted(CONFIDENCE_COMPARATOR)
        .map(i -> replyDataDao.findByIntentAndThresholdLessThanEqual(i.getName().toLowerCase().trim(), i.getConfidence()))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .findFirst();

    log.debug("ReplyData filtered with Intent={} and Reply={}", replyData.map(ReplyData::getIntent).orElse(""),
        replyData.map(ReplyData::getReply).orElse(""));
    Reply reply = replyData.map(rd -> new Reply(rd.getReply())).orElse(Reply.DEFAULT);
    log.info("Reply returned = {}", reply.getReply());
    return reply;
  }

}

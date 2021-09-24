package com.ultimate.webservertask.http.rest;

import com.ultimate.webservertask.core.model.Reply;
import com.ultimate.webservertask.core.service.ReplyService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ReplyRest {

  private final ReplyService replyService;

  @PostMapping("/api/reply")
  public Reply reply(@Valid @RequestBody ReplyRequest replyRequest) {
    log.debug("Receive request with botId={} and message={}", replyRequest.getBotId(), replyRequest.getMessage());
    return replyService.reply(replyRequest.botId, replyRequest.message);
  }

  @Data
  private static class ReplyRequest {

    @Pattern(regexp="^[a-f\\d]{24}$", message="the botId should be valid MongoObjectId")
    private String botId;
    @NotBlank
    private String message;
  }

}

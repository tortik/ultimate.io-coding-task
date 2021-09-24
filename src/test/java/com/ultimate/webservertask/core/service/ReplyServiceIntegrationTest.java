package com.ultimate.webservertask.core.service;

import com.ultimate.webservertask.core.model.Reply;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.JsonBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.Files.readAllBytes;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockserver.model.HttpRequest.request;

@Testcontainers
@SpringBootTest
@Tag("integration-test")
public class ReplyServiceIntegrationTest {

  private static final Network network = Network.newNetwork();
  private static MockServerClient mockServerClient;

  @Autowired
  private ReplyService replyService;

  @Container
  static GenericContainer mongoDBContainer = new GenericContainer<>(DockerImageName.parse("mongo")).withNetwork(network)
      .withExposedPorts(27017)
      .withCopyFileToContainer(MountableFile.forHostPath(Path.of("mongo-seed/initdb.js")), "/docker-entrypoint-initdb.d/initdb.js")
      .waitingFor(Wait.forLogMessage("(?i).*waiting for connections.*", 1));

  @Container
  private static final MockServerContainer ultimateAI = makeMockServerContainer();

  @DynamicPropertySource
  static void setProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.data.mongodb.uri", () -> String.format(
        "mongodb://%s:%d/test", mongoDBContainer.getContainerIpAddress(), mongoDBContainer.getMappedPort(27017)));
    registry.add("application.intent.url", () -> String.format("http://%s:%d/api/intents", ultimateAI.getHost(), ultimateAI.getServerPort()));
  }

  @BeforeAll
  public static void setup() {
    mockServerClient =
        new MockServerClient(
            ultimateAI.getHost(),
            ultimateAI.getServerPort());
  }

  private static MockServerContainer makeMockServerContainer() {
    return new MockServerContainer(
        DockerImageName.parse("jamesdbloom/mockserver:mockserver-5.11.2"))
        .withNetwork(network)
        .withNetworkAliases("ultimate-mock");
  }

  @Test
  void shouldReturnGoodBye() throws IOException {
    Path eventFolder = Paths.get("src/test/resources/test_data");
    String goodByeIntent = new String(readAllBytes(eventFolder.resolve("goodbye_intent_response.json")));
    mockServerClient
        .when(request("/api/intents"))
        .respond(org.mockserver.model.HttpResponse.response().withStatusCode(200)
            .withBody(JsonBody.json(goodByeIntent)));

    Reply reply = replyService.reply("5f74865056d7bb000fcd39ff", "Hello");

    assertThat(reply.getReply()).isEqualTo("Thank you for a conversation. Goodbye.");
  }
}

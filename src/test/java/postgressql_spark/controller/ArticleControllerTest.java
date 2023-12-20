package postgressql_spark.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.flywaydb.core.Flyway;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.spark.controller.response.ArticleCreateResponse;
import org.spark.controller.response.CommentAddResponse;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import postgressql_spark.Application;
import postgressql_spark.controller.responses.FindArticleResponse;
import postgressql_spark.repository.SqlArticleRepository;
import postgressql_spark.repository.SqlCommentRepository;
import postgressql_spark.service.ArticleService;
import postgressql_spark.service.CommentService;
import spark.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class ArticleControllerTest {
  private Service service;
  private static Jdbi jdbi;

  @Container
  public static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:13");

  @BeforeEach
  void beforeEach() {
    service = Service.ignite();
  }

  @BeforeAll
  static void beforeAll() {
    String postgresJdbcUrl = POSTGRES.getJdbcUrl();
    Flyway flyway = Flyway.configure().outOfOrder(true).locations("classpath:db/migrations").dataSource(postgresJdbcUrl, POSTGRES.getUsername(), POSTGRES.getPassword()).load();
    flyway.migrate();
    jdbi = Jdbi.create(postgresJdbcUrl, POSTGRES.getUsername(), POSTGRES.getPassword());
  }

  @BeforeEach
  void afterEach() {
    service.stop();
    service.awaitStop();
  }

  @Test
  void shouldUpdateArticle() throws Exception {

    ArticleService articleService = new ArticleService(new SqlArticleRepository(jdbi));
    CommentService commentService = new CommentService(new SqlCommentRepository(jdbi), articleService);
    ObjectMapper objectMapper = new ObjectMapper();
    Application application = new Application(
            List.of(new ArticleController(
                    service, articleService, objectMapper
            ), new CommentController(
                    service, objectMapper, commentService
            ))
    );

    application.start();
    service.awaitInitialization();

    // create article
    HttpResponse<String> responseArticleCreate = HttpClient.newHttpClient()
            .send(
                    HttpRequest.newBuilder()
                            .POST(
                                    HttpRequest.BodyPublishers.ofString(
                                            """
                                                        {
                                                          "name": "days",
                                                          "tags": [
                                                            "monday",
                                                            "sunday"
                                                          ]
                                                        }
                                                    """
                                    )
                            )
                            .uri(URI.create("http://localhost:%d/api/articles".formatted(service.port())))
                            .build(),
                    HttpResponse.BodyHandlers.ofString(UTF_8)
            );

    assertEquals(201, responseArticleCreate.statusCode());
    ArticleCreateResponse createArticleResponse =
            objectMapper.readValue(responseArticleCreate.body(), ArticleCreateResponse.class);
    assertEquals(1L, createArticleResponse.articleId());


    // add a comment
    HttpResponse<String> responseAddComment = HttpClient.newHttpClient()
            .send(
                    HttpRequest.newBuilder()
                            .POST(
                                    HttpRequest.BodyPublishers.ofString(
                                            """
                                                      {
                                                        "text": "Test"
                                                      }
                                                    """
                                    )
                            )
                            .uri(URI.create("http://localhost:%d/api/articles/1/comments".formatted(service.port())))
                            .build(),
                    HttpResponse.BodyHandlers.ofString(UTF_8)
            );

    assertEquals(201, responseAddComment.statusCode());
    CommentAddResponse commentAddResponse =
            objectMapper.readValue(responseAddComment.body(), CommentAddResponse.class);
    // update article
    HttpResponse<String> responseArticleUpdate = HttpClient.newHttpClient()
            .send(
                    HttpRequest.newBuilder()
                            .PUT(
                                    HttpRequest.BodyPublishers.ofString(
                                            """
                                                        {
                                                          "name": "test",
                                                          "tags": [
                                                            "day",
                                                            "weekend"
                                                          ]
                                                        }
                                                    """
                                    )
                            )
                            .uri(URI.create("http://localhost:%d/api/articles/update/1".formatted(service.port())))
                            .build(),
                    HttpResponse.BodyHandlers.ofString(UTF_8)
            );

    assertEquals(201, responseArticleUpdate.statusCode());
    assertEquals("{}", responseArticleUpdate.body());

    // delete comment
    HttpResponse<String> responseCommentDelete = HttpClient.newHttpClient()
            .send(
                    HttpRequest.newBuilder()
                            .DELETE()
                            .uri(URI.create("http://localhost:%d/api/comment/1".formatted(service.port())))
                            .build(),
                    HttpResponse.BodyHandlers.ofString(UTF_8)
            );

    assertEquals(201, responseCommentDelete.statusCode());

    // get article by id
    HttpResponse<String> responseFindArticle = HttpClient.newHttpClient()
            .send(
                    HttpRequest.newBuilder()
                            .GET()
                            .uri(URI.create("http://localhost:%d/api/articles/%d".formatted(service.port(), 1L)))
                            .build(),
                    HttpResponse.BodyHandlers.ofString(UTF_8)
            );

    assertEquals(201, responseFindArticle.statusCode());
    FindArticleResponse findArticleResponse =
            objectMapper.readValue(responseFindArticle.body(), FindArticleResponse.class);
    assertEquals(1L, findArticleResponse.id());
    assertTrue(findArticleResponse.tags().contains("weekend"));
    assertEquals(0, findArticleResponse.commentsCount());

    // delete article
    HttpResponse<String> responseToDeleteArticle = HttpClient.newHttpClient()
            .send(
                    HttpRequest.newBuilder()
                            .DELETE()
                            .uri(URI.create("http://localhost:%d/api/articles/%d".formatted(service.port(), 1L)))
                            .build(),
                    HttpResponse.BodyHandlers.ofString(UTF_8)
            );

    assertEquals(201, responseToDeleteArticle.statusCode());
    assertEquals("{}", responseToDeleteArticle.body());

    HttpResponse<String> responseFindInvalidArticleId = HttpClient.newHttpClient()
            .send(
                    HttpRequest.newBuilder()
                            .GET()
                            .uri(URI.create("http://localhost:%d/api/articles/%d".formatted(service.port(), 1L)))
                            .build(),
                    HttpResponse.BodyHandlers.ofString(UTF_8)
            );

    assertEquals(400, responseFindInvalidArticleId.statusCode());
  }

}
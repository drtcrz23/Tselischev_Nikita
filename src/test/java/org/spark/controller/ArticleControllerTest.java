package org.spark.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.spark.Application;
import org.spark.controller.response.ArticleCreateResponse;
import org.spark.controller.response.CommentAddResponse;
import org.spark.repository.InMemoryArticleRepository;
import org.spark.service.ArticleService;
import spark.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ArticleControllerTest {

  private Service service;

  @BeforeEach
  void befofeEach() {
    service = Service.ignite();
  }

  @AfterEach
  void afterEach() {
    service.stop();
    service.awaitStop();
  }

  @Test
  void should201IfAllIsSuccessfully() throws Exception {
    ArticleService articleService = new ArticleService(new InMemoryArticleRepository());
    ObjectMapper objectMapper = new ObjectMapper();
    Application application = new Application(
            List.of(new ArticleController(
                    service, articleService, objectMapper
            ))
    );

    application.start();
    service.awaitInitialization();

    HttpResponse<String> responseArticleAdd = HttpClient.newHttpClient()
            .send(
                    HttpRequest.newBuilder()
                            .POST(
                                    BodyPublishers.ofString(
                                            """
                                                    { "name": "weekend", "tags": ["saturday", "sunday"] }"""
                                    )
                            )
                            .uri(URI.create("http://localhost:%d/api/articles".formatted(service.port())))
                            .build(),
                    BodyHandlers.ofString(UTF_8)
            );

    assertEquals(201, responseArticleAdd.statusCode());
    ArticleCreateResponse articleCreateResponse =
            objectMapper.readValue(responseArticleAdd.body(), ArticleCreateResponse.class);

    HttpResponse<String> responseUpdate = HttpClient.newHttpClient()
            .send(
                    HttpRequest.newBuilder()
                            .PUT(
                                    BodyPublishers.ofString(
                                            """
                                                    { "name": "monday", "tags": ["morning", "evening"] }"""
                                    )
                            )
                            .uri(URI.create("http://localhost:%d/api/articles/update/%d".formatted(service.port(), articleCreateResponse.articleId())))
                            .build(),
                    BodyHandlers.ofString(UTF_8)
            );

    assertEquals(201, responseUpdate.statusCode());
    assertEquals("{}", responseUpdate.body());

    HttpResponse<String> responseCommentAdd = HttpClient.newHttpClient()
            .send(
                    HttpRequest.newBuilder()
                            .POST(
                                    HttpRequest.BodyPublishers.ofString(
                                            """
                                                        {
                                                          "text":"TEST"
                                                        }
                                                    """
                                    )
                            )
                            .uri(URI.create("http://localhost:%d/api/articles/%d/comments".formatted(service.port(), articleCreateResponse.articleId())))
                            .build(),
                    HttpResponse.BodyHandlers.ofString(UTF_8)
            );

    assertEquals(201, responseCommentAdd.statusCode());
    CommentAddResponse commentAddResponse =
            objectMapper.readValue(responseCommentAdd.body(), CommentAddResponse.class);

    HttpResponse<String> responseCommentDelete = HttpClient.newHttpClient()
            .send(
                    HttpRequest.newBuilder()
                            .DELETE()
                            .uri(URI.create("http://localhost:%d/api/articles/%d/comments/%d".formatted(service.port(), articleCreateResponse.articleId(), commentAddResponse.commentId())))
                            .build(),
                    HttpResponse.BodyHandlers.ofString(UTF_8)
            );

    assertEquals(201, responseCommentDelete.statusCode());
    assertEquals("{}", responseCommentDelete.body());

    HttpResponse<String> responseArticles = HttpClient.newHttpClient().send(
            HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create("http://localhost:%d/api/articles".formatted(service.port())))
                    .build(),
            HttpResponse.BodyHandlers.ofString(UTF_8)
    );

    assertEquals(201, responseArticles.statusCode());

    HttpResponse<String> responseDelete = HttpClient.newHttpClient()
            .send(
                    HttpRequest.newBuilder()
                            .DELETE()
                            .uri(URI.create("http://localhost:%d/api/articles/%d".formatted(service.port(), articleCreateResponse.articleId())))
                            .build(),
                    HttpResponse.BodyHandlers.ofString(UTF_8)
            );

    assertEquals(201, responseDelete.statusCode());
    assertEquals("{}", responseDelete.body());
  }
}

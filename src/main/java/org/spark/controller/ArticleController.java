package org.spark.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spark.controller.request.ArticleCreateRequest;
import org.spark.controller.request.ArticleUpdateRequest;
import org.spark.controller.request.CommentAddRequest;
import org.spark.controller.response.*;
import org.spark.entity.article.ArticleId;
import org.spark.entity.comment.CommentId;
import org.spark.repository.exception.ArticleNotFoundException;
import org.spark.service.ArticleService;
import org.spark.service.exception.ArticleCreateException;
import org.spark.service.exception.ArticleDeleteException;
import org.spark.service.exception.ArticleUpdateException;
import org.spark.service.exception.CommentDeleteException;
import spark.Request;
import spark.Response;
import spark.Service;

public class ArticleController implements Controller {
  private static final Logger LOG = LoggerFactory.getLogger(ArticleController.class);

  private final Service service;
  private final ArticleService articleService;
  private final ObjectMapper objectMapper;

  public ArticleController(Service service, ArticleService articleService, ObjectMapper objectMapper) {
    this.service = service;
    this.articleService = articleService;
    this.objectMapper = objectMapper;
  }

  @Override
  public void initializeEndpoints() {
    createArticle();
    updateArticle();
    deleteArticle();
    addComment();
    deleteComment();
    getAllArticles();
    getArticlesById();
  }

  private void getArticlesById() {

  }

  private void getAllArticles() {
    service.get("/api/articles", (Request request, Response response) -> {
      response.type("application/json");
      LOG.debug("Articles received");
      response.status(201);
      return objectMapper.writeValueAsString(articleService.findAll());
    });
  }

  private void createArticle() {
    service.post(
            "/api/articles",
            (Request request, Response response) -> {
              response.type("application/json");
              String body = request.body();
              ArticleCreateRequest articleCreateRequest = objectMapper.readValue(body,
                      ArticleCreateRequest.class);
              try {
                var articleId = articleService.create(articleCreateRequest.name(), articleCreateRequest.tags());
                response.status(201);
                LOG.debug("Article created");
                return objectMapper.writeValueAsString(new ArticleCreateResponse(articleId.getId()));
              } catch (ArticleCreateException e) {
                LOG.warn("Cannot create article", e);
                response.status(400);
                return objectMapper.writeValueAsString(new ErrorResponse(e.getMessage()));
              } catch (RuntimeException e) {
                LOG.error("unexpected error", e);
                response.status(500);
                return objectMapper.writeValueAsString(new ErrorResponse("Server error"));
              }
            }
    );
  }

  private void updateArticle() {
    service.put(
            "api/articles/update/:articleId",
            (Request request, Response response) -> {
              response.type("application/json");
              String body = request.body();
              String articleId = request.params(":articleId");
              ArticleUpdateRequest articleUpdateRequest = objectMapper.readValue(body,
                      ArticleUpdateRequest.class);
              try {
                articleService.update(Long.parseLong(articleId), articleUpdateRequest.name(), articleUpdateRequest.tags());
                response.status(201);
                LOG.debug("Article updated");
                return objectMapper.writeValueAsString(new ArticleUpdateResponse());
              } catch (ArticleUpdateException e) {
                LOG.warn("Cannot update article", e);
                response.status(400);
                return objectMapper.writeValueAsString(new ErrorResponse(e.getMessage()));
              } catch (RuntimeException e) {
                LOG.error("unexpected error", e);
                response.status(500);
                return objectMapper.writeValueAsString(new ErrorResponse("Server error"));
              }
            }
    );
  }

  private void deleteArticle() {
    service.delete(
            "api/articles/:articleId",
            (Request request, Response response) -> {
              response.type("application/json");
              String id = request.params(":articleId");

              try {
                ArticleId articleId = new ArticleId(Long.parseLong(id));
                articleService.delete(articleId);
                response.status(201);
                LOG.debug("Article delete");
                return objectMapper.writeValueAsString(new ArticleDeleteResponse());
              } catch (ArticleDeleteException e) {
                LOG.warn("Cannot delete article", e);
                response.status(400);
                return objectMapper.writeValueAsString(new ErrorResponse(e.getMessage()));
              } catch (RuntimeException e) {
                LOG.error("unexpected error", e);
                response.status(500);
                return objectMapper.writeValueAsString(new ErrorResponse("Server error"));
              }
            }
    );
  }

  private void addComment() {
    service.post(
            "api/articles/:articleId/comments",
            (Request request, Response response) -> {
              response.type("application/json");
              String id = request.params(":articleId");
              String body = request.body();
              CommentAddRequest commentAddRequest = objectMapper.readValue(body, CommentAddRequest.class);

              try {
                CommentId commentId = articleService.addNewComment(Long.parseLong(id), commentAddRequest.text());
                response.status(201);
                LOG.debug("Comment add");
                return objectMapper.writeValueAsString(new CommentAddResponse(commentId.getId()));
              } catch (ArticleNotFoundException e) {
                LOG.warn("Cannot add comment", e);
                response.status(400);
                return objectMapper.writeValueAsString(new ErrorResponse(e.getMessage()));
              } catch (RuntimeException e) {
                LOG.error("unexpected error", e);
                response.status(500);
                return objectMapper.writeValueAsString(new ErrorResponse("Server error"));
              }
            }
    );
  }

  private void deleteComment() {
    service.delete(
            "api/articles/:articleId/comments/:commentId",
            (Request request, Response response) -> {
              response.type("application/json");
              String articleId = request.params(":articleId");
              String commentId = request.params(":commentId");

              try {
                articleService.deleteComment(Long.parseLong(articleId), Long.parseLong(commentId));
                response.status(201);
                LOG.debug("Comment delete");
                return objectMapper.writeValueAsString(new CommentDeleteResponse());
              } catch (CommentDeleteException e) {
                LOG.warn("Cannot delete comment", e);
                response.status(400);
                return objectMapper.writeValueAsString(new ErrorResponse(e.getMessage()));
              } catch (RuntimeException e) {
                LOG.error("unexpected error", e);
                response.status(500);
                return objectMapper.writeValueAsString(new ErrorResponse("Server error"));
              }
            }
    );

  }

}

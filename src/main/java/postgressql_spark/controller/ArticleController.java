package postgressql_spark.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spark.controller.request.ArticleCreateRequest;
import org.spark.controller.request.ArticleUpdateRequest;
import org.spark.controller.response.ArticleCreateResponse;
import org.spark.controller.response.ArticleDeleteResponse;
import org.spark.controller.response.ArticleUpdateResponse;
import org.spark.controller.response.ErrorResponse;
import org.spark.service.exception.ArticleCreateException;
import org.spark.service.exception.ArticleDeleteException;
import org.spark.service.exception.ArticleFindException;
import org.spark.service.exception.ArticleUpdateException;
import postgressql_spark.service.ArticleService;
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
    getAllArticles();
    getArticleById();
  }

  private void getArticleById() {
    service.get("/api/articles/:id", (Request request, Response response) -> {
      response.type("application/json");
      String id = request.params(":id");

      try {
        var articleId = Long.parseLong(id);
        var article = articleService.findById(articleId);

        response.status(201);
        LOG.debug("Article successfully received");
        return objectMapper.writeValueAsString(article );
      } catch (ArticleFindException e) {
        LOG.warn("Cannot find article", e);
        response.status(400);
        return objectMapper.writeValueAsString(new ErrorResponse(e.getMessage()));
      } catch (RuntimeException e) {
        LOG.error("Unhandled error", e);
        response.status(500);
        return objectMapper.writeValueAsString(new ErrorResponse("Internal server error"));
      }
    });
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
                var articleId = articleService.createArticle(articleCreateRequest.name(), articleCreateRequest.tags());
                response.status(201);
                LOG.debug("Article created");
                return objectMapper.writeValueAsString(new ArticleCreateResponse(articleId));
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
              String articleId = request.params("articleId");
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
              String id = request.params("articleId");
              try {
                articleService.delete(Long.parseLong(id));
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
}

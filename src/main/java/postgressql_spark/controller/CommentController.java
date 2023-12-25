package postgressql_spark.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spark.controller.request.CommentAddRequest;
import org.spark.controller.response.CommentAddResponse;
import org.spark.controller.response.ErrorResponse;
import org.spark.service.exception.CommentDeleteException;
import postgressql_spark.exceptions.ArticleNotFoundException;
import postgressql_spark.service.CommentService;
import spark.Request;
import spark.Response;
import spark.Service;

public class CommentController implements Controller {
  private static final Logger LOG = LoggerFactory.getLogger(CommentController.class);
  private final Service service;
  private final ObjectMapper objectMapper;
  private final CommentService commentService;

  public CommentController(Service service, ObjectMapper objectMapper, CommentService commentService) {
    this.service = service;
    this.objectMapper = objectMapper;
    this.commentService = commentService;
  }

  @Override
  public void initializeEndpoints() {
    addComment();
    deleteComment();
  }

  private void addComment() {
    service.post("/api/articles/:articleId/comments", (Request request, Response response) -> {
      response.type("application/json");

      String id = request.params(":articleId");
      String body = request.body();

      CommentAddRequest commentAddRequest = objectMapper.readValue(body, CommentAddRequest.class);

      try {
        var commentId = commentService.create(Long.parseLong(id), commentAddRequest.text());
        response.status(201);
        LOG.debug("Comment added");
        return objectMapper.writeValueAsString(new CommentAddResponse(commentId));
      } catch (ArticleNotFoundException e) {
        LOG.warn("Cannot add a new comment", e);
        response.status(400);
        return objectMapper.writeValueAsString(new ErrorResponse(e.getMessage()));
      } catch (RuntimeException e) {
        LOG.error("unexpected error", e);
        response.status(500);
        return objectMapper.writeValueAsString(new ErrorResponse("server error"));
      }
    });
  }

  private void deleteComment() {
    service.delete("/api/comment/:id", (Request request, Response response) -> {
      response.type("application/json");

      String id = request.params(":id");

      try {
        commentService.delete(Long.parseLong(id));
        response.status(201);
        LOG.debug("Comment successfully deleted");
        return response;
      } catch (CommentDeleteException e) {
        LOG.warn("Cannot delete a comment", e);
        response.status(400);
        return objectMapper.writeValueAsString(new ErrorResponse(e.getMessage()));
      } catch (RuntimeException e) {
        LOG.error("Unhandled error", e);
        response.status(500);
        return objectMapper.writeValueAsString(new ErrorResponse("Internal server error"));
      }
    });
  }
}
package org.spark.service.exception;

public class CommentDeleteException extends RuntimeException {

  public CommentDeleteException(String message, RuntimeException e) {
    super(message, e);
  }
}

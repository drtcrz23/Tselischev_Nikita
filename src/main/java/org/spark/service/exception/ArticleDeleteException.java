package org.spark.service.exception;

public class ArticleDeleteException extends RuntimeException {

  public ArticleDeleteException(String message, Throwable cause) {
    super(message, cause);
  }
}
package org.spark.service.exception;

public class ArticleUpdateException extends RuntimeException {

  public ArticleUpdateException(String message, Throwable cause) {
    super(message, cause);
  }
}
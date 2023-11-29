package org.spark.service.exception;

public class ArticleFindException extends RuntimeException {

  public ArticleFindException(String message, Throwable cause) {
    super(message, cause);
  }
}
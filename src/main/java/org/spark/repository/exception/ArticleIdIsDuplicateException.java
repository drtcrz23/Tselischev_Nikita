package org.spark.repository.exception;

public class ArticleIdIsDuplicateException extends RuntimeException {
  public ArticleIdIsDuplicateException(String message) {
    super(message);
  }

  public ArticleIdIsDuplicateException(String message, Throwable cause) {
    super(message, cause);
  }
}

package org.spark.entity.exception;

import org.spark.entity.comment.CommentId;

public class CommentIdNotFoundException extends RuntimeException {
  public CommentIdNotFoundException(CommentId commentId) {
    super("Comment with id: " + commentId + "not found");
  }
}

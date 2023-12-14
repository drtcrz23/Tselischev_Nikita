package postgressql_spark.exceptions;

import postgressql_spark.entity.CommentId;

public class CommentNotFoundException extends RuntimeException {
  public CommentNotFoundException(CommentId commentId) {
    super("Comment with id '" + commentId + "' not found");
  }
}

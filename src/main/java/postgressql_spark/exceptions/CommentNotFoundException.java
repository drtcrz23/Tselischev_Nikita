package postgressql_spark.exceptions;

public class CommentNotFoundException extends RuntimeException {
  public CommentNotFoundException(long commentId) {
    super("Comment with id '" + commentId + "' not found");
  }
}

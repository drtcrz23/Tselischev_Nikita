package postgressql_spark.repository;

import postgressql_spark.entity.Comment;
import postgressql_spark.exceptions.CommentNotFoundException;

import java.sql.SQLException;

public interface CommentRepository {
  long createComment(long articleId, String text);

  Comment findById(long commentId) throws SQLException;

  /**
   * @throws CommentNotFoundException
   */
  void deleteComment(long commentId);
}

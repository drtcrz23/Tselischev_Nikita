package postgressql_spark.repository;

import org.jdbi.v3.core.Jdbi;
import postgressql_spark.entity.Comment;
import postgressql_spark.exceptions.CommentNotFoundException;

import java.util.Map;

public class SqlCommentRepository implements CommentRepository {
  private final Jdbi jdbi;

  public SqlCommentRepository(Jdbi jdbi) {
    this.jdbi = jdbi;
  }

  public Comment findById(long id) {
    Map<String, Object> map = jdbi.inTransaction(handle -> handle.select("SELECT * FROM comment WHERE id = :id")
            .bind("id", id)
            .mapToMap()
            .first());
    return new Comment((Long) map.get("id"),
            (Long) map.get("articleid"),
            (String) map.get("text"));
  }

  @Override
  public long createComment(long articleId, String text) {
    return jdbi.inTransaction(handle -> {
      var result = handle.createUpdate(
                      "INSERT INTO comment (\"articleId\", text) VALUES (:articleId, :text)")
              .bind("articleId", articleId)
              .bind("text", text)
              .executeAndReturnGeneratedKeys();

      var mapRes = result.mapToMap().first();

      return (Long) mapRes.get("id");
    });
  }

  @Override
  public void deleteComment(long articleId) {
    var map = jdbi.inTransaction(handle ->
      handle.createUpdate("DELETE FROM comment WHERE id = :id")
              .bind("id", articleId)
              .execute());
    if (map == 0) {
      throw new CommentNotFoundException(articleId);
    }
  }
}
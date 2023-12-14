package postgressql_spark.repository;

import org.jdbi.v3.core.Jdbi;
import postgressql_spark.entity.Comment;
import postgressql_spark.entity.CommentId;

import java.util.Map;

public class SqlCommentRepository implements CommentRepository {
  private final Jdbi jdbi;

  public SqlCommentRepository(Jdbi jdbi) {
    this.jdbi = jdbi;
  }

  public Comment findById(CommentId id) {
    Map<String, Object> map = jdbi.inTransaction(handle -> handle.select("SELECT * FROM comment WHERE id = :id")
            .bind("id", id.value())
            .mapToMap()
            .first());
    return new Comment(new CommentId((Long) map.get("id")),
            (Long) map.get("articleId"),
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
  public void deleteComment(CommentId commentId) {
    jdbi.inTransaction(handle -> {
      handle.createUpdate("DELETE FROM comment WHERE id = :id")
              .bind("id", commentId.value())
              .execute();

      return null;
    });
  }
}
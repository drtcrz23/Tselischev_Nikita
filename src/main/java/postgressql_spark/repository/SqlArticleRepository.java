package postgressql_spark.repository;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import org.postgresql.jdbc.PgArray;
import postgressql_spark.entity.Article;
import postgressql_spark.exceptions.ArticleNotFoundException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SqlArticleRepository implements ArticleRepository {
  private final Jdbi jdbi;

  public SqlArticleRepository(Jdbi jdbi) {
    this.jdbi = jdbi;
  }

  @Override
  public List<Article> findAll() throws SQLException {
    return jdbi.inTransaction((Handle handle) ->
            handle.select("SELECT article.id, article.name, article.tags, article.trending, COUNT(count.id) AS comments" +
                            " FROM article LEFT JOIN comment count ON count.\"articleId\" = article.id" +
                            " GROUP BY article.id")
                    .mapToMap()
                    .map(objectMap -> {
                      var tags = (PgArray) objectMap.get("tags");
                      List<String> tagsList;
                      try {
                        tagsList = Arrays.stream(((String[]) tags.getArray())).toList();
                      } catch (SQLException e) {
                        throw new RuntimeException(e);
                      }
                      return new Article((Long) objectMap.get("id"),
                              (String) objectMap.get("name"),
                              tagsList,
                              (Boolean) objectMap.get("trending"),
                              (Long) objectMap.get("comments"));
                    }).list());
  }


  @Override
  public Article findById(long id) throws SQLException {
    return jdbi.inTransaction(handle -> {
      var map = handle.createQuery("SELECT article.id, article.name, article.tags, article.trending, COUNT(count.id) AS comments" +
                      "FROM article LEFT JOIN comment count ON count.\"articleId\" = article.id" +
                      " WHERE article.id = :id GROUP BY article.id")
              .bind("id", id)
              .mapToMap()
              .first();
      var tags = (PgArray) map.get("tags");
      var tagsList = Arrays.stream(((String[]) tags.getArray())).toList();
      return (new Article((Long) map.get("id"),
              (String) map.get("name"),
              tagsList,
              (Boolean) map.get("trending"),
              (Long) map.get("comments")));
    });
  }

  @Override
  public void findByIdForUpdate(long articleId) {
    try {
      jdbi.inTransaction((Handle handle) -> {
        Map<String, Object> result = handle.createQuery(
                        "SELECT id, name, trending FROM article WHERE id = :id FOR UPDATE;"
                )
                .bind("id", articleId).mapToMap().first();
        return new Article((Long) result.get("id"),
                (String) result.get("name"),
                new ArrayList<>(),
                (Boolean) result.get("trending"), null);
      });
    } catch (IllegalStateException e) {
      throw new ArticleNotFoundException(articleId);
    }
  }

  @Override
  public long create(String name, List<String> tags) {
    return jdbi.inTransaction((Handle handle) -> {
      ResultBearing resultBearing = handle.createUpdate("INSERT INTO article (name, tags) VALUES (:name, :tags)")
              .bind("name", name)
              .bind("tags", tags.toArray(new String[0]))
              .executeAndReturnGeneratedKeys("id");
      Map<String, Object> mapResult = resultBearing.mapToMap().first();
      return ((Long) mapResult.get("id"));
    });
  }

  @Override
  public void updateTrending(long id, boolean trending) {
    jdbi.inTransaction((Handle handle) -> {
      return handle.createUpdate("UPDATE article SET trending = :trending WHERE article.id = :id")
              .bind("trending", trending)
              .bind("id", id)
              .execute();
    });
  }

  @Override
  public void update(long articleId, String name, List<String> tags) {
    var map = jdbi.inTransaction((Handle handle) -> {
      return handle.createUpdate("UPDATE article SET name = :name, tags = :tags WHERE article.id = :id")
              .bind("id", articleId)
              .bind("name", name)
              .bind("tags", tags.toArray(new String[0]))
              .execute();
    });
    if (map == 0) {
      throw new ArticleNotFoundException(articleId);
    }
  }

  @Override
  public void delete(long articleId) {
    jdbi.inTransaction(handle -> {
      handle.createUpdate("DELETE FROM article WHERE id = :id")
              .bind("id", articleId)
              .execute();
      return null;
    });
  }
}

package postgressql_spark.repository;

//import org.example.article.exceptions.ArticleNotFoundException;

import postgressql_spark.entity.Article;
import postgressql_spark.exceptions.ArticleNotFoundException;

import java.sql.SQLException;
import java.util.List;

public interface ArticleRepository {

  List<Article> findAll() throws SQLException;

  /**
   * @throws ArticleNotFoundException
   */
  Article findById(long articleId) throws SQLException;

  void findByIdForUpdate(long articleId);

  long create(String name, List<String> tags);

  void updateTrending(long id, boolean trending);

  /**
   * @throws ArticleNotFoundException
   */
  void update(long id, String name, List<String> tags);

  /**
   * @throws ArticleNotFoundException
   */
  void delete(long articleId);
}

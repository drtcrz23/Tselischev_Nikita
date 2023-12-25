package postgressql_spark.service;

import org.spark.service.exception.ArticleCreateException;
import org.spark.service.exception.ArticleDeleteException;
import org.spark.service.exception.ArticleFindException;
import org.spark.service.exception.ArticleUpdateException;
import postgressql_spark.entity.Article;
import postgressql_spark.exceptions.ArticleNotFoundException;
import postgressql_spark.repository.ArticleRepository;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;

public class ArticleService {
  private final ArticleRepository articleRepository;

  public ArticleService(ArticleRepository articleRepository) {
    this.articleRepository = articleRepository;
  }

  public List<Article> findAll() throws SQLException {
    return articleRepository.findAll();
  }

  public Article findById(long articleId) throws ArticleNotFoundException {
    try {
      return articleRepository.findById(articleId);
    } catch (Exception e) {
      throw new ArticleFindException("Could not find article with id: " + articleId, e);
    }
  }

  public long createArticle(String name, List<String> tags) throws ArticleCreateException {
    try {
      return articleRepository.create(name, new HashSet<>(tags).stream().toList());
    } catch (RuntimeException e) {
      throw new ArticleCreateException("Cannot create article.", e);
    }
  }

  public void delete(long articleId) {
    try {
      articleRepository.delete(articleId);
    } catch (ArticleNotFoundException e) {
      throw new ArticleDeleteException("Cannot delete article with id: " + articleId, e);
    }
  }

  public void update(long articleId, String name, List<String> tags) throws ArticleUpdateException {
    try {
      articleRepository.update(articleId, name, tags);
    } catch (ArticleNotFoundException e) {
      throw new ArticleUpdateException("Cannot update, article with id: " + articleId + " not found", e);
    }
  }

  public void updateTrending(long articleId, boolean isTrending) {
    articleRepository.updateTrending(articleId, isTrending);
  }
}
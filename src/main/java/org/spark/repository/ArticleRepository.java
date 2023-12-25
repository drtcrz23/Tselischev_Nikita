package org.spark.repository;

import org.spark.entity.article.Article;
import org.spark.entity.article.ArticleId;
import org.spark.repository.exception.ArticleIdIsDuplicateException;
import org.spark.repository.exception.ArticleNotFoundException;

import java.util.List;

public interface ArticleRepository {
  ArticleId generateId();

  List<Article> findAll();

  /**
   * @throws ArticleNotFoundException
   */
  Article findById(ArticleId articleId);

  /**
   * @throws ArticleIdIsDuplicateException
   */
  void create(Article article);

  /**
   * @throws ArticleNotFoundException
   */
  void update(ArticleId articleId, String name, List<String> tags);

  /**
   * @throws ArticleNotFoundException
   */
  void delete(ArticleId articleId);
}
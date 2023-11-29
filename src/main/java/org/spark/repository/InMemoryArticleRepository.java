package org.spark.repository;

import org.spark.entity.article.Article;
import org.spark.entity.article.ArticleId;
import org.spark.repository.exception.ArticleIdIsDuplicateException;
import org.spark.repository.exception.ArticleNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryArticleRepository implements ArticleRepository {
  private final static AtomicLong nextArticleId = new AtomicLong(0);
  private final Map<ArticleId, Article> articlesMap = new ConcurrentHashMap<>();
  @Override
  public ArticleId generateId() {
    return new ArticleId(nextArticleId.getAndIncrement());
  }
  @Override
  public List<Article> findAll() {
    return new ArrayList<>(articlesMap.values());
  }
  @Override
  public Article findById(ArticleId articleId) {
    Article article = articlesMap.get(articleId);
    if (article == null) {
      throw new ArticleNotFoundException("Cannot find article by id: " + articleId);
    }
    return article;
  }
  @Override
  public synchronized void create(Article article) {
    if (articlesMap.get(article.getarticleId()) != null) {
      throw new ArticleIdIsDuplicateException("Article with the given id already exists: " + article.getarticleId());
    }
    articlesMap.put(article.getarticleId(), article);
  }
  @Override
  public synchronized void update(ArticleId articleId, String name, List<String> tags) {
    Article article = findById(articleId);
    articlesMap.put(article.getarticleId(), article.withName(name).withTags(tags));
  }
  @Override
  public synchronized void delete(ArticleId articleId) {
    if (articlesMap.remove(articleId) == null) {
      throw new ArticleNotFoundException("Cannot find article by id: " + articleId);
    }
  }
}

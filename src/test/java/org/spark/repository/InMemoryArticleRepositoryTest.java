package org.spark.repository;

import org.junit.jupiter.api.Test;
import org.spark.entity.article.Article;
import org.spark.entity.article.ArticleId;
import org.spark.repository.exception.ArticleIdIsDuplicateException;
import org.spark.repository.exception.ArticleNotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryArticleRepositoryTest {

  @Test
  public void testGenerateId() {
    InMemoryArticleRepository repository = new InMemoryArticleRepository();

    ArticleId articleId1 = repository.generateId();
    ArticleId articleId2 = repository.generateId();

    assertNotNull(articleId1);
    assertNotNull(articleId2);
    assertNotEquals(articleId1, articleId2);
  }

  @Test
  public void testCreate() {
    InMemoryArticleRepository repository = new InMemoryArticleRepository();

    Article article = new Article(repository.generateId(), "Test Article", List.of("tag1", "tag2"), List.of());
    repository.create(article);

    Article retrievedArticle = repository.findById(article.getArticleId());

    assertEquals(article, retrievedArticle);
  }

  @Test
  public void testDuplicateArticleId() {
    Throwable exception = assertThrows(
            ArticleIdIsDuplicateException.class,() -> {
              InMemoryArticleRepository repository = new InMemoryArticleRepository();
              Article article1 = new Article(repository.generateId(), "Test Article 1", List.of("tag1", "tag2"), List.of());
              Article article2 = new Article(article1.getArticleId(), "Test Article 2", List.of("tag3", "tag4"), List.of());
              repository.create(article1);
              repository.create(article2);
            });
    assertEquals("Article with the given id already exists", exception.getMessage());

  }

  @Test
  public void testUpdate() {
    InMemoryArticleRepository repository = new InMemoryArticleRepository();

    Article originalArticle = new Article(repository.generateId(), "Test Article", List.of("tag1", "tag2"), List.of());
    repository.create(originalArticle);

    ArticleId articleId = originalArticle.getArticleId();
    String newName = "Updated Article";
    List<String> newTags = List.of("tag3", "tag4");

    repository.update(articleId, newName, newTags);

    Article updatedArticle = repository.findById(articleId);

    assertEquals(newName, updatedArticle.getName());
    assertEquals(newTags, updatedArticle.getTags());
  }

  @Test
  public void testDelete() {
    InMemoryArticleRepository repository = new InMemoryArticleRepository();

    Article article = new Article(repository.generateId(), "Test Article", List.of("tag1", "tag2"), List.of());
    repository.create(article);

    ArticleId articleId = article.getArticleId();

    repository.delete(articleId);

    try {
      repository.findById(articleId);
      fail("Expected ArticleNotFoundException to be thrown");
    } catch (ArticleNotFoundException e) {
      assertEquals("Cannot find article by id: " + articleId, e.getMessage());
    }
  }

  @Test
  public void testFindByIdNotFound() throws ArticleNotFoundException {
    Throwable exception = assertThrows(
            ArticleNotFoundException.class,() -> {
              InMemoryArticleRepository repository = new InMemoryArticleRepository();
              ArticleId nonExistentArticleId = new ArticleId(123);
              repository.findById(nonExistentArticleId);
            });
    assertEquals("Cannot find article by id: 123", exception.getMessage());
  }

  @Test
  public void testFindAll() {
    InMemoryArticleRepository repository = new InMemoryArticleRepository();

    Article article1 = new Article(repository.generateId(), "Test Article 1", List.of("tag1", "tag2"), List.of());
    Article article2 = new Article(repository.generateId(), "Test Article 2", List.of("tag3", "tag4"), List.of());

    repository.create(article1);
    repository.create(article2);

    List<Article> allArticles = repository.findAll();

    assertEquals(2, allArticles.size());
    assertTrue(allArticles.contains(article1));
    assertTrue(allArticles.contains(article2));
  }
}
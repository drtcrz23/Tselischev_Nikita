package org.spark.service;

import org.spark.entity.article.Article;
import org.spark.entity.article.ArticleId;
import org.spark.entity.comment.CommentId;
import org.spark.entity.exception.CommentIdNotFoundException;
import org.spark.repository.ArticleRepository;
import org.spark.repository.exception.ArticleIdIsDuplicateException;
import org.spark.repository.exception.ArticleNotFoundException;
import org.spark.service.exception.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ArticleService {
  private final ArticleRepository articleRepository;

  public ArticleService(ArticleRepository articleRepository) {
    this.articleRepository = articleRepository;
  }

  public List<Article> findAll() {
    return articleRepository.findAll();
  }

  public Article findById(ArticleId articleId) {
    try {
      return articleRepository.findById(articleId);
    } catch (ArticleIdIsDuplicateException e) {
      throw new ArticleFindException("Cannot find book by id: " + articleId, e);
    }
  }

  public ArticleId create(String name, List<String> tags) {
    ArticleId articleId = articleRepository.generateId();
    Article article = new Article(articleId, name, new HashSet<>(tags), new ArrayList<>());
    try {
      articleRepository.create(article);
    } catch (RuntimeException e) {
      throw new ArticleCreateException("Cannot create article", e);
    }
    return articleId;
  }

  public void update(long id, String name, List<String> tags) {
    try {
      var articleId = new ArticleId(id);
      articleRepository.update(articleId, name, tags);
    } catch (ArticleNotFoundException e) {
      throw new ArticleUpdateException("Cannot update article with id: " + id, e);
    }
  }

  public void delete(ArticleId articleId) {
    try {
      articleRepository.delete(articleId);
    } catch (ArticleNotFoundException e) {
      throw new ArticleDeleteException("Cannot delete article with id: " + articleId, e);
    }
  }

  public CommentId addNewComment(long id, String text) {
    ArticleId articleId = new ArticleId(id);
    Article article = articleRepository.findById(articleId);
//    CommentId commentId = article.getNewCommentId();
//    Comment comment = new Comment(text ,commentId, articleId);
    return article.addComment(text, articleId);
//    return commentId;
  }
  public void deleteComment(long articleId, long commentId) {
    try {
      Article article = articleRepository.findById(new ArticleId(articleId));
      article.deleteComment(new CommentId(commentId));
    } catch (ArticleNotFoundException e) {
      throw new CommentDeleteException("Article with id: " + articleId + " not found", e);
    } catch (CommentIdNotFoundException e) {
      throw new CommentDeleteException("Comment with id: " + articleId + " not found", e);
    }
  }
}

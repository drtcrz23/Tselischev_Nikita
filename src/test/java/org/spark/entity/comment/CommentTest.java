package org.spark.entity.comment;

import org.junit.jupiter.api.Test;
import org.spark.entity.article.ArticleId;

import static org.junit.jupiter.api.Assertions.*;

public class CommentTest {

  @Test
  public void testGetText() {
    String text = "This is a test comment";
    CommentId commentId = new CommentId(1);
    ArticleId articleId = new ArticleId(1);
    Comment comment = new Comment(text, commentId, articleId);

    assertEquals(text, comment.getText());
  }

  @Test
  public void testGetCommentId() {
    String text = "This is a test comment";
    ArticleId articleId = new ArticleId(1);
    CommentId commentId = new CommentId(1);
    Comment comment = new Comment(text, commentId, articleId);

    assertEquals(commentId, comment.getCommentId());
  }

  @Test
  public void testGetArticleId() {
    String text = "This is a test comment";
    CommentId commentId = new CommentId(1);
    ArticleId articleId = new ArticleId(1);
    Comment comment = new Comment(text, commentId, articleId);

    assertEquals(articleId, comment.getArticleId());
  }
}
package org.spark.entity.comment;

import org.spark.entity.article.ArticleId;

public class Comment {
  private final String text;
  private final CommentId commentId;
  private final ArticleId articleId;

  public Comment(String text, CommentId commentId, ArticleId articleId) {
    this.text = text;
    this.commentId = commentId;
    this.articleId = articleId;
  }
  public CommentId getcommentId() {
    return commentId;
  }
  public ArticleId getArticleId() {
    return articleId;
  }
  public String getText() {
    return text;
  }
}

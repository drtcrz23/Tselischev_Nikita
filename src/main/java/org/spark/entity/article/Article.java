package org.spark.entity.article;

import org.spark.entity.comment.Comment;
import org.spark.entity.comment.CommentId;
import org.spark.entity.exception.CommentIdNotFoundException;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class Article {
  private final ArticleId articleId;
  private final String name;
  private final Collection<String> tags;
  private final List<Comment> comments;
  private final static AtomicLong nextCommentId = new AtomicLong(0);

  public Article(ArticleId articleId, String name, Collection<String> tags, List<Comment> comments) {
    this.articleId = articleId;
    this.name = name;
    this.tags = tags;
    this.comments = comments;
  }

  public ArticleId getArticleId() {
    return articleId;
  }

  public String getName() {
    return name;
  }

  public Collection<String> getTags() {
    return tags;
  }

  public List<Comment> getComments() {
    return comments;
  }

  public Article withTags(List<String> newTags) {
    return new Article(this.articleId, this.name, newTags, this.comments);
  }

  public Article withName(String newName) {
    return new Article(this.articleId, newName, this.tags, this.comments);
  }

//  public CommentId getNewCommentId() {
//    return new CommentId(nextCommentId.getAndIncrement());
//  }

  public synchronized CommentId addComment(String text, ArticleId articleId) {
    var commentId = new CommentId(nextCommentId.getAndIncrement());
    Comment comment = new Comment(text, commentId, articleId);
    comments.add(comment);
    return commentId;
  }
  public void deleteComment(CommentId commentId) {
    for (Comment comment: comments) {
      if (commentId.equals(comment.getCommentId())) {
        comments.remove(comment);
        return;
      }
    }
    throw new CommentIdNotFoundException(commentId);
  }

}
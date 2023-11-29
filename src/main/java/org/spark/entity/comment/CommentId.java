package org.spark.entity.comment;

import java.util.Objects;

public class CommentId {
  private final long commentId;

  public CommentId(long articleId) {
    this.commentId = articleId;
  }
  public long getId() {
    return commentId;
  }

  @Override
  public String toString() {
    return Long.toString(commentId);
  }

  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CommentId id = (CommentId) o;
    return commentId == id.commentId;
  }

  @Override
  public int hashCode() {
    return Objects.hash(commentId);
  }
}

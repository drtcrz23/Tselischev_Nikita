package org.spark.entity.article;

import java.util.Objects;

public class ArticleId {

  private long articleId;

  public ArticleId(long articleId) {
    this.articleId = articleId;
  }
  public long getId() {
    return articleId;
  }

  @Override
  public String toString() {
    return Long.toString(articleId);
  }

  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ArticleId id = (ArticleId) o;
    return articleId == id.articleId;
  }

  @Override
  public int hashCode() {
    return Objects.hash(articleId);
  }
}

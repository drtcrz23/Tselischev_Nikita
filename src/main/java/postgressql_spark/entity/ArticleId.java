package postgressql_spark.entity;

public record ArticleId(long value) {

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ArticleId articleId = (ArticleId) o;
    return value == articleId.value;
  }
}

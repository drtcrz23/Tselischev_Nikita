package postgressql_spark.entity;

public record CommentId(long value) {

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CommentId commentId = (CommentId) o;
    return value == commentId.value;
  }

}

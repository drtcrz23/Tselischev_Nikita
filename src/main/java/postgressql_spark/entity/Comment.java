package postgressql_spark.entity;

public class Comment {
  private final CommentId id;
  private final long articleId;
  private final String text;

  public Comment(CommentId id, long articleId, String text) {
    this.id = id;
    this.articleId = articleId;
    this.text = text;
  }

  public CommentId getId() {
    return id;
  }

  public long getArticleId() {
    return articleId;
  }

  public String getText() {
    return text;
  }
}
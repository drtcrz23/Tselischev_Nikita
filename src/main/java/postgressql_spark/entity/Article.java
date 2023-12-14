package postgressql_spark.entity;


import java.util.Collection;

public class Article {
  private final long id;
  private final String name;
  private final Collection<String> tags;
  private final boolean trending;
  private final Long commentsCount;

  public Article(long id, String name, Collection<String> tags, boolean trending, Long commentsCount) {
    this.id = id;
    this.name = name;
    this.tags = tags;
    this.trending = trending;
    this.commentsCount = commentsCount;
  }

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Collection<String> getTags() {
    return tags;
  }

  public boolean isTrending() {
    return trending;
  }

  public long getCommentsCount() {
    return commentsCount;
  }
}


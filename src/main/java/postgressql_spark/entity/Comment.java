package postgressql_spark.entity;

public record Comment(long id, long articleId, String text) {
}
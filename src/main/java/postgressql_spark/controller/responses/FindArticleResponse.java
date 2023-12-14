package postgressql_spark.controller.responses;

import postgressql_spark.entity.ArticleId;

import java.util.Collection;

public record FindArticleResponse(ArticleId articleId, String name,
                                  Collection<String> tags,
                                  Boolean trending, Long comments) {
}

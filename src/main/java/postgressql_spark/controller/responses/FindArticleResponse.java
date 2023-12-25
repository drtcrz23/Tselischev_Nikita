package postgressql_spark.controller.responses;

import java.util.Collection;

public record FindArticleResponse(long id, String name,
                                  Collection<String> tags,
                                  Boolean trending, Long commentsCount) {
}

package org.spark.controller.request;

import org.spark.entity.article.ArticleId;

import java.util.List;

public record ArticleUpdateRequest(ArticleId articleId, String name, List<String> tags) {
}

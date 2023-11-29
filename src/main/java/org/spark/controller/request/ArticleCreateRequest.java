package org.spark.controller.request;

import java.util.List;

public record ArticleCreateRequest(String name, List<String> tags) {
}

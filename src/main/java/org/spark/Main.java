package org.spark;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.spark.controller.ArticleController;
import org.spark.controller.ArticleFreemarkerController;
import org.spark.repository.InMemoryArticleRepository;
import org.spark.service.ArticleService;
import org.spark.template.TemplateFactory;
import spark.Service;

import java.util.List;

public class Main {

  public static void main(String[] args) {
    Service service = Service.ignite();
    ObjectMapper objectMapper = new ObjectMapper();
    final var articleService = new ArticleService(
            new InMemoryArticleRepository()
    );
    Application application = new Application(
            List.of(new ArticleController(
                            service,
                            articleService,
                            objectMapper),
                    new ArticleFreemarkerController(
                            service,
                            articleService,
                            TemplateFactory.freeMarkerEngine())
            ));
    application.start();
  }
}
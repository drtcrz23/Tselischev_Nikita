package postgressql_spark;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.flywaydb.core.Flyway;
import org.jdbi.v3.core.Jdbi;
import postgressql_spark.controller.ArticleController;
import postgressql_spark.controller.ArticleFreemarker;
import postgressql_spark.controller.CommentController;
import postgressql_spark.repository.ArticleRepository;
import postgressql_spark.repository.CommentRepository;
import postgressql_spark.repository.SqlArticleRepository;
import postgressql_spark.repository.SqlCommentRepository;
import postgressql_spark.service.ArticleService;
import postgressql_spark.service.CommentService;
import postgressql_spark.template.TemplateFactory;
import spark.Service;

import java.util.List;

public class Main {
  public static void main(String[] args) {
    Config config = ConfigFactory.load();
    Service service = Service.ignite();
    ObjectMapper objectMapper = new ObjectMapper();

    Flyway flyway =
            Flyway.configure()
                    .outOfOrder(true)
                    .locations("classpath:db/migrations")
                    .dataSource(config.getString("app.database.url"), config.getString("app.database.user"),
                            config.getString("app.database.password"))
                    .load();
    flyway.migrate();

    Jdbi jdbi = Jdbi.create(config.getString("app.database.url"),
            config.getString("app.database.user"),
            config.getString("app.database.password"));

    ArticleRepository articleRepository = new SqlArticleRepository(jdbi);
    CommentRepository commentsRepository = new SqlCommentRepository(jdbi);
    ArticleService articleService = new ArticleService(articleRepository);
    CommentService commentService = new CommentService(commentsRepository, articleService);

    Application application = new Application(
            List.of(
                    new ArticleController(
                            service,
                            articleService,
                            objectMapper
                                        ),
                    new CommentController(
                            service,
                            objectMapper,
                            commentService
                    ),
                    new ArticleFreemarker(
                            service,
                            articleService,
                            TemplateFactory.freeMarkerEngine()
                    )
            )
    );
    application.start();
  }
}

package postgressql_spark.service;

import org.spark.service.exception.ArticleCreateException;
import org.spark.service.exception.CommentDeleteException;
import postgressql_spark.entity.Article;
import postgressql_spark.entity.ArticleId;
import postgressql_spark.entity.Comment;
import postgressql_spark.entity.CommentId;
import postgressql_spark.exceptions.ArticleNotFoundException;
import postgressql_spark.exceptions.CommentNotFoundException;
import postgressql_spark.repository.CommentRepository;

import java.sql.SQLException;

public class CommentService {
  private final CommentRepository commentRepository;
  private final ArticleService articleService;

  public CommentService(CommentRepository commentRepository, ArticleService articleService) {
    this.commentRepository = commentRepository;
    this.articleService = articleService;
  }


  public long create(long id, String text) {
    ArticleId articleId = new ArticleId(id);
    try {
      Article article = articleService.findById(id);
      if (article.getCommentsCount() >= 2) {
        articleService.updateTrending(id, true);
      }
      return commentRepository.createComment(id, text);
    } catch (ArticleNotFoundException e) {
      throw e;
    } catch (RuntimeException e) {
      throw  new ArticleCreateException("Cannot create comment.", e);
    }
  }

  public void delete(CommentId commentId) throws CommentDeleteException, SQLException {
    try {
      Comment comment = commentRepository.findById(commentId);
      long articleId = comment.getArticleId();
      if (articleId == 0) {
        throw new CommentNotFoundException(commentId);
      }
      Article article = articleService.findById(articleId);
      if (article.getCommentsCount() <= 3) {
        articleService.updateTrending(articleId, false);
      }
      commentRepository.deleteComment(commentId);
    } catch (RuntimeException e) {
      throw new CommentDeleteException("Cannot delete comment.", e);
    }
  }
}

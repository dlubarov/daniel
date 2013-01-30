package daniel.blog.admin;

import daniel.blog.Config;
import daniel.blog.Layout;
import daniel.blog.Notifications;
import daniel.blog.comment.Comment;
import daniel.blog.comment.CommentFormatter;
import daniel.blog.comment.CommentStorage;
import daniel.data.collection.Collection;
import daniel.data.function.Function;
import daniel.data.option.Option;
import daniel.web.html.Attribute;
import daniel.web.html.Element;
import daniel.web.html.Node;
import daniel.web.html.ParagraphBuilder;
import daniel.web.html.Tag;
import daniel.web.http.HttpRequest;
import daniel.web.http.HttpResponse;
import daniel.web.http.HttpStatus;
import daniel.web.http.server.util.HttpResponseFactory;
import daniel.web.http.server.PartialHandler;

public class ReviewCommentsHandler implements PartialHandler {
  public static final ReviewCommentsHandler singleton = new ReviewCommentsHandler();

  private ReviewCommentsHandler() {}

  @Override public Option<HttpResponse> tryHandle(HttpRequest request) {
    if (!request.getResource().startsWith("/admin/review-comments"))
      return Option.none();

    switch (request.getMethod()) {
      case GET:
      case HEAD:
        return Option.some(handleGet(request));
      case POST:
        return Option.some(handlePost(request));
      default:
        return Option.none();
    }
  }

  private HttpResponse handleGet(HttpRequest request) {
    Collection<Comment> unapprovedComments = getUnapprovedComments();
    if (unapprovedComments.isEmpty())
      return nothingToReviewResponse(request);
    Comment comment = getFirstComment(unapprovedComments);
    Element form = commentReviewForm(comment);
    Element html = Layout.createDocument(request,
        Option.some("Review Comments"), Option.<String>none(), form);
    return HttpResponseFactory.xhtmlResponse(HttpStatus.OK, html);
  }

  private static Element commentReviewForm(Comment comment) {
    Element approveButton = new Element.Builder(Tag.INPUT)
            .setAttribute(Attribute.NAME, "approve")
            .setAttribute(Attribute.TYPE, "submit")
            .setAttribute(Attribute.VALUE, "Approve")
            .build();
    Element deleteButton = new Element.Builder(Tag.INPUT)
            .setAttribute(Attribute.NAME, "delete")
            .setAttribute(Attribute.TYPE, "submit")
            .setAttribute(Attribute.VALUE, "Delete")
            .build();
    return new Element.Builder(Tag.FORM)
        .setAttribute(Attribute.ACTION, "admin/review-comments")
        .setAttribute(Attribute.METHOD, "post")
        .addChild(CommentFormatter.full(comment))
        .addChild(new ParagraphBuilder().addChild(approveButton).addChild(deleteButton).build())
        .build();
  }

  private static HttpResponse nothingToReviewResponse(HttpRequest request) {
    Node content = new ParagraphBuilder()
        .addEscapedText("There are no comments needing review.")
        .build();
    Element html = Layout.createDocument(request,
        Option.some("Review Comments"), Option.<String>none(), content);
    return HttpResponseFactory.xhtmlResponse(HttpStatus.OK, html);
  }

  private HttpResponse handlePost(HttpRequest request) {
    boolean approved = !request.getUrlencodedPostData().getValues("approve").isEmpty();
    boolean deleted = !request.getUrlencodedPostData().getValues("delete").isEmpty();
    Comment originalComment = getFirstComment(getUnapprovedComments());
    if (approved && !deleted) {
      Comment updatedComment = new Comment.Builder(originalComment).setApproved(approved).build();
      CommentStorage.updateComment(updatedComment);
      Notifications.addMessage(request, "That comment has been approved.");
    } else if (deleted && !approved) {
      CommentStorage.deleteComment(originalComment);
      Notifications.addMessage(request, "That comment has been deleted.");
    } else {
      throw new RuntimeException(String.format(
          "Expected approved xor deleted; got %s.",
          request.getUrlencodedPostData()));
    }

    String location = Config.getBaseUrl() + "/admin/review-comments";
    return HttpResponseFactory.redirectToGet(location);
  }

  private static Comment getFirstComment(Collection<Comment> unapprovedComments) {
    return unapprovedComments.sorted(Comment.ASCENDING_CREATED_AT_ORDERING).getFront();
  }

  private static Collection<Comment> getUnapprovedComments() {
    return CommentStorage.getAllComments().filter(
        new Function<Comment, Boolean>() {
          @Override public Boolean apply(Comment comment) {
            return !comment.isApproved();
          }
        });
  }
}

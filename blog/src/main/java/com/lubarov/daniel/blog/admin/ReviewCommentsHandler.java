package com.lubarov.daniel.blog.admin;

import com.lubarov.daniel.blog.Config;
import com.lubarov.daniel.blog.Layout;
import com.lubarov.daniel.blog.Notifications;
import com.lubarov.daniel.blog.comment.Comment;
import com.lubarov.daniel.blog.comment.CommentFormatter;
import com.lubarov.daniel.blog.comment.CommentStorage;
import com.lubarov.daniel.blog.post.Post;
import com.lubarov.daniel.blog.post.PostFormatter;
import com.lubarov.daniel.blog.post.PostStorage;
import com.lubarov.daniel.data.collection.Collection;
import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.data.unit.Instant;
import com.lubarov.daniel.web.html.*;
import com.lubarov.daniel.web.http.HttpRequest;
import com.lubarov.daniel.web.http.HttpResponse;
import com.lubarov.daniel.web.http.HttpStatus;
import com.lubarov.daniel.web.http.server.PartialHandler;
import com.lubarov.daniel.web.http.server.util.HttpResponseFactory;

public class ReviewCommentsHandler implements PartialHandler {
  public static final ReviewCommentsHandler singleton = new ReviewCommentsHandler();

  private ReviewCommentsHandler() {}

  @Override
  public Option<HttpResponse> tryHandle(HttpRequest request) {
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
    Post post = PostStorage.getPostByUuid(comment.getPostUuid()).getOrThrow();
    Element form = commentReviewForm(comment, post);
    Element html = Layout.createDocument(request,
        Option.some("Review Comments"), Option.<Instant>none(), form);
    return HttpResponseFactory.xhtmlResponse(HttpStatus.OK, html);
  }

  private static Element commentReviewForm(Comment comment, Post post) {
    Element approveButton = new Element.Builder(Tag.INPUT)
        .setRawAttribute(Attribute.NAME, "approve")
        .setRawAttribute(Attribute.TYPE, "submit")
        .setRawAttribute(Attribute.VALUE, "Approve")
        .build();
    Element deleteButton = new Element.Builder(Tag.INPUT)
        .setRawAttribute(Attribute.NAME, "delete")
        .setRawAttribute(Attribute.TYPE, "submit")
        .setRawAttribute(Attribute.VALUE, "Delete")
        .build();
    return new Element.Builder(Tag.FORM)
        .setRawAttribute(Attribute.ACTION, "admin/review-comments")
        .setRawAttribute(Attribute.METHOD, "post")
        .addChild(PostFormatter.summaryLink(post))
        .addChild(CommentFormatter.full(comment))
        .addChild(new ParagraphBuilder().addChild(approveButton).addChild(deleteButton).build())
        .build();
  }

  private static HttpResponse nothingToReviewResponse(HttpRequest request) {
    Node content = new ParagraphBuilder()
        .addEscapedText("There are no comments needing review.")
        .build();
    Element html = Layout.createDocument(request,
        Option.some("Review Comments"), Option.<Instant>none(), content);
    return HttpResponseFactory.xhtmlResponse(HttpStatus.OK, html);
  }

  private HttpResponse handlePost(HttpRequest request) {
    boolean approved = !request.getUrlencodedPostData().getValues("approve").isEmpty();
    boolean deleted = !request.getUrlencodedPostData().getValues("delete").isEmpty();
    Comment originalComment = getFirstComment(getUnapprovedComments());
    if (approved && !deleted) {
      Comment updatedComment = new Comment.Builder(originalComment).setApproved(true).build();
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
        comment -> !comment.isApproved());
  }
}

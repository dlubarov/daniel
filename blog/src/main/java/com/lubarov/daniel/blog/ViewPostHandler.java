package com.lubarov.daniel.blog;

import com.lubarov.daniel.blog.comment.Comment;
import com.lubarov.daniel.blog.comment.CommentStorage;
import com.lubarov.daniel.blog.post.Post;
import com.lubarov.daniel.blog.post.PostFormatter;
import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.data.sequence.Sequence;
import com.lubarov.daniel.web.html.Element;
import com.lubarov.daniel.web.http.HttpRequest;
import com.lubarov.daniel.web.http.HttpResponse;
import com.lubarov.daniel.web.http.HttpStatus;
import com.lubarov.daniel.web.http.server.Handler;
import com.lubarov.daniel.web.http.server.util.HttpResponseFactory;

final class ViewPostHandler implements Handler {
  private final Post post;

  ViewPostHandler(Post post) {
    this.post = post;
  }

  @Override
  public HttpResponse handle(HttpRequest request) {
    Sequence<Comment> comments = CommentStorage.getCommentsByPost(post.getUuid())
        .filter(Comment::isApproved)
        .sorted(Comment.ASCENDING_CREATED_AT_ORDERING);
    Element document = Layout.createDocument(request,
        Option.some(post.getSubject()),
        Option.some(post.getCreatedAt()),
        PostFormatter.full(post, comments));
    return HttpResponseFactory.xhtmlResponse(HttpStatus.OK, document);
  }
}

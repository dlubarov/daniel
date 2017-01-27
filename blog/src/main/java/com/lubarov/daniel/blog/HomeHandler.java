package com.lubarov.daniel.blog;

import com.lubarov.daniel.blog.admin.Authenticator;
import com.lubarov.daniel.blog.post.Post;
import com.lubarov.daniel.blog.post.PostFormatter;
import com.lubarov.daniel.blog.post.PostStorage;
import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.data.order.AbstractOrdering;
import com.lubarov.daniel.data.order.Relation;
import com.lubarov.daniel.data.sequence.Sequence;
import com.lubarov.daniel.data.unit.Instant;
import com.lubarov.daniel.web.html.Element;
import com.lubarov.daniel.web.html.ParagraphBuilder;
import com.lubarov.daniel.web.html.Tag;
import com.lubarov.daniel.web.http.HttpRequest;
import com.lubarov.daniel.web.http.HttpResponse;
import com.lubarov.daniel.web.http.HttpStatus;
import com.lubarov.daniel.web.http.server.PartialHandler;
import com.lubarov.daniel.web.http.server.util.HttpResponseFactory;

final class HomeHandler implements PartialHandler {
  public static final HomeHandler singleton = new HomeHandler();

  private static final String INTRO = ""
      + "I'm an engineer at Google, previously at Square. "
      + "Some of my interests are graphics, storage systems, language design and compilers. "
      + "Feel free to email me at <a href=\"daniel@lubarov.com\">daniel@lubarov.com</a>.";

  private HomeHandler() {}

  @Override
  public Option<HttpResponse> tryHandle(HttpRequest request) {
    if (!request.getResource().equals("/"))
      return Option.none();

    Sequence<Post> allPosts = PostStorage.getAllPosts()
        .sorted(new AbstractOrdering<Post>() {
          @Override
          public Relation compare(Post a, Post b) {
            return Instant.DESCENDING_ORDERING.compare(a.getCreatedAt(), b.getCreatedAt());
          }
        });

    Element.Builder listBuilder = new Element.Builder(Tag.UL);
    for (Post post : allPosts) {
      if (!post.isPublished() && !Authenticator.isAdmin(request))
        continue;
      Element summaryLink = PostFormatter.summaryLink(post);
      listBuilder.addChild(new Element(Tag.LI, summaryLink));
    }

    Element intro = new ParagraphBuilder().setId("intro").addRawText(INTRO).build();

    Element document = Layout.createDocument(request,
        Option.<String>none(), Option.<Instant>none(),
        intro, listBuilder.build());
    return Option.some(HttpResponseFactory.xhtmlResponse(HttpStatus.OK, document));
  }
}

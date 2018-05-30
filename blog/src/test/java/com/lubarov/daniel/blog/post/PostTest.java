package com.lubarov.daniel.blog.post;

import com.lubarov.daniel.data.unit.Instant;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PostTest {
  @Test
  public void testGetUrlFriendlySubject() {
    Post post = new Post.Builder()
        .setRandomUiid()
        .setCreatedAt(Instant.now())
        .setSubject("Simple \"watch\" script for OSX")
        .setPublished(true)
        .setContent("Blah.")
        .build();
    assertEquals("simple-watch-script-for-osx", post.getUrlFriendlySubject());
  }
}

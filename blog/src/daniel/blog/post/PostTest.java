package daniel.blog.post;

import daniel.data.unit.Instant;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PostTest {
  @Test
  public void testGetUrlFriendlySubject() throws Exception {
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

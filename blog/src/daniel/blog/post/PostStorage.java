package daniel.blog.post;

import daniel.bdb.SerializingDatabase;
import daniel.blog.Config;
import daniel.data.collection.Collection;
import daniel.data.option.Option;
import daniel.data.serialization.StringSerializer;

public final class PostStorage {
  private static final SerializingDatabase<String, Post> byUuid = new SerializingDatabase<>(
      Config.getDatabaseHome("posts"), StringSerializer.singleton, PostSerializer.singleton);

  private PostStorage() {}

  public static void saveNewPost(Post post) {
    byUuid.put(post.getUuid(), post);
  }

  public static void updatePost(Post post) {
    byUuid.put(post.getUuid(), post);
  }

  public static void deletePost(Post post) {
    byUuid.delete(post.getUuid());
  }

  public static Option<Post> getPostByUuid(String uuid) {
    return byUuid.get(uuid);
  }

  public static Collection<Post> getAllPosts() {
    return byUuid.getAllValues();
  }
}

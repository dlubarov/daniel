package com.lubarov.daniel.blog.post;

import com.lubarov.daniel.bdb.SerializingDatabase;
import com.lubarov.daniel.blog.Config;
import com.lubarov.daniel.data.collection.Collection;
import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.data.serialization.StringSerializer;

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

  private static void refreshAll() {
    for (Post post : getAllPosts())
      updatePost(post);
  }
}

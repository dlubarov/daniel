package com.lubarov.daniel.blog.comment;

import com.lubarov.daniel.bdb.SerializingDatabase;
import com.lubarov.daniel.blog.Config;
import com.lubarov.daniel.data.collection.Collection;
import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.data.sequence.ImmutableArray;
import com.lubarov.daniel.data.sequence.Sequence;
import com.lubarov.daniel.data.sequence.SinglyLinkedList;
import com.lubarov.daniel.data.serialization.CollectionSerializer;
import com.lubarov.daniel.data.serialization.StringSerializer;

public final class CommentStorage {
  private static final SerializingDatabase<String, Comment> byUuid =
      new SerializingDatabase<>(Config.getDatabaseHome("comments"),
          StringSerializer.singleton, CommentSerializer.singleton);

  private static final SerializingDatabase<String, Collection<String>> indexByPostUuid =
      new SerializingDatabase<>(Config.getDatabaseHome("comments-index-by-post"),
          StringSerializer.singleton, CollectionSerializer.stringCollectionSerializer);

  private CommentStorage() {}

  public static synchronized Collection<Comment> getAllComments() {
    return byUuid.getAllValues();
  }

  public static synchronized Option<Comment> getCommentByUuid(String commentUuid) {
    return byUuid.get(commentUuid);
  }

  public static synchronized void saveNewComment(Comment comment) {
    byUuid.put(comment.getUuid(), comment);
    SinglyLinkedList<String> commentUuids = SinglyLinkedList.copyOf(
        getCommentUuidsByPost(comment.getPostUuid()));
    commentUuids = commentUuids.plusFront(comment.getUuid());
    indexByPostUuid.put(comment.getPostUuid(), commentUuids);
  }

  public static synchronized void updateComment(Comment comment) {
    deleteComment(comment);
    saveNewComment(comment);
  }

  public static synchronized void deleteComment(final Comment comment) {
    byUuid.delete(comment.getUuid());
    Sequence<String> commentUuids = SinglyLinkedList.copyOf(
        getCommentUuidsByPost(comment.getPostUuid()));
    commentUuids = commentUuids.filter(uuid -> !uuid.equals(comment.getUuid()));
    indexByPostUuid.put(comment.getPostUuid(), commentUuids);
  }

  public static synchronized Collection<Comment> getCommentsByPost(String postUuid) {
    return getCommentUuidsByPost(postUuid).map(commentUuid ->
            getCommentByUuid(commentUuid).getOrThrow("No comment with UUID from index: " + commentUuid));
  }

  private static synchronized Collection<String> getCommentUuidsByPost(String postUuid) {
    return indexByPostUuid.get(postUuid).getOrDefault(ImmutableArray.<String>create());
  }
}

package daniel.blog.comment;

import daniel.bdb.SerializingDatabase;
import daniel.blog.Config;
import daniel.data.collection.Collection;
import daniel.data.function.Function;
import daniel.data.option.Option;
import daniel.data.sequence.ImmutableArray;
import daniel.data.sequence.Sequence;
import daniel.data.sequence.SinglyLinkedList;
import daniel.data.serialization.CollectionSerializer;
import daniel.data.serialization.StringSerializer;

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
    commentUuids = commentUuids.filter(new Function<String, Boolean>() {
      @Override public Boolean apply(String uuid) {
        return !uuid.equals(comment.getUuid());
      }
    });
    indexByPostUuid.put(comment.getPostUuid(), commentUuids);
  }

  public static synchronized Collection<Comment> getCommentsByPost(String postUuid) {
    return getCommentUuidsByPost(postUuid).map(new Function<String, Comment>() {
      @Override public Comment apply(String commentUuid) {
        return getCommentByUuid(commentUuid).getOrThrow(
            "No comment with UUID from index: " + commentUuid);
      }
    });
  }

  private static synchronized Collection<String> getCommentUuidsByPost(String postUuid) {
    return indexByPostUuid.get(postUuid).getOrDefault(ImmutableArray.<String>create());
  }
}

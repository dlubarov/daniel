package daniel.web.http.server;

import daniel.data.dictionary.ImmutableDictionary;
import daniel.data.dictionary.MutableHashTable;
import daniel.data.option.Option;
import daniel.data.util.Check;
import daniel.data.util.FilenameUtils;
import daniel.data.util.IOUtils;
import daniel.web.http.DateUtils;
import daniel.web.http.HttpRequest;
import daniel.web.http.HttpResponse;
import daniel.web.http.HttpStatus;
import daniel.web.http.HttpVersion;
import daniel.web.http.ResponseHeaderName;
import java.io.File;
import java.io.IOException;
import java.util.Date;

public class StaticContentHandler implements PartialHandler {
  public static class Builder {
    private Option<File> contentRoot = Option.none();
    private final MutableHashTable<String, String> mimeTypesByExtension = MutableHashTable.create();

    public Builder setContentRoot(File contentRoot) {
      this.contentRoot = Option.some(contentRoot);
      return this;
    }

    public Builder addContentType(String mimeType, String extension, String... moreExtensions) {
      mimeTypesByExtension.put(extension, mimeType);
      for (String ext : moreExtensions)
        mimeTypesByExtension.put(ext, mimeType);
      return this;
    }

    public Builder addCommonContentTypes() {
      return this
          .addContentType("text/plain", "txt")
          .addContentType("image/gif", "gif")
          .addContentType("image/png", "png")
          .addContentType("image/jpeg", "jpg", "jpeg")
          .addContentType("image/x-icon", "ico")
          .addContentType("application/zip", "zip")
          .addContentType("application/gzip", "gz")
          .addContentType("text/css", "css")
          .addContentType("application/javascript", "js");
    }

    public StaticContentHandler build() {
      return new StaticContentHandler(this);
    }
  }

  private final File contentRoot;
  private final ImmutableDictionary<String, String> mimeTypesByExtension;

  private StaticContentHandler(Builder builder) {
    contentRoot = builder.contentRoot.getOrThrow("No content root was set.");
    mimeTypesByExtension = builder.mimeTypesByExtension.toImmutable();
    Check.that(contentRoot.isDirectory(), "%s is not a directory.", contentRoot);
  }

  @Override
  public Option<HttpResponse> tryHandle(HttpRequest request) {
    Option<String> optExtension = FilenameUtils.getExtension(request.getResource());
    if (optExtension.isEmpty())
      return Option.none();

    Option<String> mimeType = mimeTypesByExtension.tryGetValue(optExtension.getOrThrow().toLowerCase());
    if (mimeType.isEmpty())
      return Option.none();

    File resourcePath = new File(contentRoot, request.getResource());
    if (!resourcePath.exists() || !isInContentRoot(resourcePath))
      return Option.none();

    Date lastModified = new Date(resourcePath.lastModified());
    byte[] content;
    try {
      content = IOUtils.readFileToByteArray(resourcePath);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return Option.some(new HttpResponse.Builder().setHttpVersion(HttpVersion._1_1)
        .setStatus(HttpStatus.OK)
        .addHeader(ResponseHeaderName.LAST_MODIFIED, DateUtils.formatDate(lastModified))
        .addHeader(ResponseHeaderName.CONTENT_TYPE, mimeType.getOrThrow())
        .addHeader(ResponseHeaderName.CONTENT_LENGTH, Integer.toString(content.length))
        .setBody(content)
        .build());
  }

  private boolean isInContentRoot(File file) {
    try {
      file = file.getCanonicalFile();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    while (file != null && !file.equals(contentRoot))
      file = file.getParentFile();
    return file != null;
  }
}

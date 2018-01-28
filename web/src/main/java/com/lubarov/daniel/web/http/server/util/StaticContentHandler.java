package com.lubarov.daniel.web.http.server.util;

import com.lubarov.daniel.common.Logger;
import com.lubarov.daniel.data.dictionary.ImmutableDictionary;
import com.lubarov.daniel.data.dictionary.MutableHashDictionary;
import com.lubarov.daniel.data.option.Option;
import com.lubarov.daniel.data.unit.Duration;
import com.lubarov.daniel.data.unit.Instant;
import com.lubarov.daniel.data.util.Check;
import com.lubarov.daniel.data.util.FilenameUtils;
import com.lubarov.daniel.data.util.IOUtils;
import com.lubarov.daniel.web.http.DateUtils;
import com.lubarov.daniel.web.http.HttpRequest;
import com.lubarov.daniel.web.http.HttpResponse;
import com.lubarov.daniel.web.http.HttpStatus;
import com.lubarov.daniel.web.http.RequestMethod;
import com.lubarov.daniel.web.http.ResponseHeaderName;
import com.lubarov.daniel.web.http.server.PartialHandler;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Date;

public final class StaticContentHandler implements PartialHandler {
  private static final Logger logger = Logger.forClass(StaticContentHandler.class);

  public static final class Builder {
    private Option<File> contentRoot = Option.none();
    private final MutableHashDictionary<String, String>
        mimeTypesByExtension = MutableHashDictionary.create();

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
          .addContentType("text/html", "html", "htm")
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

    Instant lastModified = Instant.fromDate(new Date(resourcePath.lastModified()));
    Option<String> optIfModifiedSince = request.getHeaders()
        .getValues("If-Modified-Since").tryGetOnlyElement();
    if (optIfModifiedSince.isDefined()) {
      try {
        Instant ifModifiedSince = DateUtils.parseRfc1123(optIfModifiedSince.getOrThrow());
        boolean modifiedSince = lastModified.isAfter(ifModifiedSince);
        logger.info("Modified since %s: %b.", ifModifiedSince, modifiedSince);
        if (!modifiedSince)
          return Option.some(new HttpResponse.Builder()
              .setStatus(HttpStatus.NOT_MODIFIED)
              .addHeader(ResponseHeaderName.EXPIRES, DateUtils.formatRfc1123(
                  lastModified.plus(Duration.fromHours(24))))
              .addHeader(ResponseHeaderName.LAST_MODIFIED, DateUtils.formatRfc1123(lastModified))
              .build());
      } catch (ParseException e) {
        logger.error(e, "Failed to parse If-Modified-Since header: %s",
            optIfModifiedSince.getOrThrow());
      }
    }

    byte[] bytes;
    try {
      bytes = IOUtils.readFileToByteArray(resourcePath);
    } catch (IOException e) {
      throw new RuntimeException("Failed to load ", e);
    }

    Option<byte[]> content;
    if (request.getMethod() == RequestMethod.HEAD)
      content = Option.none();
    else
      content = Option.some(bytes);

    Instant expires = Instant.now().plus(Duration.fromHours(24));

    HttpResponse.Builder responseBuilder = new HttpResponse.Builder()
        .setStatus(HttpStatus.OK)
        .addHeader(ResponseHeaderName.ETAG, getETag(bytes))
        .addHeader(ResponseHeaderName.EXPIRES, DateUtils.formatRfc1123(expires))
        .addHeader(ResponseHeaderName.LAST_MODIFIED, DateUtils.formatRfc1123(lastModified))
        .addHeader(ResponseHeaderName.CONTENT_TYPE, mimeType.getOrThrow());
    if (content.isDefined())
      responseBuilder.setBody(content);
    return Option.some(responseBuilder.build());
  }

  private static String getETag(byte[] bytes) {
    MessageDigest digest = null;
    try {
      digest = MessageDigest.getInstance("SHA-256");
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    byte[] hashBytes = digest.digest(bytes);
    StringBuilder hexBuilder = new StringBuilder();
    for (byte hashByte : hashBytes) {
      hexBuilder.append(String.format("%02x", hashByte));
    }
    return hexBuilder.toString();
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

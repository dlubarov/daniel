package daniel.web.http.server;

import daniel.data.dictionary.KeyValuePair;
import daniel.data.option.Option;
import daniel.web.http.HttpRequest;
import daniel.web.http.HttpResponse;
import daniel.web.http.RequestMethod;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

class ConnectionManager implements Runnable {
  private final Socket socket;
  private final Handler handler;

  public ConnectionManager(Socket socket, Handler handler) {
    this.socket = socket;
    this.handler = handler;
  }

  @Override
  public void run() {
    try {
      runWithExceptions();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void runWithExceptions() throws IOException {
    Option<HttpRequest> optRequest = RequestReader.readRequest(socket.getInputStream());
    if (optRequest.isEmpty())
      return;

    HttpRequest request = optRequest.getOrThrow();
    Writer writer = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.US_ASCII);

    System.out.printf("Handling request for %s%s\n",
        request.getHost(), request.getResource());
    HttpResponse response = handler.handle(request);

    writer.write(String.format("HTTP/%s %s\r\n", response.getHttpVersion(), response.getStatus()));
    for (KeyValuePair<String, String> header : response.getHeaders())
      writer.write(String.format("%s: %s\r\n", header.getKey(), header.getValue()));
    writer.write("Connection: close\r\n");
    writer.write("\r\n");
    writer.flush();
    if (response.getBody().isDefined() && request.getMethod() != RequestMethod.HEAD)
      socket.getOutputStream().write(response.getBody().getOrThrow());
    socket.getOutputStream().flush();
    socket.close();
  }
}

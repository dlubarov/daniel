package com.lubarov.daniel.junkmail;

import com.lubarov.daniel.common.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SmtpConnectionManager implements Runnable {
  private static final Logger logger = Logger.forClass(SmtpConnectionManager.class);

  private final Socket socket;
  private final Writer writer;
  private final BufferedReader bufferedReader;

  private String sender;
  private List<String> recipients = new ArrayList<>();

  public SmtpConnectionManager(Socket socket) {
    this.socket = socket;
    try {
      writer = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.US_ASCII);
      bufferedReader = new BufferedReader(new InputStreamReader(
          socket.getInputStream(), StandardCharsets.US_ASCII));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void run() {
    try {
      runWithExceptions();
    } catch (IOException e) {
      logger.error(e, "Exception in SMTP session.");
    }
  }

  private void runWithExceptions() throws IOException {
    writeLine(String.format("220 %s SMTP service ready", Config.getSmtpHostname()));

    String line;
    while ((line = bufferedReader.readLine()) != null) {
      String[] parts = line.split(" ");
      if (parts.length == 0)
        throw new IllegalArgumentException("Received an empty line.");

      switch (parts[0].toUpperCase()) {
        case "HELO":
          if (parts.length != 2)
            throw new IllegalArgumentException("Unexpected HELO line: " + line);
          handleHello(parts[1]);
          break;
        case "EHLO":
          if (parts.length != 2)
            throw new IllegalArgumentException("Unexpected EHLO line: " + line);
          handleHello(parts[1]);
          break;
        case "MAIL":
          if (parts.length != 2 || !parts[1].toUpperCase().startsWith("FROM:"))
            throw new IllegalArgumentException("Unexpected MAIL line: " + line);
          handleMail(parts[1].substring("FROM:".length()));
          break;
        case "RCPT":
          if (parts.length != 2 || !parts[1].toUpperCase().startsWith("TO:"))
            throw new IllegalArgumentException("Unexpected RCPT line: " + line);
          handleRcpt(parts[1].substring("TO:".length()));
          break;
        case "DATA":
          if (parts.length != 1)
            throw new IllegalArgumentException("Unexpected DATA line: " + line);
          handleData();
          break;
        case "QUIT":
          handleQuit();
          return;
        default:
          throw new IllegalArgumentException("Unrecognized command: " + parts[0]);
      }
    }
  }

  private void handleHello(String domain) throws IOException {
    writeLine(String.format("250 Why hello %s, it's a pleasure to make your acquaintance", domain));
  }

  private void handleMail(String sender) throws IOException {
    this.sender = sender;
    writeLine("250 Ok");
  }

  private void handleRcpt(String recipient) throws IOException {
    recipients.add(recipient);
    writeLine("250 Ok");
  }

  private void handleData() throws IOException {
    if (sender == null)
      throw new IllegalStateException("No sender!");
    if (recipients.isEmpty())
      throw new IllegalStateException("No recipients!");

    writeLine("354 End data with <CR><LF>.<CR><LF>");

    String line;
    StringBuilder stringBuilder = new StringBuilder();
    while ((line = bufferedReader.readLine()) != null) {
      if (line.equals("."))
        break;
      if (line.startsWith("."))
        line = line.substring(1);
      if (stringBuilder.length() > 0)
        stringBuilder.append('\n');
      stringBuilder.append(line);
    }

    String body = stringBuilder.toString();
    logger.info("Email from %s to %s:\n%s", sender, recipients, body);

    writeLine("250 Ok");

    sender = null;
    recipients = new ArrayList<>();
  }

  private void handleQuit() throws IOException {
    writeLine("221 Bye");
    socket.close();
  }

  private void writeLine(String line)  throws IOException {
    writer.write(line);
    writer.write('\n');
    writer.flush();
  }
}

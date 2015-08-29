package com.lubarov.daniel.junkmail;

import com.lubarov.daniel.common.Logger;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SmtpListener implements Runnable {
  public static final SmtpListener singleton = new SmtpListener();
  private static final Logger logger = Logger.forClass(SmtpListener.class);

  private final Executor executor = Executors.newCachedThreadPool(
      r -> new Thread(r, "smtp connection manager"));
  private final ServerSocket serverSocket;

  private SmtpListener() {
    try {
      serverSocket = new ServerSocket(Config.getSmtpPort());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void run() {
    for (;;) {
      try {
        Socket socket = serverSocket.accept();
        executor.execute(new SmtpConnectionManager(socket));
      } catch (IOException e) {
        logger.error(e, "Failed to accept connection.");
      }
    }
  }
}

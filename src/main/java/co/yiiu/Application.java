package co.yiiu;

import co.yiiu.server.Server;

public class Application {

  public static void main(final String[] args) {
    new Server().run(Application.class);
  }
}

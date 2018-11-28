package com.smatei.salt.net;

/**
 * Proxy settings.
 *
 * @author smatei
 *
 */
public class Proxy
{
  private final String server;
  private final int port;
  private final String user;
  private final String password;

  public Proxy(String server, int port, String user, String password)
  {
    this.server = server;
    this.port = port;
    this.user = user;
    this.password = password;
  }

  public String getServer()
  {
    return server;
  }

  public int getPort()
  {
    return port;
  }

  public String getUser()
  {
    return user;
  }

  public String getPassword()
  {
    return password;
  }
}

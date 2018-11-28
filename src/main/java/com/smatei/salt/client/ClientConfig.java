package com.smatei.salt.client;

import java.net.URI;

import com.smatei.salt.net.Proxy;

/**
 * Configuration for salt API client.
 *
 * @author smatei
 *
 */
public class ClientConfig
{
  private final URI url;
  private Proxy proxy;
  private final String username;
  private final String password;

  public ClientConfig(URI url, String username, String password)
  {
    this.url = url;
    this.username = username;
    this.password = password;
  }

  public void SetProxy(Proxy proxy)
  {
    this.proxy = proxy;
  }

  public URI getUrl()
  {
    return url;
  }

  public Proxy getProxy()
  {
    return proxy;
  }

  public String getUsername()
  {
    return username;
  }

  public String getPassword()
  {
    return password;
  }
}

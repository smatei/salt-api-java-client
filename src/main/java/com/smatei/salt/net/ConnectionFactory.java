package com.smatei.salt.net;

import com.smatei.salt.client.ClientConfig;

/**
 * Class for creating instances of an HTTP connection implementation.
 */
public class ConnectionFactory
{
  /**
   * Create a new {@link IConnection} for a given endpoint and configuration.
   *
   * @param <T>
   *          type of the result as returned by the parser
   * @param apiEndpoint
   *          the API endpoint
   * @param config
   *          the configuration
   * @param returnClass
   *          the type of the object to be returned
   * @return object representing a connection to the API
   */
  public IConnection create(String apiEndpoint, ClientConfig config)
  {
    return new RestTemplateConnection(apiEndpoint, config);
  }
}

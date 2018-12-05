package com.smatei.salt.net;

/**
 * Describes an interface for different HTTP connection implementations.
 *
 * @param <T>
 *          type of result retrieved using this HTTP connection
 */
public interface IConnection<T>
{
  /**
   * Send a GET request and parse the result into object of given type.
   *
   * @return object of type given by resultType
   */
  T request();

  /**
   * Send a POST request and parse the result into object of given type.
   *
   * @param data
   *          the data to send (in JSON format)
   * @return object of type given by resultType
   */
  T request(String params);
}

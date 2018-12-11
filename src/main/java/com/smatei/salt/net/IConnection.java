package com.smatei.salt.net;

/**
 * Describes an interface for different HTTP connection implementations.
 *
 */
public interface IConnection
{
  /**
   * Send a GET request and parse the result into object of given type.
   *
   * @return response of the GET request
   */
  String request();

  /**
   * Send a POST request and parse the result into object of given type.
   *
   * @param data
   *          the data to send (in JSON format)
   *
   * @return response of the POST request
   */
  String request(String params);
}

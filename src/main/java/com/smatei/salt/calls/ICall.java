package com.smatei.salt.calls;

import java.util.Map;

/**
 * Interface for all function calls in salt.
 *
 */
public interface ICall
{

  /**
   * Return the call content as a map of key/value pairs.
   *
   * @return call content as a map of key/value pairs
   */
  Map<String, Object> getContent();
}

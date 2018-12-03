package com.smatei.salt.utils;

import java.util.logging.Logger;

/**
 * Runtime utilities methods.
 *
 * @author smatei
 *
 */
public class RuntimeUtils
{
  private static final Logger logger = Logger.getLogger(RuntimeUtils.class.getName());

  public static String GetSystemProperty(String key, boolean optional)
  {
    String propertyValue = System.getProperty(key);

    if (!optional && propertyValue == null)
    {
      logger.severe("System property " + key + " is missing");
      System.exit(0);
    }

    return propertyValue;
  }

  /**
   * Extract the module and function name from the function string based on '.' .
   * In case of e.g. "test.ping", this method will return String array {'test','ping'}
   *
   * @param function
   *          string containing module and function name (e.g. "test.ping")
   * @return String array containing module name as 1st element and function name as 2nd
   * @throws IllegalArgumentException
   *           if a given function string does not contain a '.'
   */
  public static String[] splitFunction(final String function)
  {
    if (!function.contains("."))
    {
      throw new IllegalArgumentException(function);
    }
    return function.split("\\.");
  }
}

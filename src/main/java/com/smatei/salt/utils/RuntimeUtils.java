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
}

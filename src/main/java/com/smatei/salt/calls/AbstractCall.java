package com.smatei.salt.calls;

import com.smatei.salt.utils.RuntimeUtils;

/**
 * Abstract class for all function calls in salt.
 *
 * @param <R>
 *          the return type of the called function
 */
public abstract class AbstractCall implements ICall
{

  private final String moduleName;
  private final String functionName;
  private final String function;

  /**
   * Default constructor.
   *
   * @param function
   *          string containing module and function name (e.g. "test.ping")
   */
  AbstractCall(String function)
  {
    this.function = function;
    String[] splitFunction = RuntimeUtils.splitFunction(function);
    this.moduleName = splitFunction[0];
    this.functionName = splitFunction[1];
  }

  /**
   * Return the function string containing module and function name (e.g. "test.ping").
   *
   * @return function string containing module and function name (e.g. "test.ping")
   */
  String getFunction()
  {
    return function;
  }

  /**
   * Return the module name.
   *
   * @return moduleName the module name
   */
  public String getModuleName()
  {
    return moduleName;
  }

  /**
   * Return just the function name (e.g. "ping" ).
   *
   * @return just the function name (e.g. "ping").
   */
  public String getFunctionName()
  {
    return functionName;
  }

}

package com.smatei.salt.target;

/**
 * Possible target types for the salt API client
 *
 * @see <a
 *      href="https://docs.saltstack.com/en/latest/topics/targeting/compound.html">https://docs.saltstack.com/en/latest/topics/targeting/compound.html</a>
 *
 * @author smatei
 */
public enum TargetType
{

  GLOB("glob"), //
  NODEGROUP("nodegroup"); //

  private final String value;

  TargetType(String value)
  {
    this.value = value;
  }

  public String getValue()
  {
    return value;
  }
}
package com.smatei.salt.calls;

/**
 * Possible values for the client parameter in salt API calls.
 *
 * @see <a href="https://docs.saltstack.com/en/latest/ref/netapi/all/salt.netapi.rest_cherrypy.html">
 *      https://docs.saltstack.com/en/latest/ref/netapi/all/salt.netapi.rest_cherrypy.html</a>
 *
 * @author smatei
 *
 */
public enum Client
{
  LOCAL("local"), //
  LOCAL_ASYNC("local_async"), //
  LOCAL_BATCH("local_batch"), //
  RUNNER("runner"), //
  RUNNER_ASYNC("runner_async"), //
  SSH("ssh"), //
  WHEEL("wheel"), //
  WHEEL_ASYNC("wheel_async");

  private final String value;

  Client(String value)
  {
    this.value = value;
  }

  public String getValue()
  {
    return value;
  }
}

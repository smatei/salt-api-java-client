package com.smatei.salt.target;

/**
 * Glob matching
 *
 * @see <a
 *      href="https://docs.saltstack.com/en/latest/topics/targeting/globbing.htm">https://docs.saltstack.com/en/latest/topics/targeting/globbing.htm</a>
 *
 * @author smatei
 *
 */
public class Glob extends AbstractTarget<String> implements ITarget<String>
{
  public static final Glob ALL = new Glob("*");

  /**
   * Creates a glob matcher target
   */
  public Glob()
  {
    super(TargetType.GLOB, "*");
  }

  /**
   * Creates a glob matcher target
   *
   * @param glob
   *          Glob expression
   */
  public Glob(String glob)
  {
    super(TargetType.GLOB, glob);
  }
}

package com.smatei.salt.target;

import java.util.HashMap;
import java.util.Map;

/**
 * Base class for all target types
 *
 * @param <T>
 *          The data type of the target class
 *
 * @author smatei
 */
abstract class AbstractTarget<T>
{

  protected final T target;
  protected final TargetType type;

  protected AbstractTarget(TargetType type, T target)
  {
    this.target = target;
    this.type = type;
  }

  public T getTarget()
  {
    return target;
  }

  public TargetType getType()
  {
    return type;
  }

  /**
   * @return a map of items to include in the API call payload
   */
  public Map<String, Object> getProps()
  {
    Map<String, Object> props = new HashMap<>();
    props.put("tgt", getTarget());
    props.put("expr_form", getType().getValue());
    return props;
  }
}

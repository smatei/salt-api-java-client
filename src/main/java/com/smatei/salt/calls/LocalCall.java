package com.smatei.salt.calls;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Class representing a function call of a salt execution module.
 *
 */
public class LocalCall extends AbstractCall
{

  private final Optional<List<?>> arg;
  private final Optional<Map<String, ?>> kwarg;
  private final Optional<?> metadata;
  private final Optional<Integer> timeout;
  private final Optional<Integer> gatherJobTimeout;

  public LocalCall(String functionName, Optional<List<?>> arg,
      Optional<Map<String, ?>> kwarg)
  {
    this(functionName, arg, kwarg, Optional.empty(), Optional.empty(), Optional.empty());
  }

  public LocalCall(String functionName, Optional<List<?>> arg,
      Optional<Map<String, ?>> kwarg,
      Optional<?> metadata, Optional<Integer> timeout,
      Optional<Integer> gatherJobTimeout)
  {
    super(functionName);
    this.arg = arg;
    this.kwarg = kwarg;
    this.metadata = metadata;
    this.timeout = timeout;
    this.gatherJobTimeout = gatherJobTimeout;
  }

  @Override
  public Map<String, Object> getContent()
  {
    HashMap<String, Object> payload = new HashMap<>();
    payload.put("fun", getFunction());
    arg.ifPresent(arg -> payload.put("arg", arg));
    kwarg.ifPresent(kwarg -> payload.put("kwarg", kwarg));
    metadata.ifPresent(m -> payload.put("metadata", m));
    timeout.ifPresent(timeout -> payload.put("timeout", timeout));
    gatherJobTimeout.ifPresent(gatherJobTimeout -> payload.put("gather_job_timeout",
        gatherJobTimeout));
    return payload;
  }
}

package com.smatei.salt.modules;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Optional;

import com.smatei.salt.calls.LocalCall;

/**
 * salt.modules.test
 */
public class Test
{

  public static LocalCall ping()
  {
    return new LocalCall("test.ping", Optional.empty(), Optional.empty());
  }

  public static LocalCall versionsInformation()
  {
    return new LocalCall("test.versions_information", Optional.empty(),
        Optional.empty());
  }

  public static LocalCall moduleReport()
  {
    return new LocalCall("test.module_report", Optional.empty(), Optional.empty());
  }

  public static LocalCall providers()
  {
    return new LocalCall("test.providers", Optional.empty(), Optional.empty());
  }

  public static LocalCall provider(String module)
  {
    LinkedHashMap<String, Object> args = new LinkedHashMap<>();
    args.put("module", module);
    return new LocalCall("test.provider", Optional.empty(), Optional.of(args));
  }

  public static LocalCall randStr(Optional<Integer> size,
      Optional<HashType> hashType)
  {
    LinkedHashMap<String, Object> args = new LinkedHashMap<>();
    size.ifPresent(sz -> args.put("size", sz));
    hashType.ifPresent(ht -> args.put("hash_type", ht.getHashType()));
    return new LocalCall("test.rand_str", Optional.empty(),
        Optional.of(args));
  }

  public static LocalCall exception(String message)
  {
    LinkedHashMap<String, Object> args = new LinkedHashMap<>();
    args.put("message", message);
    return new LocalCall("test.exception", Optional.empty(), Optional.of(args));
  }

  public static LocalCall sleep(Duration duration)
  {
    LinkedHashMap<String, Object> args = new LinkedHashMap<>();
    args.put("length", duration.getSeconds());
    return new LocalCall("test.sleep", Optional.empty(), Optional.of(args));
  }

  public static LocalCall echo(String text)
  {
    LinkedHashMap<String, Object> args = new LinkedHashMap<>();
    args.put("text", text);
    return new LocalCall("test.echo", Optional.empty(), Optional.of(args));
  }

  public static LocalCall missingFunc()
  {
    return new LocalCall("test.missing_func", Optional.empty(), Optional.empty());
  }

}

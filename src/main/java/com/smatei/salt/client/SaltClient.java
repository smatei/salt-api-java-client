package com.smatei.salt.client;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.smatei.salt.calls.Client;
import com.smatei.salt.calls.ICall;
import com.smatei.salt.calls.LocalCall;
import com.smatei.salt.modules.Test;
import com.smatei.salt.net.ConnectionFactory;
import com.smatei.salt.net.Proxy;
import com.smatei.salt.target.Glob;
import com.smatei.salt.target.ITarget;
import com.smatei.salt.utils.RuntimeUtils;

/**
 *
 * Salt API client.
 *
 * @author smatei
 *
 */
public class SaltClient
{
  private static final Logger logger = Logger.getLogger(SaltClient.class.getName());
  private final ClientConfig config;
  private final Gson gson = new GsonBuilder().create();
  private final ConnectionFactory connectionFactory;

  /**
   * @param config
   *          salt API client configuration
   */
  public SaltClient(ClientConfig config)
  {
    this.config = config;
    connectionFactory = new ConnectionFactory();
  }

  public static void main(String[] args) throws Exception
  {
    String apiurl = RuntimeUtils.GetSystemProperty("apiurl", false);
    String apiuser = RuntimeUtils.GetSystemProperty("apiuser", false);
    String apipassword = RuntimeUtils.GetSystemProperty("apipassword", false);
    ClientConfig config = new ClientConfig(new URI(apiurl), apiuser, apipassword, AuthModule.PAM);

    String proxyHost = RuntimeUtils.GetSystemProperty("proxyhost", true);
    String proxyPort = RuntimeUtils.GetSystemProperty("proxyport", true);
    String proxyUser = RuntimeUtils.GetSystemProperty("proxuser", true);
    String proxyPassword = RuntimeUtils.GetSystemProperty("proxypassword", true);
    if (proxyHost != null)
    {
      Proxy proxy = new Proxy(proxyHost, Integer.parseInt(proxyPort), proxyUser, proxyPassword);
      config.SetProxy(proxy);
    }

    SaltClient client = new SaltClient(config);

    logger.info(client.login().toString());
    LocalCall testPing = Test.ping();
    logger.info(client.run(Glob.ALL, Client.LOCAL, testPing));
  }

  public String run(ITarget<?> target, Client client, ICall call)
  {
    Map<String, Object> props = new HashMap<>();
    props.put("username", config.getUsername());
    props.put("password", config.getPassword());
    props.put("eauth", config.getAuthModule().getValue());
    props.put("client", client.getValue());
    props.putAll(target.getProps());
    props.putAll(call.getContent());

    String requestParams = gson.toJson(props);

    return connectionFactory.create("/run", config).request(requestParams);
  }

  /**
   * Perform login and return the token.
   * <p>
   * {@code POST /login}
   *
   * @return JSON with the authentication token
   */
  public String login()
  {
    JsonObject requestParams = new JsonObject();
    requestParams.addProperty("username", config.getUsername());
    requestParams.addProperty("password", config.getPassword());
    requestParams.addProperty("eauth", config.getAuthModule().getValue());

    return connectionFactory.create("/login", config).request(requestParams.toString());
  }
}

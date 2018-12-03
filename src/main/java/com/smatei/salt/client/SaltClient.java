package com.smatei.salt.client;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.smatei.salt.calls.Client;
import com.smatei.salt.calls.ICall;
import com.smatei.salt.calls.LocalCall;
import com.smatei.salt.modules.Test;
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
  private RestTemplate restTemplate;
  private final Gson gson = new GsonBuilder().create();

  /**
   * @param config
   *          salt API client configuration
   */
  public SaltClient(ClientConfig config)
  {
    this.config = config;
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

    logger.info(client.login());
    LocalCall testPing = Test.ping();
    logger.info(client.run(Glob.ALL, Client.LOCAL, testPing));
  }

  public String run(ITarget<?> target, Client client, ICall call)
  {
    RestTemplate template = createRestTemplate(config.getProxy());

    Map<String, Object> props = new HashMap<>();
    props.put("username", config.getUsername());
    props.put("password", config.getPassword());
    props.put("eauth", config.getAuthModule().getValue());
    props.put("client", client.getValue());
    props.putAll(target.getProps());
    props.putAll(call.getContent());

    String requestParams = gson.toJson(props);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<String> entity = new HttpEntity<String>(requestParams, headers);
    String answer = template.postForObject(config.getUrl() + "/run", entity, String.class);
    return answer;
  }

  public String login()
  {
    RestTemplate template = createRestTemplate(config.getProxy());

    JsonObject requestParams = new JsonObject();
    requestParams.addProperty("username", config.getUsername());
    requestParams.addProperty("password", config.getPassword());
    requestParams.addProperty("eauth", config.getAuthModule().getValue());

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<String> entity = new HttpEntity<String>(requestParams.toString(), headers);
    String answer = template.postForObject(config.getUrl() + "/login", entity, String.class);
    return answer;
  }

  private RestTemplate createRestTemplate(Proxy proxy)
  {
    if (restTemplate != null)
    {
      return restTemplate;
    }

    HttpClientBuilder clientBuilder = HttpClientBuilder.create();

    if (proxy != null)
    {
      CredentialsProvider credsProvider = new BasicCredentialsProvider();
      credsProvider.setCredentials(
          new AuthScope(proxy.getServer(), proxy.getPort()),
          new UsernamePasswordCredentials(proxy.getUser(), proxy.getPassword()));

      HttpHost myProxy = new HttpHost(proxy.getServer(), proxy.getPort());
      clientBuilder.setProxy(myProxy).setDefaultCredentialsProvider(credsProvider);
    }

    HttpClient httpClient = clientBuilder.disableCookieManagement().build();
    HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
    factory.setHttpClient(httpClient);

    restTemplate = new RestTemplate(factory);
    return restTemplate;
  }
}

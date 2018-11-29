package com.smatei.salt.client;

import java.net.URI;
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

import com.google.gson.JsonObject;
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
    ClientConfig config = new ClientConfig(new URI(apiurl), apiuser, apipassword);

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

    logger.info(client.Call(Glob.ALL, "sys.list_modules", null));
    logger.info(client.Call(Glob.ALL, "sys.argspec", "test"));
  }

  public String Call(ITarget<?> target, String command, String arg)
  {
    RestTemplate template = createRestTemplate(config.getProxy());

    JsonObject requestParams = new JsonObject();
    requestParams.addProperty("username", config.getUsername());
    requestParams.addProperty("password", config.getPassword());
    requestParams.addProperty("eauth", "pam");
    requestParams.addProperty("client", "local");
    target.getProps().forEach((key, value) ->
    {
      requestParams.addProperty(key, value.toString());
    });
    requestParams.addProperty("fun", command);
    if (arg != null)
    {
      requestParams.addProperty("arg", arg);
    }

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<String> entity = new HttpEntity<String>(requestParams.toString(), headers);
    String answer = template.postForObject(config.getUrl() + "/run", entity, String.class);
    return answer;
  }

  private RestTemplate createRestTemplate()
  {
    return createRestTemplate(null);
  }

  private RestTemplate createRestTemplate(Proxy proxy)
  {
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

    HttpClient httpClient = clientBuilder.build();
    HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
    factory.setHttpClient(httpClient);

    return new RestTemplate(factory);
  }
}

package com.smatei.salt.net;

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

import com.smatei.salt.client.ClientConfig;

public class RestTemplateConnection<T> implements IConnection<T>
{
  private ClientConfig config;
  private String apiEndpoint;
  private Class<T> returnClass;

  public RestTemplateConnection(String apiEndpoint, ClientConfig config, Class<T> returnClass)
  {
    this.config = config;
    this.apiEndpoint = apiEndpoint;
    this.returnClass = returnClass;
  }

  @Override
  public T request()
  {
    return request(null);
  }

  @SuppressWarnings("unchecked")
  @Override
  public T request(String params)
  {
    RestTemplate template = initConnection();

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<String> entity = new HttpEntity<String>(params, headers);
    String response = template.postForObject(config.getUrl() + this.apiEndpoint, entity, String.class);

    // if return type is String.class, then return the response
    if (IsRetunTypeString())
    {
      return (T) response;
    }

    return parseResponse(response);
  }

  private T parseResponse(String response)
  {
    // todo: write parser

    return null;
  }

  private RestTemplate initConnection()
  {
    HttpClientBuilder clientBuilder = HttpClientBuilder.create();

    Proxy proxy = config.getProxy();
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

    return new RestTemplate(factory);
  }

  private boolean IsRetunTypeString()
  {
    return "java.lang.String".equals(returnClass.getName());
  }
}

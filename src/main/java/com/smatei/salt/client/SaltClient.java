package com.smatei.salt.client;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.google.gson.JsonObject;

public class SaltClient
{

  public static void main(String[] args)
  {
    // Test spring RestTemplate with salt-api
    // curl -sk http://localhost:8000/run -H 'Accept: application/json'
    // -d username='saltuser' -d password='saltpassword' -d eauth='pam' -d client='local' -d tgt='*' -d fun='test.ping'

    String url = "http://localhost:8000/run";
    RestTemplate template = new RestTemplate();

    JsonObject requestParams = new JsonObject();
    requestParams.addProperty("username", "saltuser");
    requestParams.addProperty("password", "saltpassword");
    requestParams.addProperty("eauth", "pam");
    requestParams.addProperty("client", "local");
    requestParams.addProperty("tgt", "*");
    requestParams.addProperty("fun", "test.ping");

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<String> entity = new HttpEntity<String>(requestParams.toString(), headers);
    String answer = template.postForObject(url, entity, String.class);
    System.out.println(answer);
  }
}

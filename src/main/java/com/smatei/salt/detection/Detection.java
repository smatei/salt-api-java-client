package com.smatei.salt.detection;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.smatei.salt.client.AuthModule;
import com.smatei.salt.client.Client;
import com.smatei.salt.client.ClientConfig;
import com.smatei.salt.client.SaltClient;
import com.smatei.salt.target.Glob;
import com.smatei.salt.utils.RuntimeUtils;

/**
 * Use the saltstack docker setup to detect modules and arguments for each module item. Save them in json files somewhere in resources so that
 * we can define classes for each module.
 *
 * TODO: Find a solution to automatically run this with maven at library build time if possible.
 *
 * @see <a
 *      href="https://github.com/smatei/salt-api-java-client/tree/master/docker">https://github.com/smatei/salt-api-java-client/tree/master/docker</a>
 *
 * @author smatei
 *
 */
public class Detection
{
  private static final Logger logger = Logger.getLogger(Detection.class.getName());
  private final SaltClient client;

  public Detection(SaltClient client)
  {
    this.client = client;
  }

  public void DetectModules()
  {
    String jsonResponse = client.run(Glob.ALL, Client.LOCAL, "sys.list_modules", null);

    // {"return": [{"minion": ["aliases", "alternatives", ..., "test", "timezone", "tls", "travisci", "uptime", "user", ...]}]}
    JsonArray modules = ParseMinionResponse(jsonResponse).getAsJsonArray();

    modules.forEach(module ->
    {
      logger.info("processing module " + module.getAsString());

      String moduleJsonResponse = client.run(Glob.ALL, Client.LOCAL, "sys.argspec", "test");
      JsonObject moduleJson = ParseMinionResponse(moduleJsonResponse).getAsJsonObject();

      logger.info(moduleJson.toString());
      try
      {
        SaveModuleToFile(module.getAsString(), moduleJson);
      }
      catch (IOException e)
      {
        logger.log(Level.SEVERE, "cannot save module json  file", e);
      }
    });
  }

  public static void main(String[] args) throws URISyntaxException
  {
    String apiurl = RuntimeUtils.GetSystemProperty("apiurl", false);
    String apiuser = RuntimeUtils.GetSystemProperty("apiuser", false);
    String apipassword = RuntimeUtils.GetSystemProperty("apipassword", false);
    ClientConfig config = new ClientConfig(new URI(apiurl), apiuser, apipassword, AuthModule.PAM);

    SaltClient client = new SaltClient(config);
    Detection detection = new Detection(client);

    detection.DetectModules();
  }

  /**
   * parse the command reponse
   *
   * {"return": [{"minion": ["aliases", "alternatives", ..., "test", "timezone", "tls", "travisci", "uptime", "user", ...]}]}
   *
   * and return the content of the minion
   *
   * @param jsonResponse
   * @return
   */
  private JsonElement ParseMinionResponse(String jsonResponse)
  {
    JsonParser parser = new JsonParser();
    JsonObject modulesResponse = parser.parse(jsonResponse).getAsJsonObject();
    JsonObject minionResponse = modulesResponse.get("return").getAsJsonArray().get(0).getAsJsonObject();
    return minionResponse.get("minion");
  }

  private String PrettyPrintJson(JsonObject object)
  {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    String json = gson.toJson(object);
    return json;
  }

  private void SaveModuleToFile(String moduleName, JsonObject moduleJson) throws IOException
  {
    String prettyJson = PrettyPrintJson(moduleJson);
    FileUtils.write(new File("src/main/resources/modules", moduleName + ".json"), prettyJson, Charset.forName("utf-8"));
  }
}

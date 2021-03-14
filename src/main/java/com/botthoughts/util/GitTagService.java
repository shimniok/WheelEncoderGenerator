/*
 * Copyright 2021 mes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.botthoughts.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import javax.crypto.KeyAgreement;
import javax.net.ssl.SSLContext;
import org.json.JSONObject;
import org.json.JSONArray;

/**
 * Service provider for Git Repository Tags API
 * @author mes
 */
public class GitTagService {
  final URL url;
  
  /**
   * Create new GitTagService
   * @param username is the username owning the repository 
   * @param repo is the name of the repository
   * @throws MalformedURLException 
   */
  public GitTagService(String username, String repo) throws MalformedURLException {
    this.url = new URL(String.format("https://api.github.com/repos/%s/%s/tags", 
        GitTagService.sanitize(username), GitTagService.sanitize(repo)));
//    System.out.println("url: "+this.url.toString());
  }
  
  /**
   * Sanitizes string using an whitelist of allowed characters: alphanumeric, "-" and "_".
   * @param s
   * @return s with illegal characters removed
   */
  private static String sanitize(String s) {
    return s.replaceAll("[^a-zA-Z0-9_-]", "");
  }
  
  /**
   * Return a list of tags from Git API for the specified repository
   * @return list of tag names
   * @throws IOException 
   */
  public ArrayList<String> getNames() throws IOException {
    ArrayList<String> list = new ArrayList();
   
    try {
      KeyAgreement.getInstance("X25519");
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");
      conn.setRequestProperty("Accept", "application/json");

//      SSLContext.setDefault(SSLContext.getInstance("TLSv1.3"));
//
//      System.out.println(SSLContext.getDefault().getProtocol());
//      
      String[] protocols = SSLContext.getDefault().getSupportedSSLParameters().getProtocols();
//      for (int i = 0; i < protocols.length; i++) {
//        System.out.println(protocols[i]);
//      }

      if (conn.getResponseCode() != 200) {
        throw new RuntimeException("Failed : HTTP error code : "
            + conn.getResponseCode());
      }

      JSONArray ja = new JSONArray(new String(conn.getInputStream().readAllBytes()));

      conn.disconnect();
      
      for (int i=0; i < ja.length(); i++) {
        JSONObject jo = ja.getJSONObject(i);
        list.add(jo.getString("name"));
      }
    } catch (javax.net.ssl.SSLHandshakeException ex) {
      System.out.println("getNames(): SSLHandshakeException: " + ex);
    } catch (NoSuchAlgorithmException ex) {
      System.out.println("getNames(): NoSuchAlgorithmException: " + ex);
    }

    return list;
  }
  
}

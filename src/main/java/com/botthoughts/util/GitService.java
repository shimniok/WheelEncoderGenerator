/*
 * Copyright 2021 Michael Shimniok.
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
import java.net.ProtocolException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import javax.crypto.KeyAgreement;
import org.json.JSONArray;

/**
 *
 * @author Michael Shimniok
 */
abstract public class GitService {
  final URL url;

  /**
   * Create new GitService
   *
   * @param username is the username owning the repository
   * @param repo is the name of the repository
   * @param path is the path for the service
   * @throws MalformedURLException
   */
  public GitService(String username, String repo, String path) throws MalformedURLException {
    this.url = new URL(String.format("https://api.github.com/repos/%s/%s/%s",
        GitService.sanitize(username), GitService.sanitize(repo), GitService.sanitize(path)));
  }

  /**
   * Sanitizes string using an whitelist of allowed characters: alphanumeric, "-" and "_".
   *
   * @param s
   * @return s with illegal characters removed
   */
  public static String sanitize(String s) {
    return s.replaceAll("[^a-zA-Z0-9_-]", "");
  }

  protected JSONArray get() throws NoSuchAlgorithmException, ProtocolException, IOException {
    KeyAgreement.getInstance("X25519");
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestMethod("GET");
    conn.setRequestProperty("Accept", "application/json");

    if (conn.getResponseCode() != 200) {
      throw new RuntimeException("Failed : HTTP error code : "
          + conn.getResponseCode());
    }
    
    return new JSONArray(new String(conn.getInputStream().readAllBytes()));
  }
  
//  protected int post(JSONArray data) throws NoSuchAlgorithmException, ProtocolException, IOException {
//    KeyAgreement.getInstance("X25519");
//    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//    conn.setRequestMethod("POST");
//    conn.setRequestProperty("Accept", "application/json");
//
//    if (conn.getResponseCode() != 200) {
//      throw new RuntimeException("Failed : HTTP error code : "
//          + conn.getResponseCode());
//    }
//
//    return conn.getResponseCode();
//  }

}

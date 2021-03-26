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
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import org.json.JSONObject;
import org.json.JSONArray;

/**
 * Service provider for Git Repository Tags API
 * @author mes
 */
public class GitTagService extends GitService {
  
  public GitTagService(String username, String repo) throws MalformedURLException {
    super(username, repo, "tags");
  }
  
  /**
   * Return a list of tags from Git API for the specified repository
   * @return list of tag names
   * @throws IOException 
   */
  public ArrayList<String> getNames() throws IOException {
    ArrayList<String> list = new ArrayList();
   
    try {
      JSONArray data = this.get();
      
      for (int i=0; i < data.length(); i++) {
        JSONObject jo = data.getJSONObject(i);
        list.add(jo.getString("name"));
//        System.out.println(jo.getString("name"));
      }
    } catch (javax.net.ssl.SSLHandshakeException ex) {
      System.out.println("getNames(): SSLHandshakeException: " + ex);
    } catch (NoSuchAlgorithmException ex) {
      System.out.println("getNames(): NoSuchAlgorithmException: " + ex);
    }

    return list;
  }
  
}

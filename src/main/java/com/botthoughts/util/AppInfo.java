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

import com.botthoughts.wheelencodergenerator.App;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Container for application info, read from /app.properties file which needs the following 
 * properties defined.
 * 
 * name: name of the application
 * abbr: abbreviated name of the application
 * description: description of application
 * version: the version of the application
 * platform: the os family the build targets
 * build.date: date of the build
 * author: author of the application
 * 
 * @author mes
 */
public class AppInfo {
  private final String version;
  private final String author;
  private final String name;
  private final String abbr;
  private final String platform;
  private final String buildDate;
  private final String description;

  /**
   * Create a new AppInfo object, loading info from properties file and populating fields.
   * @throws java.io.IOException
   */
  public AppInfo() throws IOException {
    Properties properties = new Properties();
    System.out.println("MyInfo:");

    InputStream stream = App.class.getResourceAsStream("/app.properties");
    properties.load(stream);
    this.version = properties.getProperty("version");
    System.out.println("\tversion="+version);
    this.author = properties.getProperty("author");
    System.out.println("\tauthor="+author);
    this.name = properties.getProperty("name");
    System.out.println("\tname="+name);
    this.abbr = properties.getProperty("abbr");
    System.out.println("\tname="+abbr);
    this.platform = properties.getProperty("platform");
    System.out.println("\tplatform="+platform);
    this.buildDate = properties.getProperty("build.date");
    System.out.println("\tbuild date="+buildDate);
    this.description = properties.getProperty("description");
    System.out.println("\tdescription="+description);
  }
  
  /**
   * Get app version as a String
   * @return version
   */
  public String getVersion() {
    return version;
  }

  /**
   * Get app author as a String
   * @return author
   */
  public String getAuthor() {
    return author;
  }

  /**
   * Return application name as a String
   * @return app name
   */
  public String getName() {
    return name;
  }

  /**
   * Return abbreviated app name as String
   * @return abbreviated app name
   */
  public String getAbbr() {
    return abbr;
  }

  /**
   * Return target platform as String
   * @return platform
   */
  public String getPlatform() {
    return platform;
  }

  /**
   * Return build date as String
   * @return build date
   */
  public String getBuildDate() {
    return buildDate;
  }

  /**
   * Return app description as String
   * @return description
   */
  public String getDescription() {
    return description;
  }
  
}

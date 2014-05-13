/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2014, Graz University of Technology - KTI (Knowledge Technologies Institute).
* For a list of contributors see the AUTHORS file at the top-level directory of this distribution.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package at.kc.tugraz.socialserver.utils;

public class SSSystemU{

  public static final String serverNameLocalhost            = "localhost";
  public static final String serverNameKeDemo               = "kedemo.know-center.tugraz.at";
  public static final String fileNameSSAdapterRestConf      = "ss-adapter-rest-conf.yaml";
  public static final String fileNameSSAdapterWebSocketConf = "ss-adapter-websocket-conf.yaml";
  public static final String fileNameSSConf                 = "ss-conf.yaml";
  public static final String fileNameSSJar                  = "ss.jar";
  public static final String fileNameLog4JProperties        = "log4j.properties";
  public static final String fileNameRunitSh                = "runit.sh";
  public static final String fileNameRunitBat               = "runit.bat";
  public static final String dirNameLib                     = "lib/";
  public static final String dirNameConf                    = "conf/";
  
  public static Boolean isWindows(){
    return System.getProperty("os.name").toLowerCase().contains("windows");
  }
}

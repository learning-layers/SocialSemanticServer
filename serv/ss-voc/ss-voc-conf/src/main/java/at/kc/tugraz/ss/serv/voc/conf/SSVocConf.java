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
package at.kc.tugraz.ss.serv.voc.conf;

import at.kc.tugraz.ss.serv.serv.api.SSCoreServConfA;

public class SSVocConf extends SSCoreServConfA{

  public static final String sssUri                         = "http://social.semantic.server.eu";
  public static final String systemUserLabel                = "system";
  public static final String systemEmailPostFix             = "know-center.at";
  public static final String systemUserEmail                = systemUserLabel + "@" + systemEmailPostFix;
  public static final String serverNameLocalhost            = "localhost";
  public static final String fileNameSSAdapterRestConf      = "ss-adapter-rest-conf.yaml";
  public static final String fileNameSSAdapterWebSocketConf = "ss-adapter-websocket-conf.yaml";
  public static final String fileNameSSConf                 = "ss-conf.yaml";
  public static final String fileNameSSJar                  = "ss.jar";
  public static final String fileNameLog4JProperties        = "log4j.properties";
  public static final String fileNameRunitSh                = "runit.sh";
  public static final String fileNameRunitBat               = "runit.bat";
  public static final String dirNameLib                     = "lib/";
  public static final String dirNameConf                    = "conf/";
  
  public String   uriPrefix      = null;
 
  public static SSVocConf copy(final SSVocConf orig){
    
    final SSVocConf copy = (SSVocConf) SSCoreServConfA.copy(orig, new SSVocConf());
    
    copy.uriPrefix  =        orig.uriPrefix;
    
    return copy;
  }
}

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
package at.tugraz.sss.conf;

import at.tugraz.sss.serv.conf.SSCoreServConfA;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.util.SSFileU;
import at.tugraz.sss.serv.util.*;

public class SSConf extends SSCoreServConfA{
  
  public static final String sssUri                         = "http://sss.eu/";
  public static final String systemUserLabel                = "system";
  public static final String systemEmailPostFix             = "know-center.at";
  public static final String systemUserEmail                = systemUserLabel + "@" + systemEmailPostFix;
  public static final String serverNameLocalhost            = "localhost";
  public static final String fileNameSSAdapterWebSocketConf = "sss.adapter.websocket.conf.yaml";
  public static final String fileNameSSSConf                = "sss.conf.yaml";
  public static final String fileNameSSSJar                 = "sss.jar";
  public static final String fileNameLog4JProperties        = "log4j.properties";
  public static final String fileNameRunitSh                = "runit.sh";
  public static final String fileNameRunitBat               = "runit.bat";
  public static final String dirNameLib                     = "lib/";
  public static final String dirNameConf                    = "conf/";
  public static final String restAPIResourceFile            = "files/files/";
  public static final String restAPIPathFileDownloadPublic  = "download/public/";
  
  public static SSUri systemUserUri = null;
  
  public  String    host           = null;
  public  Integer   port           = null;
  public  String    version        = null;
  public  String    restAPIPath    = null;
  
  public SSConf(){
    
    try{
      SSConf.systemUserUri = SSUri.get(vocURIPrefixGet() + systemUserLabel);
    }catch(SSErr error){
      SSLogU.err(error);
    }
  }
  
  public static SSConf copy(final SSConf orig){
    
    final SSConf copy = (SSConf) SSCoreServConfA.copy(orig, new SSConf());
    
    copy.host          = orig.host;
    copy.port          = orig.port;
    copy.version       = orig.version;
    copy.restAPIPath   = orig.restAPIPath;
    
    return copy;
  }
  
  private static SSUri vocURIPrefixGet() throws SSErr{
    return (SSUri) SSUri.get(sssUri);
  }
  
  public static SSUri vocURICreate() throws SSErr{
    return SSUri.get(vocURIPrefixGet() + SSIDU.uniqueID());
  }
  
  public static SSUri vocURICreate(final SSFileExtE fileExt) throws SSErr{
    return SSUri.get(vocURIPrefixGet() + SSIDU.uniqueID() + SSStrU.dot + fileExt.toString());
  }
  
  public static String fileIDFromSSSURI(final SSUri uri){
    
    String result = SSStrU.removeTrailingSlash(uri);
    
    if(result == null){
      return null;
    }
    
    return result.substring(result.lastIndexOf(SSStrU.slash) + 1);
  }
  
  public static SSUri vocURICreateFromId(final String id) throws Exception{
    return SSUri.get(vocURIPrefixGet() + id);
  }
  
  public void setLocalWorkPath(final String value){
    
    localWorkPath = SSFileU.correctDirPath (value);
    
    if(SSStrU.isEmpty(localWorkPath)){
      localWorkPath = SSFileU.dirWorkingTmp();
    }
  }
}
/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2014, Graz University of Technology - KTI (Knowledge Technologies Institute).
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

/**
 * @author Dieter Theiler
 */

package at.tugraz.sss.servs.conf;

import at.tugraz.sss.servs.util.SSIDU;
import at.tugraz.sss.servs.util.SSFileU;
import at.tugraz.sss.servs.util.SSFileExtE;
import at.tugraz.sss.servs.util.SSStrU;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.conf.SSCoreServConfA;

public class SSConf extends SSCoreServConfA{
  
  public static final String sssUri                         = "http://sss.eu/";
  public static final String systemUserLabel                = "system";
  public static final SSUri  systemUserUri                  = SSUri.getUnchecked(sssUri + systemUserLabel);
  public static final String systemEmailPostFix             = "know-center.at";
  public static final String systemUserEmail                = systemUserLabel + SSStrU.at + systemEmailPostFix;
  public static final String serverNameLocalhost            = "localhost";
  public static final String restAPIResourceFile            = "files/";
  public static final String restAPIPathFileDownloadPublic  = "download/public/";
  
  public static String    host           = null;
  public static Integer   port           = null;
  public static String    version        = null;
  public static String    restAPIPath    = null;

  public void setHost(final String host) {
    SSConf.host = host;
  }

  public void setPort(final Integer port) {
    SSConf.port = port;
  }

  public void setVersion(final String version) {
    SSConf.version = version;
  }

  public void setRestAPIPath(final String restAPIPath) {
    SSConf.restAPIPath = restAPIPath;
  }
  
  protected static String       sssWorkDir                 = null;
  protected static String       sssWorkDirTmp              = null;
  protected static String       sssWorkDirDataCsv          = null;
  protected static String       localWorkPath              = null;
  
  public void setSssWorkDir(final String value){
    
    sssWorkDir = SSFileU.correctDirPath (value);
    
    if(SSStrU.isEmpty(sssWorkDir)){
      sssWorkDir = SSFileU.dirWorking();
    }
    
    sssWorkDirTmp     = sssWorkDir + SSFileU.dirNameTmp;
    sssWorkDirDataCsv = sssWorkDir + SSFileU.dirNameDataCsv;
    localWorkPath     = sssWorkDirTmp;
  }
  
  public static String getSssWorkDir(){
    return sssWorkDir;
  }
  
  public static String getSssWorkDirTmp(){
    return sssWorkDirTmp;
  }
  
  public static String getSssWorkDirDataCsv(){
    return sssWorkDirDataCsv;
  }
  
  public static String getLocalWorkPath(){
    return localWorkPath;
  }
  
  public static SSConf copy(final SSConf orig){
    return (SSConf) SSCoreServConfA.copy(orig, new SSConf());
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
  
  public static SSUri vocURICreateFromId(final String id) throws SSErr{
    return SSUri.get(vocURIPrefixGet() + id);
  }
}
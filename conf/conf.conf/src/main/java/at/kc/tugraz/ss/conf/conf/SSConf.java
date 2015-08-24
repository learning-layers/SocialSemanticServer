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
package at.kc.tugraz.ss.conf.conf;

import at.tugraz.sss.serv.SSFileU;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSCoreServConfA;

public class SSConf extends SSCoreServConfA{
  
  public  String    host           = null;
  public  Integer   port           = null;
  private String    localWorkPath  = null;
  public  String    version        = null;
  public  String    restAPIHost    = null;
  
  public static SSConf copy(final SSConf orig){
    
    final SSConf copy = (SSConf) SSCoreServConfA.copy(orig, new SSConf());
    
    copy.host          = orig.host;
    copy.port          = orig.port;
    copy.localWorkPath = orig.localWorkPath;
    copy.version       = orig.version;
    copy.restAPIHost   = orig.restAPIHost;
    
    return copy;
  }
  
  public void setLocalWorkPath(final String value){
  
    localWorkPath = SSFileU.correctDirPath (value);
    
    if(SSStrU.isEmpty(this.localWorkPath)){
      this.localWorkPath = SSFileU.dirWorkingTmp();
    }
  }
  
  public String getLocalWorkPath(){
    return localWorkPath;
  }
}
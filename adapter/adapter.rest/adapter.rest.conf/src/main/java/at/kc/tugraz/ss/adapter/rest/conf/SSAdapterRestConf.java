 /**
  * Copyright 2013 Graz University of Technology - KTI (Knowledge Technologies Institute)
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
package at.kc.tugraz.ss.adapter.rest.conf;

import at.tugraz.sss.serv.conf.SSCoreConfA;
import at.tugraz.sss.conf.SSConf;

import at.kc.tugraz.ss.serv.jsonld.conf.SSJSONLDConf;
import at.tugraz.sss.serv.reg.SSServErrReg;

public class SSAdapterRestConf extends SSCoreConfA{
  
  private static SSAdapterRestConf inst;
  
  public SSConf       sss      = null;
  public SSJSONLDConf jsonLD  = null;

  public SSConf getSss(){
    return sss;
  }

  public static synchronized SSAdapterRestConf instSet(final String pathToFile) throws Exception{
    
    if(inst != null){
      return inst;
    }
    
    inst = (SSAdapterRestConf) SSCoreConfA.instSet(pathToFile, SSAdapterRestConf.class);
    
    return inst;
  }
  
  public static SSAdapterRestConf instGet() throws Exception{
    
    if(inst == null){
      SSServErrReg.regErrThrow(new Exception("conf not set"));
      return null;
    }
    
    return inst;
  }
  
  public void setSss(SSConf ss){
    this.sss = sss;
  }
  
  public SSJSONLDConf getJsonLD(){
    return jsonLD;
  }
  
  public void setJsonLD(SSJSONLDConf jsonLD){
    this.jsonLD = jsonLD;
  }
}

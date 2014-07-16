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
package at.kc.tugraz.ss.serv.dataimport.serv;

import at.kc.tugraz.socialserver.utils.SSLogU;
import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.ss.serv.dataimport.conf.SSDataImportConf;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.jobs.evernote.conf.SSEvernoteConf;
import at.kc.tugraz.ss.serv.jobs.evernote.serv.SSEvernoteServ;
import at.kc.tugraz.ss.serv.serv.api.SSConfA;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.serv.test.api.SSServInitA;
import at.kc.tugraz.ss.serv.voc.serv.SSVoc;

public class SSDataImportServInitializer extends SSServInitA{

  public SSDataImportServInitializer(final SSConfA dataImportConf){
    super(dataImportConf);
  }
  
  @Override
  protected void initServ() throws Exception{
    
    if(((SSDataImportConf)conf).initAtStartUpOps != null){
      
      for(SSMethU initAtStartUpOp : ((SSDataImportConf)conf).initAtStartUpOps){
        
        switch(initAtStartUpOp){
          
          case dataImportEvernote:{
            
            for(String authToken : ((SSEvernoteConf) SSEvernoteServ.inst.servConf).authTokens){
              
              try{
                
                SSLogU.info("start import for evernote account " + authToken);
                
                SSServCaller.dataImportEvernote(
                  SSVoc.systemUserUri,
                  authToken,
                  true);
                
                SSLogU.info("end import for evernote account " + authToken);
                
              }catch(Exception error){
                SSLogU.warn("import for evernote account " + authToken + " failed");
                SSServErrReg.reset();
              }
            }
          }
        }
      }
    }
  }
}
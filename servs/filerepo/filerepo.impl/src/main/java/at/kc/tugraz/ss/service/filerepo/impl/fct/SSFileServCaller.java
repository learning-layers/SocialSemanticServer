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
package at.kc.tugraz.ss.service.filerepo.impl.fct;

import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUpdatePar;
import at.tugraz.sss.serv.SSLogU;
import at.tugraz.sss.serv.SSMimeTypeE;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.kc.tugraz.ss.service.filerepo.datatypes.pars.SSFileUploadPar;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSFileExtE;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServReg;

public class SSFileServCaller{
  
  public static void addFileEntity(
    final SSFileUploadPar par,
    final SSUri           file) throws Exception{
    
    try{
      
      ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityUpdate(
          new SSEntityUpdatePar(
            null,
            null,
            par.user,
            file,
            SSEntityE.file, //type,
            par.label, //label
            null, //description,
            null, //entitiesToAttach,
            null, //creationTime,
            null, //read,
            false, //setPublic
            false, //withUserRestriction
            false)); //shouldCommit)
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public static void addFileContentsToSolr(
    final SSFileUploadPar par,
    final String          fileId) throws Exception{
    
    try{
      
      SSServCaller.solrAddDoc(
        par.user,
        fileId,
        par.mimeType,
        false);
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.notServerServiceForOpAvailable)){
        SSLogU.warn(SSErrE.notServerServiceForOpAvailable.toString());
        return;
      }
      
      SSLogU.warn(error.getMessage());
      
      SSServErrReg.reset();
    }
  }
  
  public static void replaceFileContentsInSolr(
    final SSUri            user, 
    final String           fileId,
    final Boolean          shouldCommit) throws Exception{
    
    try{
      
      SSServCaller.solrAddDoc(
        user,
        fileId,
        SSMimeTypeE.mimeTypeForFileExt(SSFileExtE.ext(fileId)), 
        shouldCommit);
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.notServerServiceForOpAvailable)){
        SSLogU.warn(SSErrE.notServerServiceForOpAvailable.toString());
        return;
      }
      
      SSLogU.warn(error.getMessage());
      
      SSServErrReg.reset();
    }
  }
}
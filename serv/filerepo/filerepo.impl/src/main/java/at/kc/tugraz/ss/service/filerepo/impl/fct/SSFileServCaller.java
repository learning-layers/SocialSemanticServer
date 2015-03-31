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

import at.kc.tugraz.socialserver.utils.SSLogU;
import at.kc.tugraz.socialserver.utils.SSMimeTypeE;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.service.filerepo.datatypes.pars.SSFileReplacePar;
import at.kc.tugraz.ss.service.filerepo.datatypes.pars.SSFileUploadPar;
import sss.serv.err.datatypes.SSErrE;

public class SSFileServCaller{
  
  public static void addFileEntity(
    final SSFileUploadPar par,
    final SSUri           file,
    final Boolean         shouldCommit) throws Exception{
    
    try{
      
      SSServCaller.entityEntityToPrivCircleAdd(
        par.user,
        file,
        SSEntityE.file,
        par.label,
        null, 
        null,
        shouldCommit);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public static void addFileContentsToSolr(
    final SSFileUploadPar par,
    final String          fileId,
    final Boolean         shouldCommit) throws Exception{
    
    try{
      
      SSServCaller.solrAddDoc(
        par.user,
        fileId,
        par.mimeType,
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
  
  public static void replaceFileContentsInSolr(
    final SSFileReplacePar par,
    final String           fileId,
    final Boolean          shouldCommit) throws Exception{
    
    try{
      
      SSServCaller.solrAddDoc(
        par.user,
        fileId,
        SSMimeTypeE.mimeTypeForFileExt(
          SSServCaller.fileExtGet(
            par.user,
            par.file)),
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
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
package at.kc.tugraz.ss.serv.dataimport.impl.evernote;

import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUpdatePar;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.kc.tugraz.ss.service.filerepo.api.SSFileRepoServerI;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSFileExtE;
import at.tugraz.sss.serv.SSFileU;
import at.tugraz.sss.serv.SSLogU;
import at.tugraz.sss.serv.SSMimeTypeE;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.tugraz.sss.servs.file.datatype.par.SSEntityFileAddPar;
import at.tugraz.sss.servs.file.datatype.par.SSEntityFilesGetPar;
import com.evernote.edam.type.Resource;
import java.io.FileOutputStream;

public class SSDataImportEvernoteResourceContentHandler{

  private final SSUri    resourceUri;
  private final Resource resource;
  private final SSUri    user;
  private final String   localWorkPath;
      
  public SSDataImportEvernoteResourceContentHandler(
    final SSUri    user,
    final Resource resource,
    final SSUri    resourceUri,
    final String   localWorkPath){
    
    this.user          = user;
    this.resource      = resource;
    this.resourceUri   = resourceUri;
    this.localWorkPath = localWorkPath;
  }
  
  //TODO dtheiler: currently works with local file repository only (not web dav or any remote stuff; even dont if localWorkPath != local file repo path)
  public void handleResourceContent() throws Exception{
    
    SSFileExtE fileExt = null;
    SSUri  fileUri;
    String fileId;
    
    try{
      
      try{
        fileExt = SSMimeTypeE.fileExtForMimeType1(resource.getMime()); 
        fileUri = SSServCaller.vocURICreate(fileExt);
        fileId  = SSVocConf.fileIDFromSSSURI(fileUri);
        
        SSFileU.writeFileBytes(
          new FileOutputStream(localWorkPath + fileId),
          resource.getData().getBody(),
          resource.getData().getSize());
        
      }catch(Exception error){
        SSLogU.warn("evernote resource couldnt be stored to file for fileExt: " + fileExt);
        return;
      }
      
      ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityUpdate(
        new SSEntityUpdatePar(
          user,
          fileUri,
          SSEntityE.file, //type,
          null, //label
          null, //description,
          null, //creationTime,
          null, //read,
          false, //setPublic
          true, //withUserRestriction
          false)); //shouldCommit)
      
      for(SSEntity file :
        ((SSFileRepoServerI) SSServReg.getServ(SSFileRepoServerI.class)).filesGet(
          new SSEntityFilesGetPar(
            user,
            resourceUri, //entity
            true, //withUserRestriction
            false))){  //invokeEntityHandlers
        
        SSServCaller.entityRemove(file.id, false);
        
        try{
          SSFileU.delFile(localWorkPath + SSVocConf.fileIDFromSSSURI(file.id));
          
        }catch(Exception error){
          SSLogU.warn("evernote resource file couldnt be removed");
        }
      }
      
      ((SSFileRepoServerI) SSServReg.getServ(SSFileRepoServerI.class)).fileAdd(
        new SSEntityFileAddPar(
          user,
          fileUri, //file
          null, //label
          resourceUri, //entity
          true, //withUserRestriction
          false));//shouldCommit
            
      SSDataImportEvernoteThumbHelper.addThumbFromFile(
        user,
        localWorkPath,
        resourceUri,
        fileUri,
        false);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}

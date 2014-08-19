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

import at.kc.tugraz.socialserver.utils.SSFileU;
import at.kc.tugraz.socialserver.utils.SSLogU;
import at.kc.tugraz.socialserver.utils.SSMimeTypeU;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import com.evernote.edam.type.Resource;
import java.io.FileOutputStream;

public class SSDataImportEvernoteResourceContentHandler{

  private final SSUri    resourceUri;
  private final Resource resource;
  private final SSUri    user;
  private final SSUri    userCircle;
  private final String   localWorkPath;
      
  public SSDataImportEvernoteResourceContentHandler(
    final SSUri    user,
    final SSUri    userCircle,
    final Resource resource,
    final SSUri    resourceUri,
    final String   localWorkPath){
    
    this.user          = user;
    this.userCircle    = userCircle;
    this.resource      = resource;
    this.resourceUri   = resourceUri;
    this.localWorkPath = localWorkPath;
  }
  
  //TODO dtheiler: currently works with local file repository only (not web dav or any remote stuff; even dont if localWorkPath != local file repo path)
  public void handleResourceContent() throws Exception{
    
    String fileExt = null;
    SSUri  fileUri;
    String fileId;
    
    try{
      
      try{
        fileExt = SSMimeTypeU.fileExtForMimeType(resource.getMime()); 
        //application/octet-stream //application/vnd.oasis.opendocument.text //audio/x-m4a
        fileUri = SSServCaller.fileCreateUri(user, fileExt);
        fileId  = SSServCaller.fileIDFromURI(user, fileUri);
        
        SSFileU.writeFileBytes(
          new FileOutputStream(localWorkPath + fileId),
          resource.getData().getBody(),
          resource.getData().getSize());
        
      }catch(Exception error){
        SSLogU.warn("evernote resource couldnt be stored to file for fileExt: " + fileExt);
        return;
      }
      
      SSServCaller.entityAdd(
        user,
        fileUri,
        null,
        SSEntityE.file,
        null,
        false);
      
      for(SSUri file : SSServCaller.entityFilesGet(user, resourceUri)){

        SSServCaller.entityRemove(file, false);
        
        try{
          SSFileU.delFile(localWorkPath + SSServCaller.fileIDFromURI (user, file));  
        }catch(Exception error){
          SSLogU.warn("evernote resource file couldnt be removed");
        }
      }
      
      SSServCaller.entityFileAdd(
        user,
        resourceUri,
        fileUri,
        false);
      
      SSServCaller.entityEntitiesToCircleAdd(
        user,
        userCircle,
        fileUri,
        false);
      
      SSDataImportEvernoteThumbHelper.addThumbFromFile(
        user,
        userCircle,
        localWorkPath,
        resourceUri,
        fileUri,
        false);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}

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

import at.kc.tugraz.socialserver.utils.SSFileExtU;
import at.kc.tugraz.socialserver.utils.SSFileU;
import at.kc.tugraz.socialserver.utils.SSLogU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import java.io.File;
import javax.imageio.ImageIO;

public class SSDataImportEvernoteThumbHelper{
  
  //TODO dtheiler: currently works with local file repository only (not web dav or any remote stuff; even dont if localWorkPath != local file repo path)
  public static void addThumbFromFile(
    final SSUri   user,
    final SSUri   userCircle,
    final String  localWorkPath,
    final SSUri   entity,
    final SSUri   fileUri,
    final Boolean shouldCommit) throws Exception{

    Boolean thumbCreated      = false;
    String  fileId;
    String  fileExt;    
    String  filePath;   
    SSUri   pngFileUri; 
    String  pngFilePath;
          
    try{
      
      try{
        fileId            = SSServCaller.fileIDFromURI (user, fileUri);
        fileExt           = SSServCaller.fileExtGet    (user, fileUri);
        filePath          = localWorkPath + fileId;
        pngFileUri        = SSServCaller.fileCreateUri                 (user, SSFileExtU.png);
        pngFilePath       = localWorkPath + SSServCaller.fileIDFromURI (user, pngFileUri);
        //pptx //docx
        if(SSFileExtU.imageFileExts.contains(fileExt)){
          SSFileU.scalePNGAndWrite(ImageIO.read(new File(filePath)), pngFilePath);
          thumbCreated = true;
        }
        
        if(SSStrU.equals(SSFileExtU.pdf, fileExt)){
          SSFileU.writeScaledPNGFromPDF(filePath, pngFilePath);
          thumbCreated = true;
        }
        
        if(SSStrU.equals(SSFileExtU.doc, fileExt)){
          
          final String pdfFilePath       = localWorkPath + SSServCaller.fileIDFromURI (user, SSServCaller.fileCreateUri     (user, SSFileExtU.pdf));
          
          SSFileU.writePDFFromDoc       (filePath,    pdfFilePath);
          SSFileU.writeScaledPNGFromPDF (pdfFilePath, pngFilePath);
          thumbCreated = true;
        }
        
        if(!thumbCreated){
          SSLogU.warn("thumb creation for fileExt " + fileExt + " not supported");
          return;
        }
        
      }catch(Exception error){
        SSLogU.warn("thumb couldnt be created from file");
        return;
      }
      
      SSServCaller.entityAdd(
        user,
        pngFileUri,
        null,
        SSEntityE.thumbnail,
        null,
        shouldCommit);
      
      SSServCaller.entityEntitiesToCircleAdd(
        user,
        userCircle,
        pngFileUri,
        shouldCommit);
      
      for(SSUri thumb : SSServCaller.entityThumbsGet(user, entity)){

        SSServCaller.entityRemove(thumb, false);
        
        try{
          SSFileU.delFile(localWorkPath + SSServCaller.fileIDFromURI (user, thumb));  
        }catch(Exception error){
          SSLogU.warn("thumbnail file couldnt be removed");
        }
      }
      
      SSServCaller.entityThumbAdd(
        user,
        entity,
        pngFileUri,
        shouldCommit);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}

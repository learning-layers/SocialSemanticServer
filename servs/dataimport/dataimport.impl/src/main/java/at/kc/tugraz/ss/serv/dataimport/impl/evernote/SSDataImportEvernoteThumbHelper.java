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
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.tugraz.sss.serv.SSFileExtE;
import at.tugraz.sss.serv.SSFileU;
import at.tugraz.sss.serv.SSLogU;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSImageE;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityAttatchmentsRemovePar;
import at.tugraz.sss.servs.image.api.SSImageServerI;
import at.tugraz.sss.servs.image.datatype.par.SSImageAddPar;
import at.tugraz.sss.servs.image.datatype.par.SSImagesGetPar;
import java.io.File;
import java.util.List;
import javax.imageio.ImageIO;

public class SSDataImportEvernoteThumbHelper{
  
  //TODO dtheiler: currently works with local file repository only (not web dav or any remote stuff; even dont if localWorkPath != local file repo path)
  public static void addThumbFromFile(
    final SSUri   user,
    final String  localWorkPath,
    final SSUri   entity,
    final SSUri   fileUri,
    final Boolean shouldCommit) throws Exception{

    SSUri pngFileUri; 
          
    try{
      
      try{
        
        pngFileUri = createThumbnail(user, localWorkPath, fileUri, 500, 500);
        
        if(pngFileUri == null){
          return;
        }
        
      }catch(Exception error){
        
        SSServErrReg.reset();
        SSLogU.warn("thumb couldnt be created from file");
        return;
      }
      
      final List<SSUri> thumbs = 
        ((SSImageServerI) SSServReg.getServ(SSImageServerI.class)).imagesGet(
          new SSImagesGetPar(
            null, 
            null, 
            user, 
            entity,
            SSImageE.thumb,
            true)); //withUserRestriction
      
      for(SSUri thumb : thumbs){
        
        try{
          
          SSFileU.delFile(
            localWorkPath +
              SSVocConf.fileIDFromSSSURI(
                  thumb));
          
        }catch(Exception error){
          SSLogU.warn("thumbnail file couldnt be removed");
        }
      }
      
      ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityAttachmentsRemove(
        new SSEntityAttatchmentsRemovePar(
          null,
          null,
          user,
          entity,
          thumbs, //attachments
          true, //withUserRestriction
          false)); //shouldCommit
      
      ((SSImageServerI) SSServReg.getServ(SSImageServerI.class)).imageAdd(
        new SSImageAddPar(
          null,
          null,
          user,
          pngFileUri,
          SSImageE.thumb, //imageType,
          entity, //entity
          true, //withUserRestriction,
          false)); //shouldCommit
              
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public static SSUri createThumbnail(
    final SSUri   user,
    final String  localWorkPath,
    final SSUri   fileURI,
    final Integer width, 
    final Integer height) throws Exception{
    
    try{
    
      final String      filePath          =
        localWorkPath +
        SSVocConf.fileIDFromSSSURI(
            fileURI);
      
      final SSFileExtE  fileExt           = SSFileExtE.ext(SSStrU.removeTrailingSlash(fileURI));
      final SSUri       thumbnailFileURI  = SSServCaller.vocURICreate                  (SSFileExtE.png);
      final String      thumbnailPath     =
        localWorkPath +
        SSVocConf.fileIDFromSSSURI(
            thumbnailFileURI);

      if(SSStrU.contains(SSFileExtE.imageFileExts, fileExt)){
        SSFileU.scalePNGAndWrite(ImageIO.read(new File(filePath)), thumbnailPath, width, width);
        return thumbnailFileURI;
      }

      switch(fileExt){
        
        case pdf:{
          
          SSFileU.writeScaledPNGFromPDF(filePath, thumbnailPath, width, width, false);
          return thumbnailFileURI;
        }
        
        case doc:{
          
          final String pdfFilePath  =
            localWorkPath +
            SSVocConf.fileIDFromSSSURI(
                SSServCaller.vocURICreate     (SSFileExtE.pdf));
          
          SSFileU.writePDFFromDoc       (filePath,    pdfFilePath);
          SSFileU.writeScaledPNGFromPDF (pdfFilePath, thumbnailPath, width, width, false);
          return thumbnailFileURI;
        }
      }
      
      return null;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}
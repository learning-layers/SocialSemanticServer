 /**
  * Code contributed to the Learning Layers project
  * http://www.learning-layers.eu
  * Development is partly funded by the FP7 Programme of the European Commission under
  * Grant Agreement FP7-ICT-318209.
  * Copyright (c) 2015, Graz University of Technology - KTI (Knowledge Technologies Institute).
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

package at.tugraz.sss.servs.dataimport.impl;

import at.tugraz.sss.servs.util.SSStrU;
import at.tugraz.sss.servs.entity.datatype.SSServPar;
import at.tugraz.sss.servs.entity.datatype.SSEntityGetPar;
import at.tugraz.sss.servs.entity.datatype.SSToolContextE;
import at.tugraz.sss.servs.entity.datatype.SSEntityE;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.entity.datatype.SSLabel;
import at.tugraz.sss.servs.dataimport.datatype.SSDataImportBitsAndPiecesPar;
import at.tugraz.sss.servs.conf.SSConf;
import at.tugraz.sss.servs.file.api.SSFileServerI;
import at.tugraz.sss.servs.util.SSDateU;
import at.tugraz.sss.servs.entity.datatype.SSEntity;
import at.tugraz.sss.servs.util.SSFileExtE;
import at.tugraz.sss.servs.util.SSFileU;
import at.tugraz.sss.servs.entity.api.*;
import at.tugraz.sss.servs.util.SSLogU;
import at.tugraz.sss.servs.common.impl.SSServErrReg;
import at.tugraz.sss.servs.entity.impl.*;
import at.tugraz.sss.servs.file.datatype.*;
import at.tugraz.sss.servs.file.impl.*;
import at.tugraz.sss.servs.mail.api.SSMailServerI;
import at.tugraz.sss.servs.mail.datatype.SSMail;
import at.tugraz.sss.servs.mail.datatype.SSMailsReceivePar;
import at.tugraz.sss.servs.mail.impl.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class SSDataImportBNPMailImporter {
  
  private final SSDataImportBNPCommon  common      = new SSDataImportBNPCommon();
  
  public void handle(
    final SSDataImportBitsAndPiecesPar par, 
    final SSUri                        forUser,
    final String                       localWorkPath) throws SSErr{
    
    try{
      
      if(
        par.emailInUser     == null ||
        par.emailInPassword == null ||
        par.emailInEmail    == null){
        return;
      }
      
      final SSMailServerI mailServ = new SSMailImpl();
      
      if(mailServ == null){
        return;
      }
      
      SSLogU.info("start B&P mail import for " +  par.authEmail);
      
      final SSUri         notebookUri  = handleEmailNotebook(par, forUser);
      SSMail              mail;
      SSUri               noteUri;
      
      for(SSEntity mailEntity :
        mailServ.mailsReceive(
          new SSMailsReceivePar(
            par,
            SSConf.systemUserUri,
            par.emailInUser,
            par.emailInPassword,
            par.emailInEmail,
            true,  //withUserRestriction
            false))){
        
        mail    = (SSMail) mailEntity;
        noteUri = SSConf.vocURICreate();
        
        handleMailContent          (par, forUser, localWorkPath, mail, notebookUri, noteUri);
        handleMailContentMultimedia(par, forUser, localWorkPath, mail, noteUri);
        handleMailAttachments      (par, forUser, localWorkPath, mail, noteUri);
      }
      
      SSLogU.info("end B&P mail import for " +  par.authEmail);
      
    }catch(Exception error){
      SSLogU.warn("B&P mail import failed for " + par.authEmail, error);
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private SSUri handleEmailNotebook(
    final SSDataImportBitsAndPiecesPar par, 
    final SSUri                        forUser) throws SSErr{
    
    try{
      
      final SSUri            notebookUri = SSUri.get(par.emailInUser + "_email_inbox", SSConf.sssUri);
      final SSEntityServerI   entityServ = new SSEntityImpl();
      
      final SSEntity notebook =
        entityServ.entityGet(
          new SSEntityGetPar(
            par,
            par.user,
            notebookUri,
            false,
            null));
      
      if(notebook == null){
        
        common.addNotebook(
          par,
          forUser,
          SSToolContextE.emailImport,
          notebookUri,
          SSLabel.get(par.emailInEmail + " inbox"),
          SSDateU.dateAsLong());
      }
      
      return notebookUri;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  private void handleMailContent(
    final SSServPar servPar, 
    final SSUri     forUser,
    final String    localWorkPath,
    final SSMail    mail,
    final SSUri     notebookUri,
    final SSUri     noteUri) throws SSErr{
    
    String                    txtFilePath;
    SSUri                     pdfFileUri;
    String                    pdfFilePath;
    
    try{
      
      final SSFileServerI fileServ = new SSFileImpl();
      
      if(mail.content == null){
        return;
      }
      
      txtFilePath    = localWorkPath + SSConf.fileIDFromSSSURI(SSConf.vocURICreate(SSFileExtE.txt));
      pdfFileUri     = SSConf.vocURICreate                  (SSFileExtE.pdf);
      pdfFilePath    = localWorkPath + SSConf.fileIDFromSSSURI (pdfFileUri);
      
      SSFileU.writeStr(mail.content, txtFilePath);
      
      try{
        
        SSFileU.writePDFFromText(
          pdfFilePath,
          txtFilePath);
        
      }catch(Exception error){
        SSLogU.warn("PDF from mail content failed", error);
      }finally{
        
        try{
          SSFileU.delFile(txtFilePath);
        }catch(Exception error){
          SSLogU.warn(error);
        }
      }
      
      common.addNote(
        servPar,
        forUser,
        SSToolContextE.emailImport,
        noteUri, //noteUri
        SSLabel.get(mail.subject), //noteLabel
        notebookUri, //notebookUri,
        mail.creationTime);
      
      fileServ.fileAdd(
        new SSEntityFileAddPar(
          servPar,
          forUser,
          null, //fileBytes
          null, //fileLength
          null, //fileExt
          pdfFileUri, //file
          SSEntityE.file, //type,
          SSLabel.get(mail.subject), //label
          noteUri, //entity
          true, //createThumb
          noteUri, //entityToAddThumbTo
          false, //removeExistingFilesForEntity
          true, //withUserRestriction
          false));//shouldCommit
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private void handleMailContentMultimedia(
    final SSServPar servPar, 
    final SSUri     forUser,
    final String    localWorkPath, 
    final SSMail    mail,
    final SSUri     noteUri) throws SSErr{
    
    try{
      
      final SSFileServerI     fileServ = new SSFileImpl();
      SSUri                   resourceUri;
      
      for(SSEntity attachment : mail.contentMultimedia){
        
        if(!areResourceDimensionsOk(localWorkPath, attachment.id)){
          continue;
        }
        
        resourceUri = SSConf.vocURICreate();
        
        common.addResource(
          servPar,
          forUser,
          SSToolContextE.emailImport,
          resourceUri,
          attachment.label,
          mail.creationTime,
          noteUri); //noteUri
        
        fileServ.fileAdd(
          new SSEntityFileAddPar(
            servPar,
            forUser,
            null, //fileBytes
            null, //fileLength
            null, //fileExt
            attachment.id, //file
            null, //type,
            attachment.label, //label
            resourceUri, //entity
            true, //createThumb
            resourceUri, //entityToAddThumbTo
            false, //removeExistingFilesForEntity
            true, //withUserRestriction
            false));//shouldCommit
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private void handleMailAttachments(
    final SSServPar servPar, 
    final SSUri     forUser,
    final String    localWorkPath,
    final SSMail    mail,
    final SSUri     noteUri) throws SSErr{
    
    try{
      final SSFileServerI fileServ = new SSFileImpl();
      SSUri               resourceUri;
      
      for(SSEntity attachment : mail.attachments){
        
        if(!areResourceDimensionsOk(localWorkPath, attachment.id)){
          continue;
        }
        
        resourceUri = SSConf.vocURICreate();
        
        common.addResource(
          servPar,
          forUser,
          SSToolContextE.emailImport,
          resourceUri,
          attachment.label,
          mail.creationTime,
          noteUri); //noteUri
        
        fileServ.fileAdd(
          new SSEntityFileAddPar(
            servPar,
            forUser,
            null, //fileBytes
            null, //fileLength
            null, //fileExt
            attachment.id, //file
            null, //type,
            attachment.label, //label
            resourceUri, //entity
            true, //createThumb
            resourceUri, //entityToAddThumbTo
            false, //removeExistingFilesForEntity
            true, //withUserRestriction
            false));//shouldCommit
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private boolean areResourceDimensionsOk(
    final String localWorkPath,
    final SSUri  resource) throws SSErr{
    
    try{
      
      if(!SSFileExtE.isImageFileExt(SSFileExtE.getFromStrToFormat(SSStrU.toStr(resource)))){
        return true;
      }
      
      final BufferedImage image = ImageIO.read(new File(localWorkPath + SSConf.fileIDFromSSSURI(resource)));
      
      if(
        image.getWidth()  <= SSDataImportBNPCommon.bitsAndPiecesImageMinWidth ||
        image.getHeight() <= SSDataImportBNPCommon.bitsAndPiecesImageMinHeight){
        
        SSLogU.info("mail attachment height or width < " + SSDataImportBNPCommon.bitsAndPiecesImageMinWidth);
        return false;
      }
      
      return true;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return false;
    }
  }
}
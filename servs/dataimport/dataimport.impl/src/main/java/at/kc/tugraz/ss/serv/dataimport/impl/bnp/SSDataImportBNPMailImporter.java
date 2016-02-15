 /**
  * Code contributed to the Learning Layers project
  * http://www.learning-layers.eu
  * Development is partly funded by the FP7 Programme of the European Commission under
  * Grant Agreement FP7-ICT-318209.
  * Copyright (c) 2015, Graz University of Technology - KTI (Knowledge Technologies Institute).
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
package at.kc.tugraz.ss.serv.dataimport.impl.bnp;

import at.kc.tugraz.ss.serv.dataimport.datatypes.pars.SSDataImportBitsAndPiecesPar;
import at.tugraz.sss.serv.entity.api.SSEntityServerI;
import at.tugraz.sss.conf.SSConf;
import at.tugraz.sss.servs.file.api.SSFileServerI;
import at.tugraz.sss.serv.util.SSDateU;
import at.tugraz.sss.serv.datatype.SSEntity;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.util.SSFileExtE;
import at.tugraz.sss.serv.util.SSFileU;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.par.*;
import at.tugraz.sss.serv.util.SSLogU;
import at.tugraz.sss.serv.reg.SSServErrReg;
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.servs.file.datatype.par.SSEntityFileAddPar;
import at.tugraz.sss.servs.mail.SSMailServerI;
import at.tugraz.sss.servs.mail.datatype.SSMail;
import at.tugraz.sss.servs.mail.datatype.par.SSMailsReceivePar;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class SSDataImportBNPMailImporter {
  
  private final SSDataImportBNPCommon            common = new SSDataImportBNPCommon();
  
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
      
      SSLogU.info("start B&P mail import for " +  par.authEmail);
      
      final SSMailServerI mailServ     = (SSMailServerI) SSServReg.getServ(SSMailServerI.class);
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
      
      final SSEntityServerI entityServ  = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
      final SSUri           notebookUri = SSUri.get(par.emailInUser + "_email_inbox", SSConf.sssUri);
      
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
      
      final SSFileServerI fileServ = (SSFileServerI) SSServReg.getServ(SSFileServerI.class);
      
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
      
      common.addNoteUEs(
        servPar,
        forUser,
        null, //note,
        noteUri, //noteUri,
        mail.creationTime, //creationTime,
        mail.creationTime); //updateTime
      
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
      
      final SSFileServerI fileServ = (SSFileServerI) SSServReg.getServ(SSFileServerI.class);
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
        
        common.addResourceUEs(
          servPar,
          forUser,
          resourceUri,
          mail.creationTime);
        
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
      final SSFileServerI fileServ = (SSFileServerI) SSServReg.getServ(SSFileServerI.class);
      SSUri                   resourceUri;
      
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
        
        common.addResourceUEs(
          servPar,
          forUser,
          resourceUri,
          mail.creationTime);
        
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
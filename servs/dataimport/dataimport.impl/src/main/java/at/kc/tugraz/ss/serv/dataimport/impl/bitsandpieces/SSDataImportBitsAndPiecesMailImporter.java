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
package at.kc.tugraz.ss.serv.dataimport.impl.bitsandpieces;

import at.kc.tugraz.ss.serv.dataimport.datatypes.pars.SSDataImportBitsAndPiecesPar;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.kc.tugraz.ss.service.filerepo.api.SSFileRepoServerI;
import at.kc.tugraz.ss.service.userevent.api.SSUEServerI;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSFileExtE;
import at.tugraz.sss.serv.SSFileU;
import at.tugraz.sss.serv.SSLabel;
import at.tugraz.sss.serv.SSLogU;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.tugraz.sss.servs.file.datatype.par.SSEntityFileAddPar;
import at.tugraz.sss.servs.mail.SSMailServerI;
import at.tugraz.sss.servs.mail.datatype.SSMail;
import at.tugraz.sss.servs.mail.datatype.par.SSMailsReceivePar;
import sss.serv.eval.api.SSEvalServerI;

public class SSDataImportBitsAndPiecesMailImporter {
  
  private final SSDataImportBitsAndPiecesPar     par;
  private final String                           localWorkPath;
  private final SSFileRepoServerI                fileServ;
  private final SSUri                            userUri;
  private final SSDataImportBitsAndPiecesMiscFct miscFct;

  public SSDataImportBitsAndPiecesMailImporter(
    final SSDataImportBitsAndPiecesPar par,
    final String                       localWorkPath,
    final SSEntityServerI              entityServ,
    final SSFileRepoServerI            fileServ,
    final SSEvalServerI                evalServ,
    final SSUEServerI                  ueServ,
    final SSUri                        userUri) throws Exception{
    
    this.par           = par;
    this.localWorkPath = localWorkPath;
    this.fileServ      = fileServ;
    this.userUri       = userUri;
    
    this.miscFct = 
      new SSDataImportBitsAndPiecesMiscFct(
        par,
        entityServ, //entityServ
        ueServ, //ueServ
        evalServ, //evalServ
        userUri);
  }
  
  public void handle() throws Exception{

    try{
     
      if(
        par.emailInUser     == null ||
        par.emailInPassword == null ||
        par.emailInEmail    == null){
        return;
      }
      
      final SSMailServerI mailServ = (SSMailServerI) SSServReg.getServ(SSMailServerI.class);
      SSMail mail;
      SSUri  noteUri;
      
      SSLogU.info("start B&P mail import for " +  par.authEmail);
      
      for(SSEntity mailEntity : 
        mailServ.mailsReceive(
          new SSMailsReceivePar(
            SSVocConf.systemUserUri,
            par.emailInUser,
            par.emailInPassword,
            par.emailInEmail, 
            true,  //withUserRestriction
            par.shouldCommit))){
        
        mail    = (SSMail) mailEntity;
        noteUri = SSServCaller.vocURICreate();
        
        handleMailContent          (mail, noteUri);
        handleMailContentMultimedia(mail, noteUri);
        handleMailAttachments      (mail, noteUri);
      }
      
      SSLogU.info("end B&P mail import for " +  par.authEmail);
      
    }catch(Exception error){
      SSLogU.warn("B&P mail import failed for " + par.authEmail);
      SSServErrReg.regErrThrow(error);
    }finally{
    }
  }

  private void handleMailContent(
    final SSMail mail,
    final SSUri  noteUri) throws Exception{
    
    String                    xhtmlFilePath;
    SSUri                     pdfFileUri;
    String                    pdfFilePath;
    
    try{
      
      if(mail.content == null){
        return;
      }
      
      xhtmlFilePath    = localWorkPath + SSVocConf.fileIDFromSSSURI(SSServCaller.vocURICreate(SSFileExtE.xhtml));
      pdfFileUri       = SSServCaller.vocURICreate                  (SSFileExtE.pdf);
      pdfFilePath      = localWorkPath + SSVocConf.fileIDFromSSSURI (pdfFileUri);
      
      SSFileU.writeStr(mail.content, xhtmlFilePath);
      
      try{
        
        SSFileU.writePDFFromXHTML(
          pdfFilePath,
          xhtmlFilePath,
          false);
        
      }catch(Exception error){
        
        SSServErrReg.reset();
        SSLogU.warn("PDF from mail content failed");
        
      }finally{
        
        try{
          SSFileU.delFile(xhtmlFilePath);
        }catch(Exception error){}
      }
      
      miscFct.addNote(
        noteUri, //noteUri
        SSLabel.get(mail.subject), //noteLabel
        null, //notebookUri, 
        mail.creationTime);
      
       miscFct.addNoteUEs(
         null, //note, 
         noteUri, //noteUri,
         mail.creationTime, //creationTime, 
         mail.creationTime); //updateTime
      
      fileServ.fileAdd(
        new SSEntityFileAddPar(
          userUri,
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
    final SSMail mail,
    final SSUri  noteUri) throws Exception{
    
    try{
      
      SSUri resourceUri;
      
      for(SSEntity attachment : mail.contentMultimedia){
       
        resourceUri = SSServCaller.vocURICreate();
          
        miscFct.addResource(
          resourceUri, 
          attachment.label, 
          mail.creationTime, 
          noteUri); //noteUri
        
        miscFct.addResourceUEs(
          resourceUri, 
          mail.creationTime);
        
        fileServ.fileAdd(
          new SSEntityFileAddPar(
            userUri,
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
    final SSMail mail, 
    final SSUri  noteUri) throws Exception{
    
    try{
      
      SSUri resourceUri;
      
      for(SSEntity attachment : mail.attachments){
       
        resourceUri = SSServCaller.vocURICreate();
          
        miscFct.addResource(
          resourceUri, 
          attachment.label, 
          mail.creationTime, 
          noteUri); //noteUri
        
        miscFct.addResourceUEs(
          resourceUri, 
          mail.creationTime);
        
        fileServ.fileAdd(
          new SSEntityFileAddPar(
            userUri,
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
}
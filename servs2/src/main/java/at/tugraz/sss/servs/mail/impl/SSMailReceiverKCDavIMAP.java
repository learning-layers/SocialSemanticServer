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
package at.tugraz.sss.servs.mail.impl;

import at.tugraz.sss.servs.entity.datatype.SSEntityE;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSLabel;
import at.tugraz.sss.servs.conf.SSConf;
import at.tugraz.sss.servs.entity.datatype.SSEntity;
import at.tugraz.sss.servs.entity.api.*;
import at.tugraz.sss.servs.entity.datatype.SSEntityUpdatePar;
import at.tugraz.sss.servs.util.SSFileExtE;
import at.tugraz.sss.servs.util.SSFileU;
import at.tugraz.sss.servs.util.SSLogU;
import at.tugraz.sss.servs.common.impl.SSServErrReg;
import at.tugraz.sss.servs.util.SSStrU;
import at.tugraz.sss.servs.mail.datatype.SSMail;
import at.tugraz.sss.servs.mail.datatype.SSMailsReceivePar;
import com.sun.mail.imap.IMAPMessage;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.mail.*;

public class SSMailReceiverKCDavIMAP {
  
  private final SSMailSQLFct     sql;
  private final SSEntityServerI  entityServ;
  
  public SSMailReceiverKCDavIMAP(
    final SSEntityServerI entityServ, 
    final SSMailSQLFct    sql) throws SSErr{
    
    this.entityServ = entityServ;
    this.sql        = sql;
  }

//  final String  fromUser,
//    final String  receiverEmail,
//    final String  fromPassword,
//    final boolean shouldCommit
  
  public List<SSEntity> receiveMails(
    final SSMailsReceivePar servPar) throws SSErr {
    
    Store  store  = null;
    Folder folder = null;
    
    try{
      final List<SSEntity>            mails   = new ArrayList<>();
      final Properties                props   = new Properties();
      final SSMailKCIMAPAuthenticator auth    = new SSMailKCIMAPAuthenticator(servPar.fromUser, servPar.fromPassword);
      final Session                   session = Session.getDefaultInstance(props, auth); //session.setDebug(true);
      
      store = session.getStore("imap");
      
      store.connect("kcs-davmail.know.know-center.at", 1143, servPar.fromUser, servPar.fromPassword);
      
      folder = store.getFolder("INBOX");
      
      folder.open(Folder.READ_ONLY);
      
      final Message messages [] = folder.getMessages();
      SSMail        mail;
      IMAPMessage   message;
      String        hash;
      
      for(int counter = 0; counter < messages.length; counter++){
        
        message = (IMAPMessage) messages[counter];
        mail    =
          SSMail.get(
            SSConf.vocURICreate(),
            messages[counter].getSubject(),
            messages[counter].getSentDate().getTime());

        hash = message.getMessageID() + servPar.receiverEmail;
        
        if(sql.existsMail(servPar, null, hash, servPar.receiverEmail)){
          continue;
        }
        
        createMessageFromMessageParts(
          message, 
          mail, 
          true);
        
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            servPar,
            servPar.user, 
            mail.id,
            SSEntityE.mail, 
            mail.label, //label 
            null, //description, 
            mail.creationTime, //creationTime
            null, //read, 
            false, //setPublic, 
            true, //createIfNotExists,
            servPar.withUserRestriction, 
            false));
        
        sql.addMailIfNotExists(
          servPar, 
          mail.id, 
          servPar.receiverEmail, 
          hash);
        
        mails.add(mail);
      }
      
      return mails;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      
      if(
        folder != null &&
        folder.isOpen()){
        try {
          folder.close (true);
        } catch (MessagingException ex) {
          SSLogU.err(ex);
        }
      }
      
      if(
        store != null && 
        store.isConnected()){
        try {
          store.close  ();
        } catch (MessagingException ex) {
          SSLogU.err(ex);
        }
      }
    }
  }

  private void createMessageFromMessageParts(
    final Part    message, 
    final SSMail  mail,
    final boolean isFirstLevel) throws SSErr{
    
    try{
      
      if(message.getContent() instanceof Multipart){
        handleMultiPartContent((Multipart) message.getContent(), mail, isFirstLevel);
        return;
      }
      
      if(message.getContent() instanceof String){
//        System.out.println("content as string: " + message.getContent());
        mail.content = (String) message.getContent();
        return;
      }
      
      if(message.getContent() instanceof InputStream){
        SSLogU.warn("unhandled content!!! inputstream", null);
        return;
      }
      
      if(message.getContent() instanceof Message){
//        System.out.println("content is message!!");
        createMessageFromMessageParts((Part) message.getContent(), mail, isFirstLevel);
        return;
      }
      
      SSLogU.warn("unhandled content!!!", null);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private SSEntity getAttachmentObj(final String fileName) throws SSErr{
    
    SSEntity result;
    
    try{
      
      result = 
        SSEntity.get(
          SSConf.vocURICreate(SSFileExtE.getFromStrToFormat(fileName)), 
          SSEntityE.file);
      
      result.label = SSLabel.get(fileName);
      
      return result;
            
    }catch(Exception error){
      throw error;
    }
  }
  
  private void handleAttachment(
    final Part      bodyPart,
    final SSMail    mail, 
    final boolean   isFirstLevel){
    
    try{
      
      final SSEntity attachment = getAttachmentObj(bodyPart.getFileName());
      
      SSFileU.writeFileBytes(
        SSFileU.openOrCreateFileWithPathForWrite(SSConf.getLocalWorkPath() + SSConf.fileIDFromSSSURI(attachment.id)),
        bodyPart.getInputStream(),
        10000);
      
      if(isFirstLevel){
        mail.attachments.add(attachment);
      }else{
        mail.contentMultimedia.add(attachment);
      }
      
    }catch(Exception error){
      SSLogU.warn("mail attachment creation failed", error);
    }
  }
  
  private void handleMultiPartContent(
    final Multipart multipart, 
    final SSMail    mail, 
    final boolean   isFirstLevel) throws SSErr{
    
    try{
      
      Part      bodyPart;
      String    strContent;
      
      for(int counter = 0; counter < multipart.getCount(); counter++) {
        
        bodyPart = multipart.getBodyPart(counter);
        
        if(
          Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition()) ||
          !SSStrU.isEmpty(bodyPart.getFileName())){
          
          handleAttachment(bodyPart, mail, isFirstLevel);
          continue;
        }
        
//        System.out.println("content is multipart; no attachment");
        
        if(bodyPart.getContent() instanceof Multipart){
          handleMultiPartContent((Multipart) bodyPart.getContent(), mail, false);
          continue;
        }
        
        if(bodyPart.getContent() instanceof Part){
//          System.out.println("content is multipart; no attachment; part again");
          createMessageFromMessageParts((Part) bodyPart.getContent(), mail, false);
          continue;
        }
        
        if(bodyPart.getContent() instanceof String){
//          System.out.println("content is multipart; no attachment; string");
//          System.out.println(bodyPart.getContent());
          
          strContent = (String) bodyPart.getContent();
          
          if(
            strContent.startsWith("<html") || 
            strContent.startsWith("<xhtml")){
            continue;
          }
          
          mail.content = strContent;
          continue;
        }
        
        if(bodyPart.getContent() instanceof InputStream){
          SSLogU.warn("unhandled content!!! content is multipart; no attachment; input stream", null);
          continue;
        }
        
        SSLogU.warn("unhandled content!!!", null);
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}
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
package at.tugraz.sss.servs.mail.impl.kc;

import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSFileExtE;
import at.tugraz.sss.serv.SSFileU;
import at.tugraz.sss.serv.SSLabel;
import at.tugraz.sss.serv.SSLogU;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityUpdatePar;
import at.tugraz.sss.servs.mail.conf.SSMailConf;
import at.tugraz.sss.servs.mail.datatype.SSMail;
import at.tugraz.sss.servs.mail.datatype.par.SSMailsReceivePar;
import at.tugraz.sss.servs.mail.impl.SSMailSQLFct;
import com.mysql.jdbc.StringUtils;
import com.sun.mail.imap.IMAPMessage;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;

public class SSMailReceiverKCDavIMAP {
  
  private final List<SSEntity>   mails   = new ArrayList<>();
  private final SSMailSQLFct     sqlFct;
  private final SSEntityServerI  entityServ;
  
  public SSMailReceiverKCDavIMAP(
    final SSMailSQLFct  sqlFct) throws Exception{
    
    this.sqlFct          = sqlFct;
    this.entityServ      = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
  }

//  final String  fromUser,
//    final String  receiverEmail,
//    final String  fromPassword,
//    final Boolean shouldCommit
  
  public List<SSEntity> receiveMails(
    final SSMailsReceivePar par) throws Exception {
    
    Store  store  = null;
    Folder folder = null;
    
    try{
      final Properties                props   = new Properties();
      final SSMailKCIMAPAuthenticator auth    = new SSMailKCIMAPAuthenticator(par.fromUser, par.fromPassword);
      final Session                   session = Session.getDefaultInstance(props, auth); //session.setDebug(true);
      
      store = session.getStore("imap");
      
      store.connect("kcs-davmail.know.know-center.at", 1143, par.fromUser, par.fromPassword);
      
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
            SSServCaller.vocURICreate(),
            messages[counter].getSubject(),
            messages[counter].getSentDate().getTime());

        hash = message.getMessageID() + par.receiverEmail;
        
        if(sqlFct.existsMail(null, hash, par.receiverEmail)){
          continue;
        }
        
        createMessageFromMessageParts(
          message, 
          mail, 
          true);
        
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            par.user, 
            mail.id,
            SSEntityE.mail, 
            mail.label, //label 
            null, //description, 
            mail.creationTime, //creationTime
            null, //read, 
            false, //setPublic, 
            true, //createIfNotExists,
            par.withUserRestriction, 
            false));
        
        sqlFct.addMailIfNotExists(
          mail.id, 
          par.receiverEmail, 
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
        folder.close (true);
      }
      
      if(
        store != null && 
        store.isConnected()){
        store.close  ();
      }
    }
  }

  private void createMessageFromMessageParts(
    final Part    message, 
    final SSMail  mail,
    final Boolean isFirstLevel) throws Exception{
    
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
        SSLogU.warn("unhandled content!!! inputstream");
        return;
      }
      
      if(message.getContent() instanceof Message){
//        System.out.println("content is message!!");
        createMessageFromMessageParts((Part) message.getContent(), mail, isFirstLevel);
        return;
      }
      
      SSLogU.warn("unhandled content!!!");
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private SSEntity getAttachmentObj(final String fileName) throws Exception{
    
    SSEntity result;
    
    try{
      
      result = 
        SSEntity.get(
          SSServCaller.vocURICreate(SSFileExtE.getFromStrToFormat(fileName)), 
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
    final Boolean   isFirstLevel){
    
    try{
      
      final SSEntity attachment = getAttachmentObj(bodyPart.getFileName());
      
      SSFileU.writeFileBytes(
        SSFileU.openOrCreateFileWithPathForWrite(SSMailConf.getLocalWorkPath() + SSVocConf.fileIDFromSSSURI(attachment.id)),
        bodyPart.getInputStream());
      
      if(isFirstLevel){
        mail.attachments.add(attachment);
      }else{
        mail.contentMultimedia.add(attachment);
      }
      
    }catch(Exception error){
      SSLogU.warn("mail attachment creation failed");
    }
  }
  
  private void handleMultiPartContent(
    final Multipart multipart, 
    final SSMail    mail, 
    final Boolean   isFirstLevel) throws Exception{
    
    try{
      
      Part      bodyPart;
      String    strContent;
      
      for(int counter = 0; counter < multipart.getCount(); counter++) {
        
        bodyPart = multipart.getBodyPart(counter);
        
        if(
          Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition()) ||
          !StringUtils.isEmptyOrWhitespaceOnly(bodyPart.getFileName())){
          
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
          SSLogU.warn("unhandled content!!! content is multipart; no attachment; input stream");
          continue;
        }
        
        SSLogU.warn("unhandled content!!!");
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}
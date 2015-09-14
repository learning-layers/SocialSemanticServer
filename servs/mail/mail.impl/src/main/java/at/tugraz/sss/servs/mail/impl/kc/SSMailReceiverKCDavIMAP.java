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

import at.kc.tugraz.ss.conf.conf.SSCoreConf;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSFileU;
import at.tugraz.sss.serv.SSLogU;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.tugraz.sss.servs.mail.conf.SSMailConf;
import at.tugraz.sss.servs.mail.datatype.SSMail;
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
  
  private final  SSMailConf      mailConf;
  private final  String          localWorkPath;
  private final  List<SSEntity>   mails   = new ArrayList<>();
  
  public SSMailReceiverKCDavIMAP(
    final SSMailConf mailConf) throws Exception{
    
    this.mailConf        = mailConf;
    this.localWorkPath   = SSCoreConf.instGet().getSss().getLocalWorkPath();
  }

  public List<SSEntity> receiveMails(
    final String fromUser,
    final String fromPassword) throws Exception {
    
    Store  store  = null;
    Folder folder = null;
    
    try{
      final Properties                props   = new Properties();
      final SSMailKCIMAPAuthenticator auth    = new SSMailKCIMAPAuthenticator(fromUser, fromPassword);
      final Session                   session = Session.getDefaultInstance(props, auth); //session.setDebug(true);
      
      store = session.getStore("imap");
      
      store.connect("kcs-davmail.know.know-center.at", 1143, fromUser, fromPassword);
      
      folder = store.getFolder("INBOX");
      
      folder.open(Folder.READ_ONLY);
      
      final Message messages [] = folder.getMessages();
      SSMail        mail;
      IMAPMessage   message;
      
      for(int counter = 0; counter < messages.length; counter++){
        
        message = (IMAPMessage) messages[counter];
        mail    =
          SSMail.get(
            SSServCaller.vocURICreate(),
            messages[counter].getSubject());
        
        if(createMessageFromMessageParts (message, mail)){
          mails.add(mail);
        }else{
          SSLogU.warn(
            "subject    : " + message.getSubject() +
            "date       : " + message.getSentDate() +
            "id         : " + message.getMessageID() +
            "number     : " + message.getMessageNumber() +
            "contentType: " + message.getContentType() +
            "encoding:    " + message.getEncoding());
        }
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

  private Boolean createMessageFromMessageParts(
    final Part   message, 
    final SSMail mail) throws Exception{
    
    try{
      
      if(message.getContent() instanceof Multipart){
        return handleMultiPartContent((Multipart) message.getContent(), mail);
      }
      
      if(message.getContent() instanceof String){
//        System.out.println("content as string: " + message.getContent());
        return true;
      }
      
      if(message.getContent() instanceof InputStream){
        SSLogU.warn("unhandled content!!! inputstream");
        return false;
      }
      
      if(message.getContent() instanceof Message){
//        System.out.println("content is message!!");
        return createMessageFromMessageParts((Part) message.getContent(), mail);
      }
      
      SSLogU.warn("unhandled content!!!");
      return false;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  private Boolean handleMultiPartContent(
    final Multipart multipart, 
    final SSMail    mail) throws Exception{
    
    try{
      
      Part         bodyPart;
      
      for (int counter = 0; counter < multipart.getCount(); counter++) {
        
        bodyPart = multipart.getBodyPart(counter);
        
        if(
          Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition()) ||
          !StringUtils.isEmptyOrWhitespaceOnly(bodyPart.getFileName())){
          
          //Attachment
          SSFileU.writeFileBytes(
            SSFileU.openOrCreateFileWithPathForWrite(localWorkPath + bodyPart.getFileName()),
            bodyPart.getInputStream());
          continue;
        }
        
//        System.out.println("content is multipart; no attachment");
        
        if(bodyPart.getContent() instanceof Multipart){
          return handleMultiPartContent((Multipart) bodyPart.getContent(), mail);
        }
        
        if(bodyPart.getContent() instanceof Part){
//          System.out.println("content is multipart; no attachment; part again");
          if(createMessageFromMessageParts((Part) bodyPart.getContent(), mail)){
            continue;
          }
          
          return false;
        }
        
        if(bodyPart.getContent() instanceof String){
//          System.out.println("content is multipart; no attachment; string");
//          System.out.println(bodyPart.getContent());
          return true;
        }
        
        if(bodyPart.getContent() instanceof InputStream){
          SSLogU.warn("unhandled content!!! content is multipart; no attachment; input stream");
          return false;
        }
        
        SSLogU.warn("unhandled content!!!");
        return false;
      }
      
      return true;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}
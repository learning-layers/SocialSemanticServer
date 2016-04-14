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

package at.tugraz.sss.servs.mail.impl;

import at.tugraz.sss.servs.util.SSLogU;
import at.tugraz.sss.servs.util.SSStrU;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSEntity;
import at.tugraz.sss.servs.conf.SSConf;
import at.tugraz.sss.servs.common.impl.SSServErrReg;
import at.tugraz.sss.servs.mail.conf.SSMailConf;
import at.tugraz.sss.servs.mail.datatype.SSMail;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.mail.*;

public class SSMailReceiverGMAILIMAP {
  
  private static final String     receivingHost    = "imap.gmail.com";
  private static final String     receivingPort    = "993";
  
  public SSMailReceiverGMAILIMAP(){
  }

  public List<SSEntity> receiveMails(
    final String fromEmail,
    final String fromPassword) throws SSErr {
    
    Store  store  = null;
    Folder folder = null;
    
    try{
      final List<SSEntity>  mails = new ArrayList<>();
      final Properties      props = new Properties();
      
      props.put("mail.store.protocol",        "imap");
      props.put("mail.imap.host",             receivingHost);
      props.put("mail.imap.port",             receivingPort);
      props.put("mail.imap.user",             fromEmail); //was mail.use
      props.put("mail.imap.password",         fromPassword);
      props.put("mail.imap.auth",             "true");
      props.put("mail.imap.ssl.enable",  "true");
      //    props.setProperty("mail.store.protocol", "imaps");
//    props.put("mail.imap.starttls.enable",  "true");
      
      final SSMailGMAILIMAPAuthenticator auth           = new SSMailGMAILIMAPAuthenticator(fromEmail, fromPassword);
      final Session                      session        = Session.getDefaultInstance(props, auth); //session.setDebug(true);
      
      store = session.getStore("imaps");
      
      store.connect(receivingHost, fromEmail, fromPassword);
      
      folder = store.getFolder("INBOX");
      
      folder.open(Folder.READ_ONLY);
      
      final Message messages [] = folder.getMessages();
      SSMail        mail; 
      
      for(int counter = 0; counter < messages.length; counter++){
        
        mail =
          SSMail.get(
            SSConf.vocURICreate(),
            messages[counter].getSubject(),
            messages[counter].getSentDate().getTime());
        
        mail.content = SSStrU.toStr(messages[counter].getContent());
          
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
}
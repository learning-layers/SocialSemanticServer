 /**
  * Code contributed to the Learning Layers project
  * http://www.learning-layers.eu
  * Development is partly funded by the FP7 Programme of the European Commission under
  * Grant Agreement FP7-ICT-318209.
  * Copyright (c) 2016, Graz University of Technology - KTI (Knowledge Technologies Institute).
  
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

import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.common.impl.SSServErrReg;
import at.tugraz.sss.servs.mail.conf.SSMailConf;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SSMailSenderKCDavSMTP {
  
  private static final String     sendingHost    = "kcs-davmail.know.know-center.at";
  private static final String     sendingPort    = "1025";
  
  public void sendMail(
    final SSMailConf mailConf,
    final String     fromEmail,
    final String     toEmail,
    final String     subject,
    final String     content) throws SSErr {
    
    try{
      
      final Properties props = new Properties();
      
      props.put("mail.transport.protocol",    "smtp");
      props.put("mail.smtp.host",             sendingHost);
      props.put("mail.smtp.port",             sendingPort);
      props.put("mail.smtp.user",             mailConf.sendUserName);
      props.put("mail.smtp.password",         mailConf.sendPass);
      
      props.put("mail.smtp.auth",             "true");
      props.put("mail.smtp.starttls.enable",  "true");
      props.put("mail.smtp.ssl.enable",       "true");
      
      final SSMailKCIMAPAuthenticator  auth           = new SSMailKCIMAPAuthenticator(mailConf.sendUserName, mailConf.sendPass);
      final InternetAddress            fromAddress    = new InternetAddress(fromEmail);
      final InternetAddress            toAddress      = new InternetAddress(toEmail);
      final Session                    session        = Session.getDefaultInstance(props, auth); 
      final Message                    message        = new MimeMessage(session);
      
      message.setFrom      (fromAddress);
      message.setRecipient (Message.RecipientType.TO, toAddress);
      message.setSubject   (subject);
      message.setText      (content);
      
      // to add CC or BCC use
      // message.setRecipient(RecipientType.CC, new InternetAddress("CC_Recipient@any_mail.com"));
      // message.setRecipient(RecipientType.BCC, new InternetAddress("CBC_Recipient@any_mail.com"));
      
      final Transport transport = session.getTransport("smtp");

      transport.send(message);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}

package at.kc.tugraz.ss.main;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SSMail {
  
  protected final String gmxUserName   = "xxxx@gmx.at";
  protected final String gmxPassword   = "xxxx";
  protected final String gmailUserName = "xxxx@gmail.com";
  protected final String gmailPassword = "xxxx";
  
  private void sendGMXSMTP() throws AddressException, MessagingException{
    
    final String               mailSubject    = new String("Testing Mail 1");
    final String               mailText       = new String("Have an Nice Day ...........!!!");
    final String               sendingHost    = "mail.gmx.net";
    final Integer              sendingPort    = 587;
    final Properties           props          = new Properties();
    final GMXSMTPAuthenticator auth           = new GMXSMTPAuthenticator();
    InternetAddress            fromAddress    = null;
    InternetAddress            toAddress      = null;
    Session                    session        = null;
    Message                    message        = null;
    Transport                  transport      = null;
    
    props.put("mail.transport.protocol",    "smtp");
    props.put("mail.smtp.host",             sendingHost);
    props.put("mail.smtp.port",             String.valueOf(sendingPort));
    props.put("mail.smtp.user",             gmxUserName);
    props.put("mail.smtp.password",         gmxPassword);
    props.put("mail.smtp.auth",             "true");
    props.put("mail.smtp.starttls.enable",  "true");
//    props.put("mail.smtp.ssl.enable",  "true");    
    
    session = Session.getDefaultInstance(props, auth);
    
//    session.setDebug(true);
    
    message     = new MimeMessage(session);
    fromAddress = new InternetAddress(gmxUserName);
    toAddress   = new InternetAddress(gmxUserName);
    
    message.setFrom      (fromAddress);
    message.setRecipient (Message.RecipientType.TO, toAddress);
    message.setSubject   (mailSubject);
    message.setText      (mailText);
    // to add CC or BCC use
    // simpleMessage.setRecipient(RecipientType.CC, new InternetAddress("CC_Recipient@any_mail.com"));
    // simpleMessage.setRecipient(RecipientType.BCC, new InternetAddress("CBC_Recipient@any_mail.com"));
    
    transport = session.getTransport("smtp");
    
    transport.send(message);
  }
  
  public void readGMAILIMAP() throws NoSuchProviderException, MessagingException {
    
    final String                 receivingHost  = "imap.gmail.com"; //for imap protocol
    final Integer                receivingPort  = 993;
    final Properties             props          = new Properties();
    final GMAILIMAPAuthenticator auth           = new GMAILIMAPAuthenticator();
    Session                      session        = null;
    Store                        store          = null;
    Folder                       folder         = null;
    Message                      messages []    = null;
    
//    props.setProperty("mail.store.protocol", "imaps");
    props.put("mail.store.protocol",        "imap");
    props.put("mail.imap.host",             receivingHost);
    props.put("mail.imap.port",             String.valueOf(receivingPort));
    props.put("mail.imap.user",             gmxUserName);
    props.put("mail.imap.password",         gmxPassword);
    props.put("mail.imap.auth",             "true");
//    props.put("mail.imap.starttls.enable",  "true");
    props.put("mail.imap.ssl.enable",  "true"); 
    
    session = Session.getDefaultInstance(props, auth);
    
    session.setDebug(true);
    
    store   = session.getStore("imaps");
    
    store.connect(receivingHost, gmailUserName, gmxPassword);
    
    folder = store.getFolder("INBOX");
    
    folder.open(Folder.READ_ONLY);//open folder only to read
    
    messages = folder.getMessages();
    
    for(int counter = 0; counter < messages.length; counter++){
      System.out.println(messages[counter].getSubject());
    }
    
    folder.close (true);
    store.close  ();
  }
  
  public class GMXSMTPAuthenticator extends Authenticator {
    
    public GMXSMTPAuthenticator() {
      
      super();
    }
    
    @Override
    public PasswordAuthentication getPasswordAuthentication() {
      return new PasswordAuthentication(gmxUserName, gmxPassword);
    }
  }
  
  public class GMAILIMAPAuthenticator extends Authenticator {
    
    public GMAILIMAPAuthenticator() {
      
      super();
    }
    
    @Override
    public PasswordAuthentication getPasswordAuthentication() {
      return new PasswordAuthentication(gmailUserName, gmailPassword);
    }
  }
}

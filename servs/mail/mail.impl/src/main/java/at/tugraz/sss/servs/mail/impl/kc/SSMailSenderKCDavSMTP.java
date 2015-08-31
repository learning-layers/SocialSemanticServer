/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package at.tugraz.sss.servs.mail.impl.kc;

import at.tugraz.sss.servs.mail.conf.SSMailConf;

public class SSMailSenderKCDavSMTP {
  
  private static final String     sendingHost    = "kcs-davmail.know.know-center.at";
  private static final String     sendingPort    = "1025";
  private final        SSMailConf mailConf;
  
  public SSMailSenderKCDavSMTP(
    final SSMailConf mailConf){
    
    this.mailConf = mailConf;
  }
}

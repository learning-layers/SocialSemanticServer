package sss.serv.eval.impl.helpers;

import at.tugraz.sss.serv.datatype.*;

public class SSMessageSentInfo {
  
  public SSLabel userLabel   = null;
  public SSLabel targetLabel = null;
  public String  content     = null;
  public Long    timestamp   = null;
  
  public SSMessageSentInfo(
    final SSLabel userLabel,
    final SSLabel targetLabel,
    final String  content,
    final Long    timestamp){
    
    this.userLabel   = userLabel;
    this.targetLabel = targetLabel;
    this.content     = content;
    this.timestamp   = timestamp;
  }
}

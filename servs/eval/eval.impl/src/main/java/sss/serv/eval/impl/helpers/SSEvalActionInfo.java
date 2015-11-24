package sss.serv.eval.impl.helpers;

import sss.serv.eval.datatypes.SSEvalLogE;

public class SSEvalActionInfo {
  
  public SSEvalLogE type      = null;
  public Long       timestamp = null;
  public String     content   = null;
  
  public SSEvalActionInfo(
    final SSEvalLogE type, 
    final Long       timestamp, 
    final String     content){
    
    this.type      = type;
    this.timestamp = timestamp;
    this.content   = content;
  }
}

package sss.serv.eval.impl.helpers;

import sss.serv.eval.datatypes.SSEvalLogE;

public class SSEvalActionInfo {
  
  public SSEvalLogE type      = null;
  public Long       timestamp = null;
  
  public SSEvalActionInfo(
    final SSEvalLogE type, 
    final Long       timestamp){
    
    this.type      = type;
    this.timestamp = timestamp;
  }
}

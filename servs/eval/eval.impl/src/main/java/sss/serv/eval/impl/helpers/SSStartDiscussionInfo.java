package sss.serv.eval.impl.helpers;

import at.tugraz.sss.serv.datatype.*;

public class SSStartDiscussionInfo {
  
  public SSLabel userLabel       = null;
  public SSLabel discussionLabel = null;
  public SSLabel targetLabel     = null;
  
  public SSStartDiscussionInfo(
    final SSLabel userLabel,
    final SSLabel discussionLabel,
    final SSLabel targetLabel){
    
    this.userLabel       = userLabel;
    this.discussionLabel = discussionLabel;
    this.targetLabel     = targetLabel;
  }
}

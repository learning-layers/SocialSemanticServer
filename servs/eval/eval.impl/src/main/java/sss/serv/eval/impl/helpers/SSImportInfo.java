package sss.serv.eval.impl.helpers;

import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSLabel;
import at.tugraz.sss.serv.SSUri;

public class SSImportInfo {

  public SSUri     bitID        = null;
  public SSLabel   bitLabel     = null;
  public SSEntityE bitType      = null;
  public Long      timestamp    = null;
  public SSLabel   targetBit    = null;
  public String    content      = null;
  
  public SSImportInfo(
    final SSUri     bitID,
    final SSLabel   bitLabel,
    final Long      timestamp){
    
    this.bitID        = bitID;
    this.bitLabel     = bitLabel;
    this.timestamp    = timestamp;
  }
}

package sss.serv.eval.impl.helpers;

import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSLabel;

public class SSImportInfo {

  public SSLabel   bitLabel     = null;
  public SSEntityE bitType      = null;
  public Long      timestamp    = null;
  public SSLabel   targetBit    = null;
  public String    content      = null;
  
  public SSImportInfo(
    final SSLabel   bitLabel,
    final Long      timestamp){
    
    this.bitLabel     = bitLabel;
    this.timestamp    = timestamp;
  }
}

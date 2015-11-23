package sss.serv.eval.impl.helpers;

import at.tugraz.sss.serv.SSLabel;
import at.tugraz.sss.serv.SSUri;
import java.util.ArrayList;
import java.util.List;
import sss.serv.eval.datatypes.SSEvalLogE;

public class SSEpisodeShareInfo {
  
  public SSUri                             id                  = null;
  public SSLabel                           label               = null;
  public Long                              timestamp           = null;
  public Long                              creationTime        = null;
  public List<SSLabel>                     targetUsers         = new ArrayList<>();
  public String                            selectedBitsMeasure = null;
  public SSEvalLogE                        shareType           = null;
  
  //  public Integer                           numBits      = null;
//  public Integer                           numCircles   = null;
}

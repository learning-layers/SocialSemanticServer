package sss.serv.eval.impl.helpers;

import at.tugraz.sss.serv.SSLabel;
import java.util.ArrayList;
import java.util.List;
import sss.serv.eval.datatypes.SSEvalLogE;

public class SSWorkedOnReceivedDiscussionInfo {
  
  public SSLabel                  discussionLabel  = null;
  public Integer                  totalActionsDone = 0;
  public List<SSEvalLogE>         actions          = new ArrayList<>();
  public List<SSEvalActionInfo>   actionDetails    = new ArrayList<>();
  public List<SSLabel>            contributors     = new ArrayList<>();
}

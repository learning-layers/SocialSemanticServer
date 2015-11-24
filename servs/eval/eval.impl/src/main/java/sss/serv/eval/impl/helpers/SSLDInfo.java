package sss.serv.eval.impl.helpers;

import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSLabel;
import at.tugraz.sss.serv.SSUri;
import java.util.ArrayList;
import java.util.List;

public class SSLDInfo {
  
  public SSUri          ldID                   = null;
  public SSLabel        ldLabel                = null;
  public List<SSEntity> attachedBitsFromDT     = new ArrayList<>();
}
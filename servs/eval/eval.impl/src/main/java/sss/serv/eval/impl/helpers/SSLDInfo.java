package sss.serv.eval.impl.helpers;

import at.tugraz.sss.serv.datatype.SSEntity;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.*;
import java.util.ArrayList;
import java.util.List;

public class SSLDInfo {
  
  public SSUri          ldID                   = null;
  public SSLabel        ldLabel                = null;
  public List<SSEntity> attachedBitsFromDT     = new ArrayList<>();
}
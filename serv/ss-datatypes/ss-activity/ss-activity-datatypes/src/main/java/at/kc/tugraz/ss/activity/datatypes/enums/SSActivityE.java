package at.kc.tugraz.ss.activity.datatypes.enums;

import at.kc.tugraz.socialserver.utils.SSStrU;
import java.util.ArrayList;
import java.util.List;

public enum SSActivityE{

  copyEntityForUsers,
  shareEntityWithUsers,
  newDiscussionByDiscussEntity,
  discussEntity,
  addDiscussionComment;

  public static String toStr(final SSActivityE type){
    return SSStrU.toStr(type);
  }
  
  public static SSActivityE get(final String value){
    return SSActivityE.valueOf(value);
  }
  
  public static List<SSActivityE> get(final List<String> values){
    
    final List<SSActivityE> result = new ArrayList<>();
    
    for(String value : values){
      result.add(get(value));
    }
    
    return result;
  }
}
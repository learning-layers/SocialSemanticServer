/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package at.kc.tugraz.sss.flag.datatypes;

import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSVarNames;
import at.tugraz.sss.serv.SSJSONLDPropI;
import java.util.ArrayList;
import java.util.List;

public enum SSFlagE implements SSJSONLDPropI{

  importance,
  deadline;

   public static List<SSFlagE> get(final List<String> values){
  
    final List<SSFlagE> result = new ArrayList<>();
    
    for(String value : values){
      result.add(get(value));
    }
    
    return result;
  }
  
  public static SSFlagE get(final String value) {
    return SSFlagE.valueOf(value);
  }

  public static List<SSFlagE> asListWithoutNullAndEmpty(final SSFlagE... flags){
   
    final List<SSFlagE> result = new ArrayList<>();
    
    if(flags == null){
      return result;
    }
    
    for(SSFlagE flag : flags){
      
      if(SSStrU.isEmpty(flag)){
        continue;
      }
      
      result.add(flag);
    }
    
    return result;
  }
  
  @Override
  public Object jsonLDDesc(){
    return SSVarNames.xsd + SSStrU.colon + SSStrU.valueString;
  }
}

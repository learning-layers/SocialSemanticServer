/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package at.kc.tugraz.ss.serv.job.dataexport.impl;

import at.kc.tugraz.ss.service.tag.datatypes.*;
import at.tugraz.sss.serv.*;
import java.util.*;

/**
 *
 * @author dtheiler
 */
public class ResourceObj {
  
  public SSUri        resource  = null;
  public List<String> tags      = new ArrayList<>();
  public long         timestamp = -1;
  
  public static Map<String, ResourceObj> getObjs(final List<SSEntity> tagAss) throws Exception {
    
    try{
      final Map<String, ResourceObj>     result = new HashMap<>();
      ResourceObj                        obj;
      String                             entity;
      
      for(SSEntity tagEntity : tagAss){
        
        entity = SSStrU.toStr(((SSTag)tagEntity).entity);
        
        if(!result.containsKey(entity)){
          result.put(entity, new ResourceObj());
        }
        
        obj          = result.get(entity);
        obj.resource = ((SSTag)tagEntity).entity;
        
        if(!SSStrU.contains(obj.tags, ((SSTag)tagEntity).label)){
          obj.tags.add(((SSTag)tagEntity).label.toString());
        }
        
        if(obj.timestamp < tagEntity.creationTime){
          obj.timestamp = tagEntity.creationTime;
        }
      }
      
      return result;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}
/**
 * Code contributed to the Learning Layers project
 * http://www.learning-layers.eu
 * Development is partly funded by the FP7 Programme of the European Commission under
 * Grant Agreement FP7-ICT-318209.
 * Copyright (c) 2015, Graz University of Technology - KTI (Knowledge Technologies Institute).
 * For a list of contributors see the AUTHORS file at the top-level directory of this distribution.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package at.tugraz.sss.serv.misc;

import at.tugraz.sss.serv.reg.SSServErrReg;
import at.tugraz.sss.serv.datatype.SSEntity;
import at.tugraz.sss.serv.datatype.*;
import java.util.HashMap;
import java.util.Map;

public class SSEntityFiller {
  
  private final Map<String, SSEntity> filledEntities = new HashMap<>();
  
  public void addFilledEntity(
    final SSUri    id,
    final SSEntity entity) throws Exception{
    
    try{
      
      if(id == null){
        return;
      }
      
      if(filledEntities.containsKey(id.toString())){
        
        if(entity != null){
          filledEntities.put(id.toString(), entity);
        }
      }else{
        filledEntities.put(id.toString(), entity);        
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public Boolean containsFilledEntity(final SSUri entityURI) throws Exception {
    
    try{
      
      if(entityURI == null){
        return false;
      }
      
      return filledEntities.containsKey(entityURI.toString());
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public Boolean containsFilledEntity(final SSEntity entity) throws Exception {
    
    try{
      
      if(entity == null){
        return false;
      }
      
      return filledEntities.containsKey(entity.id.toString());
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public SSEntity getFilledEntity(final SSEntity entity) throws Exception{
    
    try{
      
      if(entity == null){
        return null;
      }
      
      return filledEntities.getOrDefault(entity.id.toString(), null);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public SSEntity getFilledEntity(final SSUri entityURI) throws Exception{
    
    try{
      
      if(entityURI == null){
        return null;
      }
      
      return filledEntities.getOrDefault(entityURI.toString(), null);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}

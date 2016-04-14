/**
 * Code contributed to the Learning Layers project
 * http://www.learning-layers.eu
 * Development is partly funded by the FP7 Programme of the European Commission under
 * Grant Agreement FP7-ICT-318209.
 * Copyright (c) 2015, Graz University of Technology - KTI (Knowledge Technologies Institute).
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

/**
 * @author Dieter Theiler
 */

package at.tugraz.sss.servs.util;

import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.common.impl.SSServErrReg;
import at.tugraz.sss.servs.entity.datatype.SSEntity;
import java.util.HashMap;
import java.util.Map;

public class SSEntityFiller {
  
  private final Map<String, SSEntity> filledEntities = new HashMap<>();
  
  public void addFilledEntity(
    final SSUri    id,
    final SSEntity entity) throws SSErr{
    
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
  
  public boolean containsFilledEntity(final SSUri entityURI) throws SSErr {
    
    try{
      
      if(entityURI == null){
        return false;
      }
      
      return filledEntities.containsKey(entityURI.toString());
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return false;
    }
  }
  
  public boolean containsFilledEntity(final SSEntity entity) throws SSErr {
    
    try{
      
      if(entity == null){
        return false;
      }
      
      return filledEntities.containsKey(entity.id.toString());
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return false;
    }
  }
  
  public SSEntity getFilledEntity(final SSEntity entity) throws SSErr{
    
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
  
  public SSEntity getFilledEntity(final SSUri entityURI) throws SSErr{
    
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

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
package at.kc.tugraz.ss.service.disc.datatypes;

import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSVarNames;
import at.tugraz.sss.serv.SSLabel;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSEntity;
import java.util.*;

public class SSDisc extends SSEntity {
  
  public SSUri  entity = null;

  public String getEntity() throws Exception{
    return SSStrU.removeTrailingSlash(entity);
  }
  
  @Override
  public Object jsonLDDesc() {
   
    final Map<String, Object> ld = (Map<String, Object>) super.jsonLDDesc();
    
    ld.put(SSVarNames.entity, SSVarNames.sss + SSStrU.colon + SSUri.class.getName());
    
    return ld;
  }
    
  public static SSDisc get(
    final SSUri                  id,
    final SSEntityE              type,
    final SSLabel                label,
    final SSUri                  target) throws Exception{
    
    return new SSDisc(
      id, 
      type,
      label, 
      target);
  }
  
   public static SSDisc get(
    final SSDisc              disc,
    final SSEntity            entity) throws Exception{
    
    return new SSDisc(
      disc, 
      entity);
  }

  protected SSDisc(
    final SSDisc              disc,
    final SSEntity            entity) throws Exception{
    
     super(disc, entity);
     
     if(disc.entity != null){
       this.entity = disc.entity;
     }else{
       
       if(entity instanceof SSDisc){
         this.entity = ((SSDisc) entity).entity;
       }
     }
     
     this.entity = disc.entity;
   }
   
  protected SSDisc(
    final SSUri                  id,
    final SSEntityE              type,
    final SSLabel                label,
    final SSUri                  target) throws Exception{
    
    super(id, type, label);
    
    this.entity = target;
  }
}
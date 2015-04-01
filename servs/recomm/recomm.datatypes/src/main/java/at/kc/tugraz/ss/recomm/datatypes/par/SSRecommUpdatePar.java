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
package at.kc.tugraz.ss.recomm.datatypes.par;

import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSVarU;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSServErrReg;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.JsonNode;

public class SSRecommUpdatePar extends SSServPar{
  
  public String         realm       = null;
  public SSUri          forUser     = null;
  public SSUri          entity      = null;
  public List<String>   tags        = new ArrayList<>();
  public List<String>   categories  = new ArrayList<>();
  
  public SSRecommUpdatePar(
    final SSServOpE             op,
    final String              key,
    final SSUri               user,
    final String              realm,
    final SSUri               forUser, 
    final SSUri               entity, 
    final List<String>        tags,
    final List<String>        categories){
    
    super(op, key, user);
    
    this.realm   = realm;
    this.forUser = forUser;
    this.entity  = entity;
    
    if(tags != null){
      this.tags.addAll(tags);
    }
    
    if(categories != null){
      this.categories.addAll(categories);
    }
  }
   
  public SSRecommUpdatePar(final SSServPar par) throws Exception{
    
    super(par);
    
    try{
      
      if(pars != null){
        realm       = (String)       pars.get(SSVarU.realm);
        forUser     = (SSUri)        pars.get(SSVarU.forUser);
        entity      = (SSUri)        pars.get(SSVarU.entity);
        tags        = (List<String>) pars.get(SSVarU.tags);
        categories  = (List<String>) pars.get(SSVarU.categories);
      }
      
      if(par.clientJSONObj != null){
        
        realm      = par.clientJSONObj.get(SSVarU.realm).getTextValue();
        forUser    = SSUri.get  (par.clientJSONObj.get(SSVarU.forUser).getTextValue());
        entity     = SSUri.get  (par.clientJSONObj.get(SSVarU.entity).getTextValue());
        
        try{
          for (final JsonNode objNode : par.clientJSONObj.get(SSVarU.tags)) {
            tags.add(objNode.getTextValue());
          }
        }catch(Exception error){}
        
        try{
          for (final JsonNode objNode : par.clientJSONObj.get(SSVarU.categories)) {
            categories.add(objNode.getTextValue());
          }
        }catch(Exception error){}
      }   
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  /* json getters */
  public String getForUser(){
    return SSStrU.removeTrailingSlash(forUser);
  }
  
  public String getEntity(){
    return SSStrU.removeTrailingSlash(entity);
  }
}

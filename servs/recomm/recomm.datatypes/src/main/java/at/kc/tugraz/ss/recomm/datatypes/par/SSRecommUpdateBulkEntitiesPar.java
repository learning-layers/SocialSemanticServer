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
import at.tugraz.sss.serv.SSVarNames;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSServErrReg;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.JsonNode;

import at.tugraz.sss.serv.SSServErrReg;

public class SSRecommUpdateBulkEntitiesPar extends SSServPar{
  
  public String               realm       = null;
  public SSUri                forUser     = null;
  public List<SSUri>          entities    = new ArrayList<>();
  public List<List<String>>   tags        = new ArrayList<>();
  public List<List<String>>   categories  = new ArrayList<>();
  
  public SSRecommUpdateBulkEntitiesPar(
    final SSServOpE             op,
    final String              key,
    final SSUri               user,
    final String              realm,
    final SSUri               forUser, 
    final List<SSUri>         entities, 
    final List<List<String>>  tags,
    final List<List<String>>  categories){
    
    super(op, key, user);
    
    this.realm   = realm;
    this.forUser = forUser;
    
    if(entities != null){
      this.entities.addAll(entities);
    }
    
    if(tags != null){
      this.tags.addAll(tags);
    }
    
    if(categories != null){
      this.categories.addAll(categories);
    }
  }
   
  public SSRecommUpdateBulkEntitiesPar(final SSServPar par) throws Exception{
    
    super(par);
    
    try{
      
      if(pars != null){
        realm       = (String)             pars.get(SSVarNames.realm);
        forUser     = (SSUri)              pars.get(SSVarNames.forUser);
        entities    = (List<SSUri>)        pars.get(SSVarNames.entities);
        tags        = (List<List<String>>) pars.get(SSVarNames.tags);
        categories  = (List<List<String>>) pars.get(SSVarNames.categories);
      }
      
      if(par.clientJSONObj != null){
        
        realm      = par.clientJSONObj.get(SSVarNames.realm).getTextValue();
        forUser    = SSUri.get  (par.clientJSONObj.get(SSVarNames.forUser).getTextValue());
        
        for (final JsonNode objNode : par.clientJSONObj.get(SSVarNames.entities)) {
          entities.add(SSUri.get(objNode.getTextValue()));
        }
        
        try{
          List<String> entityTags;
          
          for (final JsonNode objNode : par.clientJSONObj.get(SSVarNames.tags)) {
            
            entityTags = new ArrayList<>();
            
            for(int counter = 0; counter < objNode.size(); counter++){
              entityTags.add(objNode.get(counter).getTextValue());
            }
            
            tags.add(entityTags);
          }
        }catch(Exception error){}
        
       try{
          List<String> entityCategories;
          
          for (final JsonNode objNode : par.clientJSONObj.get(SSVarNames.categories)) {
            
            entityCategories = new ArrayList<>();
            
            for(int counter = 0; counter < objNode.size(); counter++){
              entityCategories.add(objNode.get(counter).getTextValue());
            }
            
            categories.add(entityCategories);
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
  
  public List<String> getEntities() throws Exception{
    return SSStrU.removeTrailingSlash(entities);
  }
}

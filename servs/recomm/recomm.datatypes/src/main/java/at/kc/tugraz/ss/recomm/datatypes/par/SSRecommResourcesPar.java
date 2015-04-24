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

import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSVarNames;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServOpE;
import java.util.ArrayList;
import java.util.List;

public class SSRecommResourcesPar extends SSServPar{
  
  public String          realm             = null;
  public SSUri           forUser           = null;
  public SSUri           entity            = null;
  public List<String>    categories        = new ArrayList<>();
  public Integer         maxResources      = 10;
  public Boolean         setCircleTypes    = null;
  public List<SSEntityE> typesToRecommOnly = new ArrayList<>();
  public Boolean         includeOwn        = true;
  
  public void setForUser(final String forUser) {
    try{ this.forUser = SSUri.get(forUser); }catch(Exception error){}
  }
  
  public void setEntity(final String entity) {
    try{ this.entity = SSUri.get(entity); }catch(Exception error){}
  }

  public void setTypesToRecommOnly(final List<String> typesToRecommOnly){
    try{ this.typesToRecommOnly = SSEntityE.get(typesToRecommOnly); }catch(Exception error){}
  }

  public String getForUser(){
    return SSStrU.removeTrailingSlash(forUser);
  }

  public String getEntity(){
    return SSStrU.removeTrailingSlash(entity);
  }
  
  public List<String> getTypesToRecommOnly() throws Exception{
    return SSStrU.toStr(typesToRecommOnly);
  }

  public SSRecommResourcesPar(){}
  
  public SSRecommResourcesPar(
    final SSServOpE       op,
    final String          key,
    final SSUri           user,
    final String          realm, 
    final SSUri           forUser, 
    final SSUri           entity, 
    final List<String>    categories, 
    final Integer         maxResources, 
    final List<SSEntityE> typesToRecommendOnly, 
    final Boolean         setCircleTypes, 
    final Boolean         includeOwn){
    
    super(op, key, user);
    
    this.realm = realm;
    this.forUser = forUser;
    this.entity   = entity;
    
    if(categories != null){
      this.categories.addAll(categories);
    }
    
    this.maxResources = maxResources;
    
    if(typesToRecommendOnly != null){
      this.typesToRecommOnly.addAll(typesToRecommendOnly);
    }
    
    this.setCircleTypes = setCircleTypes;
    this.includeOwn     = includeOwn;
  }
    
  public static SSRecommResourcesPar get(final SSServPar par) throws Exception{
    
    try{
      
      if(par.clientCon != null){
        return (SSRecommResourcesPar) par.getFromJSON(SSRecommResourcesPar.class);
      }
      
      return new SSRecommResourcesPar(
        par.op,
        par.key,
        par.user,
        (String)          par.pars.get(SSVarNames.realm),
        (SSUri)           par.pars.get(SSVarNames.forUser),
        (SSUri)           par.pars.get(SSVarNames.entity),
        (List<String>)    par.pars.get(SSVarNames.categories),
        (Integer)         par.pars.get(SSVarNames.maxResources),
        (List<SSEntityE>) par.pars.get(SSVarNames.typesToRecommOnly),
        (Boolean)         par.pars.get(SSVarNames.setCircleTypes),
        (Boolean)         par.pars.get(SSVarNames.includeOwn));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}
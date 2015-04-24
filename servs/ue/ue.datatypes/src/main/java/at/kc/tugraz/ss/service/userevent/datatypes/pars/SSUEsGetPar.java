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
package at.kc.tugraz.ss.service.userevent.datatypes.pars;

import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSVarNames;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSServPar;
import at.kc.tugraz.ss.service.userevent.datatypes.SSUEE;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServOpE;
import java.util.ArrayList;
import java.util.List;

public class SSUEsGetPar extends SSServPar{
  
  public SSUri           forUser        = null;
  public SSUri           entity         = null;
  public List<SSUEE>     types          = new ArrayList<>();
  public Long            startTime      = null;
  public Long            endTime        = null;

  public void setForUser(final String forUser) {
    try{ this.forUser = SSUri.get(forUser); }catch(Exception error){}
  }
  
  public void setEntity(final String entity){ 
    try{ this.entity = SSUri.get(entity); }catch(Exception error){}
  }
  
  public void setTypes(final List<String> types){
    try{ this.types.addAll(SSUEE.get(types)); }catch(Exception error){}
  }
  
  public String getForUser(){
    return SSStrU.removeTrailingSlash(forUser);
  }
  
  public String getEntity(){
    return SSStrU.removeTrailingSlash(entity);
  }
  
  public List<String> getTypes() throws Exception{
    return SSStrU.toStr(types);
  }
  
  public SSUEsGetPar(){}
  
  public SSUEsGetPar(
    final SSServOpE   op, 
    final String      key, 
    final SSUri       user, 
    final SSUri       forUser, 
    final SSUri       entity, 
    final List<SSUEE> types, 
    final Long        startTime, 
    final Long        endTime){
    
    super(op, key, user);

    this.forUser = forUser;
    this.entity  = entity;
    
    if(types !=  null){
      this.types.addAll(types);
    }
    
    this.startTime = startTime;
    this.endTime   = endTime;
  }
  
  public static SSUEsGetPar get(final SSServPar par) throws Exception{
    
    try{
      
      if(par.clientCon != null){
        return (SSUEsGetPar) par.getFromJSON(SSUEsGetPar.class);
      }
      
      return new SSUEsGetPar(
        par.op,
        par.key,
        par.user,
        (SSUri)       par.pars.get(SSVarNames.forUser),
        (SSUri)       par.pars.get(SSVarNames.entity),
        (List<SSUEE>) par.pars.get(SSVarNames.types),
        (Long)        par.pars.get(SSVarNames.startTime),
        (Long)        par.pars.get(SSVarNames.endTime));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}
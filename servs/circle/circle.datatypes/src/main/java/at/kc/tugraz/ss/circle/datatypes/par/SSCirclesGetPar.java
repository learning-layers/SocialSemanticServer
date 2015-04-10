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
package at.kc.tugraz.ss.circle.datatypes.par;

import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSVarU;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSServErrReg;
import java.util.ArrayList;
import java.util.List;

public class SSCirclesGetPar extends SSServPar{

  public SSUri           forUser                  = null;
  public SSUri           entity                   = null;
  public List<SSEntityE> entityTypesToIncludeOnly = new ArrayList<>();
  public Boolean         withSystemCircles        = false;
  public Boolean         invokeEntityHandlers     = false;

  public void setForUser(final String forUser){
    try{ this.forUser = SSUri.get(forUser); }catch(Exception error){}
  }

  public void setEntity(final String entity){
    try{ this.entity = SSUri.get(entity); }catch(Exception error){}
  }

  public void setEntityTypesToIncludeOnly(final List<String> entityTypesToIncludeOnly){
    try{ this.entityTypesToIncludeOnly = SSEntityE.get(entityTypesToIncludeOnly); }catch(Exception error){}
  }

  public String getForUser(){
    return SSStrU.removeTrailingSlash(forUser);
  }
  
  public String getEntity(){
    return SSStrU.removeTrailingSlash(entity);
  }
  
  public List<String> getEntityTypesToIncludeOnly() throws Exception{
    return SSStrU.toStr(entityTypesToIncludeOnly);
  }
  
  public SSCirclesGetPar(){}
  
  public SSCirclesGetPar(
    final SSServOpE       op,
    final String          key,
    final SSUri           user,
    final SSUri           forUser,
    final SSUri           entity,
    final List<SSEntityE> entityTypesToIncludeOnly,
    final Boolean         withUserRestriction,
    final Boolean         withSystemCircles,
    final Boolean         invokeEntityHandlers) throws Exception{
    
    super(op, key, user);
    
    this.forUser              = forUser;
    this.entity               = entity;
    
    if(entityTypesToIncludeOnly != null){
      this.entityTypesToIncludeOnly.addAll(entityTypesToIncludeOnly);
    }
    
    this.withUserRestriction  = withUserRestriction;
    this.withSystemCircles    = withSystemCircles;
    this.invokeEntityHandlers = invokeEntityHandlers;
  }
  
  public static SSCirclesGetPar get(final SSServPar par) throws Exception{
    
    try{
      
      if(par.clientCon != null){
        return (SSCirclesGetPar) par.getFromJSON(SSCirclesGetPar.class);
      }
      
      return new SSCirclesGetPar(
        par.op,
        par.key,
        par.user,
        (SSUri)           par.pars.get(SSVarU.forUser),
        (SSUri)           par.pars.get(SSVarU.entity),
        (List<SSEntityE>) par.pars.get(SSVarU.entityTypesToIncludeOnly),
        (Boolean)         par.pars.get(SSVarU.withUserRestriction),
        (Boolean)         par.pars.get(SSVarU.withSystemCircles),
        (Boolean)         par.pars.get(SSVarU.invokeEntityHandlers));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}

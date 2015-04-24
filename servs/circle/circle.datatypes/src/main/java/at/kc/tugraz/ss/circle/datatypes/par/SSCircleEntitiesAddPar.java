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
import at.tugraz.sss.serv.SSVarNames;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSUri;
import java.util.ArrayList;
import java.util.List;
import at.tugraz.sss.serv.SSServErrReg;

public class SSCircleEntitiesAddPar extends SSServPar{
  
  public SSUri       circle               = null;
  public List<SSUri> entities             = new ArrayList<>();
  public Boolean     invokeEntityHandlers = true;

  public void setCircle(final String circle) throws Exception{
    this.circle = SSUri.get(circle);
  }

  public void setEntities(final List<String> entities) throws Exception{
    this.entities = SSUri.get(entities);
  }
  
  public String getCircle() throws Exception{
    return SSStrU.removeTrailingSlash(circle);
  }
   
  public List<String> getEntities() throws Exception{
    return SSStrU.removeTrailingSlash(entities);
  }
  
  public SSCircleEntitiesAddPar(){}
  
  public SSCircleEntitiesAddPar(
    final SSServOpE     op,
    final String        key,
    final SSUri         user,
    final SSUri         circle,
    final List<SSUri>   entities,
    final Boolean       withUserRestriction,
    final Boolean       invokeEntityHandlers, 
    final Boolean       shouldCommit){
    
    super(op, key, user);
    
    this.circle                 = circle;
    
    if(entities != null){
      this.entities.addAll(entities);
    }
    
    this.withUserRestriction    = withUserRestriction;
    this.invokeEntityHandlers   = invokeEntityHandlers;
    this.shouldCommit           = shouldCommit;
  }
  
  public static SSCircleEntitiesAddPar get(final SSServPar par) throws Exception{
    
    try{
      
      if(par.clientCon != null){
        return (SSCircleEntitiesAddPar) par.getFromJSON(SSCircleEntitiesAddPar.class);
      }
      
      return new SSCircleEntitiesAddPar(
        par.op,
        par.key,
        par.user,
        (SSUri)         par.pars.get(SSVarNames.circle),
        (List<SSUri>)   par.pars.get(SSVarNames.entities),
        (Boolean)       par.pars.get(SSVarNames.withUserRestriction),
        (Boolean)       par.pars.get(SSVarNames.invokeEntityHandlers), 
        (Boolean)       par.pars.get(SSVarNames.shouldCommit));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}

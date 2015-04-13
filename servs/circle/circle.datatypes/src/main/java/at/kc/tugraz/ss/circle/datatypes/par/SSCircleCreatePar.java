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
import at.tugraz.sss.serv.SSVarU;
import at.tugraz.sss.serv.SSTextComment;
import at.tugraz.sss.serv.SSLabel;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSStrU;
import java.util.ArrayList;
import java.util.List;

public class SSCircleCreatePar extends SSServPar{
  
  public SSLabel               label                = null;
  public List<SSUri>           entities             = new ArrayList<>();
  public List<SSUri>           users                = new ArrayList<>();
  public SSTextComment         description          = null;
  public Boolean               isSystemCircle       = false;
  public Boolean               invokeEntityHandlers = null;

  public void setLabel(final String label) throws Exception{
    this.label = SSLabel.get(label);
  }

  public void setEntities(final List<String> entities){
    try{ this.entities = SSUri.get(entities); }catch(Exception error){}
  }

  public void setUsers(final List<String> users){
    try{ this.users = SSUri.get(users); }catch(Exception error){}
  }

  public void setDescription(final String description){
    try{ this.description = SSTextComment.get(description); }catch(Exception error){}
  }
  
  public String getLabel() throws Exception{
    return SSStrU.toStr(label);
  }
  
  public List<String> getEntities() throws Exception{
    return SSStrU.removeTrailingSlash(entities);
  }
  
  public List<String> getUsers() throws Exception{
    return SSStrU.removeTrailingSlash(users);
  }
  
  public String getDescription() throws Exception{
    return SSStrU.toStr(description);
  }
  
  public SSCircleCreatePar(){}
    
  public SSCircleCreatePar(
    final SSServOpE       op,
    final String          key,
    final SSUri           user,
    final SSLabel         label,
    final List<SSUri>     entities,
    final List<SSUri>     users,
    final SSTextComment   description,
    final Boolean         isSystemCircle,
    final Boolean         withUserRestriction, 
    final Boolean         invokeEntityHandlers, 
    final Boolean         shouldCommit){
    
    super(op, key, user);
    
    this.label     = label;
    
    if(entities != null){
      this.entities.addAll(entities);
    }
    
    if(users != null){
      this.users.addAll(users);
    }
    
    this.description              = description;
    this.isSystemCircle           = isSystemCircle;
    this.withUserRestriction      = withUserRestriction;
    this.invokeEntityHandlers     = invokeEntityHandlers;
    this.shouldCommit             = shouldCommit;
  }
  
  public static SSCircleCreatePar get(final SSServPar par) throws Exception{
    
    try{
      
      if(par.clientCon != null){
        return (SSCircleCreatePar) par.getFromJSON(SSCircleCreatePar.class);
      }
      
      return new SSCircleCreatePar(
        par.op,
        par.key,
        par.user,
        (SSLabel)         par.pars.get(SSVarU.label),
        (List<SSUri>)     par.pars.get(SSVarU.entities),
        (List<SSUri>)     par.pars.get(SSVarU.users),
        (SSTextComment)   par.pars.get(SSVarU.description),
        (Boolean)         par.pars.get(SSVarU.isSystemCircle),
        (Boolean)         par.pars.get(SSVarU.withUserRestriction),
        (Boolean)         par.pars.get(SSVarU.invokeEntityHandlers),
        (Boolean)         par.pars.get(SSVarU.shouldCommit));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}
/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2014, Graz University of Technology - KTI (Knowledge Technologies Institute).
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

import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.SSTextComment;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.JsonNode;

public class SSCircleCreatePar extends SSServPar{
  
  public SSLabel               label                = null;
  public List<SSUri>           entities             = new ArrayList<>();
  public List<SSUri>           users                = new ArrayList<>();
  public SSTextComment         description          = null;
  public Boolean               isSystemCircle       = false;
  public Boolean               invokeEntityHandlers = null;
  
  public SSCircleCreatePar(
    final SSMethU       op,
    final String        key,
    final SSUri         user,
    final SSLabel       label,
    final List<SSUri>   entities,
    final List<SSUri>   users,
    final SSTextComment description,
    final Boolean       isSystemCircle,
    final Boolean       withUserRestriction, 
    final Boolean       invokeEntityHandlers){
    
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
  }
  
  public SSCircleCreatePar(final SSServPar par) throws Exception{
    
    super(par);
    
    try{
      
      if(pars != null){
        
        label                = (SSLabel)         pars.get(SSVarU.label);
        entities             = (List<SSUri>)     pars.get(SSVarU.entities);
        users                = (List<SSUri>)     pars.get(SSVarU.users);
        description          = (SSTextComment)   pars.get(SSVarU.description);
        isSystemCircle       = (Boolean)         pars.get(SSVarU.isSystemCircle);
        withUserRestriction  = (Boolean)         pars.get(SSVarU.withUserRestriction);
        invokeEntityHandlers = (Boolean)         pars.get(SSVarU.invokeEntityHandlers);
      }
      
      if(par.clientJSONObj != null){
        
        this.isSystemCircle = false;
        
        try{
          invokeEntityHandlers = par.clientJSONObj.get(SSVarU.invokeEntityHandlers).getBooleanValue();
        }catch(Exception error){}
        
        label          = SSLabel.get         (par.clientJSONObj.get(SSVarU.label).getTextValue());
        
        try{
          for (final JsonNode objNode : par.clientJSONObj.get(SSVarU.entities)) {
            entities.add(SSUri.get(objNode.getTextValue()));
          }
        }catch(Exception error){}
        
        try{
          for (final JsonNode objNode : par.clientJSONObj.get(SSVarU.users)) {
            users.add(SSUri.get(objNode.getTextValue()));
          }
        }catch(Exception error){}
        
        try{
          description         = SSTextComment.get(par.clientJSONObj.get(SSVarU.description).getTextValue());
        }catch(Exception error){}
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  /* json getters */
  public List<String> getEntities() throws Exception{
    return SSStrU.removeTrailingSlash(entities);
  }
  
  public List<String> getUsers() throws Exception{
    return SSStrU.removeTrailingSlash(users);
  }
  
  public String getDescription() throws Exception{
    return SSStrU.toStr(description);
  }
  
  public String getLabel() throws Exception{
    return SSStrU.toStr(label);
  }
}
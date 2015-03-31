/**
 * Copyright 2014 Graz University of Technology - KTI (Knowledge Technologies Institute)
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

import at.tugraz.sss.serv.SSMethU;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSVarU;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSServErrReg;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.JsonNode;

public class SSCircleGetPar extends SSServPar{
  
  public SSUri           forUser                    = null;
  public SSUri           circle                     = null;
  public List<SSEntityE> entityTypesToIncludeOnly   = new ArrayList<>();
  public Boolean         withSystemCircles          = false;
  public Boolean         invokeEntityHandlers       = false;
  
  public SSCircleGetPar(){}
  
  public SSCircleGetPar(
    final SSMethU         op,
    final String          key,
    final SSUri           user,
    final SSUri           forUser,
    final SSUri           circle,
    final List<SSEntityE> entityTypesToIncludeOnly,
    final Boolean         invokeEntityHandlers) throws Exception{
    
    super(op, key, user);
    
    this.forUser               = forUser;
    this.circle                = circle;
    this.invokeEntityHandlers  = invokeEntityHandlers;
    
    if(entityTypesToIncludeOnly != null){
      this.entityTypesToIncludeOnly.addAll(entityTypesToIncludeOnly);
    }
  }
  
  public SSCircleGetPar(final SSServPar par) throws Exception{
    
    super(par);
    
    try{
      
      if(pars != null){
        forUser                   = (SSUri)           pars.get(SSVarU.forUser);
        circle                    = (SSUri)           pars.get(SSVarU.circle);
        entityTypesToIncludeOnly  = (List<SSEntityE>) pars.get(SSVarU.entityTypesToIncludeOnly);
        withUserRestriction       = (Boolean)         pars.get(SSVarU.withUserRestriction);
        withSystemCircles         = (Boolean)         pars.get(SSVarU.withSystemCircles);
        invokeEntityHandlers      = (Boolean)         pars.get(SSVarU.invokeEntityHandlers);
      }
      
      if(par.clientJSONObj != null){
        
        withUserRestriction = true;
        withSystemCircles   = false;
        circle              = SSUri.get(par.clientJSONObj.get(SSVarU.circle).getTextValue());
        
        try{
          for (final JsonNode objNode : par.clientJSONObj.get(SSVarU.entityTypesToIncludeOnly)) {
            entityTypesToIncludeOnly.add(SSEntityE.get(objNode.getTextValue()));
          }
        }catch(Exception error){}
        
        try{
          invokeEntityHandlers      = par.clientJSONObj.get(SSVarU.invokeEntityHandlers).getBooleanValue();
        }catch(Exception error){}
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
   /* json getters */
  public String getCircle() throws Exception{
    return SSStrU.removeTrailingSlash(circle);
  }
  
  public List<String> getEntityTypesToIncludeOnly() throws Exception{
    return SSStrU.toStr(entityTypesToIncludeOnly);
  }
}
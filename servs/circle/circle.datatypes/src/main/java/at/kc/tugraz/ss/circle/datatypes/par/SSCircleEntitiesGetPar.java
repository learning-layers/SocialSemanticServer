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

import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSVarNames;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSServErrReg;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.JsonNode;

public class SSCircleEntitiesGetPar extends SSServPar{
  
  public SSUri           forUser               = null;
  public List<SSEntityE> types                 = new ArrayList<>();
  public Boolean         withSystemCircles     = false;
  public Boolean         invokeEntityHandlers  = false;
  
  public SSCircleEntitiesGetPar(
    final SSServOpE         op,
    final String          key,
    final SSUri           user,
    final SSUri           forUser,
    final List<SSEntityE> types,
    final Boolean         withSystemCircles,
    final Boolean         invokeEntityHandlers) throws Exception{
    
    super(op, key, user);
    
    this.forUser              = forUser;
    this.withSystemCircles    = withSystemCircles;
    this.invokeEntityHandlers = invokeEntityHandlers;
    
    if(types != null){
      this.types.addAll(types);
    }
  }
  
  public SSCircleEntitiesGetPar(SSServPar par) throws Exception{
    
    super(par);
    
    try{
      
      if(pars != null){
        forUser              = (SSUri)           pars.get(SSVarNames.forUser);
        types                = (List<SSEntityE>) pars.get(SSVarNames.types);
        withSystemCircles    = (Boolean)         pars.get(SSVarNames.withSystemCircles);
        invokeEntityHandlers = (Boolean)         pars.get(SSVarNames.invokeEntityHandlers);
      }
      
      if(par.clientJSONObj != null){
        
        withUserRestriction = true;
        
        try{
          forUser = SSUri.get(par.clientJSONObj.get(SSVarNames.forUser).getTextValue());
        }catch(Exception error){}
        
        try{
          for (final JsonNode objNode : par.clientJSONObj.get(SSVarNames.types)) {
            types.add(SSEntityE.get(objNode.getTextValue()));
          }
        }catch(Exception error){}
        
        try{
          withSystemCircles = par.clientJSONObj.get(SSVarNames.withSystemCircles).getBooleanValue();
        }catch(Exception error){}
        
        try{
          invokeEntityHandlers      = par.clientJSONObj.get(SSVarNames.invokeEntityHandlers).getBooleanValue();
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
}
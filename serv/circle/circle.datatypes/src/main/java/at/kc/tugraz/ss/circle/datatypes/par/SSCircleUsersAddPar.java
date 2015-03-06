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

import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.JsonNode;

public class SSCircleUsersAddPar extends SSServPar{

  public SSUri       circle  = null;
  public List<SSUri> users   = new ArrayList<>();
  
  public SSCircleUsersAddPar(
    final SSMethU      op,
    final String       key,
    final SSUri        user,
    final SSUri        circle,
    final List<SSUri>  users,
    final Boolean      withUserRestriction){
    
    super(op, key, user);
    
    this.circle = circle;
    
    if(users != null){
      this.users.addAll(users);
    }
    
    this.withUserRestriction      = withUserRestriction;
  }
  
  public SSCircleUsersAddPar(final SSServPar par) throws Exception{
    
    super(par);
    
    try{
      
      if(pars != null){
        circle        = (SSUri)         pars.get(SSVarU.circle);
        users         = (List<SSUri>)   pars.get(SSVarU.users);
      }
      
      if(par.clientJSONObj != null){
        
        circle        = SSUri.get (par.clientJSONObj.get(SSVarU.circle).getTextValue());
        
        for (final JsonNode objNode : par.clientJSONObj.get(SSVarU.users)) {
          users.add(SSUri.get(objNode.getTextValue()));
        }
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  /* json getters */
  public List<String> getUsers() throws Exception{
    return SSStrU.removeTrailingSlash(users);
  }
  
  public String getCircle() throws Exception{
    return SSStrU.removeTrailingSlash(circle);
  }
}
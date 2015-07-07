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
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSServPar;
import java.util.ArrayList;
import java.util.List;

public class SSCircleGetPar extends SSServPar{
  
  public SSUri           circle                     = null;
  public SSUri           forUser                    = null;
  public List<SSEntityE> entityTypesToIncludeOnly   = new ArrayList<>();
  public Boolean         invokeEntityHandlers       = false;

  public void setCircle(final String circle) throws Exception{
    this.circle = SSUri.get(circle);
  }

  public void setForUser(final String forUser){
    try{ this.forUser = SSUri.get(forUser); }catch(Exception error){}
  }

  public void setEntityTypesToIncludeOnly(final List<String> entityTypesToIncludeOnly){
    try{ this.entityTypesToIncludeOnly = SSEntityE.get(entityTypesToIncludeOnly); }catch(Exception error){}
  }
  
  public String getCircle() throws Exception{
    return SSStrU.removeTrailingSlash(circle);
  }
  
  public String getForUser() throws Exception{
    return SSStrU.removeTrailingSlash(forUser);
  }
  
  public List<String> getEntityTypesToIncludeOnly() throws Exception{
    return SSStrU.toStr(entityTypesToIncludeOnly);
  }
  
  public SSCircleGetPar(){}
  
  public SSCircleGetPar(
    final SSServOpE       op,
    final String          key,
    final SSUri           user,
    final SSUri           circle,
    final SSUri           forUser,
    final List<SSEntityE> entityTypesToIncludeOnly,
    final Boolean         withUserRestriction,
    final Boolean         invokeEntityHandlers) throws Exception{
    
    super(op, key, user);
    
    this.circle                = circle;
    this.forUser               = forUser;
    
    if(entityTypesToIncludeOnly != null){
      this.entityTypesToIncludeOnly.addAll(entityTypesToIncludeOnly);
    }
    
    this.withUserRestriction  = withUserRestriction;
    this.invokeEntityHandlers = invokeEntityHandlers;
  }
}
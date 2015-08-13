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

import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSStrU;

public class SSUEGetPar extends SSServPar{
  
  public SSUri   userEvent            = null;
  public Boolean invokeEntityHandlers = false;
  public Boolean setTags              = false;
  public Boolean setFlags             = false;

  public String getUserEvent(){
    return SSStrU.removeTrailingSlash(userEvent);
  }

  public void setUserEvent(final String userEvent) throws Exception{
    this.userEvent = SSUri.get(userEvent);
  }
  
  public SSUEGetPar(){}
  
  public SSUEGetPar(
    final SSUri     user,
    final SSUri     userEvent, 
    final Boolean   withUserRestriction, 
    final Boolean   invokeEntityHandlers){
    
    super(SSServOpE.userEventGet, null, user);
    
    this.userEvent            = userEvent;
    this.withUserRestriction  = withUserRestriction;
    this.invokeEntityHandlers = invokeEntityHandlers;
  }
}
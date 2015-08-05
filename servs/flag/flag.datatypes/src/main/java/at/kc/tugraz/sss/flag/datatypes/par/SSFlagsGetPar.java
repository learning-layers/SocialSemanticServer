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
 package at.kc.tugraz.sss.flag.datatypes.par;

import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSServPar;
import at.kc.tugraz.sss.flag.datatypes.SSFlagE;
import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSStrU;
import java.util.ArrayList;
import java.util.List;

public class SSFlagsGetPar extends SSServPar{

  public List<SSUri>   entities             = new ArrayList<>();
  public List<SSFlagE> types                = new ArrayList<>();
  public Long          startTime            = null;
  public Long          endTime              = null;
  public Boolean       invokeEntityHandlers = false;

  public List<String> getEntities(){
    return SSStrU.removeTrailingSlash(entities);
  }

  public void setEntities(final List<String> entities) throws Exception{
    this.entities = SSUri.get(entities);
  }

  public List<String> getTypes(){
    return SSStrU.toStr(types);
  }

  public void setTypes(final List<String> types){
    this.types = SSFlagE.get(types);
  }
  
  public SSFlagsGetPar(){}
    
  public SSFlagsGetPar(
    final SSUri         user,
    final List<SSUri>   entities,
    final List<SSFlagE> types,
    final Long          startTime, 
    final Long          endTime,
    final Boolean       withUserRestriction, 
    final Boolean       invokeEntityHandlers){
    
    super(SSServOpE.flagsGet, null, user);
     
    SSUri.addDistinctWithoutNull   (this.entities, entities);
    SSFlagE.addDistinctWithoutNull (this.types,    types);
    
    this.startTime            = startTime;
    this.endTime              = endTime;
    this.withUserRestriction  = withUserRestriction;
    this.invokeEntityHandlers = invokeEntityHandlers;
  }
}
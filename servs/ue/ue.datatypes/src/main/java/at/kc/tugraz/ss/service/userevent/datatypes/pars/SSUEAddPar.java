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

import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.par.SSServPar; import at.tugraz.sss.serv.util.*;
import at.kc.tugraz.ss.service.userevent.datatypes.SSUEE;

import at.tugraz.sss.serv.util.*;

public class SSUEAddPar extends SSServPar{
  
  public SSUri            entity       = null;
  public SSUEE            type         = null;
  public String           content      = null;
  public Long             creationTime = null;

  public String getEntity(){
    return SSStrU.removeTrailingSlash(entity);
  }

  public void setEntity(final String entity) throws Exception{
    this.entity = SSUri.get(entity);
  }

  public String getType(){
    return SSStrU.toStr(type);
  }

  public void setType(final String type) throws Exception{
    this.type = SSUEE.get(type);
  }
  
  public SSUEAddPar(){}
    
  public SSUEAddPar(
    final SSUri       user,
    final SSUri       entity,
    final SSUEE       type,
    final String      content,
    final Long        creationTime,
    final Boolean     withUserRestriction, 
    final Boolean     shouldCommit){
    
    super(SSVarNames.userEventAdd, null, user);
    
    this.entity              = entity;
    this.type                = type;
    this.content             = content;
    this.creationTime        = creationTime;
    this.withUserRestriction = withUserRestriction;
    this.shouldCommit        = shouldCommit;
  }
}
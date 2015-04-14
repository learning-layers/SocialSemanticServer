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
package at.kc.tugraz.sss.flag.datatypes.par;

import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSVarU;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSServErrReg;
import at.kc.tugraz.sss.flag.datatypes.SSFlagE;
import at.tugraz.sss.serv.SSServOpE;
import java.util.ArrayList;
import java.util.List;

public class SSFlagsUserGetPar extends SSServPar{
  
  public List<SSUri>   entities       = new ArrayList<>();
  public List<SSFlagE> types          = new ArrayList<>();
  public Long          startTime      = null;
  public Long          endTime        = null;
  
  public void setEntities(final List<String> entities){
    try{ this.entities = SSUri.get(entities); }catch(Exception error){}
  }
  
  public void setTypes(final List<String> types) throws Exception{
    try{ this.types = SSFlagE.get(types); }catch(Exception error){} 
  }
  
  public List<String> getEntities() throws Exception{
    return SSStrU.removeTrailingSlash(entities);
  }
  
  public List<String> getTypes() throws Exception{
    return SSStrU.toStr(types);
  }
  
  public SSFlagsUserGetPar(){}
  
  public SSFlagsUserGetPar(
    final SSServOpE     op,
    final String        key,
    final SSUri         user,
    final List<SSUri>   entities, 
    final List<SSFlagE> types, 
    final Long          startTime, 
    final Long          endTime){
    
    super(op, key, user);
    
    if(entities != null){
      this.entities.addAll(entities);
    }
    
    if(types != null){
      this.types.addAll(types);
    }
    
    this.startTime = startTime;
    this.endTime   = endTime;
  }
    
  public static SSFlagsUserGetPar get(final SSServPar par) throws Exception{
    
      try{
      
      if(par.clientCon != null){
        return (SSFlagsUserGetPar) par.getFromJSON(SSFlagsUserGetPar.class);
      }
      
      return new SSFlagsUserGetPar(
        par.op,
        par.key,
        par.user,
        (List<SSUri>)              par.pars.get(SSVarU.entities),
        (List<SSFlagE>)            par.pars.get(SSVarU.types),
        (Long)                     par.pars.get(SSVarU.startTime),
        (Long)                     par.pars.get(SSVarU.endTime));
        
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}
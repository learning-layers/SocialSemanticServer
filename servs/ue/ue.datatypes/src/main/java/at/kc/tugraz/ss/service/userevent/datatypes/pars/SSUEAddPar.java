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

import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSVarU;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSServPar;
import at.kc.tugraz.ss.service.userevent.datatypes.SSUEE;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServOpE;

public class SSUEAddPar extends SSServPar{
  
  public SSUri            entity     = null;
  public SSUEE            type       = null;
  public String           content    = null;
  
  public void setEntity(final String entity) {
    try{ this.entity = SSUri.get(entity); }catch(Exception error){}
  }  

  public void setType(final String type) throws Exception{
    this.type = SSUEE.get(type);
  }
  
  public String getEntity(){
    return SSStrU.removeTrailingSlash(entity);
  }
  
  public String getType(){
    return SSStrU.toStr(type);
  }
  
  public SSUEAddPar(){}
  
  public SSUEAddPar(
    final SSServOpE op,
    final String    key,
    final SSUri     user, 
    final SSUri     entity, 
    final SSUEE     type, 
    final String    content, 
    final Boolean   shouldCommit){
    
    super(op, key, user);
    
    this.entity       = entity;
    this.type         = type;
    this.content      = content;
    this.shouldCommit = shouldCommit;
  }
  
  public static SSUEAddPar get(final SSServPar par) throws Exception{
    
    try{
      
      if(par.clientCon != null){
        return (SSUEAddPar) par.getFromJSON(SSUEAddPar.class);
      }

      return new SSUEAddPar(
        par.op,
        par.key,
        par.user,
        (SSUri)    par.pars.get(SSVarU.entity),
        (SSUEE)    par.pars.get(SSVarU.type),
        (String)   par.pars.get(SSVarU.content),
        (Boolean)  par.pars.get(SSVarU.shouldCommit));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}
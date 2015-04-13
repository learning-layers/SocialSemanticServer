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
package at.kc.tugraz.ss.like.datatypes.par;

import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSVarU;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSServErrReg;

public class SSLikeUserSetPar extends SSServPar{
  
  public SSUri    entity        = null;
  public Integer  value         = null;

  public void setEntity(final String entity) throws Exception{
    this.entity = SSUri.get(entity);
  }
  
  public String getEntity(){
    return SSStrU.removeTrailingSlash(entity);
  }
  
  public SSLikeUserSetPar(){}
    
  public SSLikeUserSetPar(
    final SSServOpE  op,
    final String     key,
    final SSUri      user,
    final SSUri      entity,
    final Integer    value,
    final Boolean    shouldCommit){
    
    super(op, key, user);
    
    this.entity       = entity;
    this.value        = value;
    this.shouldCommit = shouldCommit;
  }
  
  public static SSLikeUserSetPar get(final SSServPar par) throws Exception{
    
    try{
      
      if(par.clientCon != null){
        return (SSLikeUserSetPar) par.getFromJSON(SSLikeUserSetPar.class);
      }
      
      return new SSLikeUserSetPar(
        par.op,
        par.key,
        par.user,
        (SSUri)   par.pars.get(SSVarU.entity),
        (Integer) par.pars.get(SSVarU.value), 
        (Boolean) par.pars.get(SSVarU.shouldCommit));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}
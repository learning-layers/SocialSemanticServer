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
 package at.kc.tugraz.ss.service.tag.datatypes.pars;

import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSVarU;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSServPar;
import at.kc.tugraz.ss.service.tag.datatypes.SSTagLabel;
import at.tugraz.sss.serv.SSServErrReg;
 
public class SSTagUserEditPar extends SSServPar{
  
  public SSTagLabel      tag     = null;
  public SSUri           entity  = null;
  public SSTagLabel      label   = null;

  public void setTag(final String tag) throws Exception{
    this.tag = SSTagLabel.get(tag);
  }

  public void setEntity(final String entity){
    try{ this.entity = SSUri.get(entity); }catch(Exception error){}
  }

  public void setLabel(final String label) throws Exception{
    this.label = SSTagLabel.get(label);
  }
  
  public String getTag(){
    return SSStrU.toStr(tag);
  }
  
  public String getLabel(){
    return SSStrU.toStr(label);
  }
  
  public String getEntity(){
    return SSStrU.removeTrailingSlash(entity);
  }
  
  public SSTagUserEditPar(){}
  
  public SSTagUserEditPar(
    final SSServOpE  op,
    final String     key, 
    final SSUri      user, 
    final SSTagLabel tag, 
    final SSUri      entity,
    final SSTagLabel label,
    final Boolean    shouldCommit){
    
    super(op, key, user);
    
    this.tag          = tag;
    this.entity       = entity;
    this.label        = label;
    this.shouldCommit = shouldCommit;
  }
  
  public static SSTagUserEditPar get(final SSServPar par) throws Exception{
      
    try{
      
      if(par.clientCon != null){
        return (SSTagUserEditPar) par.getFromJSON(SSTagUserEditPar.class);
      }
      
      return new SSTagUserEditPar(
        par.op,
        par.key,
        par.user,
        (SSTagLabel) par.pars.get(SSVarU.tag), 
        (SSUri)      par.pars.get(SSVarU.entity),
        (SSTagLabel) par.pars.get(SSVarU.label),
        (Boolean)    par.pars.get(SSVarU.shouldCommit));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}
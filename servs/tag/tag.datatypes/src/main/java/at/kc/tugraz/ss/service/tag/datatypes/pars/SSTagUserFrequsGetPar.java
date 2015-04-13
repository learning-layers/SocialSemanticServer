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
import at.tugraz.sss.serv.SSSpaceE;
import at.tugraz.sss.serv.SSServPar;
import at.kc.tugraz.ss.service.tag.datatypes.SSTagLabel;
import java.util.ArrayList;
import java.util.List;
import at.tugraz.sss.serv.SSServErrReg;

public class SSTagUserFrequsGetPar extends SSServPar{

  public SSUri              forUser              = null;
  public List<SSUri>        entities             = new ArrayList<>();
  public List<SSTagLabel>   labels               = new ArrayList<>();
  public SSSpaceE           space                = null;
  public Long               startTime            = null;
  public Boolean            useUsersEntities     = false;

  public void setForUser(final String forUser){
    try{ this.forUser = SSUri.get(forUser); }catch(Exception error){}
  } 

  public void setEntities(final List<String> entities){
    try{ this.entities = SSUri.get(entities); }catch(Exception error){}
  }

  public void setLabels(final List<String> labels){
    try{ this.labels = SSTagLabel.get(labels); }catch(Exception error){}
  }

  public void setSpace(final String space){
    try{ this.space = SSSpaceE.get(space); }catch(Exception error){}
  }
  
  public String getForUser(){
    return SSStrU.removeTrailingSlash(forUser);
  }

  public List<String> getEntities() throws Exception{
    return SSStrU.removeTrailingSlash(entities);
  }

  public List<String> getLabels() throws Exception{
    return SSStrU.toStr(labels);
  }

  public String getSpace(){
    return SSStrU.toStr(space);
  }
  
  public SSTagUserFrequsGetPar(){}
   
  public SSTagUserFrequsGetPar(
    final SSServOpE          op,
    final String             key, 
    final SSUri              user, 
    final SSUri              forUser, 
    final List<SSUri>        entities, 
    final List<SSTagLabel>   labels, 
    final SSSpaceE           space, 
    final Long               startTime,
    final Boolean            useUsersEntities){
    
    super(op, key, user);
    
    this.forUser = forUser;
    
    if(entities != null){
      this.entities.addAll(entities);
    }
    
    if(labels != null){
      this.labels.addAll(labels);
    }
    
    this.space                = space;
    this.startTime            = startTime;
    this.useUsersEntities     = useUsersEntities;
  }
    
  public static SSTagUserFrequsGetPar get(final SSServPar par) throws Exception{
    
    try{
      
      if(par.clientCon != null){
        return (SSTagUserFrequsGetPar) par.getFromJSON(SSTagUserFrequsGetPar.class);
      }
      
      return new SSTagUserFrequsGetPar(
        par.op,
        par.key,
        par.user,
        (SSUri)                        par.pars.get(SSVarU.forUser),
        (List<SSUri>)                  par.pars.get(SSVarU.entities),
        (List<SSTagLabel>)             par.pars.get(SSVarU.labels),
        (SSSpaceE)                     par.pars.get(SSVarU.space),
        (Long)                         par.pars.get(SSVarU.startTime),
        (Boolean)                      par.pars.get(SSVarU.useUsersEntities));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}
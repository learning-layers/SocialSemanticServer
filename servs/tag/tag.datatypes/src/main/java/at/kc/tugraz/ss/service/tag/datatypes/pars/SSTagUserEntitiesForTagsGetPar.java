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
import at.tugraz.sss.serv.SSVarNames;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSSpaceE;
import at.kc.tugraz.ss.service.tag.datatypes.SSTagLabel;
import at.tugraz.sss.serv.SSServPar;
import java.util.ArrayList;
import java.util.List;
import at.tugraz.sss.serv.SSServErrReg;

public class SSTagUserEntitiesForTagsGetPar extends SSServPar{

  public SSUri             forUser   = null;
  public List<SSTagLabel>  labels    = new ArrayList<>();
  public SSSpaceE          space     = null;
  public Long              startTime = null;

  public void setForUser(final String forUser){
    try{ this.forUser = SSUri.get(forUser); }catch(Exception error){}
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

  public List<String> getLabels() throws Exception{
    return SSStrU.toStr(labels);
  }

  public String getSpace(){
    return SSStrU.toStr(space);
  }
  
  public SSTagUserEntitiesForTagsGetPar(){}
    
  public SSTagUserEntitiesForTagsGetPar(
    final SSServOpE        op,
    final String           key,
    final SSUri            user,
    final SSUri            forUser,
    final List<SSTagLabel> labels,
    final SSSpaceE         space,
    final Long             startTime){
    
    super(op, key, user);
    
    this.forUser = forUser;
    
    if(labels != null){
      this.labels.addAll(labels);
    }
    
    this.space     = space;
    this.startTime = startTime;
  }
  
  public static SSTagUserEntitiesForTagsGetPar get(final SSServPar par) throws Exception{
    
    try{
     
      if(par.clientCon != null){
        return (SSTagUserEntitiesForTagsGetPar) par.getFromJSON(SSTagUserEntitiesForTagsGetPar.class);
      }
      
      return new SSTagUserEntitiesForTagsGetPar(
        par.op,
        par.key,
        par.user,
        (SSUri)                       par.pars.get(SSVarNames.forUser),
        (List<SSTagLabel>)            par.pars.get(SSVarNames.labels),
        (SSSpaceE)                    par.pars.get(SSVarNames.space),
        (Long)                        par.pars.get(SSVarNames.startTime));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}
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
package at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par;

import at.tugraz.sss.serv.SSVarU;
import at.tugraz.sss.serv.SSTextComment;
import at.tugraz.sss.serv.SSLabel;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSJSONU;
import java.util.ArrayList;
import java.util.List;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSStrU;

public class SSEntityUserUpdatePar extends SSServPar{
  
  public SSUri               entity        = null;
  public SSLabel             label         = null;
  public SSTextComment       description   = null;
  public List<SSTextComment> comments      = new ArrayList<>();
  public Boolean             read          = null;

  public String getEntity(){
    return SSStrU.removeTrailingSlash(entity);
  }

  public void setEntity(final String entity) throws Exception{
    this.entity = SSUri.get(entity);
  }

  public String getLabel(){
    return SSStrU.toStr(label);
  }

  public void setLabel(final String label){
    try{ this.label = SSLabel.get(label); }catch(Exception error){}
  }
    
  public String getDescription(){
    return SSStrU.toStr(description);
  }

  public void setDescription(final String description){
    try{ this.description = SSTextComment.get(description); }catch(Exception error){}
  }

  public List<String> getComments() throws Exception{
    return  SSStrU.toStr(comments);
  }

  public void setComments(final List<String> comments){
    try{ this.comments = SSTextComment.get(comments); }catch(Exception error){}
  }

  public Boolean getRead(){
    return read;
  }

  public void setRead(Boolean read){
    this.read = read;
  }
  
  public SSEntityUserUpdatePar(
    final SSServOpE           op,
    final String              key,
    final SSUri               user,
    final SSUri               entity,
    final SSLabel             label,
    final SSTextComment       description, 
    final List<SSTextComment> comments,
    final Boolean             read){
    
    super(op, key, user);
    
    this.entity        = entity;
    this.label         = label;
    this.description   = description;
    
    if(comments != null){
      this.comments.addAll(comments);
    }
    
    this.read = read;
  }
  
  public SSEntityUserUpdatePar(){}
  
  public static SSEntityUserUpdatePar get(final SSServPar par) throws Exception{
    
    try{
      
      if(par.clientCon != null){
        return (SSEntityUserUpdatePar) par.getFromJSON(SSEntityUserUpdatePar.class);
      }
      
      return new SSEntityUserUpdatePar(
        par.op,
        par.key,
        par.user,
        (SSUri)               par.pars.get(SSVarU.entity),
        (SSLabel)             par.pars.get(SSVarU.label),
        (SSTextComment)       par.pars.get(SSVarU.description),
        (List<SSTextComment>) par.pars.get(SSVarU.comments),
        (Boolean)             par.pars.get(SSVarU.read));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}
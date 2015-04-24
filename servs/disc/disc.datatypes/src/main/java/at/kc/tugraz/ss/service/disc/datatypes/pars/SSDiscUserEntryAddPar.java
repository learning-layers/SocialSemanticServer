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
 package at.kc.tugraz.ss.service.disc.datatypes.pars;

import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSVarNames;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSTextComment;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSLabel;
import at.tugraz.sss.serv.SSServOpE;
import java.util.ArrayList;
import java.util.List;

public class SSDiscUserEntryAddPar extends SSServPar{
  
  public SSUri               disc           = null;
  public SSUri               entity         = null;
  public SSTextComment       entry          = null;
  public Boolean             addNewDisc     = null;
  public SSEntityE           type           = null;
  public SSLabel             label          = null;
  public SSTextComment       description    = null;
  public List<SSUri>         users          = new ArrayList<>();
  public List<SSUri>         circles        = new ArrayList<>();
  public List<SSUri>         entities       = new ArrayList<>();
  
  public void setDisc(final String disc){
    try{ this.disc = SSUri.get(disc); }catch(Exception error){}
  }
  
  public void setEntity(final String entity){
    try{ this.entity = SSUri.get(entity); }catch(Exception error){}
  }
  
  public void setEntry(final String entry){
    try{ this.entry = SSTextComment.get(entry); }catch(Exception error){}
  }
  
  public void setType(final String type){
    try{ this.type = SSEntityE.get(type); }catch(Exception error){}
  }
  
  public void setLabel(final String label){
    try{ this.label = SSLabel.get(label); }catch(Exception error){}
  }

  public void setDescription(final String description){
    try{ this.description = SSTextComment.get(description); }catch(Exception error){}
  }
  
  public void setUsers(final List<String> users){
    try{ this.users = SSUri.get(users); }catch(Exception error){}
  }
  
  public void setCircles(final List<String> circles){
    try{ this.circles = SSUri.get(circles); }catch(Exception error){}
  }
  
  public void setEntities(final List<String> entities){
    try{ this.entities = SSUri.get(entities); }catch(Exception error){}
  }
  
  public String getDisc(){
    return SSStrU.removeTrailingSlash(disc);
  }

  public String getEntity(){
    return SSStrU.removeTrailingSlash(entity);
  }

  public String getEntry(){
    return SSStrU.toStr(entry);
  }

  public String getType(){
    return SSStrU.toStr(type);
  }

  public String getLabel(){
    return SSStrU.toStr(label);
  }

  public String getDescription(){
    return SSStrU.toStr(description);
  }

  public List<String> getUsers() throws Exception{
    return SSStrU.removeTrailingSlash(users);
  }

  public List<String> getEntities() throws Exception{
    return SSStrU.removeTrailingSlash(entities);
  }
  
  public SSDiscUserEntryAddPar(){}
    
  public SSDiscUserEntryAddPar(
    final SSServOpE     op,
    final String        key,
    final SSUri         user,
    final SSUri         disc,
    final SSUri         entity, 
    final SSTextComment entry, 
    final Boolean       addNewDisc,
    final SSEntityE     type, 
    final SSLabel       label, 
    final SSTextComment description, 
    final List<SSUri>   users, 
    final List<SSUri>   circles, 
    final List<SSUri>   entities, 
    final Boolean       shouldCommit){
    
    super(op, key, user);
  
    this.disc        = disc;
    this.entity      = entity;
    this.entry       = entry;
    this.addNewDisc  = addNewDisc;
    this.type        = type;
    this.label       = label;
    this.description = description;
    
    if(users != null){
      this.users.addAll(users);
    }
    
    if(circles!= null){
      this.circles.addAll(circles);
    }
    
    if(entities != null){
      this.entities.addAll(entities);
    }
    
    this.shouldCommit = shouldCommit;
  }
  
  public static SSDiscUserEntryAddPar get(final SSServPar par) throws Exception{
      
    try{

      if(par.clientCon != null){
        return (SSDiscUserEntryAddPar) par.getFromJSON(SSDiscUserEntryAddPar.class);
      }
      
      return new SSDiscUserEntryAddPar(
        par.op,
        par.key,
        par.user, 
        (SSUri)              par.pars.get(SSVarNames.disc),
        (SSUri)              par.pars.get(SSVarNames.entity),
        (SSTextComment)      par.pars.get(SSVarNames.entry), 
        (Boolean)            par.pars.get(SSVarNames.addNewDisc), 
        (SSEntityE)          par.pars.get(SSVarNames.type),
        (SSLabel)            par.pars.get(SSVarNames.label),
        (SSTextComment)      par.pars.get(SSVarNames.description),
        (List<SSUri>)        par.pars.get(SSVarNames.users),
        (List<SSUri>)        par.pars.get(SSVarNames.circles),
        (List<SSUri>)        par.pars.get(SSVarNames.entities),
        (Boolean)            par.pars.get(SSVarNames.shouldCommit));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}

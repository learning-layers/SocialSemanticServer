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
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSTextComment;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSLabel;
import at.tugraz.sss.serv.SSServOpE;
import java.util.ArrayList;
import java.util.List;

public class SSDiscEntryAddPar extends SSServPar{
  
  public SSUri               disc           = null;
  public List<SSUri>         targets        = new ArrayList<>();
  public SSTextComment       entry          = null;
  public Boolean             addNewDisc     = null;
  public SSEntityE           type           = null;
  public SSLabel             label          = null;
  public SSTextComment       description    = null;
  public List<SSUri>         entities       = new ArrayList<>();
  public List<SSLabel>       entityLabels   = new ArrayList<>();

  public void setDisc(final String disc) throws Exception{
    this.disc = SSUri.get(disc);
  }

  public void setTargets(final List<String> targets) throws Exception{
    this.targets.addAll(SSUri.get(targets));
  }
  
  public void setEntry(final String entry) throws Exception{
    this.entry = SSTextComment.get(entry); 
  }
  
  public void setType(final String type) throws Exception{
    this.type = SSEntityE.get(type); 
  }
  
  public void setLabel(final String label) throws Exception{
    this.label = SSLabel.get(label); 
  }

  public void setDescription(final String description) throws Exception{
    this.description = SSTextComment.get(description); 
  }
  
  public void setEntities(final List<String> entities) throws Exception{
    this.entities = SSUri.get(entities); 
  }
  
  public void setEntityLabels(final List<String> entityLabels) throws Exception{
    this.entityLabels = SSLabel.get(entityLabels);
  }
  
  public String getDisc(){
    return SSStrU.removeTrailingSlash(disc);
  }

  public List<String> getTargets(){
    return SSStrU.removeTrailingSlash(targets);
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

  public List<String> getEntities() throws Exception{
    return SSStrU.removeTrailingSlash(entities);
  }
  
  public List<String> getEntityLabels(){
    return SSStrU.toStr(entityLabels);
  }
  
  public SSDiscEntryAddPar(){}
    
  public SSDiscEntryAddPar(
    final SSServOpE     op,
    final String        key,
    final SSUri         user,
    final SSUri         disc,
    final List<SSUri>   targets, 
    final SSTextComment entry, 
    final Boolean       addNewDisc,
    final SSEntityE     type, 
    final SSLabel       label, 
    final SSTextComment description, 
    final List<SSUri>   entities, 
    final List<SSLabel> entityLabels,
    final Boolean       shouldCommit){
    
    super(op, key, user);
  
    this.disc        = disc;
    
    SSUri.addDistinctWithoutNull(this.targets, targets);
    
    this.entry       = entry;
    this.addNewDisc  = addNewDisc;
    this.type        = type;
    this.label       = label;
    this.description = description;

    SSUri.addDistinctWithoutNull(this.entities, entities);
    
    if(entityLabels != null){
      this.entityLabels.addAll(entityLabels);
    }
    
    this.shouldCommit = shouldCommit;
  }
}

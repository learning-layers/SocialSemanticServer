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
 package at.tugraz.sss.servs.disc.datatype;

import at.tugraz.sss.servs.util.SSVarNames;
import at.tugraz.sss.servs.util.SSStrU;
import at.tugraz.sss.servs.entity.datatype.SSEntityE;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.entity.datatype.SSLabel;
import at.tugraz.sss.servs.entity.datatype.SSServPar; 
import at.tugraz.sss.servs.entity.datatype.SSTextComment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SSDiscEntryAddPar extends SSServPar{
  
  public SSUri               disc           = null;
  public List<SSUri>         targets        = new ArrayList<>();
  public SSTextComment       entry          = null;
  public boolean             addNewDisc     = false;
  public SSEntityE           type           = null;
  public SSLabel             label          = null;
  public SSTextComment       description    = null;
  public List<SSUri>         entities       = new ArrayList<>();
  public List<SSLabel>       entityLabels   = new ArrayList<>();

  public void setDisc(final String disc) throws SSErr{
    this.disc = SSUri.get(disc);
  }

  public void setTargets(final List<String> targets) throws SSErr{
    this.targets.addAll(SSUri.get(targets));
  }
  
  public void setEntry(final String entry) throws SSErr{
    this.entry = SSTextComment.get(entry); 
  }
  
  public void setType(final String type) throws SSErr{
    this.type = SSEntityE.get(type); 
  }
  
  public void setLabel(final String label) throws SSErr{
    this.label = SSLabel.get(label); 
  }

  public void setDescription(final String description) throws SSErr{
    this.description = SSTextComment.get(description); 
  }
  
  public void setEntities(final List<String> entities) throws SSErr{
    this.entities = SSUri.get(entities); 
  }
  
  public void setEntityLabels(final List<String> entityLabels) throws SSErr{
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

  public List<String> getEntities() throws SSErr{
    return SSStrU.removeTrailingSlash(entities);
  }
  
  public List<String> getEntityLabels(){
    return SSStrU.toStr(entityLabels);
  }
  
  public SSDiscEntryAddPar(){/* Do nothing because of only JSON Jackson needs this */ }
    
  public SSDiscEntryAddPar(
    final SSServPar servPar,
    final SSUri         user,
    final SSUri         disc,
    final List<SSUri>   targets, 
    final SSTextComment entry, 
    final boolean       addNewDisc,
    final SSEntityE     type, 
    final SSLabel       label, 
    final SSTextComment description, 
    final List<SSUri>   entities, 
    final List<SSLabel> entityLabels,
    final boolean       shouldCommit){
    
    super(SSVarNames.discEntryAdd, null, user, servPar.sqlCon);
  
    this.disc        = disc;
    
    SSUri.addDistinctWithoutNull(this.targets, targets);
    
    this.entry       = entry;
    this.addNewDisc  = addNewDisc;
    this.type        = type;
    this.label       = label;
    this.description = description;

    SSUri.addDistinctWithoutNull   (this.entities,     entities);
    SSLabel.addDistinctNotNull     (this.entityLabels, entityLabels);
    
    this.shouldCommit = shouldCommit;
  }
}

/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2014, Graz University of Technology - KTI (Knowledge Technologies Institute).
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
package at.tugraz.sss.serv;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSTextComment;
import at.tugraz.sss.serv.SSLabel;

import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSServPar; import at.tugraz.sss.serv.SSVarNames;
import at.tugraz.sss.serv.SSStrU;

public class SSEntityUpdatePar extends SSServPar{

  public SSUri               entity            = null;
  public SSEntityE           type              = null;
  public SSLabel             label             = null;
  public SSTextComment       description       = null;
  public Long                creationTime      = null;
  public Boolean             read              = null;
  public Boolean             setPublic         = false;
  public Boolean             createIfNotExists = false;

  public Boolean             fromClient       = false;
  
  public String getEntity(){
    return SSStrU.removeTrailingSlash(entity);
  }

  public void setEntity(final String entity) throws Exception{
    this.entity = SSUri.get(entity);
  }
  
  public String getType(){
    return SSStrU.toStr(type);
  }

  public void setType(final String type) throws Exception{
    this.type = SSEntityE.get(type);
  }
  
  public String getLabel(){
    return SSStrU.toStr(label);
  }

  public void setLabel(final String label) throws Exception{
    this.label = SSLabel.get(label);
  }

  public String getDescription(){
    return SSStrU.toStr(description);
  }

  public void setDescription(final String description) throws Exception{
    this.description = SSTextComment.get(description);
  }

  public SSEntityUpdatePar(){}
  
  public SSEntityUpdatePar(
    final SSUri               user,
    final SSUri               entity,
    final SSEntityE           type, 
    final SSLabel             label,
    final SSTextComment       description,
    final Long                creationTime, 
    final Boolean             read,
    final Boolean             setPublic,
    final Boolean             createIfNotExists, 
    final Boolean             withUserRestriction, 
    final Boolean             shouldCommit){

    super(SSVarNames.entityUpdate, null, user);
  
    this.entity              = entity;
    this.type                = type;
    this.label               = label;
    this.description         = description;
    this.creationTime        = creationTime;
    this.read                = read;
    this.setPublic           = setPublic;
    this.createIfNotExists   = createIfNotExists;
    this.withUserRestriction = withUserRestriction;
    this.shouldCommit        = shouldCommit;
  }
}
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


import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSSpaceE;
import at.tugraz.sss.serv.SSServPar; import at.tugraz.sss.serv.SSVarNames;
import at.kc.tugraz.ss.service.tag.datatypes.SSTagLabel;
import at.tugraz.sss.serv.SSVarNames;

public class SSTagsRemovePar extends SSServPar{
  
  public SSUri        forUser    = null;
  public SSUri        entity     = null;
  public SSTagLabel   label      = null;
  public SSSpaceE     space      = null;
  public SSUri        circle     = null;

  public void setForUser(final String forUser) throws Exception{
    this.forUser = SSUri.get(forUser);
  }
  
  public void setEntity(final String entity) throws Exception{
    this.entity = SSUri.get(entity);
  }

  public void setLabel(final String label) throws Exception{
    this.label = SSTagLabel.get(label);
  }

  public void setSpace(final String space) throws Exception{
    this.space =  SSSpaceE.get(space);
  }
  
  public void setCircle(final String circle) throws Exception{
    this.circle = SSUri.get(circle);
  }
  
  public String getForUser(){
    return SSStrU.removeTrailingSlash(forUser);
  }
  
  public String getEntity(){
    return SSStrU.removeTrailingSlash(entity);
  }
  
  public String getLabel(){
    return SSStrU.toStr(label);
  }
  
  public String getSpace(){
    return SSStrU.toStr(space);
  }
  
   public String getCircle(){
    return SSStrU.removeTrailingSlash(circle);
  }
  
  public SSTagsRemovePar(){}
  
  public SSTagsRemovePar(
    final SSUri        user,
    final SSUri        forUser,
    final SSUri        entity,
    final SSTagLabel   label,
    final SSSpaceE     space,
    final SSUri        circle,
    final Boolean      withUserRestriction,
    final Boolean      shouldCommit){
  
    super(SSVarNames.tagsRemove, null, user);
  
    this.forUser             = forUser;
    this.entity              = entity;
    this.label               = label;
    this.space               = space;
    this.circle              = circle;
    this.withUserRestriction = withUserRestriction;
    this.shouldCommit        = shouldCommit;
  }
}
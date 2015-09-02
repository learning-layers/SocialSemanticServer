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
package at.tugraz.sss.servs.file.datatype.par;

import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSLabel;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSStrU;

public class SSEntityFileAddPar extends SSServPar{
  
  public SSUri     file     = null;
  public SSEntityE type     = null;
  public SSLabel   label    = null;
  public SSUri     entity   = null;

  public String getFile(){
    return SSStrU.removeTrailingSlash(file);
  }

  public void setFile(final String file) throws Exception{
    this.file = SSUri.get(file);
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

  public String getEntity(){
    return SSStrU.removeTrailingSlash(entity);
  }

  public void setEntity(final String entity) throws Exception{
   this.entity = SSUri.get(entity);
  }
  
  public SSEntityFileAddPar(){} 
  
  public SSEntityFileAddPar(
    final SSUri         user,
    final SSUri         file,
    final SSEntityE     type,
    final SSLabel       label,
    final SSUri         entity, 
    final Boolean       withUserRestriction, 
    final Boolean       shouldCommit) {
    
    super(SSServOpE.fileAdd, null, user);
    
    this.file                 = file;
    this.type                 = type;
    this.label                = label;
    this.entity               = entity;
    this.withUserRestriction  = withUserRestriction;
    this.shouldCommit         = shouldCommit;
  }
}
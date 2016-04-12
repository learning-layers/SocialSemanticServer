/**
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
package at.tugraz.sss.servs.video.datatype;

import at.tugraz.sss.servs.util.SSVarNames;
import at.tugraz.sss.servs.util.SSStrU;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.entity.datatype.SSLabel;
import at.tugraz.sss.servs.video.datatype.SSVideoE;

import at.tugraz.sss.servs.entity.datatype.SSTextComment;
import at.tugraz.sss.servs.entity.datatype.SSServPar; 
import java.sql.*;

public class SSVideoUserAddPar extends SSServPar{
  
  public String                uuid             = null;
  public SSUri                 link             = null;
  public SSVideoE              type             = null;
  public SSUri                 forEntity        = null;
  public String                genre            = null;
  public SSLabel               label            = null;
  public SSTextComment         description      = null;
  public Long                  creationTime     = null;
  public SSUri                 file             = null;

  public void setLink(final String link) throws SSErr{
    this.link = SSUri.get(link);
  }

  public String getLink(){
    return SSStrU.removeTrailingSlash(link);
  }
  
  public void setType(final String type) throws SSErr{
    this.type = SSVideoE.get(type);
  }

  public String getType(){
    return SSStrU.toStr(type);
  }
  
  public void setForEntity(final String forEntity) throws SSErr{
    this.forEntity = SSUri.get(forEntity);
  }

  public String getForEntity(){
    return SSStrU.removeTrailingSlash(forEntity);
  }
  
  public void setLabel(final String label) throws SSErr{
    this.label = SSLabel.get(label);
  }

   public String getLabel(){
    return SSStrU.toStr(label);
  }
   
  public void setDescription(final String description) throws SSErr{
    this.description = SSTextComment.get(description);
  }

  public String getDescription(){
    return SSStrU.toStr(description);
  }
  
  public void setFile(final String file) throws SSErr{
    this.file = SSUri.get(file);
  }

  public String getFile(){
    return SSStrU.toStr(file);
  }
  
  public SSVideoUserAddPar(){/* Do nothing because of only JSON Jackson needs this */ }
  
  public SSVideoUserAddPar(
    final SSServPar servPar,
    final SSUri          user,
    final String         uuid,
    final SSUri          link,
    final SSVideoE       type,
    final SSUri          forEntity,
    final String         genre,
    final SSLabel        label,
    final SSTextComment  description,
    final Long           creationTime,
    final SSUri          file, 
    final boolean        withUserRestriction, 
    final boolean        shouldCommit){
    
    super(SSVarNames.videoAdd, null, user, servPar.sqlCon);
    
    this.uuid                = uuid;
    this.link                = link;
    this.type                = type;
    this.forEntity           = forEntity;
    this.genre               = genre;
    this.label               = label;
    this.description         = description;
    this.creationTime        = creationTime;
    this.file                = null;
    this.withUserRestriction = withUserRestriction;
    this.shouldCommit        = shouldCommit;
  }
}
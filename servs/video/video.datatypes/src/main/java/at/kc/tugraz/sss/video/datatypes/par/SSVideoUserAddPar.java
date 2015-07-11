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
package at.kc.tugraz.sss.video.datatypes.par;

import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSTextComment;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSLabel;
import at.tugraz.sss.serv.SSServPar;

public class SSVideoUserAddPar extends SSServPar{
  
  public String                uuid             = null;
  public SSUri                 link             = null;
  public SSUri                 forEntity        = null;
  public String                genre            = null;
  public SSLabel               label            = null;
  public SSTextComment         description      = null;
  public Long                  creationTime     = null;
  public Double                latitude         = null;
  public Double                longitude        = null;
  public Float                 accuracy         = null;

  public void setLink(final String link) throws Exception{
    this.link = SSUri.get(link);
  }

  public void setForEntity(final String forEntity) throws Exception{
    this.forEntity = SSUri.get(forEntity);
  }

  public void setLabel(final String label) throws Exception{
    this.label = SSLabel.get(label);
  }

  public void setDescription(final String description) throws Exception{
    this.description = SSTextComment.get(description);
  }

  public String getForEntity(){
    return SSStrU.removeTrailingSlash(forEntity);
  }
  
  public String getLink(){
    return SSStrU.removeTrailingSlash(link);
  }
  
  public String getLabel(){
    return SSStrU.toStr(label);
  }

  public String getDescription(){
    return SSStrU.toStr(description);
  }
  
  public SSVideoUserAddPar(){}
  
  public SSVideoUserAddPar(
    final SSServOpE      op,
    final String         key,
    final SSUri          user,
    final String         uuid,
    final SSUri          link,
    final SSUri          forEntity,
    final String         genre,
    final SSLabel        label,
    final SSTextComment  description,
    final Long           creationTime,
    final Double         latitude,
    final Double         longitude,
    final Float          accuracy, 
    final Boolean        withUserRestriction, 
    final Boolean        shouldCommit){
    
    super(op, key, user);
    
    this.uuid                = uuid;
    this.link                = link;
    this.forEntity           = forEntity;
    this.genre               = genre;
    this.label               = label;
    this.description         = description;
    this.creationTime        = creationTime;
    this.latitude            = latitude;
    this.longitude           = longitude;
    this.accuracy            = accuracy;
    this.withUserRestriction = withUserRestriction;
    this.shouldCommit        = shouldCommit;
  }
}
/**
 * Code contributed to the Learning Layers project
 * http://www.learning-layers.eu
 * Development is partly funded by the FP7 Programme of the European Commission under
 * Grant Agreement FP7-ICT-318209.
 * Copyright (c) 2015, Graz University of Technology - KTI (Knowledge Technologies Institute).
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

/**
 * @author Dieter Theiler
 */

package at.tugraz.sss.servs.image.datatype;

import at.tugraz.sss.servs.util.SSVarNames;
import at.tugraz.sss.servs.util.SSStrU;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.entity.datatype.SSLabel;
import at.tugraz.sss.servs.entity.datatype.SSServPar; 

public class SSImageAddPar extends SSServPar{
  
  public String   uuid                                 = null;  
  public SSUri    link                                 = null;
  public SSImageE imageType                            = null;
  public SSUri    entity                               = null;
  public SSUri    file                                 = null;
  public SSLabel  label                                = null;
  public boolean  createThumb                          = false;
  public boolean  isImageToAddTheThumb                 = false;
  public boolean  removeThumbsFromEntity               = false;
  
  public String getLink(){
    return SSStrU.removeTrailingSlash(link);
  }
  
  public void setLink(final String link) throws SSErr{
    this.link = SSUri.get(link);
  }
  
  public String getEntity(){
    return SSStrU.removeTrailingSlash(entity);
  }

  public String getFile(){
    return SSStrU.removeTrailingSlash(file);
  }
  
  public String getLabel(){
    return SSStrU.toStr(label);
  }
  
  public void setEntity(final String entity) throws SSErr{
    this.entity = SSUri.get(entity);
  }
  
  public String getImageType(){
    return SSStrU.toStr(imageType);
  }
  
  public void setImageType(final String imageType) throws SSErr{
    this.imageType = SSImageE.get(imageType);
  }
  
  public void setFile(final String file) throws SSErr{
    this.file = SSUri.get(file);
  }
  
  public void setLabel(final String label) throws SSErr{
    this.label = SSLabel.get(label);
  }
  
  public SSImageAddPar(){/* Do nothing because of only JSON Jackson needs this */ }
  
  public SSImageAddPar(
    final SSServPar servPar,
    final SSUri         user,
    final String        uuid,
    final SSUri         link,
    final SSImageE      imageType,
    final SSUri         entity,
    final SSUri         file,
    final SSLabel       label,
    final boolean       createThumb,
    final boolean       isImageToAddTheThumb,
    final boolean       removeThumbsFromEntity, 
    final boolean       withUserRestriction,
    final boolean       shouldCommit){
    
    super(SSVarNames.imageAdd, null, user, servPar.sqlCon);
    
    this.uuid                                 = uuid;
    this.link                                 = link;
    this.imageType                            = imageType;
    this.entity                               = entity;
    this.file                                 = file;
    this.label                                = label;
    this.createThumb                          = createThumb;
    this.isImageToAddTheThumb                 = isImageToAddTheThumb;
    this.removeThumbsFromEntity               = removeThumbsFromEntity;
    this.withUserRestriction                  = withUserRestriction;
    this.shouldCommit                         = shouldCommit;
  }
}
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

import at.tugraz.sss.servs.entity.datatype.SSServPar; 

public class SSImagesGetPar extends SSServPar{
  
  public SSUri    entity    = null;
  public SSImageE imageType = null;

  public String getEntity(){
    return SSStrU.removeTrailingSlash(entity);
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
  
  public SSImagesGetPar(){/* Do nothing because of only JSON Jackson needs this */ }
  
  public SSImagesGetPar(
    final SSServPar servPar,
    final SSUri         user,
    final SSUri         entity,
    final SSImageE      imageType,
    final boolean       withUserRestriction){
    
    super(SSVarNames.imagesGet, null, user, servPar.sqlCon);
    
    this.entity              = entity;
    this.imageType           = imageType;
    this.withUserRestriction = withUserRestriction;
  }
}
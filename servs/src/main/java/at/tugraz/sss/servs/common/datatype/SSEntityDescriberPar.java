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

package at.tugraz.sss.servs.common.datatype;

import at.tugraz.sss.servs.util.SSStrU;
import at.tugraz.sss.servs.entity.datatype.SSEntityE;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.entity.datatype.SSSpaceE;
import java.util.ArrayList;
import java.util.List;

public class SSEntityDescriberPar{
  
  public SSUri       recursiveEntity     = null;     
  public SSUri       user                = null;
  public SSSpaceE    space               = null;
  public SSUri       circle              = null;
  public boolean     withUserRestriction = true;
  public boolean     setAttachedEntities = false;
  public boolean     setRead             = false;
  public boolean     setFiles            = false;
  public boolean     setTags             = false;
  public boolean     setOverallRating    = false;
  public boolean     setDiscs            = false;
  public boolean     setUEs              = false;
  public boolean     setThumb            = false;
  public boolean     setFlags            = false;
  public boolean     setCircles          = false;
  public boolean     setComments         = false;
  public boolean     setFriends          = false;
  public boolean     setCategories       = false;
  public boolean     setCircleTypes      = false;
  public boolean     setLocations        = false;
  public boolean     setLikes            = false;
  public boolean     setProfilePicture   = false;
  public boolean     setMessages         = false;
  public boolean     setActivities       = false;
  public boolean     setColls            = false;
  
  public List<SSEntityE> entityTypesToIncludeOnly = new ArrayList<>();

  public String getCircle() {
    return SSStrU.removeTrailingSlash(circle);
  }

  public void setCircle(final String circle) throws SSErr {
    this.circle = SSUri.get(circle);
  }
  
  public String getRecursiveEntity() {
    return SSStrU.removeTrailingSlash(recursiveEntity);
  }

  public void setRecursiveEntity(final String recursiveEntity) throws SSErr {
    this.recursiveEntity = SSUri.get(recursiveEntity);
  }

  public List<String> getEntityTypesToIncludeOnly() {
    return SSStrU.toStr(entityTypesToIncludeOnly);
  }

  public void setEntityTypesToIncludeOnly(final List<String> entityTypesToIncludeOnly) throws SSErr {
    this.entityTypesToIncludeOnly = SSEntityE.get(entityTypesToIncludeOnly);
  }
  
  public SSEntityDescriberPar(){/* Do nothing because of only JSON Jackson needs this */ }
  
  public SSEntityDescriberPar(
    final SSUri         recursiveEntity){
    
    this.recursiveEntity = recursiveEntity;
  }
}
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
package at.tugraz.sss.serv;

import java.util.ArrayList;
import java.util.List;

public class SSEntityDescriberPar{
  
  public SSUri recursiveEntity           = null;     
  
  public SSUri       user                = null;
  public Boolean     withUserRestriction = null;
  public Boolean     setAttachedEntities = false;
  public Boolean     setRead             = false;
  public Boolean     setFiles            = false;
  public Boolean     setTags             = false;
  public Boolean     setOverallRating    = false;
  public Boolean     setDiscs            = false;
  public Boolean     setUEs              = false;
  public Boolean     setThumb            = false;
  public Boolean     setFlags            = false;
  public Boolean     setCircles          = false;
  public Boolean     setComments         = false;
  public Boolean     setFriends          = false;
  public Boolean     setCategories       = false;
  public Boolean     setCircleTypes      = false;
  public Boolean     setLocations        = false;
  
  public List<SSEntityE> entityTypesToIncludeOnly = new ArrayList<>();

  public String getRecursiveEntity() {
    return SSStrU.removeTrailingSlash(recursiveEntity);
  }

  public void setRecursiveEntity(final String recursiveEntity) throws Exception {
    this.recursiveEntity = SSUri.get(recursiveEntity);
  }

  public List<String> getEntityTypesToIncludeOnly() {
    return SSStrU.toStr(entityTypesToIncludeOnly);
  }

  public void setEntityTypesToIncludeOnly(final List<String> entityTypesToIncludeOnly) throws Exception {
    this.entityTypesToIncludeOnly = SSEntityE.get(entityTypesToIncludeOnly);
  }
  
  public SSEntityDescriberPar(){}
  
  public SSEntityDescriberPar(final SSUri recursiveEntity){
    this.recursiveEntity = recursiveEntity;
  }
}
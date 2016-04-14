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

package at.tugraz.sss.servs.rating.datatype;

import at.tugraz.sss.servs.util.SSVarNames;
import at.tugraz.sss.servs.util.SSStrU;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.entity.datatype.SSServPar; 
import java.util.ArrayList;
import java.util.List;

public class SSRatingEntityURIsGetPar extends SSServPar{

  public List<SSUri> entities         = new ArrayList<>();
  public Integer     minOverallRating = null;
  public Integer     maxOverallRating = null;

  public List<String> getEntities() {
    return SSStrU.removeTrailingSlash(entities);
  }

  public void setEntities(final List<String> entities) throws SSErr {
    this.entities = SSUri.get(entities);
  }
  
  public SSRatingEntityURIsGetPar(){/* Do nothing because of only JSON Jackson needs this */ }
  
  public SSRatingEntityURIsGetPar(
    final SSServPar   servPar,
    final SSUri       user,
    final List<SSUri> entities,
    final Integer     minOverallRating,
    final Integer     maxOverallRating,
    final boolean     withUserRestriction){
    
    super(SSVarNames.ratingEntityURIsGet, null, user, servPar.sqlCon);
    
    SSUri.addDistinctWithoutNull(this.entities, entities);
    
    this.minOverallRating    = minOverallRating;
    this.maxOverallRating    = maxOverallRating;
    this.withUserRestriction = withUserRestriction;
  }
}
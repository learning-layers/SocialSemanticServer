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

package at.tugraz.sss.servs.rating.api;

import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.rating.datatype.SSRatingOverall;
import at.tugraz.sss.servs.rating.datatype.SSRatingEntityURIsGetPar;
import at.tugraz.sss.servs.rating.datatype.SSRatingSetPar;
import at.tugraz.sss.servs.rating.datatype.SSRatingGetPar;
import at.tugraz.sss.servs.rating.datatype.SSRatingOverallGetPar;
import at.tugraz.sss.servs.rating.datatype.SSRatingsRemovePar;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.common.api.SSServServerI;
import java.util.List;

public interface SSRatingServerI extends SSServServerI{
  
  public boolean               ratingSet           (final SSRatingSetPar           par)     throws SSErr;
  public Integer               ratingGet           (final SSRatingGetPar           par)     throws SSErr;
  public SSRatingOverall       ratingOverallGet    (final SSRatingOverallGetPar    par)     throws SSErr;
  public boolean               ratingsRemove       (final SSRatingsRemovePar       par)     throws SSErr;
  public List<SSUri>           ratingEntityURIsGet (final SSRatingEntityURIsGetPar par)     throws SSErr;
}

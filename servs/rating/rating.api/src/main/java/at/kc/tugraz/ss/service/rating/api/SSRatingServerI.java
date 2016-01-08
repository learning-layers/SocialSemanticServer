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
 package at.kc.tugraz.ss.service.rating.api;

import at.kc.tugraz.ss.service.rating.datatypes.SSRatingOverall;
import at.kc.tugraz.ss.service.rating.datatypes.pars.SSRatingEntityURIsGetPar;
import at.kc.tugraz.ss.service.rating.datatypes.pars.SSRatingSetPar;
import at.kc.tugraz.ss.service.rating.datatypes.pars.SSRatingGetPar;
import at.kc.tugraz.ss.service.rating.datatypes.pars.SSRatingOverallGetPar;
import at.kc.tugraz.ss.service.rating.datatypes.pars.SSRatingsRemovePar;
import at.tugraz.sss.serv.datatype.SSErr;
import at.tugraz.sss.serv.impl.api.SSServServerI;
import at.tugraz.sss.serv.datatype.*;
import java.util.List;

public interface SSRatingServerI extends SSServServerI{
  
  public boolean               ratingSet           (final SSRatingSetPar           par)     throws SSErr;
  public Integer               ratingGet           (final SSRatingGetPar           par)     throws SSErr;
  public SSRatingOverall       ratingOverallGet    (final SSRatingOverallGetPar    par)     throws SSErr;
  public boolean               ratingsRemove       (final SSRatingsRemovePar       par)     throws SSErr;
  public List<SSUri>           ratingEntityURIsGet (final SSRatingEntityURIsGetPar par)     throws SSErr;
}

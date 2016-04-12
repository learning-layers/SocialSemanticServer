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
package at.tugraz.sss.servs.recomm.api;

import at.tugraz.sss.servs.recomm.datatype.SSResourceLikelihood;
import at.tugraz.sss.servs.recomm.datatype.SSUserLikelihood;
import at.tugraz.sss.servs.recomm.datatype.SSRecommLoadUserRealmsPar;
import at.tugraz.sss.servs.recomm.datatype.SSRecommResourcesPar;
import at.tugraz.sss.servs.recomm.datatype.SSRecommTagsPar;
import at.tugraz.sss.servs.recomm.datatype.SSRecommUpdateBulkEntitiesPar;
import at.tugraz.sss.servs.recomm.datatype.SSRecommUpdateBulkPar;
import at.tugraz.sss.servs.recomm.datatype.SSRecommUpdateBulkUserRealmsFromCirclesPar;
import at.tugraz.sss.servs.recomm.datatype.SSRecommUpdateBulkUserRealmsFromConfPar;
import at.tugraz.sss.servs.recomm.datatype.SSRecommUpdatePar;
import at.tugraz.sss.servs.recomm.datatype.SSRecommUsersPar;
import at.tugraz.sss.servs.tag.datatype.SSTagLikelihood;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.common.api.SSServServerI;
import java.util.List;

public interface SSRecommServerI extends SSServServerI{

  public List<SSUserLikelihood>     recommUsers                             (final SSRecommUsersPar                           par) throws SSErr;
  public List<SSTagLikelihood>      recommTags                              (final SSRecommTagsPar                            par) throws SSErr;
  public List<SSResourceLikelihood> recommResources                         (final SSRecommResourcesPar                       par) throws SSErr;
  public void                       recommUpdateBulk                        (final SSRecommUpdateBulkPar                      par) throws SSErr;
  public void                       recommUpdateBulkUserRealmsFromConf      (final SSRecommUpdateBulkUserRealmsFromConfPar    par) throws SSErr;
  public void                       recommUpdateBulkUserRealmsFromCircles   (final SSRecommUpdateBulkUserRealmsFromCirclesPar par) throws SSErr;
  public boolean                    recommUpdate                            (final SSRecommUpdatePar                          par) throws SSErr;
  public boolean                    recommUpdateBulkEntities                (final SSRecommUpdateBulkEntitiesPar              par) throws SSErr;
  public void                       recommLoadUserRealms                    (final SSRecommLoadUserRealmsPar                  par) throws SSErr;
}

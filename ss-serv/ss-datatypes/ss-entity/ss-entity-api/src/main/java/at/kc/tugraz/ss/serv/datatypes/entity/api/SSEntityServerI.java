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
package at.kc.tugraz.ss.serv.datatypes.entity.api;

import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityDescA;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSEntityCircle;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSEntity;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSCircleE;
import at.kc.tugraz.ss.service.user.datatypes.SSUser;
import java.util.List;

public interface SSEntityServerI {
  
  public SSLabel                      entityLabelGet                           (final SSServPar parA) throws Exception;
  public SSUri                           entityAuthorGet                          (final SSServPar parA) throws Exception;
  public SSUri                           entityLabelSet                           (final SSServPar parA) throws Exception;
  public SSUri                           entityUserCircleCreate                   (final SSServPar parA) throws Exception;
  public SSUri                           entityUserUsersToCircleAdd               (final SSServPar parA) throws Exception;
  public SSUri                           entityUserEntitiesToCircleAdd            (final SSServPar parA) throws Exception;
  public Boolean                         entityUserAllowedIs                      (final SSServPar parA) throws Exception;
  public SSUri                           entityUserPublicSet                      (final SSServPar parA) throws Exception;
  public SSUri                           entityUserShare                          (final SSServPar parA) throws Exception;
  public List<SSEntityCircle>                  entityUserCirclesGet                     (final SSServPar parA) throws Exception;
  public List<SSCircleE>       entityUserEntityCircleTypesGet           (final SSServPar parA) throws Exception;
  public List<SSEntityCircle>                  entityUserEntityCirclesGet               (final SSServPar parA) throws Exception; 
  public List<SSUser>                    entityUserEntityUsersGet                 (final SSServPar parA) throws Exception;
  
  public SSEntity                        entityGet                                (final SSServPar parA) throws Exception;
  public List<SSUri>                     entitySearchWithKeywordWithin            (final SSServPar parA) throws Exception;
  public SSUri                           entityCircleURIPublicGet                 (final SSServPar parA) throws Exception;
  public SSUri                           entityCircleCreate                       (final SSServPar parA) throws Exception;
  public SSUri                           entityCirclePublicAdd                    (final SSServPar parA) throws Exception;
  public SSUri                           entityEntitiesToCircleAdd                (final SSServPar parA) throws Exception;
  public SSUri                           entityUsersToCircleAdd                   (final SSServPar parA) throws Exception;
  public SSCircleE                       entityMostOpenCircleTypeGet              (final SSServPar parA) throws Exception;
  public SSEntityE                       entityTypeGet                            (final SSServPar parA) throws Exception;
  public SSEntityDescA                   entityDescGet                            (final SSServPar parA) throws Exception;
  public SSUri                           entityAdd                                (final SSServPar parA) throws Exception;
  public SSUri                           entityAddAtCreationTime                  (final SSServPar parA) throws Exception;
  public Long                            entityCreationTimeGet                    (final SSServPar parA) throws Exception;
  public SSUri                           entityUserDirectlyAdjoinedEntitiesRemove (final SSServPar parA) throws Exception;
  public SSUri                           entityRemove                             (final SSServPar parA) throws Exception;
}
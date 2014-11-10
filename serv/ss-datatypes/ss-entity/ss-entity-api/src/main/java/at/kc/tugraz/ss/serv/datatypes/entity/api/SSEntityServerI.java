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

import at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityA;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityCircle;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntity;
import at.kc.tugraz.ss.datatypes.datatypes.SSCircleE;
import at.kc.tugraz.ss.datatypes.datatypes.SSImage;
import at.kc.tugraz.ss.datatypes.datatypes.SSVideo;
import java.util.List;

public interface SSEntityServerI {

  public SSEntity                        entityUserGet                            (final SSServPar parA) throws Exception;
  public SSEntity                        entityDescGet                            (final SSServPar parA) throws Exception;
  public List<SSEntityA>                 entityDescsGet                           (final SSServPar parA) throws Exception;
  public SSUri                           entityUserCircleCreate                   (final SSServPar parA) throws Exception;
  public SSUri                           entityUserUsersToCircleAdd               (final SSServPar parA) throws Exception;
  public SSUri                           entityUserEntitiesToCircleAdd            (final SSServPar parA) throws Exception;
  public void                            entityUserCan                            (final SSServPar parA) throws Exception;
  public SSUri                           entityUserPublicSet                      (final SSServPar parA) throws Exception;
  public SSUri                           entityUserShare                          (final SSServPar parA) throws Exception;
  public List<SSEntityCircle>            entityUserCirclesGet                     (final SSServPar parA) throws Exception;
  public List<SSCircleE>                 entityUserEntityCircleTypesGet           (final SSServPar parA) throws Exception;
  public List<SSEntityCircle>            entityUserEntityCirclesGet               (final SSServPar parA) throws Exception; 
  public List<SSEntity>                  entityUserEntityUsersGet                 (final SSServPar parA) throws Exception;
  public List<SSUri>                     entityUserSubEntitiesGet                 (final SSServPar parA) throws Exception;
  public List<SSUri>                     entityUserParentEntitiesGet              (final SSServPar parA) throws Exception;
  public Boolean                         entityUserCopy                           (final SSServPar parA) throws Exception;
  public SSUri                           entityUserUpdate                         (final SSServPar parA) throws Exception;
  public List<SSUri>                     entityUserEntitiesAttach                 (final SSServPar parA) throws Exception;
  public SSEntityCircle                  entityUserCircleGet                      (final SSServPar parA) throws Exception;
  
  public List<SSEntityCircle>            entityEntityCirclesGet                   (final SSServPar parA) throws Exception;
  public List<SSEntity>                  entityEntitiesAttachedGet                (final SSServPar parA) throws Exception;
  public SSUri                           entityFileAdd                            (final SSServPar parA) throws Exception;
  public List<SSUri>                     entityFilesGet                           (final SSServPar parA) throws Exception;
  public SSUri                           entityThumbAdd                           (final SSServPar parA) throws Exception;
  public List<SSUri>                     entityThumbsGet                          (final SSServPar parA) throws Exception;
  public List<SSEntity>                  entitiesForLabelsAndDescriptionsGet      (final SSServPar parA) throws Exception;
  public List<SSEntity>                  entitiesForLabelsGet                     (final SSServPar parA) throws Exception;
  public List<SSEntity>                  entitiesForDescriptionsGet               (final SSServPar parA) throws Exception;
  public Boolean                         entityExists                             (final SSServPar parA) throws Exception;
  public SSEntity                        entityGet                                (final SSServPar parA) throws Exception;
  public SSUri                           entityCircleURIPrivGet                   (final SSServPar parA) throws Exception;
  public SSUri                           entityCircleURIPubGet                    (final SSServPar parA) throws Exception;
  public SSUri                           entityCircleCreate                       (final SSServPar parA) throws Exception;
  public void                            entityEntityToPrivCircleAdd              (final SSServPar parA) throws Exception;
  public void                            entityEntityToPubCircleAdd               (final SSServPar parA) throws Exception;
  public SSUri                           entityEntitiesToCircleAdd                (final SSServPar parA) throws Exception;
  public SSUri                           entityUsersToCircleAdd                   (final SSServPar parA) throws Exception;
  public SSCircleE                       entityMostOpenCircleTypeGet              (final SSServPar parA) throws Exception;
  public SSUri                           entityAdd                                (final SSServPar parA) throws Exception;
  public SSUri                           entityUserDirectlyAdjoinedEntitiesRemove (final SSServPar parA) throws Exception;
  public SSUri                           entityRemove                             (final SSServPar parA) throws Exception;
  public SSUri                           entityUpdate                             (final SSServPar parA) throws Exception;
  public Boolean                         entityReadGet                            (final SSServPar parA) throws Exception;
  public List<SSVideo>                   entityVideosGet                          (final SSServPar parA) throws Exception;
  public List<SSImage>                   entityScreenShotsGet                     (final SSServPar parA) throws Exception;
  public List<SSUri>                     entityDownloadURIsGet                    (final SSServPar parA) throws Exception;
}
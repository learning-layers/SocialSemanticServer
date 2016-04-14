/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2014, Graz University of Technology - KTI (Knowledge Technologies Institute).
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

package at.tugraz.sss.servs.entity.api;

import at.tugraz.sss.servs.entity.datatype.SSEntityE;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.entity.datatype.SSCircle;
import at.tugraz.sss.servs.entity.datatype.SSEntity;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSEntitiesAccessibleGetRet;
import at.tugraz.sss.servs.entity.datatype.SSCircleIsEntityPublicPar;
import at.tugraz.sss.servs.entity.datatype.SSEntityUpdatePar;
import at.tugraz.sss.servs.entity.datatype.SSCircleRemovePar;
import at.tugraz.sss.servs.entity.datatype.SSCircleGetPar;
import at.tugraz.sss.servs.entity.datatype.SSCircleUsersInvitePar;
import at.tugraz.sss.servs.entity.datatype.SSCircleTypesGetPar;
import at.tugraz.sss.servs.entity.datatype.SSEntityAttachEntitiesPar;
import at.tugraz.sss.servs.entity.datatype.SSCircleIsEntityPrivatePar;
import at.tugraz.sss.servs.entity.datatype.SSCircleIsEntitySharedPar;
import at.tugraz.sss.servs.common.datatype.SSEntityCopyPar;
import at.tugraz.sss.servs.entity.datatype.SSCirclesGetPar;
import at.tugraz.sss.servs.entity.datatype.SSEntityDownloadURIsGetPar;
import at.tugraz.sss.servs.entity.datatype.SSCircleTypeChangePar;
import at.tugraz.sss.servs.entity.datatype.SSEntityGetPar;
import at.tugraz.sss.servs.entity.datatype.SSEntityEntitiesAttachedRemovePar;
import at.tugraz.sss.servs.entity.datatype.SSCirclePrivURIGetPar;
import at.tugraz.sss.servs.entity.datatype.SSEntityRemovePar;
import at.tugraz.sss.servs.entity.datatype.SSEntityDownloadsAddPar;
import at.tugraz.sss.servs.entity.datatype.SSEntitySharePar;
import at.tugraz.sss.servs.entity.datatype.SSEntitiesAccessibleGetPar;
import at.tugraz.sss.servs.entity.datatype.SSCircleEntitiesRemovePar;
import at.tugraz.sss.servs.entity.datatype.SSEntityFromTypeAndLabelGetPar;
import at.tugraz.sss.servs.entity.datatype.SSCirclePubURIGetPar;
import at.tugraz.sss.servs.entity.datatype.SSEntityURIsGetPar;
import at.tugraz.sss.servs.entity.datatype.SSEntitiesGetPar;
import at.tugraz.sss.servs.entity.datatype.SSEntityUnpublicizePar;
import at.tugraz.sss.servs.entity.datatype.SSEntityTypesGetPar;
import at.tugraz.sss.servs.entity.datatype.SSCircleEntitiesAddPar;
import at.tugraz.sss.servs.entity.datatype.SSCircleCreatePar;
import at.tugraz.sss.servs.entity.datatype.SSCircleAddEntitiesToCircleOfEntityPar;
import at.tugraz.sss.servs.entity.datatype.SSCircleUsersAddPar;
import at.tugraz.sss.servs.entity.datatype.SSCircleUsersRemovePar;
import at.tugraz.sss.servs.entity.datatype.SSEntitiesAccessibleGetCleanUpPar;
import at.tugraz.sss.servs.entity.datatype.SSCircleE;
import at.tugraz.sss.servs.common.api.SSServServerI;
import java.util.List;

public interface SSEntityServerI extends SSServServerI{

  public List<SSEntityE>                 entityTypesGet                (final SSEntityTypesGetPar                par) throws SSErr;
  public List<SSEntity>                  entitiesGet                   (final SSEntitiesGetPar                   par) throws SSErr;
  public List<SSUri>                     entityURIsGet                 (final SSEntityURIsGetPar                 par) throws SSErr;
  public SSUri                           entityUpdate                  (final SSEntityUpdatePar                  par) throws SSErr;
  public boolean                         entityCopy                    (final SSEntityCopyPar                    par) throws SSErr;
  public SSEntity                        entityGet                     (final SSEntityGetPar                     par) throws SSErr;
  public List<SSEntity>                  entityFromTypeAndLabelGet     (final SSEntityFromTypeAndLabelGetPar     par) throws SSErr;
  public SSUri                           entityShare                   (final SSEntitySharePar                   par) throws SSErr;
  public List<SSUri>                     entityDownloadsGet            (final SSEntityDownloadURIsGetPar         par) throws SSErr;
  public SSUri                           entityDownloadsAdd            (final SSEntityDownloadsAddPar            par) throws SSErr;
  public SSUri                           entityEntitiesAttach          (final SSEntityAttachEntitiesPar          par) throws SSErr;
  public SSUri                           entityEntitiesAttachedRemove  (final SSEntityEntitiesAttachedRemovePar  par) throws SSErr;
  public SSUri                           entityUnpublicize             (final SSEntityUnpublicizePar             par) throws SSErr;
  public SSEntitiesAccessibleGetRet      entitiesAccessibleGet         (final SSEntitiesAccessibleGetPar         par) throws SSErr;
  public void                            entitiesAccessibleGetCleanUp  (final SSEntitiesAccessibleGetCleanUpPar  par) throws SSErr;
  public SSUri                           entityRemove                  (final SSEntityRemovePar                  par) throws SSErr;
  
  public SSUri                           circleCreate                             (final SSCircleCreatePar                      par) throws SSErr;
  public SSUri                           circleUsersAdd                           (final SSCircleUsersAddPar                    par) throws SSErr;
  public SSUri                           circleEntitiesAdd                        (final SSCircleEntitiesAddPar                 par) throws SSErr;
  public List<SSCircleE>                 circleTypesGet                           (final SSCircleTypesGetPar                    par) throws SSErr;
  public SSCircle                        circleGet                                (final SSCircleGetPar                         par) throws SSErr;
  public List<SSEntity>                  circlesGet                               (final SSCirclesGetPar                        par) throws SSErr;
  public SSUri                           circlePrivURIGet                         (final SSCirclePrivURIGetPar                  par) throws SSErr;
  public SSUri                           circlePubURIGet                          (final SSCirclePubURIGetPar                   par) throws SSErr;
  public List<SSUri>                     circleEntitiesRemove                     (final SSCircleEntitiesRemovePar              par) throws SSErr;
  public List<SSUri>                     circleUsersRemove                        (final SSCircleUsersRemovePar                 par) throws SSErr;
  public SSUri                           circleRemove                             (final SSCircleRemovePar                      par) throws SSErr;
  public SSUri                           circleUsersInvite                        (final SSCircleUsersInvitePar                 par) throws SSErr;
  public boolean                         circleIsEntityPrivate                    (final SSCircleIsEntityPrivatePar             par) throws SSErr;
  public boolean                         circleIsEntityShared                     (final SSCircleIsEntitySharedPar              par) throws SSErr;
  public boolean                         circleIsEntityPublic                     (final SSCircleIsEntityPublicPar              par) throws SSErr;
  public SSUri                           circleTypeChange                         (final SSCircleTypeChangePar                  par) throws SSErr;
  public void                            circleAddEntitiesToCirclesOfEntity       (final SSCircleAddEntitiesToCircleOfEntityPar par) throws SSErr;
}
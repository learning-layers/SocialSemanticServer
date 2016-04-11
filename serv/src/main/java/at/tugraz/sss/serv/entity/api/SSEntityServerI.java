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
package at.tugraz.sss.serv.entity.api;

import at.tugraz.sss.serv.datatype.SSCircle;
import at.tugraz.sss.serv.datatype.SSEntity;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.SSErr;
import at.tugraz.sss.serv.datatype.ret.SSEntitiesAccessibleGetRet;
import at.tugraz.sss.serv.datatype.par.SSCircleIsEntityPublicPar;
import at.tugraz.sss.serv.datatype.par.SSEntityUpdatePar;
import at.tugraz.sss.serv.datatype.par.SSCircleRemovePar;
import at.tugraz.sss.serv.datatype.par.SSCircleGetPar;
import at.tugraz.sss.serv.datatype.par.SSCircleUsersInvitePar;
import at.tugraz.sss.serv.datatype.par.SSCircleTypesGetPar;
import at.tugraz.sss.serv.datatype.par.SSEntityAttachEntitiesPar;
import at.tugraz.sss.serv.datatype.par.SSCircleIsEntityPrivatePar;
import at.tugraz.sss.serv.datatype.par.SSCircleIsEntitySharedPar;
import at.tugraz.sss.serv.datatype.par.SSEntityCopyPar;
import at.tugraz.sss.serv.datatype.par.SSCirclesGetPar;
import at.tugraz.sss.serv.datatype.par.SSEntityDownloadURIsGetPar;
import at.tugraz.sss.serv.datatype.par.SSCircleTypeChangePar;
import at.tugraz.sss.serv.datatype.par.SSEntityGetPar;
import at.tugraz.sss.serv.datatype.par.SSEntityEntitiesAttachedRemovePar;
import at.tugraz.sss.serv.datatype.par.SSCirclePrivURIGetPar;
import at.tugraz.sss.serv.datatype.par.SSEntityRemovePar;
import at.tugraz.sss.serv.datatype.par.SSEntityDownloadsAddPar;
import at.tugraz.sss.serv.datatype.par.SSEntitySharePar;
import at.tugraz.sss.serv.datatype.par.SSEntitiesAccessibleGetPar;
import at.tugraz.sss.serv.datatype.par.SSCircleEntitiesRemovePar;
import at.tugraz.sss.serv.datatype.par.SSEntityFromTypeAndLabelGetPar;
import at.tugraz.sss.serv.datatype.par.SSCirclePubURIGetPar;
import at.tugraz.sss.serv.datatype.par.SSEntityURIsGetPar;
import at.tugraz.sss.serv.datatype.par.SSEntitiesGetPar;
import at.tugraz.sss.serv.datatype.par.SSEntityUnpublicizePar;
import at.tugraz.sss.serv.datatype.par.SSEntityTypesGetPar;
import at.tugraz.sss.serv.datatype.par.SSCircleEntitiesAddPar;
import at.tugraz.sss.serv.datatype.par.SSCircleCreatePar;
import at.tugraz.sss.serv.datatype.par.SSCircleAddEntitiesToCircleOfEntityPar;
import at.tugraz.sss.serv.datatype.par.SSCircleUsersAddPar;
import at.tugraz.sss.serv.datatype.par.SSCircleUsersRemovePar;
import at.tugraz.sss.serv.datatype.par.SSEntitiesAccessibleGetCleanUpPar;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.datatype.enums.SSCircleE;
import at.tugraz.sss.serv.impl.api.SSServServerI;
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
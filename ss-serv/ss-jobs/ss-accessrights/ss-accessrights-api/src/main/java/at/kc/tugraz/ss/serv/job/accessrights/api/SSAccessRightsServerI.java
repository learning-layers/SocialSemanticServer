/**
 * Copyright 2014 Graz University of Technology - KTI (Knowledge Technologies Institute)
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
package at.kc.tugraz.ss.serv.job.accessrights.api;

import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSAccessRightsCircleTypeE;
import at.kc.tugraz.ss.serv.job.accessrights.datatypes.SSCircle;
import java.util.List;

public interface SSAccessRightsServerI{
  
  public SSUri                           accessRightsCirclePublicAdd             (final SSServPar parA) throws Exception;
  public SSUri                           accessRightsCircleURIPublicGet          (final SSServPar parA) throws Exception;
  public SSUri                           accessRightsUserCircleCreate            (final SSServPar parA) throws Exception;
  public SSAccessRightsCircleTypeE       accessRightsEntityMostOpenCircleTypeGet (final SSServPar parA) throws Exception;
  public void                            accessRightsCircleUserAdd               (final SSServPar parA) throws Exception;
  public List<SSAccessRightsCircleTypeE> accessRightsUserCircleTypesForEntityGet (final SSServPar parA) throws Exception;
  public Boolean                         accessRightsUserEntitiesToCircleAdd     (final SSServPar parA) throws Exception;
  public List<SSCircle>                  accessRightsUserEntityCirclesGet        (final SSServPar parA) throws Exception;
  public Boolean                         accessRightsUserUsersToCircleAdd        (final SSServPar parA) throws Exception;
  public List<SSCircle>                  accessRightsUserCirclesGet              (final SSServPar parA) throws Exception;
  public List<SSUri>                     accessRightsEntityCircleURIsGet         (final SSServPar parA) throws Exception;
  public Boolean                         accessRightsUserAllowedIs               (final SSServPar parA) throws Exception;
}

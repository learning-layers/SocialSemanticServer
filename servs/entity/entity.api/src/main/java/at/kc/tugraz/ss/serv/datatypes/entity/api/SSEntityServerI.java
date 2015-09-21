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

import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSEntityCopyPar;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSServServerI;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntitiesGetPar;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityAttachEntitiesPar;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityDownloadURIsGetPar;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityDownloadsAddPar;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityEntitiesAttachedRemovePar;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityFromTypeAndLabelGetPar;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityGetPar;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntitySharePar;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityTypesGetPar;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityUpdatePar;
import java.util.List;

public interface SSEntityServerI extends SSServServerI{

  public List<SSEntityE> entityTypesGet                (final SSEntityTypesGetPar                par) throws Exception;
  public List<SSEntity>  entitiesGet                   (final SSEntitiesGetPar                   par) throws Exception;
  public SSUri           entityUpdate                  (final SSEntityUpdatePar                  par) throws Exception;
  public Boolean         entityCopy                    (final SSEntityCopyPar                    par) throws Exception;
  public SSEntity        entityGet                     (final SSEntityGetPar                     par) throws Exception;
  public SSEntity        entityFromTypeAndLabelGet     (final SSEntityFromTypeAndLabelGetPar     par) throws Exception;
  public SSUri           entityShare                   (final SSEntitySharePar                   par) throws Exception;
  public List<SSUri>     entityDownloadsGet            (final SSEntityDownloadURIsGetPar         par) throws Exception;
  public SSUri           entityDownloadsAdd            (final SSEntityDownloadsAddPar            par) throws Exception;
  public SSUri           entityEntitiesAttach          (final SSEntityAttachEntitiesPar          par) throws Exception;
  public SSUri           entityEntitiesAttachedRemove  (final SSEntityEntitiesAttachedRemovePar  par) throws Exception;
  
  public List<SSUri>                     entityUserSubEntitiesGet                 (final SSServPar parA) throws Exception;
  public List<SSUri>                     entityUserParentEntitiesGet              (final SSServPar parA) throws Exception;
  
  public SSUri                           entityRemove                             (final SSServPar parA) throws Exception;
}
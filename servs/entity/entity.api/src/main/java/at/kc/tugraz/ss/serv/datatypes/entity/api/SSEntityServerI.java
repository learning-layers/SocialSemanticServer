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

import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntitiesGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityReadGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUpdatePar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityCopyPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityGetPar;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSServServerI;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityAttatchmentsRemovePar;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntitySharePar;
import java.util.List;

public interface SSEntityServerI extends SSServServerI{

  public List<SSEntity> entitiesGet              (final SSEntitiesGetPar                par) throws Exception;
  public SSUri          entityUpdate             (final SSEntityUpdatePar               par) throws Exception;
  public Boolean        entityReadGet            (final SSEntityReadGetPar              par) throws Exception;
  public Boolean        entityCopy               (final SSEntityCopyPar                 par) throws Exception;
  public SSEntity       entityGet                (final SSEntityGetPar                  par) throws Exception;
  public SSUri          entityShare              (final SSEntitySharePar                par) throws Exception;
  public SSUri          entityAttachmentsRemove  (final SSEntityAttatchmentsRemovePar   par) throws Exception;
  
  public List<SSUri>                     entityUserSubEntitiesGet                 (final SSServPar parA) throws Exception;
  public List<SSUri>                     entityUserParentEntitiesGet              (final SSServPar parA) throws Exception;
  
  public List<SSEntity>                  entityEntitiesAttachedGet                (final SSServPar parA) throws Exception;
  public SSUri                           entityFileAdd                            (final SSServPar parA) throws Exception;
  public List<SSUri>                     entityFilesGet                           (final SSServPar parA) throws Exception;
  public List<SSEntity>                  entitiesForLabelsAndDescriptionsGet      (final SSServPar parA) throws Exception;
  public List<SSEntity>                  entitiesForLabelsGet                     (final SSServPar parA) throws Exception;
  public List<SSEntity>                  entitiesForDescriptionsGet               (final SSServPar parA) throws Exception;
  public SSUri                           entityRemove                             (final SSServPar parA) throws Exception;
}
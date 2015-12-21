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
 package at.kc.tugraz.ss.service.coll.api;

import at.tugraz.sss.serv.datatype.*;
import at.kc.tugraz.ss.service.coll.datatypes.SSColl;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollGetPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollCumulatedTagsGetPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserEntriesAddPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserEntriesDeletePar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserEntryAddPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserEntryDeletePar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserHierarchyGetPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserParentGetPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserRootAddPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserRootGetPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollsGetPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollsUserEntityIsInGetPar;
import at.kc.tugraz.ss.service.tag.datatypes.SSTagFrequ;
import at.tugraz.sss.serv.datatype.SSEntity;
import at.tugraz.sss.serv.datatype.SSErr;
import at.tugraz.sss.serv.impl.api.SSServServerI;
import java.util.List;

public interface SSCollServerI extends SSServServerI{
  
  public SSColl           collGet                        (final SSCollGetPar                  par) throws SSErr;
  public SSColl           collParentGet                  (final SSCollUserParentGetPar        par) throws SSErr;
  public SSColl           collRootGet                    (final SSCollUserRootGetPar          par) throws SSErr;
  public List<SSEntity>   collHierarchyGet               (final SSCollUserHierarchyGetPar     par) throws SSErr;
  public List<SSEntity>   collsEntityIsInGet             (final SSCollsUserEntityIsInGetPar   par) throws SSErr;
  public List<SSEntity>   collsGet                       (final SSCollsGetPar                 par) throws SSErr;
  public Boolean          collRootAdd                    (final SSCollUserRootAddPar          par) throws SSErr;
  public SSUri            collEntryAdd                   (final SSCollUserEntryAddPar         par) throws SSErr;
  public List<SSUri>      collEntriesAdd                 (final SSCollUserEntriesAddPar       par) throws SSErr;
  public SSUri            collEntryDelete                (final SSCollUserEntryDeletePar      par) throws SSErr;
  public List<SSUri>      collEntriesDelete              (final SSCollUserEntriesDeletePar    par) throws SSErr;
  public List<SSTagFrequ> collCumulatedTagsGet           (final SSCollCumulatedTagsGetPar     par) throws SSErr;
  
}
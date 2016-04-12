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
 package at.tugraz.sss.servs.coll.api;

import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.coll.datatype.SSColl;
import at.tugraz.sss.servs.coll.datatype.SSCollGetPar;
import at.tugraz.sss.servs.coll.datatype.SSCollCumulatedTagsGetPar;
import at.tugraz.sss.servs.coll.datatype.SSCollUserEntriesAddPar;
import at.tugraz.sss.servs.coll.datatype.SSCollUserEntriesDeletePar;
import at.tugraz.sss.servs.coll.datatype.SSCollUserEntryAddPar;
import at.tugraz.sss.servs.coll.datatype.SSCollUserEntryDeletePar;
import at.tugraz.sss.servs.coll.datatype.SSCollUserHierarchyGetPar;
import at.tugraz.sss.servs.coll.datatype.SSCollUserParentGetPar;
import at.tugraz.sss.servs.coll.datatype.SSCollUserRootAddPar;
import at.tugraz.sss.servs.coll.datatype.SSCollUserRootGetPar;
import at.tugraz.sss.servs.coll.datatype.SSCollsGetPar;
import at.tugraz.sss.servs.coll.datatype.SSCollsUserEntityIsInGetPar;
import at.tugraz.sss.servs.tag.datatype.SSTagFrequ;
import at.tugraz.sss.servs.entity.datatype.SSEntity;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.common.api.SSServServerI;
import java.util.List;

public interface SSCollServerI extends SSServServerI{
  
  public SSColl           collGet                        (final SSCollGetPar                  par) throws SSErr;
  public SSColl           collParentGet                  (final SSCollUserParentGetPar        par) throws SSErr;
  public SSColl           collRootGet                    (final SSCollUserRootGetPar          par) throws SSErr;
  public List<SSEntity>   collHierarchyGet               (final SSCollUserHierarchyGetPar     par) throws SSErr;
  public List<SSEntity>   collsEntityIsInGet             (final SSCollsUserEntityIsInGetPar   par) throws SSErr;
  public List<SSEntity>   collsGet                       (final SSCollsGetPar                 par) throws SSErr;
  public boolean          collRootAdd                    (final SSCollUserRootAddPar          par) throws SSErr;
  public SSUri            collEntryAdd                   (final SSCollUserEntryAddPar         par) throws SSErr;
  public List<SSUri>      collEntriesAdd                 (final SSCollUserEntriesAddPar       par) throws SSErr;
  public SSUri            collEntryDelete                (final SSCollUserEntryDeletePar      par) throws SSErr;
  public List<SSUri>      collEntriesDelete              (final SSCollUserEntriesDeletePar    par) throws SSErr;
  public List<SSTagFrequ> collCumulatedTagsGet           (final SSCollCumulatedTagsGetPar     par) throws SSErr;
  
}
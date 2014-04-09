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
 package at.kc.tugraz.ss.service.coll.api;

import at.kc.tugraz.ss.datatypes.datatypes.SSSpaceEnum;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.service.coll.datatypes.SSColl;
import at.kc.tugraz.ss.service.tag.datatypes.SSTagFrequ;
import java.util.List;

public interface SSCollServerI{
  
  public Boolean                    collUserEntryDelete                           (final SSServPar parA) throws Exception;
  public Boolean                    collUserEntriesDelete                         (final SSServPar parA) throws Exception;
  public Boolean                    collUserShare                                 (final SSServPar parA) throws Exception;
  public SSUri                      collUserSetPublic                             (final SSServPar parA) throws Exception;
  public Boolean                    collUserEntryChangePos                        (final SSServPar parA) throws Exception;
  public SSColl                     collUserWithEntries                           (final SSServPar parA) throws Exception;
  public List<SSColl>               collsUserWithEntries                          (final SSServPar parA) throws Exception;
  public SSColl                     collUserRootGet                               (final SSServPar parA) throws Exception;
  public SSColl                     collUserParentGet                             (final SSServPar parA) throws Exception;
  public Boolean                    collUserRootAdd                               (final SSServPar parA) throws Exception;
  public List<SSColl>               collUserHierarchyGet                          (final SSServPar parA) throws Exception;
  public SSUri                      collUserEntryAdd                              (final SSServPar parA) throws Exception;
  public Boolean                    collUserEntriesAdd                            (final SSServPar parA) throws Exception;
  public Boolean                    collEntityPrivateForUserIs                    (final SSServPar parA) throws Exception;
  public Boolean                    collEntitySharedOrFollowedForUserIs           (final SSServPar parA) throws Exception;
  public SSSpaceEnum                collUserSpaceGet                              (final SSServPar parA) throws Exception;
  public List<SSTagFrequ>           collUserCumulatedTagsGet                      (final SSServPar parA) throws Exception;
  public List<SSColl>               collsUserEntityIsInGet                        (final SSServPar parA) throws Exception;
  public List<SSColl>               collsUserCouldSubscribeGet                    (final SSServPar parA) throws Exception;
}
/**
 * Copyright 2013 Graz University of Technology - KTI (Knowledge Technologies Institute)
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

import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;

public interface SSCollClientI{
  
  public void collUserParentGet         (final SSSocketCon sSCon, final SSServPar parA) throws Exception;
  public void collUserRootGet           (final SSSocketCon sSCon, final SSServPar parA) throws Exception;
  public void collUserEntryDelete       (final SSSocketCon sSCon, final SSServPar parA) throws Exception;
  public void collUserEntriesDelete     (final SSSocketCon sSCon, final SSServPar parA) throws Exception;
  public void collSharedAll             (final SSSocketCon sSCon, final SSServPar parA) throws Exception;
  public void collUserShare             (final SSSocketCon sSCon, final SSServPar parA) throws Exception;
  public void collUserEntryAdd          (final SSSocketCon sSCon, final SSServPar parA) throws Exception;
  public void collUserEntriesAdd        (final SSSocketCon sSCon, final SSServPar parA) throws Exception;
  public void collUserEntryChangePos    (final SSSocketCon sSCon, final SSServPar parA) throws Exception;
  public void collUserWithEntries       (final SSSocketCon sSCon, final SSServPar parA) throws Exception;
  public void collsUserWithEntries      (final SSSocketCon sSCon, final SSServPar parA) throws Exception;
  public void collUserHierarchyGet      (final SSSocketCon sSCon, final SSServPar parA) throws Exception; 
  public void collUserCumulatedTagsGet  (final SSSocketCon sSCon, final SSServPar parA) throws Exception; 
  public void collsUserEntityIsInGet    (final SSSocketCon sSCon, final SSServPar parA) throws Exception;
}
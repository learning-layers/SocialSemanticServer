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
 package at.kc.tugraz.ss.service.disc.api;

import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.service.disc.datatypes.SSDisc;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscEntryAddRet;
import java.util.List;

public interface SSDiscServerI {
  
  public SSDiscEntryAddRet        discEntryAdd              (final SSServPar parA) throws Exception;
  public List<SSDisc>             discsAll                  (final SSServPar parA) throws Exception;
  public List<SSDisc>             discsWithEntries          (final SSServPar parA) throws Exception;
  public SSDisc                   discWithEntries           (final SSServPar parA) throws Exception;
  public List<SSUri>              discUrisForTarget         (final SSServPar parA) throws Exception;
  public SSUri                    discUserRemove            (final SSServPar parA) throws Exception;
}

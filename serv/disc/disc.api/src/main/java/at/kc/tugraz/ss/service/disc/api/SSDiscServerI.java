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
 package at.kc.tugraz.ss.service.disc.api;

import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.service.disc.datatypes.SSDisc;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscUserEntryAddRet;
import java.util.List;

public interface SSDiscServerI {
  
  public List<SSDisc>             discsUserAllGet               (final SSServPar parA) throws Exception;
  public List<SSUri>              discUserDiscURIsForTargetGet  (final SSServPar parA) throws Exception;
  public SSDisc                   discUserWithEntriesGet        (final SSServPar parA) throws Exception;
  public List<SSDisc>             discsUserWithEntriesGet       (final SSServPar parA) throws Exception;
  public SSDiscUserEntryAddRet    discUserEntryAdd              (final SSServPar parA) throws Exception;
  public SSUri                    discUserRemove                (final SSServPar parA) throws Exception;
  public List<SSUri>              discEntryURIsGet              (final SSServPar parA) throws Exception;
}

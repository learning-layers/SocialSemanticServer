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

import at.tugraz.sss.serv.SSUri;
import at.kc.tugraz.ss.service.disc.datatypes.SSDisc;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscEntryAddPar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscEntryURIsGetPar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscRemovePar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscURIsForTargetGetPar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscWithEntriesGetPar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscsAllGetPar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscsWithEntriesGetPar;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscEntryAddRet;
import at.tugraz.sss.serv.SSServServerI;
import java.util.List;

public interface SSDiscServerI extends SSServServerI{
  
  
  public List<SSDisc>         discsWithEntriesGet       (final SSDiscsWithEntriesGetPar  par) throws Exception;
  public List<SSUri>          discURIsForTargetGet      (final SSDiscURIsForTargetGetPar par) throws Exception;
  public SSUri                discRemove                (final SSDiscRemovePar           par) throws Exception;
  public SSDisc               discWithEntriesGet        (final SSDiscWithEntriesGetPar   par) throws Exception;
  public SSDiscEntryAddRet    discEntryAdd              (final SSDiscEntryAddPar         par) throws Exception;
  public List<SSUri>          discEntryURIsGet          (final SSDiscEntryURIsGetPar     par) throws Exception;
  public List<SSDisc>         discsAllGet               (final SSDiscsAllGetPar          par) throws Exception;
}

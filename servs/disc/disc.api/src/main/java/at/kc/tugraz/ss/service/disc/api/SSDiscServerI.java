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
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscEntryAcceptPar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscEntryAddPar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscEntryUpdatePar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscRemovePar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscGetPar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscTargetsAddPar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscUpdatePar;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscsGetPar;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscEntryAddRet;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscEntryUpdateRet;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscUpdateRet;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSServServerI;
import java.util.List;

public interface SSDiscServerI extends SSServServerI{
  
  public SSDisc               discGet                   (final SSDiscGetPar              par) throws SSErr;
  public List<SSEntity>       discsGet                  (final SSDiscsGetPar             par) throws SSErr;
  public SSUri                discRemove                (final SSDiscRemovePar           par) throws SSErr;
  public SSDiscEntryAddRet    discEntryAdd              (final SSDiscEntryAddPar         par) throws SSErr;
  public SSUri                discEntryAccept           (final SSDiscEntryAcceptPar      par) throws SSErr;
  public SSUri                discTargetsAdd            (final SSDiscTargetsAddPar       par) throws SSErr;
  public SSDiscUpdateRet      discUpdate                (final SSDiscUpdatePar           par) throws SSErr;
  public SSDiscEntryUpdateRet discEntryUpdate           (final SSDiscEntryUpdatePar      par) throws SSErr;
}

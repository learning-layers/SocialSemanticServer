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
 package at.tugraz.sss.servs.disc.api;

import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.disc.datatype.SSDiscsGetPar;
import at.tugraz.sss.servs.disc.datatype.SSDisc;
import at.tugraz.sss.servs.disc.datatype.SSDiscDailySummaryGetPar;
import at.tugraz.sss.servs.disc.datatype.SSDiscEntryAcceptPar;
import at.tugraz.sss.servs.disc.datatype.SSDiscEntryAddPar;
import at.tugraz.sss.servs.disc.datatype.SSDiscEntryUpdatePar;
import at.tugraz.sss.servs.disc.datatype.SSDiscRemovePar;
import at.tugraz.sss.servs.disc.datatype.SSDiscGetPar;
import at.tugraz.sss.servs.disc.datatype.SSDiscTargetsAddPar;
import at.tugraz.sss.servs.disc.datatype.SSDiscUpdatePar;
import at.tugraz.sss.servs.disc.datatype.SSDiscDailySummaryGetRet;
import at.tugraz.sss.servs.disc.datatype.SSDiscEntryAddRet;
import at.tugraz.sss.servs.disc.datatype.SSDiscEntryUpdateRet;
import at.tugraz.sss.servs.disc.datatype.SSDiscUpdateRet;
import at.tugraz.sss.servs.entity.datatype.SSEntity;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.common.api.SSServServerI;
import java.util.List;

public interface SSDiscServerI extends SSServServerI{

  public SSDiscDailySummaryGetRet   discDailySummaryGet       (final SSDiscDailySummaryGetPar  par) throws SSErr;  
  public SSDisc                     discGet                   (final SSDiscGetPar              par) throws SSErr;
  public List<SSEntity>             discsGet                  (final SSDiscsGetPar             par) throws SSErr;
  public SSUri                      discRemove                (final SSDiscRemovePar           par) throws SSErr;
  public SSDiscEntryAddRet          discEntryAdd              (final SSDiscEntryAddPar         par) throws SSErr;
  public SSUri                      discEntryAccept           (final SSDiscEntryAcceptPar      par) throws SSErr;
  public SSUri                      discTargetsAdd            (final SSDiscTargetsAddPar       par) throws SSErr;
  public SSDiscUpdateRet            discUpdate                (final SSDiscUpdatePar           par) throws SSErr;
  public SSDiscEntryUpdateRet       discEntryUpdate           (final SSDiscEntryUpdatePar      par) throws SSErr;
}

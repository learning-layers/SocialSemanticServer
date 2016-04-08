/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2015, Graz University of Technology - KTI (Knowledge Technologies Institute).
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
package at.tugraz.sss.servs.learnep.api;

import at.tugraz.sss.servs.learnep.datatype.SSLearnEp;
import at.tugraz.sss.servs.learnep.datatype.SSLearnEpTimelineState;
import at.tugraz.sss.servs.learnep.datatype.SSLearnEpVersion;
import at.tugraz.sss.servs.learnep.datatype.*;
import at.tugraz.sss.serv.datatype.SSEntity;
import at.tugraz.sss.serv.datatype.SSErr;
import at.tugraz.sss.serv.impl.api.SSServServerI;
import at.tugraz.sss.serv.datatype.*;
import java.util.List;

public interface SSLearnEpServerI extends SSServServerI{
  
  public SSLearnEpDailySummaryGetRet            learnEpDailySummaryGet               (final SSLearnEpDailySummaryGetPar              par) throws SSErr;
  public SSLearnEpCircleEntityStructureGetRet   learnEpCircleEntityStructureGet      (final SSLearnEpCircleEntityStructureGetPar     par) throws SSErr;
  public SSLearnEp                              learnEpGet                           (final SSLearnEpGetPar                          par) throws SSErr;
  public List<SSEntity>                         learnEpsGet                          (final SSLearnEpsGetPar                         par) throws SSErr;
  public List<SSEntity>                         learnEpVersionsGet                   (final SSLearnEpVersionsGetPar                  par) throws SSErr;
  public SSLearnEpVersion                       learnEpVersionGet                    (final SSLearnEpVersionGetPar                   par) throws SSErr;
  public SSUri                                  learnEpRemove                        (final SSLearnEpRemovePar                       par) throws SSErr;
  public SSUri                                  learnEpVersionCreate                 (final SSLearnEpVersionCreatePar                par) throws SSErr;
  public SSUri                                  learnEpVersionCircleAdd              (final SSLearnEpVersionCircleAddPar             par) throws SSErr;
  public SSUri                                  learnEpVersionEntityAdd              (final SSLearnEpVersionEntityAddPar             par) throws SSErr;
  public SSUri                                  learnEpCreate                        (final SSLearnEpCreatePar                       par) throws SSErr;
  public boolean                                learnEpVersionCircleUpdate           (final SSLearnEpVersionCircleUpdatePar          par) throws SSErr;
  public boolean                                learnEpVersionEntityUpdate           (final SSLearnEpVersionEntityUpdatePar          par) throws SSErr;
  public boolean                                learnEpVersionCircleRemove           (final SSLearnEpVersionCircleRemovePar          par) throws SSErr;
  public boolean                                learnEpVersionEntityRemove           (final SSLearnEpVersionEntityRemovePar          par) throws SSErr;
  public SSUri                                  learnEpTimelineStateSet              (final SSLearnEpTimelineStateSetPar             par) throws SSErr;
  public SSLearnEpTimelineState                 learnEpTimelineStateGet              (final SSLearnEpTimelineStateGetPar             par) throws SSErr;
  public SSLearnEpVersion                       learnEpVersionCurrentGet             (final SSLearnEpVersionCurrentGetPar            par) throws SSErr;
  public SSUri                                  learnEpVersionCurrentSet             (final SSLearnEpVersionCurrentSetPar            par) throws SSErr;
  public List<SSLearnEpLockHoldRet>             learnEpsLockHold                     (final SSLearnEpsLockHoldPar                    par) throws SSErr;
  public SSLearnEpLockHoldRet                   learnEpLockHold                      (final SSLearnEpLockHoldPar                     par) throws SSErr;
  public boolean                                learnEpLockSet                       (final SSLearnEpLockSetPar                      par) throws SSErr;
  public boolean                                learnEpLockRemove                    (final SSLearnEpLockRemovePar                   par) throws SSErr;
  public SSLearnEpCirclesWithEntriesGetRet      learnEpVersionCirclesWithEntriesGet  (final SSLearnEpVersionCirclesWithEntriesGetPar par) throws SSErr;
}
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
package at.kc.tugraz.ss.serv.datatypes.learnep.api;

import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEp;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpTimelineState;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.SSLearnEpVersion;
import at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.ret.SSLearnEpLockHoldRet;
import java.util.List;

public interface SSLearnEpServerI {
  
  public SSUri                        learnEpRemove                     (final SSServPar parA) throws Exception;
  public List<SSLearnEp>              learnEpsGet                       (final SSServPar parA) throws Exception;
  public List<SSLearnEpVersion>       learnEpVersionsGet                (final SSServPar parA) throws Exception;
  public SSLearnEpVersion             learnEpVersionGet                 (final SSServPar parA) throws Exception;
  public SSUri                        learnEpVersionCreate              (final SSServPar parA) throws Exception;
  public SSUri                        learnEpVersionAddCircle           (final SSServPar parA) throws Exception;
  public SSUri                        learnEpVersionAddEntity           (final SSServPar parA) throws Exception;
  public SSUri                        learnEpCreate                     (final SSServPar parA) throws Exception;
  public Boolean                      learnEpVersionUpdateCircle        (final SSServPar parA) throws Exception;
  public Boolean                      learnEpVersionUpdateEntity        (final SSServPar parA) throws Exception;
  public Boolean                      learnEpVersionRemoveCircle        (final SSServPar parA) throws Exception;
  public Boolean                      learnEpVersionRemoveEntity        (final SSServPar parA) throws Exception;
  public SSUri                        learnEpVersionSetTimelineState    (final SSServPar parA) throws Exception;
  public SSLearnEpTimelineState       learnEpVersionGetTimelineState    (final SSServPar parA) throws Exception;
  public SSLearnEpVersion             learnEpVersionCurrentGet          (final SSServPar parA) throws Exception;
  public SSUri                        learnEpVersionCurrentSet          (final SSServPar parA) throws Exception;
  
  public SSUri                        learnEpUserCopyForUser            (final SSServPar parA) throws Exception;
  public SSLearnEpLockHoldRet         learnEpLockHold                   (final SSServPar parA) throws Exception;
  public List<SSLearnEpLockHoldRet>   learnEpsLockHold                  (final SSServPar parA) throws Exception;
  public Boolean                      learnEpLockSet                    (final SSServPar parA) throws Exception;
  public Boolean                      learnEpLockRemove                 (final SSServPar parA) throws Exception;
}

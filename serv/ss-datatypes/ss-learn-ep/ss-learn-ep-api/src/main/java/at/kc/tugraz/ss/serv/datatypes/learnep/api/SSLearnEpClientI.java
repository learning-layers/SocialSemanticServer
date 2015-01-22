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

import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;

public interface SSLearnEpClientI {
  
  public void learnEpsGet                       (final SSSocketCon sSCon, final SSServPar par) throws Exception;
  public void learnEpVersionsGet                (final SSSocketCon sSCon, final SSServPar par) throws Exception;
  public void learnEpVersionGet                 (final SSSocketCon sSCon, final SSServPar par) throws Exception;
  public void learnEpVersionCreate              (final SSSocketCon sSCon, final SSServPar par) throws Exception;
  public void learnEpVersionAddCircle           (final SSSocketCon sSCon, final SSServPar par) throws Exception;
  public void learnEpVersionAddEntity           (final SSSocketCon sSCon, final SSServPar par) throws Exception;
  public void learnEpCreate                     (final SSSocketCon sSCon, final SSServPar par) throws Exception;
  public void learnEpVersionUpdateCircle        (final SSSocketCon sSCon, final SSServPar par) throws Exception;
  public void learnEpVersionUpdateEntity        (final SSSocketCon sSCon, final SSServPar par) throws Exception;
  public void learnEpVersionRemoveCircle        (final SSSocketCon sSCon, final SSServPar par) throws Exception;
  public void learnEpVersionRemoveEntity        (final SSSocketCon sSCon, final SSServPar par) throws Exception;
  public void learnEpVersionSetTimelineState    (final SSSocketCon sSCon, final SSServPar par) throws Exception;
  public void learnEpVersionGetTimelineState    (final SSSocketCon sSCon, final SSServPar par) throws Exception;
  public void learnEpVersionCurrentGet          (final SSSocketCon sSCon, final SSServPar par) throws Exception;
  public void learnEpVersionCurrentSet          (final SSSocketCon sSCon, final SSServPar par) throws Exception;
  
  public void learnEpLockHold                   (final SSSocketCon sSCon, final SSServPar par) throws Exception;
  public void learnEpLockSet                    (final SSSocketCon sSCon, final SSServPar par) throws Exception;
  public void learnEpLockRemove                 (final SSSocketCon sSCon, final SSServPar par) throws Exception;
}

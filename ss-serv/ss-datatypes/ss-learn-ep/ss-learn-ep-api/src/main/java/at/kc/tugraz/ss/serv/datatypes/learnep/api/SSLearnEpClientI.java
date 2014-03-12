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
package at.kc.tugraz.ss.serv.datatypes.learnep.api;

import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;

public interface SSLearnEpClientI {
  
  public void learnEpsGet                       (SSSocketCon sSCon, SSServPar par) throws Exception;
  public void learnEpVersionsGet                (SSSocketCon sSCon, SSServPar par) throws Exception;
  public void learnEpVersionGet                 (SSSocketCon sSCon, SSServPar par) throws Exception;
  public void learnEpVersionCreate              (SSSocketCon sSCon, SSServPar par) throws Exception;
  public void learnEpVersionAddCircle           (SSSocketCon sSCon, SSServPar par) throws Exception;
  public void learnEpVersionAddEntity           (SSSocketCon sSCon, SSServPar par) throws Exception;
  public void learnEpCreate                     (SSSocketCon sSCon, SSServPar par) throws Exception;
  public void learnEpVersionUpdateCircle        (SSSocketCon sSCon, SSServPar par) throws Exception;
  public void learnEpVersionUpdateEntity        (SSSocketCon sSCon, SSServPar par) throws Exception;
  public void learnEpVersionRemoveCircle        (SSSocketCon sSCon, SSServPar par) throws Exception;
  public void learnEpVersionRemoveEntity        (SSSocketCon sSCon, SSServPar par) throws Exception;
  public void learnEpVersionSetTimelineState    (SSSocketCon sSCon, SSServPar par) throws Exception;
  public void learnEpVersionGetTimelineState    (SSSocketCon sSCon, SSServPar par) throws Exception;
  public void learnEpVersionCurrentGet          (SSSocketCon sSCon, SSServPar par) throws Exception;
  public void learnEpVersionCurrentSet          (SSSocketCon sSCon, SSServPar par) throws Exception;
}

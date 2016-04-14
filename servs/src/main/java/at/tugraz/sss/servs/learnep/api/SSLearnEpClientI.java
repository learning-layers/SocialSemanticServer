/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2014, Graz University of Technology - KTI (Knowledge Technologies Institute).
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

/**
 * @author Dieter Theiler
 */

package at.tugraz.sss.servs.learnep.api;

import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSClientE;
import at.tugraz.sss.servs.entity.datatype.SSServPar; 
import at.tugraz.sss.servs.entity.datatype.SSServRetI; 

public interface SSLearnEpClientI {

  public SSServRetI learnEpCircleEntityStructureGet     (final SSClientE clientType, final SSServPar parA) throws SSErr;
  public SSServRetI learnEpRemove                       (final SSClientE clientType, final SSServPar parA) throws SSErr;
  public SSServRetI learnEpsGet                         (final SSClientE clientType, final SSServPar parA) throws SSErr;
  public SSServRetI learnEpVersionsGet                  (final SSClientE clientType, final SSServPar parA) throws SSErr;
  public SSServRetI learnEpVersionGet                   (final SSClientE clientType, final SSServPar parA) throws SSErr;
  public SSServRetI learnEpVersionCreate                (final SSClientE clientType, final SSServPar parA) throws SSErr;
  public SSServRetI learnEpVersionCircleAdd             (final SSClientE clientType, final SSServPar parA) throws SSErr;
  public SSServRetI learnEpVersionEntityAdd             (final SSClientE clientType, final SSServPar parA) throws SSErr;
  public SSServRetI learnEpCreate                       (final SSClientE clientType, final SSServPar parA) throws SSErr;
  public SSServRetI learnEpVersionCircleUpdate          (final SSClientE clientType, final SSServPar parA) throws SSErr;
  public SSServRetI learnEpVersionEntityUpdate          (final SSClientE clientType, final SSServPar parA) throws SSErr;
  public SSServRetI learnEpVersionCircleRemove          (final SSClientE clientType, final SSServPar parA) throws SSErr;
  public SSServRetI learnEpVersionEntityRemove          (final SSClientE clientType, final SSServPar parA) throws SSErr;
  public SSServRetI learnEpTimelineStateSet             (final SSClientE clientType, final SSServPar parA) throws SSErr;
  public SSServRetI learnEpTimelineStateGet             (final SSClientE clientType, final SSServPar parA) throws SSErr;
  public SSServRetI learnEpVersionCurrentGet            (final SSClientE clientType, final SSServPar parA) throws SSErr;
  public SSServRetI learnEpVersionCurrentSet            (final SSClientE clientType, final SSServPar parA) throws SSErr;
  public SSServRetI learnEpsLockHold                    (final SSClientE clientType, final SSServPar parA) throws SSErr;
  public SSServRetI learnEpLockSet                      (final SSClientE clientType, final SSServPar parA) throws SSErr;
  public SSServRetI learnEpLockRemove                   (final SSClientE clientType, final SSServPar parA) throws SSErr;
}

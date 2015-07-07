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
package at.kc.tugraz.ss.circle.api;

import at.kc.tugraz.ss.circle.datatypes.par.SSCircleCanAccessPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleCreatePar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntitiesAddPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntitiesRemovePar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntitySharePar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntityUsersGetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleGetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleMostOpenCircleTypeGetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCirclePrivURIGetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCirclePubURIGetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleTypesGetPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleUsersAddPar;
import at.kc.tugraz.ss.circle.datatypes.par.SSCirclesGetPar;
import at.tugraz.sss.serv.SSCircleE;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSEntityCircle;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSServServerI;
import java.util.List;

public interface SSCircleServerI extends SSServServerI{

  public SSUri                           circleCreate                             (final SSCircleCreatePar                par) throws Exception;
  public SSUri                           circleUsersAdd                           (final SSCircleUsersAddPar              par) throws Exception;
  public SSUri                           circleEntitiesAdd                        (final SSCircleEntitiesAddPar           par) throws Exception;
  public SSCircleE                       circleMostOpenCircleTypeGet              (final SSCircleMostOpenCircleTypeGetPar par) throws Exception;
  public List<SSCircleE>                 circleTypesGet                           (final SSCircleTypesGetPar              par) throws Exception;
  public SSEntityCircle                  circleGet                                (final SSCircleGetPar                   par) throws Exception;
  public List<SSEntityCircle>            circlesGet                               (final SSCirclesGetPar                  par) throws Exception;
  public SSEntity                        circleCanAccess                          (final SSCircleCanAccessPar             par) throws Exception;
  public SSUri                           circlePrivURIGet                         (final SSCirclePrivURIGetPar            par) throws Exception;
  public SSUri                           circlePubURIGet                          (final SSCirclePubURIGetPar             par) throws Exception;
  public List<SSEntity>                  circleEntityUsersGet                     (final SSCircleEntityUsersGetPar        par) throws Exception;  
  public List<SSUri>                     circleEntitiesRemove                     (final SSCircleEntitiesRemovePar        par) throws Exception;
  public SSUri                           circleEntityShare                        (final SSCircleEntitySharePar           par) throws Exception;
}
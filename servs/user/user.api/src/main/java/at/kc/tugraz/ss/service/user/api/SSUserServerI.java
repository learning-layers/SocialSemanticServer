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
 package at.kc.tugraz.ss.service.user.api;

import at.tugraz.sss.serv.SSUri;
import at.kc.tugraz.ss.service.user.datatypes.SSUser;
import at.kc.tugraz.ss.service.user.datatypes.pars.SSUserAddPar;
import at.kc.tugraz.ss.service.user.datatypes.pars.SSUserAllPar;
import at.kc.tugraz.ss.service.user.datatypes.pars.SSUserExistsPar;
import at.kc.tugraz.ss.service.user.datatypes.pars.SSUserURIGetPar;
import at.kc.tugraz.ss.service.user.datatypes.pars.SSUsersGetPar;
import at.tugraz.sss.serv.SSServServerI;
import java.util.List;

public interface SSUserServerI extends SSServServerI{

  public Boolean        userExists           (final SSUserExistsPar par)     throws Exception;
  public SSUri          userURIGet           (final SSUserURIGetPar par)     throws Exception;
  public List<SSUser>   userAll              (final SSUserAllPar    par)     throws Exception;
  public List<SSUser>   usersGet             (final SSUsersGetPar   par)     throws Exception;
  public SSUri          userAdd              (final SSUserAddPar    par)     throws Exception;
}

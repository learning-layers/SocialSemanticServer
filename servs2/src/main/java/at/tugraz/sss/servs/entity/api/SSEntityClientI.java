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
package at.tugraz.sss.servs.entity.api;

import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.enums.SSClientE;
import at.tugraz.sss.serv.datatype.par.SSServPar; 
import at.tugraz.sss.serv.datatype.ret.SSServRetI;

public interface SSEntityClientI {

  public SSServRetI entityTypesGet        (final SSClientE clientType, final SSServPar parA) throws SSErr;
  public SSServRetI entitiesGet           (final SSClientE clientType, final SSServPar parA) throws SSErr;
  public SSServRetI entityUpdate          (final SSClientE clientType, final SSServPar parA) throws SSErr;
  public SSServRetI entityGet             (final SSClientE clientType, final SSServPar parA) throws SSErr;
  public SSServRetI entityCopy            (final SSClientE clientType, final SSServPar parA) throws SSErr;
  public SSServRetI entityShare           (final SSClientE clientType, final SSServPar parA) throws SSErr;
  public SSServRetI entityUnpublicize     (final SSClientE clientType, final SSServPar parA) throws SSErr;
  public SSServRetI entitiesAccessibleGet (final SSClientE clientType, final SSServPar parA) throws SSErr;
  public SSServRetI entityEntitiesAttach  (final SSClientE clientType, final SSServPar parA) throws SSErr;
    
  public SSServRetI circleGet             (final SSClientE clientType, final SSServPar parA) throws SSErr;
  public SSServRetI circleEntitiesAdd     (final SSClientE clientType, final SSServPar parA) throws SSErr;
  public SSServRetI circleUsersAdd        (final SSClientE clientType, final SSServPar parA) throws SSErr;
  public SSServRetI circlesGet            (final SSClientE clientType, final SSServPar parA) throws SSErr;
  public SSServRetI circleCreate          (final SSClientE clientType, final SSServPar parA) throws SSErr;
  public SSServRetI circleEntitiesRemove  (final SSClientE clientType, final SSServPar parA) throws SSErr;
  public SSServRetI circleUsersRemove     (final SSClientE clientType, final SSServPar parA) throws SSErr;
  public SSServRetI circleRemove          (final SSClientE clientType, final SSServPar parA) throws SSErr;
  public SSServRetI circleUsersInvite     (final SSClientE clientType, final SSServPar parA) throws SSErr;
  public SSServRetI circleTypeChange      (final SSClientE clientType, final SSServPar parA) throws SSErr;
}

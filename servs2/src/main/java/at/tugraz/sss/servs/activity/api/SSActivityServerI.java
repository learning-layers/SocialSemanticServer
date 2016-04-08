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
package at.tugraz.sss.servs.activity.api;

import at.tugraz.sss.servs.activity.datatype.SSActivityE;
import at.tugraz.sss.servs.activity.datatype.SSActivitiesGetPar;
import at.tugraz.sss.servs.activity.datatype.SSActivityAddPar;
import at.tugraz.sss.servs.activity.datatype.SSActivityContentAddPar;
import at.tugraz.sss.servs.activity.datatype.SSActivityContentsAddPar;
import at.tugraz.sss.servs.activity.datatype.SSActivityTypesGetPar;
import at.tugraz.sss.serv.datatype.SSEntity;
import at.tugraz.sss.serv.datatype.SSErr;
import at.tugraz.sss.serv.impl.api.SSServServerI;
import at.tugraz.sss.serv.datatype.*;
import java.util.List;

public interface SSActivityServerI extends SSServServerI{
  
  public SSUri             activityAdd            (final SSActivityAddPar         par) throws SSErr;
  public SSUri             activityContentAdd     (final SSActivityContentAddPar  par) throws SSErr;
  public void              activityContentsAdd    (final SSActivityContentsAddPar par) throws SSErr;
  public List<SSEntity>    activitiesGet          (final SSActivitiesGetPar       par) throws SSErr;
  public List<SSActivityE> activityTypesGet       (final SSActivityTypesGetPar    par) throws SSErr;
}

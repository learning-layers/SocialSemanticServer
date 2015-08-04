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
package at.tugraz.sss.servs.location.api;

import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.servs.location.datatype.SSLocation;
import at.tugraz.sss.servs.location.datatype.par.SSLocationAddPar;
import at.tugraz.sss.servs.location.datatype.par.SSLocationGetPar;
import at.tugraz.sss.servs.location.datatype.par.SSLocationsGetPar;
import java.util.List;

public interface SSLocationServerI{

  public SSUri          locationAdd  (final SSLocationAddPar  par) throws Exception;
  public SSLocation     locationGet  (final SSLocationGetPar  par) throws Exception;
  public List<SSEntity> locationsGet (final SSLocationsGetPar par) throws Exception;
}

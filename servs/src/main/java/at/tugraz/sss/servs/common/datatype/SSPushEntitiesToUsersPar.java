/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2015, Graz University of Technology - KTI (Knowledge Technologies Institute).
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

package at.tugraz.sss.servs.common.datatype;

import at.tugraz.sss.servs.entity.datatype.SSEntity;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import java.util.ArrayList;
import java.util.List;

public class SSPushEntitiesToUsersPar {

  public SSUri          user                = null;
  public List<SSEntity> entities            = new ArrayList<>();
  public List<SSUri>    users               = new ArrayList<>();
  public boolean        withUserRestriction = true;
  
  public SSPushEntitiesToUsersPar(
    final SSUri          user, 
    final List<SSEntity> entities, 
    final List<SSUri>    users, 
    final boolean        withUserRestriction){
    
    this.user           = user;
    
    SSEntity.addEntitiesDistinctWithoutNull (this.entities, entities);
    SSUri.addDistinctWithoutNull            (this.users,    users);
    
    this.withUserRestriction = withUserRestriction;
  }
}
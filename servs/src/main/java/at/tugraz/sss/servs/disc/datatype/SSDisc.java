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

package at.tugraz.sss.servs.disc.datatype;

import at.tugraz.sss.servs.entity.datatype.SSEntityE;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.entity.datatype.SSEntity;
import io.swagger.annotations.*;
import java.util.*;

@ApiModel
public class SSDisc extends SSEntity {

  @ApiModelProperty
  public List<SSEntity> targets = new ArrayList<>();
  
  public static SSDisc get(
    final SSUri                  id,
    final SSEntityE              type,
    final List<SSEntity>         targets) throws SSErr{
    
    return new SSDisc(
      id, 
      type,
      targets);
  }
  
  public static SSDisc get(
    final SSDisc              disc,
    final SSEntity            entity) throws SSErr{
    
    return new SSDisc(
      disc,
      entity);
  }

  public SSDisc(){/* Do nothing because of only JSON Jackson needs this */ }
     
  protected SSDisc(
    final SSDisc              disc,
    final SSEntity            entity) throws SSErr{
    
     super(disc, entity);
     
     SSEntity.addEntitiesDistinctWithoutNull(this.targets, disc.targets);

     if(entity instanceof SSDisc){
       SSEntity.addEntitiesDistinctWithoutNull(this.targets, ((SSDisc) entity).targets);
     }
   }
   
  protected SSDisc(
    final SSUri                  id,
    final SSEntityE              type,
    final List<SSEntity>         targets) throws SSErr{
    
    super(id, type);
    
    SSEntity.addEntitiesDistinctWithoutNull(this.targets, targets);
  }
}
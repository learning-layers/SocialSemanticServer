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

package at.tugraz.sss.servs.learnep.datatype;

import at.tugraz.sss.servs.entity.datatype.SSEntityE;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.entity.datatype.SSLabel;
import at.tugraz.sss.servs.entity.datatype.SSEntity;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSTextComment;
import io.swagger.annotations.*;
import java.util.List;

@ApiModel
public class SSLearnEp extends SSEntity {

  @ApiModelProperty
  public boolean    locked       = false;
  
  @ApiModelProperty
  public boolean    lockedByUser = false;
  
  public void setVersions(final List<SSEntity> versions){
    this.entries = versions;
  }
  
  public List<SSEntity> getVersions(){
    return entries;
  }
  
  public static SSLearnEp get(
    final SSLearnEp learnEp, 
    final SSEntity  entity) throws SSErr{
    
    return new SSLearnEp(learnEp, entity);
  }
   
  public static SSLearnEp get(
    final SSUri           id) throws SSErr{
    
    return new SSLearnEp(id);
  }
  
  public static SSLearnEp get(
    final SSUri           id,
    final List<SSEntity>  versions) throws SSErr {
    
    return new SSLearnEp(id, versions);
  }
  
  public static SSLearnEp get(
    final SSUri         id,
    final SSLabel       label,
    final SSTextComment description,
    final Long          creationTime,
    final SSEntity      author) throws SSErr{
    
    return new SSLearnEp(
      id,
      label,
      description,
      creationTime,
      author);
  }
  
  public SSLearnEp(){/* Do nothing because of only JSON Jackson needs this */ }
  
  protected SSLearnEp(
    final SSUri                  id,
    final List<SSEntity>         versions) throws SSErr {
    
    super(id, SSEntityE.learnEp);
    
    SSEntity.addEntitiesDistinctWithoutNull(this.entries, versions);
  }
  
  protected SSLearnEp(
    final SSLearnEp   learnEp,
    final SSEntity    entity) throws SSErr {
    
    super(learnEp, entity);
  }
  
  protected SSLearnEp(
    final SSUri id) throws SSErr{
    
    super(id, SSEntityE.learnEp);
  }
  
  protected SSLearnEp(
    final SSUri         id,
    final SSLabel       label,
    final SSTextComment description,
    final Long          creationTime,
    final SSEntity      author) throws SSErr{
    
    super(
      id,
      SSEntityE.learnEp,
      label,
      description,
      creationTime,
      author);
  }
}


//  public static SSLearnEp copy(
//    final SSLearnEp learnEp) throws SSErr{
//
//    final SSLearnEp   copy = get(learnEp.user, learnEp.id, learnEp.label, null);
//    SSLearnEpVersion  versionCopy;
//
//    for(SSLearnEpVersion version : learnEp.versions){
//
//      versionCopy =
//        SSLearnEpVersion.get(
//          version.id,
//          version.learnEp,
//          version.timestamp,
//          null,
//          null);
//      
//      for(SSLearnEpCircle circle : version.circles){
//        
//        versionCopy.circles.add(
//          SSLearnEpCircle.get(
//            circle.id,
//            circle.label,
//            circle.xLabel,
//            circle.yLabel,
//            circle.xR,
//            circle.yR,
//            circle.xC,
//            circle.yC));
//      }
//      
//      for(SSLearnEpEntity entity : version.entities){
//        
//        versionCopy.entities.add(
//          SSLearnEpEntity.get(
//            entity.id,
//            entity.entity,
//            entity.x,
//            entity.y));
//      }
//      
//      copy.versions.add(versionCopy);
//    }
//    
//    return copy;
//  }
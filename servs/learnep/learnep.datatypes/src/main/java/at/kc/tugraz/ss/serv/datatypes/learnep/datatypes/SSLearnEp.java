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
package at.kc.tugraz.ss.serv.datatypes.learnep.datatypes;

import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSEntityE;
import java.util.List;

public class SSLearnEp extends SSEntity {

  public Boolean    locked       = false;
  public Boolean    lockedByUser = false;
  
  public List<SSEntity> getVersions(){
    return entries;
  }
  
  @Override
  public Object jsonLDDesc(){
    throw new UnsupportedOperationException();
  }
  
   public static SSLearnEp get(
    final SSLearnEp learnEp, 
    final SSEntity entity) throws Exception{
    
    return new SSLearnEp(learnEp, entity);
  }
   
  public static SSLearnEp get(
    final SSUri           id) throws Exception{
    
    return new SSLearnEp(id);
  }
  
  public static SSLearnEp get(
    final SSUri           id,
    final List<SSEntity>  versions) throws Exception{
    
    return new SSLearnEp(id, versions);
  }
  
  protected SSLearnEp(
    final SSUri                  id,
    final List<SSEntity> versions) throws Exception{
    
    super(id, SSEntityE.learnEp);
    
    SSEntity.addEntitiesDistinctWithoutNull(this.entries, versions);
  }
  
  protected SSLearnEp(
    final SSLearnEp   learnEp,
    final SSEntity    entity) throws Exception{
    
    super(learnEp, entity);
  }
  
  protected SSLearnEp(
    final SSUri                  id) throws Exception{
    
    super(id, SSEntityE.learnEp);
  }
}


//  public static SSLearnEp copy(
//    final SSLearnEp learnEp) throws Exception{
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
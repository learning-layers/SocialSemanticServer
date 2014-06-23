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

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSSpaceE;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityA;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSCircleE;
import at.kc.tugraz.ss.serv.jsonld.util.SSJSONLDU;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSLearnEp extends SSEntityA {

  public SSUri                   id          = null;
  public SSUri                   user        = null;
  public SSLabel                 label       = null;
  public List<SSLearnEpVersion>  versions    = new ArrayList<>();
  public List<SSCircleE>         circleTypes = new ArrayList<>();
  
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

  public static SSLearnEp get(
    final SSUri                  user, 
    final SSUri                  id, 
    final SSLabel                label,
    final List<SSLearnEpVersion> versions,
    final List<SSCircleE>        circleTypes) throws Exception{
    
    return new SSLearnEp(user, id, label, versions, circleTypes);
  }
  
  private SSLearnEp(
    final SSUri                  user, 
    final SSUri                  id, 
    final SSLabel                label,
    final List<SSLearnEpVersion> versions,
    final List<SSCircleE>        circleTypes) throws Exception{
    
    super(id);
    
    this.id          = id;
    this.user        = user;
    this.label       = label;
    
    if(versions != null){
      this.versions.addAll(versions);
    }
    
    if(circleTypes != null){
      this.circleTypes.addAll(circleTypes);
    }
  }
  
  @Override
  public Object jsonLDDesc(){
    
    final Map<String, Object> ld             = new HashMap<>();
    final Map<String, Object> circleTypesObj = new HashMap<>();
    final Map<String, Object> versionsObj    = new HashMap<>();
    
    ld.put(SSVarU.user,       SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.id,         SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.label,      SSVarU.sss + SSStrU.colon + SSSpaceE.class.getName());
    
    versionsObj.put(SSJSONLDU.id,        SSVarU.sss + SSStrU.colon + SSLearnEpVersion.class.getName());
    versionsObj.put(SSJSONLDU.container, SSJSONLDU.set);
    
    ld.put(SSVarU.versions, versionsObj);
    
    circleTypesObj.put(SSJSONLDU.id,        SSVarU.sss + SSStrU.colon + SSCircleE.class.getName());
    circleTypesObj.put(SSJSONLDU.container, SSJSONLDU.set);
    
    ld.put(SSVarU.circleTypes, circleTypesObj);
    
    return ld;
  }
  
  /* getters to allow for json enconding */
  
  public String getUser() throws Exception {
    return SSStrU.removeTrailingSlash(user);
  }

  public String getId() throws Exception {
    return SSStrU.removeTrailingSlash(id);
  }

  public String getLabel() {
    return SSStrU.toStr(label);
  }
  
  public List<SSLearnEpVersion> getVersions() {
    return versions;
  }
  
  public List<SSCircleE> getCircleTypes(){
    return circleTypes;
  } 
}

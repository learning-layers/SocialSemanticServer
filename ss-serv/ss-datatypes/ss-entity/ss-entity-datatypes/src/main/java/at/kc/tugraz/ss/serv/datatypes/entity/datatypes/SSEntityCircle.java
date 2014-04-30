/**
 * Copyright 2014 Graz University of Technology - KTI (Knowledge Technologies Institute)
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
package at.kc.tugraz.ss.serv.datatypes.entity.datatypes;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityA;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.jsonld.util.SSJSONLDU;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSEntityCircle extends SSEntityA{
 
  public SSUri                          circleUri    = null;
  public SSLabel                     label        = null;
  public SSCircleE            circleType   = null;
  public List<SSCircleRightE>       circleRights = new ArrayList<SSCircleRightE>();
  public List<SSUri>                    userUris     = new ArrayList<SSUri>();
  public List<SSUri>                    entityUris   = new ArrayList<SSUri>();
  
  public static SSEntityCircle get(
    final SSUri                          circleUri,
    final SSLabel                     label, 
    final SSCircleE            circleType, 
    final List<SSCircleRightE>       circleRights,
    final List<SSUri>                    userUris,
    final List<SSUri>                    entityUris) throws Exception{
    
    return new SSEntityCircle(circleUri, label, circleType, circleRights, userUris, entityUris);
  }

  protected SSEntityCircle(
    final SSUri                           circleUri,
    final SSLabel                         label,
    final SSCircleE                       circleType, 
    final List<SSCircleRightE>            circleRights,
    final List<SSUri>                     userUris,
    final List<SSUri>                     entityUris) throws Exception{
    
    super(SSUri.toStr(circleUri));
    
    this.circleUri    = circleUri;
    this.label        = label;
    this.circleType   = circleType;
    
    if(circleRights != null){
      this.circleRights.addAll(circleRights);
    }
    
    if(userUris != null){
      this.userUris.addAll(userUris);
    }
    
    if(entityUris != null){
      this.entityUris.addAll(entityUris);
    }    
  }

  public Object jsonLDDesc(){
    
    final Map<String, Object> ld              = new HashMap<String, Object>();
    final Map<String, Object> circleRightsObj = new HashMap<String, Object>();
    final Map<String, Object> userUrisObj     = new HashMap<String, Object>();
    final Map<String, Object> entityUrisObj   = new HashMap<String, Object>();
    
    ld.put(SSVarU.circleUri,  SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.label,      SSVarU.sss + SSStrU.colon + SSLabel.class.getName());
    ld.put(SSVarU.circleType, SSVarU.sss + SSStrU.colon + SSCircleE.class.getName());
    
    circleRightsObj.put(SSJSONLDU.id,        SSVarU.sss + SSStrU.colon + SSCircleRightE.class.getName());
    circleRightsObj.put(SSJSONLDU.container, SSJSONLDU.set);
    
    ld.put(SSVarU.circleRights, circleRightsObj);
    
    userUrisObj.put(SSJSONLDU.id,        SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    userUrisObj.put(SSJSONLDU.container, SSJSONLDU.set);
    
    ld.put(SSVarU.userUris, userUrisObj);
    
    entityUrisObj.put(SSJSONLDU.id,        SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    entityUrisObj.put(SSJSONLDU.container, SSJSONLDU.set);
    
    ld.put(SSVarU.entityUris, entityUrisObj);
    
    return ld;
  }

  /* getters to allow for json enconding  */
  
  public String getCircleUri(){
    return SSUri.toStrWithoutSlash(circleUri);
  }

  public String getLabel(){
    return SSLabel.toStr(label);
  }

  public SSCircleE getCircleType(){
    return circleType;
  }

  public List<SSCircleRightE> getCircleRights(){
    return circleRights;
  }

  public List<String> getUserUris(){
    return SSUri.toStrWithoutSlash(userUris);
  }

  public List<String> getEntityUris(){
    return SSUri.toStrWithoutSlash(entityUris);
  }
}

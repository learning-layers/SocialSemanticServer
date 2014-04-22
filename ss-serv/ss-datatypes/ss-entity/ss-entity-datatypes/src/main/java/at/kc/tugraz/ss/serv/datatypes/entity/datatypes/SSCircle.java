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
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityA;
import at.kc.tugraz.ss.datatypes.datatypes.SSLabelStr;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.serv.jsonld.util.SSJSONLDU;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSCircle extends SSEntityA{
 
  public SSUri                          circleUri    = null;
  public SSLabelStr                     label        = null;
  public SSEntityCircleTypeE            circleType   = null;
  public List<SSEntityRightTypeE>       circleRights = new ArrayList<SSEntityRightTypeE>();
  public List<SSUri>                    userUris     = new ArrayList<SSUri>();
  public List<SSUri>                    entityUris   = new ArrayList<SSUri>();
  
  public static SSCircle get(
    final SSUri                          circleUri,
    final SSLabelStr                     label, 
    final SSEntityCircleTypeE      circleType, 
    final List<SSEntityRightTypeE> circleRights,
    final List<SSUri>                    userUris,
    final List<SSUri>                    entityUris){
    
    return new SSCircle(circleUri, label, circleType, circleRights, userUris, entityUris);
  }

  protected SSCircle(
    final SSUri                          circleUri,
    final SSLabelStr                     label,
    final SSEntityCircleTypeE      circleType, 
    final List<SSEntityRightTypeE> circleRights,
    final List<SSUri>                    userUris,
    final List<SSUri>                    entityUris){
    
    super(circleUri.toString());
    
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
    ld.put(SSVarU.label,      SSVarU.sss + SSStrU.colon + SSLabelStr.class.getName());
    ld.put(SSVarU.circleType, SSVarU.sss + SSStrU.colon + SSEntityCircleTypeE.class.getName());
    
    circleRightsObj.put(SSJSONLDU.id,        SSVarU.sss + SSStrU.colon + SSEntityRightTypeE.class.getName());
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
}

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
import at.kc.tugraz.ss.datatypes.datatypes.SSTextComment;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityA;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.jsonld.util.SSJSONLDU;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSEntityCircle extends SSEntityA{
 
  public SSUri                          id             = null;
  public SSLabel                        label          = null;
  public SSTextComment                  description    = null;
  public SSCircleE                      type           = null;
  public List<SSCircleRightE>           accessRights   = new ArrayList<>();
  public List<SSUri>                    users          = new ArrayList<>();
  public List<SSUri>                    entities       = new ArrayList<>();
  public Boolean                        isSystemCircle = null;
  
  public static SSEntityCircle get(
    final SSUri                          circleUri,
    final SSLabel                        label, 
    final SSTextComment                  description,
    final SSCircleE                      circleType, 
    final List<SSCircleRightE>           circleRights,
    final List<SSUri>                    userUris,
    final List<SSUri>                    entityUris,
    final Boolean                        isSystemCircle) throws Exception{
    
    return new SSEntityCircle(circleUri, label, description, circleType, circleRights, userUris, entityUris, isSystemCircle);
  }

  protected SSEntityCircle(
    final SSUri                           circleUri,
    final SSLabel                         label,
    final SSTextComment                   description,
    final SSCircleE                       circleType, 
    final List<SSCircleRightE>            circleRights,
    final List<SSUri>                     userUris,
    final List<SSUri>                     entityUris,
    final Boolean                         isSystemCircle) throws Exception{
    
    super(SSStrU.toStr(circleUri));
    
    this.id             = circleUri;
    this.label          = label;
    this.description    = description;
    this.type           = circleType;
    this.isSystemCircle = isSystemCircle;
    
    if(circleRights != null){
      this.accessRights.addAll(circleRights);
    }
    
    if(userUris != null){
      this.users.addAll(userUris);
    }
    
    if(entityUris != null){
      this.entities.addAll(entityUris);
    }    
  }

  @Override
  public Object jsonLDDesc(){
    
    final Map<String, Object> ld              = new HashMap<>();
    final Map<String, Object> circleRightsObj = new HashMap<>();
    final Map<String, Object> userUrisObj     = new HashMap<>();
    final Map<String, Object> entityUrisObj   = new HashMap<>();
    
    ld.put(SSVarU.id,              SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.label,           SSVarU.sss + SSStrU.colon + SSLabel.class.getName());
    ld.put(SSVarU.description,     SSVarU.sss + SSStrU.colon + SSTextComment.class.getName());
    ld.put(SSVarU.type,            SSVarU.sss + SSStrU.colon + SSCircleE.class.getName());
    ld.put(SSVarU.isSystemCircle,  SSVarU.xsd + SSStrU.colon + SSStrU.valueBoolean);
    
    circleRightsObj.put(SSJSONLDU.id,        SSVarU.sss + SSStrU.colon + SSCircleRightE.class.getName());
    circleRightsObj.put(SSJSONLDU.container, SSJSONLDU.set);
    
    ld.put(SSVarU.accessRights, circleRightsObj);
    
    userUrisObj.put(SSJSONLDU.id,        SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    userUrisObj.put(SSJSONLDU.container, SSJSONLDU.set);
    
    ld.put(SSVarU.users, userUrisObj);
    
    entityUrisObj.put(SSJSONLDU.id,        SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    entityUrisObj.put(SSJSONLDU.container, SSJSONLDU.set);
    
    ld.put(SSVarU.entities, entityUrisObj);
    
    return ld;
  }

  /* json getters  */
  
  public String getId() throws Exception{
    return SSStrU.removeTrailingSlash(id);
  }

  public String getLabel(){
    return SSStrU.toStr(label);
  }
  
  public String getDescription(){
    return SSStrU.toStr(description);
  }

  public List<String> getUsers() throws Exception{
    return SSStrU.removeTrailingSlash(users);
  }

  public List<String> getEntities() throws Exception{
    return SSStrU.removeTrailingSlash(entities);
  }
}

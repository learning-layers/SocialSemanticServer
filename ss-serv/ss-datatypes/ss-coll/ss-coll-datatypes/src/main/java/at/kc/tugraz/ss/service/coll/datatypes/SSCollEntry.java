/**
 * Copyright 2013 Graz University of Technology - KTI (Knowledge Technologies Institute)
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
 package at.kc.tugraz.ss.service.coll.datatypes;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityEnum;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.SSSpaceEnum;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityA;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSAccessRightsCircleTypeE;
import at.kc.tugraz.ss.serv.jsonld.util.SSJSONLDU;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSCollEntry extends SSEntityA {

  public  SSUri                           uri         = null;
  public  Integer                         pos         = -1;
  public  List<SSAccessRightsCircleTypeE> circleTypes = new ArrayList<SSAccessRightsCircleTypeE>();
  public  String                          label       = null;
  public  SSEntityEnum                    entityType  = null;

  public static SSCollEntry get(
    SSUri         uri,
    String        label  ,
    List<SSAccessRightsCircleTypeE> circleTypes,
    Integer       pos,
    SSEntityEnum  entityType){
    
    return new SSCollEntry(uri, label, circleTypes, pos, entityType);
  }
  
  private SSCollEntry(
    SSUri        uri,
    String       label,
    List<SSAccessRightsCircleTypeE> circleTypes,
    Integer      pos, 
    SSEntityEnum entityType){

    super(uri);
    
    this.uri        = uri;
    this.label      = label;
    this.pos        = pos;
    this.entityType = entityType;
    
    if(circleTypes != null){
      this.circleTypes.addAll(circleTypes);
    }
  }
  
  public SSCollEntry(){
    super(SSStrU.empty);
  }

  @Override
  public Object jsonLDDesc(){
    
    final Map<String, Object> ld             = new HashMap<String, Object>();
    final Map<String, Object> circleTypesObj = new HashMap<String, Object>();
    
    circleTypesObj.put(SSJSONLDU.id,        SSVarU.sss + SSStrU.colon + SSAccessRightsCircleTypeE.class.getName());
    circleTypesObj.put(SSJSONLDU.container, SSJSONLDU.set);
    
    ld.put(SSVarU.circleTypes, circleTypesObj);
    
    ld.put(SSVarU.uri,        SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.pos,        SSVarU.xsd + SSStrU.colon + SSStrU.valueInteger);
    ld.put(SSVarU.space,      SSVarU.sss + SSStrU.colon + SSSpaceEnum.class.getName());
    ld.put(SSVarU.label,      SSVarU.xsd + SSStrU.colon + SSStrU.valueString);
    ld.put(SSVarU.entityType, SSVarU.sss + SSStrU.colon + SSEntityEnum.class.getName());
    
    return ld;
  }
  
  /* getters to allow for jason enconding */
  public String getUri() throws Exception{
    return SSUri.toStrWithoutSlash(uri);
  }

  public int getPos(){
    return pos;
  }

  public List<SSAccessRightsCircleTypeE> getCircleTypes(){
    return circleTypes;
  }

  public String getLabel(){
    return label;
  }
  
  public String getEntityType(){
    return SSEntityEnum.toStr(entityType);
  }
}
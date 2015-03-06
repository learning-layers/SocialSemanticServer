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
 package at.kc.tugraz.ss.service.coll.datatypes;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSSpaceE;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityA;
import at.kc.tugraz.ss.datatypes.datatypes.SSCircleE;
import at.kc.tugraz.ss.serv.jsonld.util.SSJSONLDU;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSCollEntry extends SSEntityA {

  public  SSUri                           id          = null;
  public  Integer                         pos         = -1;
  public  List<SSCircleE>                 circleTypes = new ArrayList<>();
  public  String                          label       = null;
  public  SSEntityE                       type        = null;

  public static SSCollEntry get(
    SSUri         uri,
    String        label  ,
    List<SSCircleE> circleTypes,
    Integer       pos,
    SSEntityE     entityType) throws Exception{
    
    return new SSCollEntry(uri, label, circleTypes, pos, entityType);
  }
  
  private SSCollEntry(
    SSUri        uri,
    String       label,
    List<SSCircleE> circleTypes,
    Integer      pos, 
    SSEntityE entityType) throws Exception{

    super(uri);
    
    this.id         = uri;
    this.label      = label;
    this.pos        = pos;
    this.type       = entityType;
    
    if(circleTypes != null){
      this.circleTypes.addAll(circleTypes);
    }
  }
  
  private SSCollEntry() throws Exception{
    super(SSStrU.empty);
  }

  @Override
  public Object jsonLDDesc(){
    
    final Map<String, Object> ld             = new HashMap<>();
    final Map<String, Object> circleTypesObj = new HashMap<>();
    
    circleTypesObj.put(SSJSONLDU.id,        SSVarU.sss + SSStrU.colon + SSCircleE.class.getName());
    circleTypesObj.put(SSJSONLDU.container, SSJSONLDU.set);
    
    ld.put(SSVarU.circleTypes, circleTypesObj);
    
    ld.put(SSVarU.id,         SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.pos,        SSVarU.xsd + SSStrU.colon + SSStrU.valueInteger);
    ld.put(SSVarU.space,      SSVarU.sss + SSStrU.colon + SSSpaceE.class.getName());
    ld.put(SSVarU.label,      SSVarU.xsd + SSStrU.colon + SSStrU.valueString);
    ld.put(SSVarU.type,       SSVarU.sss + SSStrU.colon + SSEntityE.class.getName());
    
    return ld;
  }
  
  /* getters to allow for jason enconding */
  public String getId() throws Exception{
    return SSStrU.removeTrailingSlash(id);
  }

  public int getPos(){
    return pos;
  }

  public List<SSCircleE> getCircleTypes(){
    return circleTypes;
  }

  public String getLabel(){
    return label;
  }
  
  public String getType(){
    return SSStrU.toStr(type);
  }
}
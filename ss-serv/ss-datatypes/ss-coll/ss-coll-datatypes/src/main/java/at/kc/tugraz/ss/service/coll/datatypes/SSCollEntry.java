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
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityEnum;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.SSSpaceEnum;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityA;
import java.util.HashMap;
import java.util.Map;

public class SSCollEntry extends SSEntityA {

  public  SSUri         uri        = null;
  public  Integer       pos        = -1;
  public  SSSpaceEnum   space      = null;
  public  String        label      = null;
  public  SSEntityEnum  entityType = null;

  public static SSCollEntry get(
    SSUri         uri,
    String        label,
    SSSpaceEnum   space,
    Integer       pos,
    SSEntityEnum  entityType){
    
    return new SSCollEntry(uri, label, space, pos, entityType);
  }
  
  private SSCollEntry(
    SSUri        uri,
    String       label,
    SSSpaceEnum  space,
    Integer      pos, 
    SSEntityEnum entityType){

    super(uri);
    
    this.uri        = uri;
    this.label      = label;
    this.space      = space;
    this.pos        = pos;
    this.entityType = entityType;
  }
  
  public SSCollEntry(){
    super(SSStrU.empty);
  }

  @Override
  public Object jsonLDDesc(){
    
    Map<String, Object> ld         = new HashMap<String, Object>();
    
    ld.put(SSVarU.uri,        SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.pos,        SSVarU.xsd + SSStrU.colon + SSStrU.valueInteger);
    ld.put(SSVarU.space,      SSVarU.sss + SSStrU.colon + SSSpaceEnum.class.getName());
    ld.put(SSVarU.label,      SSVarU.xsd + SSStrU.colon + SSStrU.valueString);
    ld.put(SSVarU.entityType, SSVarU.sss + SSStrU.colon + SSEntityEnum.class.getName());
    
    return ld;
  }
  
  /*************** getters to allow for jason enconding ********************/
  public String getUri() throws Exception{
    return SSUri.toStrWithoutSlash(uri);
  }

  public int getPos(){
    return pos;
  }

  public String getSpace(){
    return SSSpaceEnum.toStr(space);
  }

  public String getLabel(){
    return label;
  }
  
  public String getEntityType(){
    return SSEntityEnum.toStr(entityType);
  }
}
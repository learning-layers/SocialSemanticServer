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
package at.kc.tugraz.ss.datatypes.datatypes;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import java.util.HashMap;
import java.util.Map;

public abstract class SSEntityDescA extends SSEntityA{

	public  SSLabelStr          label           = null;
	public  Long                creationTime    = null;
  public  SSUri               entityUri       = null;
  public  SSEntityEnum        entityType      = null;
  public  SSEntityEnum        entityDescType  = null;
  public  SSUri               author          =  null;
	
  protected SSEntityDescA(
    final SSUri        entityUri, 
    final SSLabelStr   label, 
    final Long         creationTime, 
    final SSEntityEnum entityType, 
    final SSEntityEnum entityDescType,
    final SSUri        author){
    
    super(entityUri);
    
    this.entityUri      = entityUri;
    this.label          = label;
    this.creationTime   = creationTime;
    this.entityType     = entityType;
    this.entityDescType = entityDescType;
    this.author         = author;
  }
  
  @Override
  public Object jsonLDDesc(){
    
    Map<String, Object> ld         = new HashMap<String, Object>();

    ld.put(SSVarU.entityUri,      SSVarU.sss  + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.label,          SSVarU.sss  + SSStrU.colon + SSLabelStr.class.getName());
    ld.put(SSVarU.creationTime,   SSVarU.xsd + SSStrU.colon + SSStrU.valueLong);
    ld.put(SSVarU.entityType,     SSVarU.sss + SSStrU.colon + SSEntityEnum.class.getName());
    ld.put(SSVarU.entityDescType, SSVarU.sss + SSStrU.colon + SSEntityEnum.class.getName());
    ld.put(SSVarU.author,         SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    
    return ld;
  }
  
  /*************** getters to allow for json enconding ********************/
  public String getEntityUri() throws Exception {
    return SSUri.toStrWithoutSlash(entityUri);
  }

  public String getLabel() {
    return SSLabelStr.toStr(label);
  }

  public Long getCreationTime() {
    return creationTime;
  }

  public String getEntityType() {
    return SSEntityEnum.toStr(entityType);
  }

  public String getEntityDescType() {
    return SSEntityEnum.toStr(entityDescType);
  }
  
  public String getAuthor() throws Exception {
    return SSUri.toStrWithoutSlash(author);
  }
}
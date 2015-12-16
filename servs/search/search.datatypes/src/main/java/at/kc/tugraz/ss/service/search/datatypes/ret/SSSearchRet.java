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
package at.kc.tugraz.ss.service.search.datatypes.ret;


import at.tugraz.sss.serv.SSJSONLDU;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSVarNames;
import at.tugraz.sss.serv.SSServRetI; import at.tugraz.sss.serv.SSVarNames;
import at.tugraz.sss.serv.SSEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSSearchRet extends SSServRetI{
  
  public String         pagesID     = null;
  public Integer        pageNumber  = null;
  public Integer        pageNumbers = null;
  public List<SSEntity> entities    = new ArrayList<>();
  
  public static SSSearchRet get(
    final List<SSEntity> entities,
    final String         pagesID,
    final Integer        pageNumber,
    final Integer        pageNumbers){
    
    return new SSSearchRet(entities, pagesID, pageNumber, pageNumbers);
  }
  
  private SSSearchRet(
    final List<SSEntity> entities,
    final String         pagesID,
    final Integer        pageNumber,
    final Integer        pageNumbers){
    
    super(SSVarNames.search);
    
    SSEntity.addEntitiesDistinctWithoutNull(this.entities, entities);
    
    this.pagesID     = pagesID;
    this.pageNumber  = pageNumber;
    this.pageNumbers = pageNumbers;
  }
  
  @Override
  public Map<String, Object> jsonLDDesc(){
    
    final Map<String, Object> ld          = new HashMap<>();
    final Map<String, Object> entitiesObj = new HashMap<>();
    
    entitiesObj.put(SSJSONLDU.id,        SSVarNames.sss + SSStrU.colon + SSEntity.class.getName());
    entitiesObj.put(SSJSONLDU.container, SSJSONLDU.set);
    
    ld.put(SSVarNames.entities, entitiesObj);
    
    ld.put(SSVarNames.pagesID,     SSVarNames.xsd + SSStrU.colon + SSStrU.valueString);
    ld.put(SSVarNames.pageNumber,  SSVarNames.xsd + SSStrU.colon + SSStrU.valueInteger);
    ld.put(SSVarNames.pageNumbers, SSVarNames.xsd + SSStrU.colon + SSStrU.valueInteger);
    
    return ld;
  }
}

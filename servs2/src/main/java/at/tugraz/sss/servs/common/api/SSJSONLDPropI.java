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
package at.tugraz.sss.servs.common.api;

public interface SSJSONLDPropI{
  public Object jsonLDDesc();
}

//e.g. 
//@Override
//  public Object jsonLDDesc(){
//    
//    return super.jsonLDDesc();
//  }

//e.g.
//@Override
//  public Object jsonLDDesc(){
//    return SSVarNames.xsd + SSStrU.colon + SSStrU.valueString;
//  }

//e.g.
//@Override
//  public Object jsonLDDesc(){
//    return SSLinkU.schemaOrgUrl;
//  }

//e.g.
//  @Override
//  public Object jsonLDDesc() {
//  
//    final Map<String, Object> ld = new HashMap<>();
//    
//    ld.put(SSVarNames.label,   SSVarNames.sss + SSStrU.colon + SSCategoryLabel.class.getName());
//    ld.put(SSVarNames.space,   SSVarNames.sss + SSStrU.colon + SSSpaceE.class.getName());
//    ld.put(SSVarNames.frequ,   SSVarNames.xsd + SSStrU.colon + SSStrU.valueInteger);
//    
//    return ld;
//  }  

//e.g.
//@Override
//  public Object jsonLDDesc(){
//    
//    final Map<String, Object> ld                 = (Map<String, Object>) super.jsonLDDesc();
//    final Map<String, Object> learnEpCirclesObj  = new HashMap<>();
//    final Map<String, Object> learnEpEntitiesObj = new HashMap<>();
//    
//    ld.put(SSVarNames.learnEp,                SSVarNames.sss + SSStrU.colon + SSUri.class.getName());
//    ld.put(SSVarNames.learnEpTimelineState,   SSVarNames.sss + SSStrU.colon + SSLearnEpTimelineState.class.getName());
//    
//    learnEpEntitiesObj.put(SSJSONLDU.id,        SSVarNames.sss + SSStrU.colon + SSLearnEpEntity.class.getName());
//    learnEpEntitiesObj.put(SSJSONLDU.container, SSJSONLDU.set);
//    
//    ld.put(SSVarNames.learnEpEntities, learnEpEntitiesObj);
//    
//    learnEpCirclesObj.put(SSJSONLDU.id,        SSVarNames.sss + SSStrU.colon + SSLearnEpCircle.class.getName());
//    learnEpCirclesObj.put(SSJSONLDU.container, SSJSONLDU.set);
//    
//    ld.put(SSVarNames.learnEpCircles, learnEpCirclesObj);
//    
//    return ld;
//  }
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
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityA;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSCircleE;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSEntity;
import at.kc.tugraz.ss.serv.jsonld.util.SSJSONLDU;
import java.util.*;

public class SSColl extends SSEntity{

  public static SSColl get(
    final SSUri                     id,
    final List<SSEntityA>           entries,
    final SSUri                     author,
    final SSLabel                   label,
    final List<SSCircleE>           circleTypes) throws Exception{
    
    return new SSColl(id, entries, author, label, circleTypes);
  }
  
  private SSColl(
    final SSUri                           id,
    final List<SSEntityA>                 entries,
    final SSUri                           author,
    final SSLabel                         label,
    final List<SSCircleE>                 circleTypes) throws Exception{
    
    super(id,
      label,
      null,
      SSEntityE.coll,
      author,
      null,
      circleTypes,
      entries);
  }

  @Override
  public Object jsonLDDesc(){
    
    final Map<String, Object>  ld             = (Map<String, Object>) super.jsonLDDesc();
    final Map<String, Object>  entriesObj     = new HashMap<>();

    entriesObj.put(SSJSONLDU.id,        SSVarU.sss + SSStrU.colon + SSCollEntry.class.getName());
    entriesObj.put(SSJSONLDU.container, SSJSONLDU.set);
    
    ld.put(SSVarU.entries, entriesObj);

    return ld;
  }
  
  public static SSColl[] toCollArray(Collection<SSColl> toConvert) {
    return (SSColl[]) toConvert.toArray(new SSColl[toConvert.size()]);
  }
}
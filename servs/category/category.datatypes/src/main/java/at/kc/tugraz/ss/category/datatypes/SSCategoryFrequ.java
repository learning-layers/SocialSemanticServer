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
package at.kc.tugraz.ss.category.datatypes;

import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.api.SSEntityA;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.enums.SSSpaceE;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSCategoryFrequ extends SSEntityA{
  
  public SSCategoryLabel    label  = null;
  public SSSpaceE           space  = null;
	public Integer            frequ  = -1;
   
   public String getLabel() {
		return SSStrU.toStr(label);
	}
  
  public String getSpace() {
		return SSStrU.toStr(space);
	}
  
  public static SSCategoryFrequ get(
    final SSCategoryLabel    label,
    final SSSpaceE           space,
    final Integer            frequ) throws Exception{
    
    return new SSCategoryFrequ(label, space, frequ);
  }  
	
  protected SSCategoryFrequ(
    final SSCategoryLabel    label,
    final SSSpaceE           space,
    final Integer            frequ) throws Exception{
		
    super(label);
    
		this.label      = label;
    this.space      = space;
		this.frequ      = frequ;
	}
  
  public static void addDistinctWithoutNull(
    final List<SSCategoryFrequ>     entities,
    final SSCategoryFrequ           entity){
    
    if(
      SSObjU.isNull  (entities, entity) ||
      SSStrU.contains(entities, entity)){
      return;
    }
    
    entities.add(entity);
  }
  
  public static void addDistinctWithoutNull(
    final List<SSCategoryFrequ>  entities,
    final List<SSCategoryFrequ>  toAddEntities){
    
    if(SSObjU.isNull(entities, toAddEntities)){
      return;
    }
    
    for(SSCategoryFrequ entity : toAddEntities){
      
      if(entity == null){
        continue;
      }
      
      if(!SSStrU.contains(entities, entity)){
        entities.add(entity);
      }
    }
  }
}
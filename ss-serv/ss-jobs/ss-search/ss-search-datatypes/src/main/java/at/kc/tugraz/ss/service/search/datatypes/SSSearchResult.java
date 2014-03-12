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
 package at.kc.tugraz.ss.service.search.datatypes;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.SSSpaceEnum;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityEnum;
import at.kc.tugraz.ss.serv.jsonld.datatypes.api.SSJSONLDPropI;
import at.kc.tugraz.ss.service.rating.datatypes.*;
import java.util.*;

public class SSSearchResult implements SSJSONLDPropI{
  
  public  SSUri                      uri                             = null;
  public  SSSpaceEnum                space                           = null;
  public  String                     label                           = null;
  public  SSEntityEnum               type                            = null;

  public SSSearchResult(
    SSUri       uri,
    SSSpaceEnum space){
    
    this.uri    = uri;
    this.space  = space;
  }
  
  @Override
  public Object jsonLDDesc(){
    
    final Map<String, Object> ld = new HashMap<String, Object>();
    
    ld.put(SSVarU.uri,           SSVarU.sss + SSStrU.colon  + SSUri.class.getName());
    ld.put(SSVarU.space,         SSVarU.sss + SSStrU.colon  + SSSpaceEnum.class.getName());
    ld.put(SSVarU.label,         SSVarU.xsd + SSStrU.colon  + SSStrU.valueString);
    ld.put(SSVarU.type,          SSVarU.sss + SSStrU.colon  + SSEntityEnum.class.getName());
    ld.put(SSVarU.userRating,    SSVarU.xsd + SSStrU.colon  + SSStrU.valueInteger);
    ld.put(SSVarU.overallRating, SSVarU.sss + SSStrU.colon  + SSRatingOverall.class.getName());
    
    return ld;
  }

  /*************** getters to allow for json enconding ********************/
  public String getUri() throws Exception{
    return SSUri.toStrWithoutSlash(uri);
  }

  public String getSpace(){
    return SSSpaceEnum.toStr(space);
  }

  public String getLabel(){
    return label;
  }

  public String getType(){
    return SSEntityEnum.toStr(type);
  }
}
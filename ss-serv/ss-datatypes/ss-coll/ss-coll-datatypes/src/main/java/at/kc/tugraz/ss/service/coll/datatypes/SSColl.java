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
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityA;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSCircleE;
import at.kc.tugraz.ss.serv.jsonld.util.SSJSONLDU;
import java.util.*;

public class SSColl extends SSEntityA{

  public  SSUri                           id          = null;
	public  List<SSCollEntry>               entries     = new ArrayList<>();
	public  SSUri                           author      = null;
	public  String                          label       = null;
	public  List<SSCircleE>                 circleTypes = new ArrayList<>();
	
  public static SSColl get(
    SSUri                           uri    ,
    List<SSCollEntry>               entries,
    SSUri                           author ,
    String                          label  ,
    List<SSCircleE> circleTypes) throws Exception{
    
    return new SSColl(uri, entries, author, label, circleTypes);
  }
  
  private SSColl(
    SSUri                           uri    ,
    List<SSCollEntry>               entries,
    SSUri                           author ,
    String                          label  ,
    List<SSCircleE>                 circleTypes) throws Exception{

    super(uri);
    
    this.id       = uri;
    this.author   = author;
    this.label    = label;
    
    if(entries != null){
      this.entries  = entries;
    }
    
    if(circleTypes != null){
      this.circleTypes.addAll(circleTypes);
    }
  }

  private SSColl() throws Exception{
    super(SSStrU.empty);
  }
    
  @Override
  public Object jsonLDDesc(){
    
    Map<String, Object> ld             = new HashMap<>();
    Map<String, Object> entriesObj     = new HashMap<>();
    Map<String, Object> circleTypesObj = new HashMap<>();
    
    ld.put(SSVarU.id, SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    
    entriesObj.put(SSJSONLDU.id,        SSVarU.sss + SSStrU.colon + SSCollEntry.class.getName());
    entriesObj.put(SSJSONLDU.container, SSJSONLDU.set);
    
    ld.put(SSVarU.entries, entriesObj);
    
    circleTypesObj.put(SSJSONLDU.id,        SSVarU.sss + SSStrU.colon + SSCircleE.class.getName());
    circleTypesObj.put(SSJSONLDU.container, SSJSONLDU.set);
    
    ld.put(SSVarU.circleTypes, circleTypesObj);
    
    ld.put(SSVarU.author,        SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.label,         SSVarU.xsd + SSStrU.colon + SSStrU.valueString);
    
    return ld;
  }
  
  public static SSColl[] toCollArray(Collection<SSColl> toConvert) {
    return (SSColl[]) toConvert.toArray(new SSColl[toConvert.size()]);
  }
  
  /* getters to allow for json enconding */
  
  public String getId() throws Exception{
    return SSStrU.removeTrailingSlash(id);
  }

  public List<SSCollEntry> getEntries(){
    return entries;
  }

  public String getAuthor() throws Exception{
    return SSStrU.removeTrailingSlash(author);
  }

  public String getLabel(){
    return label;
  }

  public List<SSCircleE> getCircleTypes(){
    return circleTypes;
  }  
}
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
 package at.kc.tugraz.ss.service.userevent.datatypes;

import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityA;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSUE extends SSEntityA {

  public         SSUri            id         = null;
  public         SSUri            user       = null;
  public         SSUri            entity     = null;
  public         SSUEE            type       = null;
  public         String           content    = null;
  public         Long             timestamp  = -1L;

  public static SSUE get(
    final SSUri           uri, 
    final SSUri           user,
    final SSUEE        type,
    final SSUri           resource,
    final String          content,
    final Long            timestamp) throws Exception{
    
    return new SSUE(uri, user, type, resource, content, timestamp);
  }
  
  @Override
  public Object jsonLDDesc(){
    
    Map<String, Object> ld = new HashMap<>();

    ld.put(SSVarU.id,         SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.user,       SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.entity,     SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.type,       SSVarU.sss + SSStrU.colon + SSUEE.class.getName());
    ld.put(SSVarU.content,    SSVarU.xsd + SSStrU.colon + SSStrU.valueString);
    ld.put(SSVarU.timestamp,  SSVarU.xsd + SSStrU.colon + SSStrU.valueLong);
    
    return ld;
  }

  /* sorts SSUserEventEnum array ascending according to time */
  public static List<SSUE> sort(final List<SSUE> toSortUserEvents) {

    boolean    changedSomething = true;
    SSUE[]     helper           = new SSUE[toSortUserEvents.size()];
    List<SSUE> result           = new ArrayList<>();
    SSUE       storage;
    
    for (int counter = 0; counter < toSortUserEvents.size(); counter++) {

      helper[counter] = toSortUserEvents.get(counter);
    }

    while (changedSomething) {

      changedSomething = false;

      for (int counter = 0; counter < toSortUserEvents.size(); counter++) {

        if (counter + 1 < toSortUserEvents.size()) {

          if (helper[counter].timestamp > helper[counter + 1].timestamp) {

            storage = helper[counter];

            helper[counter] = helper[counter + 1];
            helper[counter + 1] = storage;

            changedSomething = true;
          }
        }
      }
    }
    
    result.addAll(Arrays.asList(helper));

    return result;
  }
  
	public static Boolean isRatingContentCorrect(final SSUE event) {
	
    if(SSStrU.equals(event.content, SSStrU.valueNA)){
      return false;
    }
    
    try{
      Integer.parseInt(event.content);
    }catch(Exception error){
      return false;
    }
    
    return true;
	}
  
  private SSUE(
    SSUri           uri, 
    SSUri           user,
    SSUEE        type,
    SSUri           resource,
    String          content,
    Long            timestamp) throws Exception{

    super(SSStrU.toStr(type));
    
    this.id        = uri;
    this.user       = user;
    this.type       = type;
    this.entity   = resource;
    this.content    = content;
    this.timestamp  = timestamp;
  }

  /* getters to allow for json enconding */
  public String getId() throws Exception{
    return SSStrU.removeTrailingSlash(id);
  }

  public String getUser() throws Exception{
    return SSStrU.removeTrailingSlash(user);
  }

  public String getType(){
    return SSStrU.toStr(type);
  }

  public String getEntity() throws Exception{
    return SSStrU.removeTrailingSlash(entity);
  }

  public String getContent(){
    return content;
  }

  public Long getTimestamp(){
    return timestamp;
  }
}
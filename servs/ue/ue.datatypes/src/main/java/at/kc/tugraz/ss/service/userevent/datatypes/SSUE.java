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

import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSEntityE;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SSUE extends SSEntity {

  public         SSEntity         user           = null;
  public         SSEntity         entity         = null;
  public         SSUEE            userEventType  = null;
  public         String           content        = null;

  public void setUserEventType(final String userEventType) throws Exception{
    this.userEventType = SSUEE.get(userEventType);
  }
  
  public String getUserEventType(){
    return SSStrU.toStr(userEventType);
  }

  public static SSUE get(
    final SSUE           userEvent, 
    final SSEntity       entity) throws Exception{
    
    return new SSUE(userEvent, entity);
  }
  
  protected SSUE(
    final SSUE           userEvent, 
    final SSEntity       entity) throws Exception{
     
    super(entity);
    
    this.user          = userEvent.user;
    this.entity        = userEvent.entity;
    this.userEventType = userEvent.userEventType;
    this.content       = userEvent.content;    
   }
  
  public static SSUE get(
    final SSUri           id, 
    final SSEntity        user,
    final SSUEE           userEventType,
    final SSEntity        entity,
    final String          content) throws Exception{
    
    return new SSUE(id, user, userEventType, entity, content);
  }
  
  protected SSUE(
    final SSUri           id, 
    final SSEntity        user,
    final SSUEE           userEventType,
    final SSEntity        entity,
    final String          content) throws Exception{

    super(id, SSEntityE.userEvent);
    
    this.user              = user;
    this.userEventType     = userEventType;
    this.entity            = entity;
    this.content           = content;
  }
  
  @Override
  public Object jsonLDDesc(){
    throw new UnsupportedOperationException();
  }

  /* sorts SSUserEventEnum array ascending according to time */
  public static List<SSUE> sort(final List<SSEntity> toSortUserEvents) {

    boolean        changedSomething = true;
    SSUE[]         helper           = new SSUE[toSortUserEvents.size()];
    List<SSUE>     result           = new ArrayList<>();
    SSUE           storage;
    
    for (int counter = 0; counter < toSortUserEvents.size(); counter++) {
      helper[counter] = (SSUE) toSortUserEvents.get(counter);
    }

    while (changedSomething) {

      changedSomething = false;

      for (int counter = 0; counter < toSortUserEvents.size(); counter++) {

        if (counter + 1 < toSortUserEvents.size()) {

          if (helper[counter].creationTime > helper[counter + 1].creationTime) {

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
}
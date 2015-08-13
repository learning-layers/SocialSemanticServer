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
 package at.kc.tugraz.ss.serv.modeling.ue.impl;

import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSUri;
import at.kc.tugraz.ss.serv.modeling.ue.datatypes.SSModelUEEntity;
import at.kc.tugraz.ss.service.userevent.api.SSUEServerI;
import at.tugraz.sss.serv.SSServImplA;
import at.kc.tugraz.ss.service.userevent.datatypes.*;
import at.kc.tugraz.ss.service.userevent.datatypes.pars.SSUEsGetPar;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSServReg;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSModelUEUESetter{

  private final Map<String, SSModelUEEntity>        resources;
  private final SSServImplA                           serv;
  
  public SSModelUEUESetter(
    final SSServImplA              serv,
    Map<String, SSModelUEEntity> resources){
    
    this.resources = resources;
    this.serv      = serv;
  }
  
	/**
	 * 0 remove events from resources <br>
	 */
	public void removeOldEventsFromModel(){
		
		for(SSModelUEEntity resource : resources.values()){
			resource.events.clear();       
			resource.personsEvents.clear();
		}
	}
	
	/* set new events to RM */
	public List<SSUE> setNewEventsToModel(long lastUpdateTime) throws Exception{

		HashMap<String, List<SSUE>>  eventsPerResource;
		HashMap<String, List<SSUE>>  eventsPerUser;
		SSModelUEEntity              foundOrCreatedResource;
    List<SSEntity>               newEvents;
    List<SSUE>                   sortedEventsSinceLastUpdate;
    
    newEvents = 
      ((SSUEServerI) SSServReg.getServ(SSUEServerI.class)).userEventsGet(
        new SSUEsGetPar(
          null, 
          null, 
          null, 
          null, 
          lastUpdateTime, 
          null, 
          false, 
          false));
      
		sortedEventsSinceLastUpdate = SSUE.sort(newEvents);

		eventsPerResource = getUserEventsPerResourceFromUserEventList (sortedEventsSinceLastUpdate);
		eventsPerUser     = getUserEventsPerUserFromUserEventList     (sortedEventsSinceLastUpdate);
					
		//for each evented resource
		for(String resourceUrl : eventsPerResource.keySet()){

			foundOrCreatedResource        = findResourceOrUserOrAddNewOne(resourceUrl);
			foundOrCreatedResource.events.addAll(eventsPerResource.get(resourceUrl)); 
		}
		
		//for each user which evented a resource
		for(String userUrl : eventsPerUser.keySet()){

			foundOrCreatedResource               = findResourceOrUserOrAddNewOne(userUrl);
			foundOrCreatedResource.personsEvents.addAll(eventsPerUser.get(userUrl));
		}	
    
    return sortedEventsSinceLastUpdate;
	}
  
  /**
	 *	0 find the resource/user with this url OR create a new one if it not exists <br>
	 */
  private SSModelUEEntity findResourceOrUserOrAddNewOne(
			String resourceOrUserUrl) throws Exception{
		
		SSModelUEEntity foundOrCreatedResourceOrUser;
		
		if(resources.containsKey(resourceOrUserUrl) == false){
			
			foundOrCreatedResourceOrUser = new SSModelUEEntity(SSUri.get(resourceOrUserUrl));
			
			resources.put(resourceOrUserUrl, foundOrCreatedResourceOrUser);
			
		}else{
			foundOrCreatedResourceOrUser = resources.get(resourceOrUserUrl);
		}
		
		return foundOrCreatedResourceOrUser;
	}
	
  private HashMap<String, List<SSUE>> getUserEventsPerUserFromUserEventList(List<SSUE> sortedUserEvents) {

		HashMap<String, List<SSUE>>        eventsPerUser         = new HashMap<String, List<SSUE>>();
		List<SSUE>                    eventsForResource;
		
		for(SSUE userEvent : sortedUserEvents){
			
			if(eventsPerUser.containsKey(SSStrU.toStr(userEvent.user))){
				
				eventsPerUser.get(SSStrU.toStr(userEvent.user)).add(userEvent);
				
			}else{
			
				eventsForResource = new ArrayList<SSUE>();
				
				eventsForResource.add(userEvent);
				
				eventsPerUser.put(SSStrU.toStr(userEvent.user), eventsForResource);
			}
		}
		
		return eventsPerUser;
	}
  
  private HashMap<String, List<SSUE>> getUserEventsPerResourceFromUserEventList(List<SSUE> sortedUserEvents){
		
		HashMap<String, List<SSUE>>        eventsPerResource     = new HashMap<String, List<SSUE>>();
		List<SSUE>                         eventsForResource;
		
		for(SSUE userEvent : sortedUserEvents){
			
			if(eventsPerResource.containsKey(SSStrU.toStr(userEvent.entity))){
				
				eventsPerResource.get(SSStrU.toStr(userEvent.entity)).add(userEvent);
				
			}else{
			
				eventsForResource = new ArrayList<SSUE>();
				
				eventsForResource.add(userEvent);
				
				eventsPerResource.put(SSStrU.toStr(userEvent.entity), eventsForResource);
			}
		}
		
		return eventsPerResource;
	}
}
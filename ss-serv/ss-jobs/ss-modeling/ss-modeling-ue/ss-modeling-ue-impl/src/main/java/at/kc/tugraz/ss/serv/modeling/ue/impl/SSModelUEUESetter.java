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
 package at.kc.tugraz.ss.serv.modeling.ue.impl;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.serv.modeling.ue.datatypes.SSModelUEResource;
import at.kc.tugraz.ss.serv.serv.api.SSServImplA;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.service.userevent.datatypes.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSModelUEUESetter{

  private final Map<String, SSModelUEResource>        resources;
  private final SSServImplA                           serv;
  
  public SSModelUEUESetter(
    final SSServImplA              serv,
    Map<String, SSModelUEResource> resources){
    
    this.resources = resources;
    this.serv      = serv;
  }
  
	/**
	 * 0 remove events from resources <br>
	 */
	public void removeOldEventsFromModel(){
		
		for(SSModelUEResource resource : resources.values()){
			resource.events.clear();       
			resource.personsEvents.clear();
		}
	}
	
	/**
	 * 0 set new events to RM <br>
	 */
	public List<SSUE> setNewEventsToModel(long lastUpdateTime) throws Exception{

		HashMap<String, List<SSUE>>  eventsPerResource;
		HashMap<String, List<SSUE>>  eventsPerUser;
		SSModelUEResource            foundOrCreatedResource;
    List<SSUE>                   newEvents;
    List<SSUE>                   sortedEventsSinceLastUpdate;
    
    newEvents = SSServCaller.getUEs(null, null, null, null, lastUpdateTime, null);
    
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
  private SSModelUEResource findResourceOrUserOrAddNewOne(
			String resourceOrUserUrl) throws Exception{
		
		SSModelUEResource foundOrCreatedResourceOrUser;
		
		if(resources.containsKey(resourceOrUserUrl) == false){
			
			foundOrCreatedResourceOrUser = new SSModelUEResource(SSUri.get(resourceOrUserUrl));
			
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
			
			if(eventsPerUser.containsKey(SSStrU.toString(userEvent.user))){
				
				eventsPerUser.get(SSStrU.toString(userEvent.user)).add(userEvent);
				
			}else{
			
				eventsForResource = new ArrayList<SSUE>();
				
				eventsForResource.add(userEvent);
				
				eventsPerUser.put(SSStrU.toString(userEvent.user), eventsForResource);
			}
		}
		
		return eventsPerUser;
	}
  
  private HashMap<String, List<SSUE>> getUserEventsPerResourceFromUserEventList(List<SSUE> sortedUserEvents){
		
		HashMap<String, List<SSUE>>        eventsPerResource     = new HashMap<String, List<SSUE>>();
		List<SSUE>                         eventsForResource;
		
		for(SSUE userEvent : sortedUserEvents){
			
			if(eventsPerResource.containsKey(SSStrU.toString(userEvent.resource))){
				
				eventsPerResource.get(SSStrU.toString(userEvent.resource)).add(userEvent);
				
			}else{
			
				eventsForResource = new ArrayList<SSUE>();
				
				eventsForResource.add(userEvent);
				
				eventsPerResource.put(SSStrU.toString(userEvent.resource), eventsForResource);
			}
		}
		
		return eventsPerResource;
	}
}
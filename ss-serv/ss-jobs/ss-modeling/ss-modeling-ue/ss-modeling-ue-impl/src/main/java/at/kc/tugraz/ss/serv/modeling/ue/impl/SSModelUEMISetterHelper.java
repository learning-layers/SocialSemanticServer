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

import at.kc.tugraz.ss.datatypes.datatypes.SSUEEnum;
import at.kc.tugraz.socialserver.utils.SSNumberU;
import at.kc.tugraz.ss.serv.modeling.ue.datatypes.SSModelUEResource;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityEnum;
import at.kc.tugraz.ss.service.userevent.datatypes.*;
import java.util.ArrayList;
import java.util.List;

public class SSModelUEMISetterHelper {

  private static List<String> usedMis = new ArrayList<String>();
  
	public boolean getNumberOfTimesMaturingIndicator(
			SSModelUEResource resource,
			int           counter,
			double        collectionThreshold,
			double        discussionThreshold,
			double        digitalResourceThreshold,
			double        personThreshold){
		
		if(
				SSEntityEnum.isColl(resource.type)      &&
				SSNumberU.isGreaterThan(counter,collectionThreshold)){
			
			return true;

		}else if(
				SSEntityEnum.isDisc(resource.type)         &&		
				SSNumberU.isGreaterThan(counter,discussionThreshold)){

			return true;
			
		}else if(
				SSEntityEnum.isResourceOrFile(resource.type)   &&
				SSNumberU.isGreaterThan(counter,digitalResourceThreshold)){

			return true;
			
		}else if(
				SSEntityEnum.isUser(resource.type) && 
				SSNumberU.isGreaterThan(counter,personThreshold)){

			return true;
		}
		
		return false;
	}


	public boolean getLessNumberOfTimesMaturityIndicator(
			SSModelUEResource resource,
			int        counter,
			double     collectionThreshold,
			double     discussionThreshold,
			double     digitalResourceThreshold){
		
		if(
				SSEntityEnum.isColl(resource.type) &&
				SSNumberU.isLessThanOrEqual(counter,collectionThreshold)                ){
		
			return true;
			
		}else if(
				SSEntityEnum.isDisc(resource.type)   &&				
				SSNumberU.isLessThanOrEqual(counter,discussionThreshold)                ){
			
			return true;
			
		}else if(
				SSEntityEnum.isResourceOrFile(resource.type)  &&
				SSNumberU.isLessThanOrEqual(counter,digitalResourceThreshold)           ){
			
			return true;
		}
		
		return false;
	}

	/**
	 * 0 returns events from resource's event history matching given event types
	 */
	public List<SSUE> getResourceEventsOfType(
			SSModelUEResource              resource,
			List<SSUEEnum>      eventTypes) {

		List<SSUE> result = new ArrayList<SSUE>();
		
		for(SSUE event : resource.events){
			
			for(SSUEEnum eventType : eventTypes){
				
				if(SSUEEnum.isSame(event.type, eventType)){
					
					result.add(event);
				}
			}
		}
		
		return result;
	}
	
	public void addIndicator(
			SSModelUEResource  resource,
			List<String>       indicatorsForCurrentDay,
			String             indicator){
		
		indicatorsForCurrentDay.add(indicator);
    
    if(!usedMis.contains(indicator)){
    
      usedMis.add(indicator);
    }
    		
		if(resource.mIDayCounts.containsKey(indicator) == false){
			
			resource.mIDayCounts.put(indicator, 1);
		}else{
			
			int number = resource.mIDayCounts.get(indicator) + 1;
			
			resource.mIDayCounts.put(indicator, number);
		}
	}
}

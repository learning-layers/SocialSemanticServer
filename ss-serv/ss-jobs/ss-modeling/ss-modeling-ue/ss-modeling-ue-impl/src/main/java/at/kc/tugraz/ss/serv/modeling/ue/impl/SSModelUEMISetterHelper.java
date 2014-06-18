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

import at.kc.tugraz.ss.service.userevent.datatypes.SSUEE;
import at.kc.tugraz.socialserver.utils.SSNumberU;
import at.kc.tugraz.ss.serv.modeling.ue.datatypes.SSModelUEEntity;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.service.userevent.datatypes.*;
import java.util.ArrayList;
import java.util.List;

public class SSModelUEMISetterHelper {

  private static List<String> usedMis = new ArrayList<>();
  
	public boolean getNumberOfTimesMaturingIndicator(
			SSModelUEEntity resource,
			int           counter,
			double        collectionThreshold,
			double        discussionThreshold,
			double        digitalResourceThreshold,
			double        personThreshold){
		
		if(
				SSEntityE.isColl(resource.type)      &&
				SSNumberU.isGreaterThan(counter,collectionThreshold)){
			
			return true;

		}else if(
				SSEntityE.isDisc(resource.type)         &&		
				SSNumberU.isGreaterThan(counter,discussionThreshold)){

			return true;
			
		}else if(
				SSEntityE.isResourceOrFile(resource.type)   &&
				SSNumberU.isGreaterThan(counter,digitalResourceThreshold)){

			return true;
			
		}else if(
				SSEntityE.isUser(resource.type) && 
				SSNumberU.isGreaterThan(counter,personThreshold)){

			return true;
		}
		
		return false;
	}


	public boolean getLessNumberOfTimesMaturityIndicator(
			SSModelUEEntity resource,
			int        counter,
			double     collectionThreshold,
			double     discussionThreshold,
			double     digitalResourceThreshold){
		
		if(
				SSEntityE.isColl(resource.type) &&
				SSNumberU.isLessThanOrEqual(counter,collectionThreshold)                ){
		
			return true;
			
		}else if(
				SSEntityE.isDisc(resource.type)   &&				
				SSNumberU.isLessThanOrEqual(counter,discussionThreshold)                ){
			
			return true;
			
		}else if(
				SSEntityE.isResourceOrFile(resource.type)  &&
				SSNumberU.isLessThanOrEqual(counter,digitalResourceThreshold)           ){
			
			return true;
		}
		
		return false;
	}

	/**
	 * 0 returns events from resource's event history matching given event types
	 */
	public List<SSUE> getResourceEventsOfType(
			SSModelUEEntity              resource,
			List<SSUEE>      eventTypes) {

		List<SSUE> result = new ArrayList<SSUE>();
		
		for(SSUE event : resource.events){
			
			for(SSUEE eventType : eventTypes){
				
				if(SSUEE.equals(event.type, eventType)){
					
					result.add(event);
				}
			}
		}
		
		return result;
	}
	
	public void addIndicator(
			SSModelUEEntity  resource,
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

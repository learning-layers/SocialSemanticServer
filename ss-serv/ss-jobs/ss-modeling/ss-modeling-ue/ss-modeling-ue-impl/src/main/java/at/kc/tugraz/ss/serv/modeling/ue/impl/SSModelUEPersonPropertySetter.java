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
import at.kc.tugraz.ss.datatypes.datatypes.SSTagLabel;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityEnum;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.serv.modeling.ue.datatypes.SSModelUEResource;
import at.kc.tugraz.ss.serv.modeling.ue.datatypes.SSModelUETopicScore;
import at.kc.tugraz.ss.serv.modeling.ue.datatypes.enums.SSModelUEResourceCounterEnum;
import at.kc.tugraz.ss.serv.modeling.ue.utils.SSModelUEU;
import at.kc.tugraz.ss.serv.serv.api.SSServImplA;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.service.tag.datatypes.*;
import at.kc.tugraz.ss.service.userevent.datatypes.*;
import java.util.*;

public class SSModelUEPersonPropertySetter {

  private final List<SSUE>                     sortedEventsSinceLastUpdate;
  private final Map<String, SSModelUEResource> resources;
  private final Map<String, String>            currentTopicCreators;
  private final SSServImplA                    serv;
  
  public SSModelUEPersonPropertySetter(
    final SSServImplA              serv,
    final List<SSUE>                     sortedEventsSinceLastUpdate, 
    final Map<String, SSModelUEResource> resources){
    
    this.sortedEventsSinceLastUpdate  = sortedEventsSinceLastUpdate;
    this.resources                    = resources;
    this.currentTopicCreators         = new HashMap<String, String>();
    this.serv                         = serv;
  }
  
	public void setPersonIndependentProperties(
    SSModelUEResource     resource) throws Exception{
		
		List<Long> recentTimeStamps = new ArrayList<Long>();
		
		if(SSEntityEnum.isUser(resource.type)){
			
			recentTimeStamps.add(0L);
			recentTimeStamps.add(0L);
			
			for(SSUE event : resource.personsEvents){
				
				increaseCountersDiscussionOf                         (resource, event);
				increaseCountersRelateResource                       (resource, event);
				setRecentProperties                                  (resource, event, recentTimeStamps);
			}
			
			setPersonsTopics           (resource);
			setPersonsRelatedPersons   (resource);
		}
	}
	
	public void setSomething() {
		
    //		String                 creatorUrl         = null;

		SSModelUEResource user;
		
		for (SSUE event : sortedEventsSinceLastUpdate){

			if(SSUEEnum.contains(SSModelUEU.useTopicEventTypes, event.type)){
				
				user = resources.get(SSStrU.toString(event.user));
				
				//topic of event does already exist
				if(currentTopicCreators.containsKey(event.content)){

					//user is author of topic
					if(SSStrU.equals(currentTopicCreators.get(event.content), SSStrU.toString(user.resourceUrl))){

						addTopicUsingEvent(user,event);
					
					}else{//user is not author of topic
						
//						creatorUrl = 
//							RMGlobalConstants.currentTopicCreators.get(event.topic);
//						
//						addTopicUsingEvent(RM.resources.get(creatorUrl),event);
						addTopicUsingEvent(user,event);
					}
				}else{//topic is new

					currentTopicCreators.put(event.content, SSStrU.toString(user.resourceUrl));

					addTopicCreationEvent(user,event);
				} 			
			}
		}
	}
	
	private void addTopicUsingEvent(
			SSModelUEResource  user,
			SSUE          event){
		
		List<SSUE> events;
		
		if(user.personsUsingTopicEvents.containsKey(event.content) == false){
			
			events = new ArrayList<SSUE>();
			
			events.add(event);
			
			user.personsUsingTopicEvents.put(
					event.content,
					events);
		}else{
			
			events = user.personsUsingTopicEvents.get(event.content); 
			
			events.add(event);
		}
		
	}
	
	private void addTopicCreationEvent(
			SSModelUEResource  user,
			SSUE          event){
		
		List<SSUE> events;
		
		if(user.personsCreatedTopicEvents.containsKey(event.content) == false){
			
			events = new ArrayList<SSUE>();
			
			events.add(event);
			
			user.personsCreatedTopicEvents.put(
					event.content,
					events);
		}else{
			
			events = user.personsCreatedTopicEvents.get(event.content); 
			
			events.add(event);
		}
	}
	
//	/**
//	 * 0 sets the collections the resource owns <br>
//	 */
//	private void setPersonsOwnedCollections(SSModelUEResource resource) throws Exception{
//		
//		resource.personsCollections             = new ArrayList<SSColl>();
//		resource.personsSharedCollections       = new ArrayList<SSColl>();
//		resource.personsFollowedCollections     = new ArrayList<SSColl>();
//		
//		resource.personsCollections = SSServCaller.collsUserWithEntries(resource.resourceUrl);
//
//		for(SSColl coll : resource.personsCollections){
//
//			if(SSSpaceEnum.isShared(coll.space)){
//				resource.personsSharedCollections.add(coll);
//			}
//
//			if(SSSpaceEnum.isFollow(coll.space)){
//				resource.personsFollowedCollections.add(coll);
//			}
//		}
//	}	
  private void setPersonsTopics(SSModelUEResource resource) throws Exception{

		double                   maxTopicFrequency   = -1;
		double                   thirdThreshold;
		String                   topic;
		SSModelUETopicScore      topicScore;

		resource.personsTopicFrequencies.clear();
		resource.personsTopicScores.clear();

		for (SSTag tagAssignment : SSServCaller.tagsUserGet(resource.resourceUrl, null, null, null)){

			topic = SSStrU.toString(tagAssignment.label);

			if(resource.personsTopicFrequencies.containsKey(topic) == false){

				resource.personsTopicFrequencies.put(topic,1);
			}else{
				resource.personsTopicFrequencies.put(topic,resource.personsTopicFrequencies.get(topic) + 1);
			}
			
			if (resource.personsTopicFrequencies.get(topic) > maxTopicFrequency){

				maxTopicFrequency = resource.personsTopicFrequencies.get(topic);
			}
		}

		thirdThreshold = maxTopicFrequency * 0.3;

		for (Map.Entry<String, Integer> topicAndFrequency : resource.personsTopicFrequencies.entrySet()){

			topicScore = new SSModelUETopicScore(
        SSTagLabel.get(topicAndFrequency.getKey()), 
        -1, 
        topicAndFrequency.getValue());

			if (topicAndFrequency.getValue() > thirdThreshold * 2){

				topicScore.level = 1;

			}else if(
					topicAndFrequency.getValue() > thirdThreshold){

				topicScore.level = 2;

			}else{
				topicScore.level = 3;
			}

			resource.personsTopicScores.add(topicScore);
    }
  }
  
  private void setPersonsRelatedPersons(
    SSModelUEResource resource){
    
    resource.personsRelatedPersons.clear();
    
    for(SSUri relatedResourceUrl : resource.personsRelatedResources){
      
      for(SSUri personUrl : resources.get(SSStrU.toString(relatedResourceUrl)).relatedPersons){
        
        if (
          SSUri.isNotSame   (personUrl, resource.resourceUrl) &&
          SSUri.containsNot (resource.personsRelatedPersons, personUrl)){
          
          resource.personsRelatedPersons.add(personUrl);
        }
      }
    }
  }
  
  private void setRecentProperties(
			SSModelUEResource   resource,
			SSUE                event,
			List<Long>          recentTimeStamps) throws Exception{
	
		if (event.timestamp > recentTimeStamps.get(0)){

			resource.personsRecentArtifact   = event.resource;
			recentTimeStamps.set(0,event.timestamp);
		}

		if (
				SSUEEnum.contains(SSModelUEU.useTopicEventTypes, event.type) &&
				event.timestamp > recentTimeStamps.get(1)){	

			resource.personsRecentTopic = SSTagLabel.get(event.content);
			recentTimeStamps.set(1,event.timestamp);
		}	
	}
	
	private void increaseCountersDiscussionOf(
			SSModelUEResource  resource,
			SSUE    event) {
		
		if(
      SSUEEnum.isSame(event.type, SSUEEnum.addDiscussionComment) &&
      SSUri.contains(resource.personsDiscussions, event.resource)){
					
			resource.personsDiscussions.add(event.resource);
		}
	}
	
	private void increaseCountersRelateResource(
			SSModelUEResource  resource, 
			SSUE          event){

		if(
				SSUEEnum.contains (SSModelUEU.relateResourceEventTypes, event.type) &&
				SSUri.containsNot (resource.personsRelatedResources,    event.resource)){
			
			resource.counters.put(
					   SSModelUEResourceCounterEnum.counterPersonsRelatedResources.toString(),
					resource.counters.get(SSModelUEResourceCounterEnum.counterPersonsRelatedResources.toString()) + 1);
			
			resource.personsRelatedResources.add(event.resource);
		}
	}
}

//public static double EXPERT_DEFAULT_THRESHOLD = 6;
//public static double WORKER_DEFAULT_THRESHOLD = 3;
//public static double LEARNER_DEFAULT_THRESHOLD = 0;

//public void calculateSimpleExpertiseLevels() {
//
//	HashMap<String, Integer> topicfreq_direct = new HashMap<String,Integer>();
//	//HashMap<String, Integer> topicfreq_indirect = new HashMap<String,Integer>();
//	
//	for(RMResource resource : RM.resources.values()){
//		
//		//HashMap<String, List<UMEventObject>> indirect_eventmap = u.getIndirectUserEvents();
//		
//		for(String topic : resource.personsCreatedTopicEvents.keySet()){
//			
//			if (topicfreq_direct.containsKey(topic)){
//				Integer val = topicfreq_direct.get(topic);
//				topicfreq_direct.put(topic, val + u.directUserEvents.get(topic).size());
//			} else {
//				topicfreq_direct.put(topic, u.directUserEvents.get(topic).size());
//			}
//		}
//		
//		/*Set<String> topics_in = direct_eventmap.keySet();
//		Iterator<String> it_in = topics_in.iterator();
//		while (it_in.hasNext()){
//			String topic = it_in.next();
//			//List<UMEventObject> eventlist = direct_eventmap.get(topic);
//			if (topicfreq_direct.containsKey(topic)){
//				Integer val = topicfreq_direct.get(topic);
//				topicfreq_indirect.put(topic, val+direct_eventmap.get(topic).size());
//			} else {
//				topicfreq_indirect.put(topic, direct_eventmap.get(topic).size());
//			}
//		}*/
//	
//	}
//	
//	
//	Iterator<String> it_t = topicfreq_direct.keySet().iterator();
//	System.out.println("TopicFreq:"+topicfreq_direct.keySet().toString());
//	while (it_t.hasNext()){
//		String topic = it_t.next();
//		Integer overall_direct_freq = topicfreq_direct.get(topic);
//		
//		//Integer overall_indirect_freq = topicfreq_indirect.get(topic);
//		
//		it = usernames.iterator();
//		
//		Double mean_val = (double)overall_direct_freq / (double)usernames.size();
//		//Double mean_val_ind = (double)overall_indirect_freq / (double)usernames.size();
//		
//		while(it.hasNext()){
//			String username = it.next();
//			UMUser u = usermodel.get(username);
//			
//			//HashMap<String, List<UMEventObject>> indirect_eventmap = u.getIndirectUserEvents();
//
//			Double val1 = (double)u.directUserEvents.get(topic).size() / (double)overall_direct_freq;
//			//Double val2 = (double)indirect_eventmap.get(topic).size() / (double)overall_indirect_freq;
//			Double val = val1; //+ GeneralConstants.INDIRECT_EXPERTISE_EVENTS_RATIO * val2;
//			
//			u.rawTopicFrequencies.put(topic, val1);
//
//			if (val >= mean_val){
//				u.topicFrequencies.put(topic, 2);
//			} else {
//				u.topicFrequencies.put(topic, 1);
//			}
//			
//			/*if (val2 >= mean_val){
//				u.setKTScore(topic,u.getKTScore(topic)+1);
//			}*/
//		}
//	}
//}
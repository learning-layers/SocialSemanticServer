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
import at.kc.tugraz.ss.serv.modeling.ue.datatypes.SSModelUEResource;
import at.kc.tugraz.ss.serv.modeling.ue.datatypes.enums.SSModelUEResourceCounterEnum;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.socialserver.utils.SSNumberU;
import at.kc.tugraz.ss.serv.modeling.ue.utils.SSModelUEU;
import at.kc.tugraz.ss.service.userevent.datatypes.*;
import java.util.Map;

public class SSModelUEResourcePropertySetterHelper {

  private final Map<String, SSModelUEResource>        resources;
  
  public SSModelUEResourcePropertySetterHelper(Map<String, SSModelUEResource>  resources){
    this.resources = resources;
  }
  
	/**
	 * o set total event counter to the resource (according to new events) <br>
	 */
	public void setCounterEvent(SSModelUEResource resource) {
		
		resource.counters.put(
				SSModelUEResourceCounterEnum.counterEvent.toString(),
				resource.counters.get(SSModelUEResourceCounterEnum.counterEvent.toString()) + resource.events.size()); 
	}

	/**
	 * o increase change counters <br>
	 * o add changing person <br>
	 */
	public void increaseCountersChanging(
			SSModelUEResource  resource,
			SSUE    event){

		if(SSUEEnum.contains(SSModelUEU.changingEventTypes, event.type)){

			resource.counters.put(
					SSModelUEResourceCounterEnum.counterChange.toString(),
					resource.counters.get(SSModelUEResourceCounterEnum.counterChange.toString()) + 1); 
			
			if(SSUri.containsNot(resource.personsChanged, event.user)){
				
				resource.personsChanged.add(event.user);
				
				resource.counters.put(
						SSModelUEResourceCounterEnum.counterChangePerson.toString(),
						resource.counters.get(SSModelUEResourceCounterEnum.counterChangePerson.toString()) + 1);
			}
		}		
	}

	/**
	 * o increase tagging counters <br>
	 */
	public void increaseCountersTagging(
			SSModelUEResource  resource, 
			SSUE    event) {
		
		if(SSUEEnum.contains(SSModelUEU.taggingEventTypes, event.type)){

			resource.counters.put(
					SSModelUEResourceCounterEnum.counterTag.toString(),
					resource.counters.get(SSModelUEResourceCounterEnum.counterTag.toString()) + 1);
		}
	}
	
//	/**
//	 * o decrease collection counters <br>
//	 * o remove collection the resource was in <br>
//	 */
//	public void decreaseCountersCollection(
//			SSModelUEResource  resource,
//			SSUE event) throws Exception {
//		
//		if(SSUEEnum.contains(SSModelUEU.removeFromCollectionEventTypes, event.type)){
//
//			if(
//					SSUE.isContentCorrect (event) &&
//					SSUri.contains            (resource.parentCollections, SSUri.get(event.content))){
//				
//				resource.counters.put(
//						SSModelUEResourceCounterEnum.counterCollection.toString(),
//						resource.counters.get(SSModelUEResourceCounterEnum.counterCollection.toString()) - 1);
//				
//				SSUri.remove(resource.parentCollections, SSUri.get(event.content));
//			}
//		}		
//	}
//
//	/**
//	 * o increase discussion about counters <br>
//	 * o add discussion about the resource <br>
//	 */
//	public void increaseCountersDiscussionAbout(
//			SSModelUEResource  resource,
//			SSUE event) throws Exception {
//		
//		if(
//				SSUEEnum.contains            (SSModelUEU.startDicussionEventTypes, event.type) &&
//				SSUE.isContentCorrect  (event) &&
//				SSUri.containsNot          (resource.parentDiscussions, SSUri.get(event.content))){
//			
//			resource.counters.put(
//					SSModelUEResourceCounterEnum.counterDiscussionAbout.toString(),
//					resource.counters.get(SSModelUEResourceCounterEnum.counterDiscussionAbout.toString()) + 1);
//
//			resource.parentDiscussions.add(SSUri.get(event.content));
//		}		
//	}
	
	/**
	 * o increase viewing counters <br>
	 * o add viewing persons <br>
	 */
	public void increaseCountersView(
			SSModelUEResource  resource, 
			SSUE event) {
		
		if(SSUEEnum.contains(SSModelUEU.viewEntityEventTypes, event.type)){

			resource.counters.put(
					SSModelUEResourceCounterEnum.counterView.toString(),
					resource.counters.get(SSModelUEResourceCounterEnum.counterView.toString()) + 1);
			
			if(SSUri.containsNot(resource.personsView, event.user)){

				resource.counters.put(
						SSModelUEResourceCounterEnum.counterViewPerson.toString(),
						resource.counters.get(SSModelUEResourceCounterEnum.counterViewPerson.toString()) + 1);
				
				resource.personsView.add(event.user);
			}
		}		
	}

//	/**
//	 * o increase search result counters <br>
//	 */
//	public void increaseCountersSearchResult(
//			SSModelUEResource  resource,
//			SSUE event) {
//		
//		if(SSUEEnum.contains(SSModelUEU.appearInSearchResultEventTypes, event.type)){
//
//			resource.counters.put(
//					SSModelUEResourceCounterEnum.counterSearchResult.toString(),
//					resource.counters.get(SSModelUEResourceCounterEnum.counterSearchResult.toString()) + 1);
//		}		
//	}

	/**
	 * 0 increase organizing collection counters <br>
	 */
	public void increaseCountersOrganizeCollection(
			SSModelUEResource  resource,
			SSUE event) {
		
		if(SSUEEnum.contains(SSModelUEU.organizingInCollectionsEventTypes, event.type)){

			resource.counters.put(
					SSModelUEResourceCounterEnum.counterOrganizeCollection.toString(),
					resource.counters.get(SSModelUEResourceCounterEnum.counterOrganizeCollection.toString()) + 1);
		}		
	}

	/**
	 * 0 increase initial collection collaborating counters <br>
	 */
	public void increaseCountersCollaborationCollectionInitial(
			SSModelUEResource  resource,
			SSUE event){
		
		if(SSUEEnum.contains(SSModelUEU.initialCollectionCollaborationEventTypes, event.type)){
			
			resource.counters.put(
					SSModelUEResourceCounterEnum.counterCollaborateCollectionInitial.toString(),
					resource.counters.get(SSModelUEResourceCounterEnum.counterCollaborateCollectionInitial.toString()) + 1);
		}		
	}
	
	/**
	 * 0 increase initial discussion collaborating counters <br>
	 */
	public void increaseCountersCollaborationDiscussionInitial(
			SSModelUEResource  resource,
			SSUE event){
		
		if(SSUEEnum.contains(SSModelUEU.initialDiscussionCollaborationEventTypes, event.type)){
			
			resource.counters.put(
					SSModelUEResourceCounterEnum.counterCollaborateDiscussionInitial.toString(),
					resource.counters.get(SSModelUEResourceCounterEnum.counterCollaborateDiscussionInitial.toString()) + 1);
		}		
	}
	
	/**
	 * 0 increase actual collection collaborating counters <br>
	 */
	public void increaseCountersCollaborationCollection(
			SSModelUEResource  resource,
			SSUE event) {

		if(SSUEEnum.contains(SSModelUEU.collaborateCollectionEventTypes, event.type)){
			
			resource.counters.put(
					SSModelUEResourceCounterEnum.counterCollaborateCollection.toString(),
					resource.counters.get(SSModelUEResourceCounterEnum.counterCollaborateCollection.toString()) + 1);
		}		
	}
	
	/**
	 * 0 increase actual discussion collaborating counters <br>
	 */
	public void increaseCountersCollaborationDiscussion(
			SSModelUEResource  resource,
			SSUE event) {

		if(SSUEEnum.contains(SSModelUEU.collaborateDiscussionEventTypes, event.type)){
			
			resource.counters.put(
					SSModelUEResourceCounterEnum.counterCollaborateDiscussion.toString(),
					resource.counters.get(SSModelUEResourceCounterEnum.counterCollaborateDiscussion.toString()) + 1);
		}		
	}

	/**
	 * 0 decrease collection collaborating counters <br>
	 */
	public void decreaseCountersInitialCollectionsCollaboration(
			SSModelUEResource  resource,
			SSUE event) {
		
		if(SSUEEnum.contains(SSModelUEU.removeInitialCollectionCollaborationEventTypes, event.type)){
			
			resource.counters.put(
					SSModelUEResourceCounterEnum.counterCollaborateCollectionInitial.toString(),
					resource.counters.get(SSModelUEResourceCounterEnum.counterCollaborateCollectionInitial.toString()) - 1);
		}		
	}

	/**
	 * 0 decrease awareness counters <br>
	 */
	public void increaseCountersAwareness(
			SSModelUEResource  resource, 
			SSUE event) {
		
		if(SSUEEnum.contains(SSModelUEU.awarenessEventTypes, event.type)){
			
			resource.counters.put(
					SSModelUEResourceCounterEnum.counterAware.toString(),
					resource.counters.get(SSModelUEResourceCounterEnum.counterAware.toString()) + 1);
		}		
	}

	/**
	 * 0 decrease recommending counters <br>
	 */
	public void increaseCountersRecommendation(
			SSModelUEResource  resource,
			SSUE event) {
		
		if(SSUEEnum.contains(SSModelUEU.recommendEventTypes, event.type)){
			
			resource.counters.put(
					SSModelUEResourceCounterEnum.counterRecommend.toString(),
					resource.counters.get(SSModelUEResourceCounterEnum.counterRecommend.toString()) + 1);
		}		
	}

	/**
	 * 0 increase editing counters <br>
	 * 0 add editing persons <br>
	 */
	public void increaseCountersEditor(
			SSModelUEResource  resource, 
			SSUE    event) {

		if(
      SSUEEnum.contains(SSModelUEU.sharingWithCommunityEventTypes,    event.type) ||
			SSUEEnum.contains(SSModelUEU.addingAndDeletingEventTypes,       event.type) || 
			SSUEEnum.contains(SSModelUEU.changingEventTypes,                event.type) ||
			SSUEEnum.contains(SSModelUEU.organizingInCollectionsEventTypes, event.type) || 
			SSUEEnum.contains(SSModelUEU.taggingEventTypes,                 event.type)){
			
			if(SSUri.containsNot (resource.editors, event.user)){

				resource.editors.add(event.user);		
			}
		}		
	}

	/**
	 * 0 increase associating counters <br>
	 * 0 add associated persons <br>
	 */
	public void increaseCountersAssociated(
			SSModelUEResource  resource,
			SSUE event) {
		
		if(
				SSUri.containsNot        (resource.relatedPersons,             event.user) &&
				SSUEEnum.contains (SSModelUEU.personAssociationEventTypes, event.type)){
			
			resource.counters.put(
					SSModelUEResourceCounterEnum.counterAssociatePerson.toString(),
					resource.counters.get(SSModelUEResourceCounterEnum.counterAssociatePerson.toString()) + 1);
			
			resource.relatedPersons.add(event.user);				
		}		
	}
	
	/**
	 * 0 increase adding and deleting counters <br>
	 */
	public void increaseCountersAddAndDelete(
			SSModelUEResource  resource,
			SSUE event) {
		
		if(SSUEEnum.contains(SSModelUEU.addingAndDeletingEventTypes, event.type)){
			
			resource.counters.put(
					SSModelUEResourceCounterEnum.counterAddAndDelete.toString(),
					resource.counters.get(SSModelUEResourceCounterEnum.counterAddAndDelete.toString()) + 1);
		}
	}		

	/**
	 * 0 increase rating high counters <br>
	 */
	public void increaseCountersRateHigh(
			SSModelUEResource  resource, 
			SSUE event) {

		if(
				SSUEEnum.contains             (SSModelUEU.rateEventTypes, event.type) &&
				SSUE.isContentCorrect         (event)                                    &&
				Integer.parseInt              (event.content) > SSModelUEU.THRESHOLD_RATE_HIGH){

			resource.counters.put(
					SSModelUEResourceCounterEnum.counterRateHigh.toString(),
					resource.counters.get(SSModelUEResourceCounterEnum.counterRateHigh.toString()) + 1);
		}
	}

	public void increaseCountersShareCommunity(
			SSModelUEResource  resource,
			SSUE event) {
		
		if(SSUEEnum.contains(SSModelUEU.sharingWithCommunityEventTypes, event.type)){

			resource.counters.put(
					SSModelUEResourceCounterEnum.counterShareCommunity.toString(),
					resource.counters.get(SSModelUEResourceCounterEnum.counterShareCommunity.toString()) + 1);
		}
	}

	/**
	 * 0 increase selecting from others counters <br>
	 */
	public void increaseCountersSelectFromOther(
			SSModelUEResource  resource,
			SSUE event){
		
		if(SSUEEnum.contains(SSModelUEU.selectedFromOthersEventTypes, event.type)){
			
			resource.counters.put(
					SSModelUEResourceCounterEnum.counterSelectFromOther.toString(),
					resource.counters.get(SSModelUEResourceCounterEnum.counterSelectFromOther.toString()) + 1);
		}		
	}

	/**
	 * 0 increase similar collection counters <br>
	 * 1. get collection entries of the collections the resource is in <br> 
	 * 2. check whether those entries have same shared tags as the resource <br>
	 * 3. increase the similar collection counter respectively <br>
	 */
	public void increaseCountersCollectionSimilar(
			SSModelUEResource resource) throws Exception {

//		ArrayList<String> resourceTags = null;
		
		resource.counters.put(SSModelUEResourceCounterEnum.counterCollectSimilar.toString(), 0);
		
		//TODO
//		resourceTags = 
//			RMGlobalConstants.METHOD.getDistinctSharedTagLabelsFromResource(
//					resource.url);
//		
//		for(String collectionUrl : resource.collections){		
//
//			for(String collectionEntryUrl :	RMGlobalConstants.METHOD.getCollectionEntryUrls(resources,collectionUrl)){
//
//				if(RMGlobalConstants.METHOD.isEqual(collectionEntryUrl, resource.url) == false){
//					
//					if(RMGlobalConstants.METHOD.hasResourceSameTags(collectionEntryUrl, resourceTags) == true){
//						
//						resource.counterCollectSimilar++;
//						
//						break;
//					}
//				}
//			}
//		}
	}
//	
//	/**
//	 * 0 returns whether given resourceUrl has at least one shared tag out of given tags <br>
//	 */
//	public boolean hasResourceSameTags(
//			String            resourceUrl,
//			ArrayList<String> tags) {
//		
//		for(
//				String tag : 
//					RMGlobalConstants.TYPE_CONVERTER.getDistinctTagLabelsFromTagAssignments(
//							RMGlobalConstants.SOCKET.getTagAssignments(
//									GC.stringEmpty, 
//									GC.stringEmpty, 
//									resourceUrl, 
//									RMGlobalConstants.TYPE_SHARED))){
//			
//			if(tags.contains(tag) == true){
//				
//				return true;
//			}
//		}
//		
//		return false;
//	}

	/**
	 * 0 increase selecting from others counters <br>
	 */
	public void increaseCountersPresentAudience(
			SSModelUEResource  resource,
			SSUE event) {

		if(SSUEEnum.contains(SSModelUEU.exportCollectionItemEventTypes, event.type)){
			
			resource.counters.put(
					SSModelUEResourceCounterEnum.counterPresentAudience.toString(),
					resource.counters.get(SSModelUEResourceCounterEnum.counterPresentAudience.toString()) + 1);
		}		
	}

	/**
	 * 0 increase assessing counters <br>
	 */
	public void increaseCountersAssess(
			SSModelUEResource  resource, 
			SSUE event) {
		
		if(SSUEEnum.contains(SSModelUEU.assessEventTypes, event.type)){
			
			resource.counters.put(
					SSModelUEResourceCounterEnum.counterAssess.toString(),
					resource.counters.get(SSModelUEResourceCounterEnum.counterAssess.toString()) + 1);
		}		
	}

	/**
	 * 0 set being editor and author counters <br>
	 */
	public void setCountersIsEditor(
			SSModelUEResource                    resource) {
		
		resource.counters.put(
				SSModelUEResourceCounterEnum.counterIsEditor.toString(),
				0);
		
		for(SSModelUEResource otherResource : resources.values()){
			
			if(SSUri.contains(otherResource.editors, resource.resourceUrl)){
			
				resource.counters.put(
						SSModelUEResourceCounterEnum.counterIsEditor.toString(),
						resource.counters.get(SSModelUEResourceCounterEnum.counterIsEditor.toString()) + 1);
			}
		}
	}

	/**
	 * 0 increase contributing to discussion counters <br>
	 */
	public void increaseCountersContributedDiscussion(
			SSModelUEResource                    resource) {

		for(SSModelUEResource otherResource : resources.values()){
			
			for(SSUE otherResourceEvent : otherResource.events){
			
				if(
						SSUEEnum.contains(SSModelUEU.contributeToDiscussionEventTypes, otherResourceEvent.type) &&
						SSUri.isSame(otherResourceEvent.user, resource.resourceUrl)){

					resource.counters.put(
							SSModelUEResourceCounterEnum.counterContributedDiscussion.toString(),
							resource.counters.get(SSModelUEResourceCounterEnum.counterContributedDiscussion.toString()) + 1);
					
					break;
				}
			}
		}		
	}

	/**
	 * 0 increase got rated counters <br>
	 */
	public void increaseCountersGotRated(
			SSModelUEResource  resource, 
			SSUE event){
		
		if(SSUEEnum.contains(SSModelUEU.rateEventTypes, event.type)){
			
			resource.counters.put(
					SSModelUEResourceCounterEnum.counterGotRated.toString(),
					resource.counters.get(SSModelUEResourceCounterEnum.counterGotRated.toString()) + 1);
		}	
	}
	
	

	/**
	 * 0 increase participation counters <br>
	 */
	public void increaseCounterParticipated(
			SSModelUEResource                    resource) {

		for(SSModelUEResource otherResource : resources.values()){
			
			for(SSUE otherResourceEvent : otherResource.events){
			
				if(
						SSUEEnum.contains(SSModelUEU.participationEventTypes, otherResourceEvent.type) &&
						SSUri.isSame(otherResourceEvent.user, resource.resourceUrl)){

					resource.counters.put(
							SSModelUEResourceCounterEnum.counterParticipated.toString(),
							resource.counters.get(SSModelUEResourceCounterEnum.counterParticipated.toString()) + 1);
				}
			}
		}		
	}

	/**
	 * 0 set referred by other resources counters <br>
	 */
	public void setCountersReferredBy(
			SSModelUEResource                    resource) {

		resource.counters.put(
				SSModelUEResourceCounterEnum.counterReferredBy.toString(),
				0);
		
		resource.counters.put(
				SSModelUEResourceCounterEnum.counterReferredBy.toString(),
				0 + 
				resource.counters.get(SSModelUEResourceCounterEnum.counterCollection.toString()));

		resource.counters.put(
				SSModelUEResourceCounterEnum.counterReferredBy.toString(),
				resource.counters.get(SSModelUEResourceCounterEnum.counterReferredBy.toString()) + 
				resource.counters.get(SSModelUEResourceCounterEnum.counterDiscussionAbout.toString()));

	}

	/**
	 * 0 increase active periods counter <br>
	 */
	public void increaseActivePeriod(SSModelUEResource resource) {
		
		if(SSNumberU.isGreaterThan(resource.events.size(), 0)){
			
			resource.counters.put(
					SSModelUEResourceCounterEnum.counterActivePeriod.toString(),
					resource.counters.get(SSModelUEResourceCounterEnum.counterActivePeriod.toString()) + 1);
		}
	}
}
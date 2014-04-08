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

import at.kc.tugraz.socialserver.utils.SSNumberU;
import at.kc.tugraz.ss.serv.modeling.ue.datatypes.SSModelUEResource;
import at.kc.tugraz.ss.serv.modeling.ue.datatypes.enums.SSModelUEMIEnum;
import at.kc.tugraz.ss.serv.modeling.ue.datatypes.enums.SSModelUEResourceCounterEnum;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityEnum;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.serv.modeling.ue.utils.SSModelUEU;
import at.kc.tugraz.ss.service.userevent.datatypes.*;
import java.util.ArrayList;
import java.util.Map;

public class SSModelUEMISetter {

	private SSModelUEMISetterHelper  maturingIndicatorSetterHelper           = new SSModelUEMISetterHelper();
	
	/**
	 * 0 set maturing indicators text representation for the resource <br>
	 */
	public void setTextualMI(SSModelUEResource resource) {

		ArrayList<String>    result    = new ArrayList<String>();
			
		if(SSEntityEnum.isColl(resource.type)){
			
			if(resource.mIMadeOutOfOthersNumberOf){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.has_many_entries.toString());
			}
			if(resource.mIMadeOutOfOthersNot){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.no_entries.toString());
			}
			if(resource.mIMadeOutOfOthers){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.has_at_least_one_entry.toString());
			}
		}

		if(
				SSEntityEnum.isColl(resource.type)||
				SSEntityEnum.isDisc(resource.type)||
				SSEntityEnum.isResourceOrFile(resource.type)){

			if(resource.mIAssociatePersonNumberOf ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.associated_with_many_persons.toString());
			}

			if(resource.mICreateJust ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.just_created.toString()); 
			}
			if(resource.miChangeNumberOf ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.changed_a_lot.toString());
			}
			if(resource.mIChange ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.changed_at_least_one_time.toString());
			}
			if(resource.mIChangeNot ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.not_changed_yet.toString());
			}
			if(resource.mICollaborateCollectionNot ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.no_collection_collaboration.toString());
			}
			if(resource.mICollaborateCollection ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.in_collection_collaboration.toString());
			}
			if(resource.mICollaborateCollectionNumberOf ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.a_lot_collection_collaboration.toString());
			}
			if(resource.mICollaborateDiscussionNot ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.no_discussion_collaboration.toString());
			}
			if(resource.mICollaborateDiscussion ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.in_a_discussion_collaboration.toString());
			}
			if(resource.mICollaborateDiscussionNumberOf ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.a_lot_discussion_collaboration.toString());
			}
			if(resource.mIChangePersonNumberOf ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.changed_by_lot_of_persons.toString());
			}
			if(resource.mIChangePersonNot ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.not_changed_by_any_person.toString());
			}
			if(resource.mIChangePerson ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.changed_by_at_least_one_person.toString());
			}
		}			

		if(
				SSEntityEnum.isColl(resource.type)       ||
				SSEntityEnum.isDisc(resource.type)       ||
				SSEntityEnum.isUser(resource.type)           ||
				SSEntityEnum.isResourceOrFile(resource.type)){

			if(resource.mITagNot ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.no_tag.toString()); 
			}
			if(resource.mIStandard ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.became_standard.toString()); 
			}
			if(resource.mITagNumberOf ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.tagged_a_lot.toString());
			}
			if(resource.mIUseWide ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.used_widely.toString());
			}
			if(resource.mIUseWideNot ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.not_used_widely.toString());
			}
			if(resource.mIGotRatedNot ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.no_rating.toString());
			}
			if(resource.mIGotRatedNumberOf ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.often_rated.toString());
			}
			if(resource.mICollect ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.in_a_collection.toString());
			}
			if(resource.mICollectNumberOf ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.in_many_collections.toString());
			}
//			if(resource.mISearchResultsNumberOf ){
//				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.appears_in_many_search_results.toString());
//			}
			if(resource.mIViewNumberOf ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.viewed_a_lot.toString());
			}
			if(resource.mIRateHighNumberOf ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.often_rated_high.toString());
			}
			if(resource.mIAware ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.reached_high_awareness.toString());
			}
			if(resource.mIViewPersonNumberOf ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.viewed_by_different_persons.toString()); 
			}
			if(resource.mIShareCommunityNumberOf ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.shared_a_lot_within_community.toString()); 
			}
			if(resource.mIReferredByNumberOf ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.referred_a_lot.toString());   
			}
			
			if(resource.mIStandardNot ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.standard_not_reached.toString()); 
			}
			if(resource.mIAwareNot ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.got_no_awareness.toString());
			}
			if(resource.mIViewPerson ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.viewed_by_at_least_one_person.toString());
			}
			if(resource.mITag ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.tagged.toString()); 
			}
			if(resource.mIGotRated ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.rated.toString());  
			}
//			if(resource.mISearchResults ){
//				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.appears_in_search_result.toString()); 
//			}
			if(resource.mIReferredBy ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.referred.toString());
			}
			if(resource.mIShareCommunity ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.shared_within_community.toString());
			}
			if(resource.mIPresentAudience ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.was_prepared_for_audience.toString());
			}			
			if(resource.mIRecommend ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.got_recommended.toString());
			}
			if(resource.mIRecommendNumberOf ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.often_recommended.toString());
			}
			if(resource.mIAsses ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.assessed.toString());
			}
			if(resource.mIAssesNumberOf ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.assessed_many_times.toString());
			}	
			if(resource.mIReferredByNot ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.not_referred_yet.toString());
			}
			if(resource.mIPresentAudienceNot ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.wasnt_prepared_for_audience_yet.toString()); 
			}
			if(resource.mIPresentAudienceNumberOf ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.was_prepared_for_audience_many_times.toString());
			}
			if(resource.mIDiscuss ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.has_discussion.toString());
			}
			if(resource.mIDiscussNot ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.no_discussion.toString());
			}
			if(resource.mIChangeByAddOrDelete ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.changed_by_adding_or_deleting_steps.toString());
			}
			if(resource.mIChangeByAddOrDeleteNot ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.not_changed_by_adding_or_deleting_steps.toString());
			}
			if(resource.mIChangeByAddOrDeleteNumberOf){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.changed_by_many_adding_or_deleting_steps.toString());
			}
			if(resource.mIViewNot ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.not_viewed_yet.toString());
			}
			if(resource.mIShareCommunityNot ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.not_shared_with_community.toString());
			}
			if(resource.mIOrganizeCollectionNot ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.not_organized_in_a_collection.toString());
			}
			if(resource.mIOrganizeCollection ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.organized_in_a_collection.toString());
			}
			if(resource.mIOrganizeCollectionNumberOf ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.often_organized_in_collections.toString());
			}
			if(resource.mISelectFromOther ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.selected_from_others.toString());
			}
			if(resource.mISelectFromOtherNot ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.not_selected_from_others.toString());
			}
			if(resource.mISelectFromOtherNumberOf ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.often_selected_from_others.toString());
			}
			if(resource.mICollectNot ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.in_no_collection.toString());
			}
			if(resource.mIRateHighNot ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.not_rated_high_yet.toString());
			}
			if(resource.mIRateHigh ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.rated_high.toString());
			}
			if(resource.mIRecommendNot ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.not_recommended_yet.toString());
			}
//			if(resource.mISearchResultsNot ){
//				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.appears_in_no_search_results.toString());
//			}
			if(resource.mIViewPersonNot ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.not_viewed_by_any_person.toString());
			}
//			if(resource.mICollectSimilarNot ){
//				maturingIndicatorSetterHelper.addIndicator(resource,result,ModelMIType.in_no_collection_with_similar_content.toString());
//			}
//			if(resource.mICollectSimilar ){
//				maturingIndicatorSetterHelper.addIndicator(resource,result,ModelMIType.in_a_collection_with_similar_content.toString());
//			}
//			if(resource.mICollectSimilarNumberOf ){
//				maturingIndicatorSetterHelper.addIndicator(resource,result,ModelMIType.in_many_collections_with_similar_content.toString());
//			}
			if(resource.mIAssesNot ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.not_assessed_so_far.toString()); 
			}
			if(resource.mIActivePeriodNot ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.not_very_active.toString());
			}
			if(resource.mIActivePeriodNumberOf ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.very_active.toString());
			}
			if(resource.mIView ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.viewed.toString());
			}
		}

		if(SSEntityEnum.isUser(resource.type)){

			if(resource.mIIsEditorNot ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.didnt_edit_a_resource_yet.toString());
			}
			if(resource.mIIsEditor ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.edited_a_resource.toString());
			}
			if(resource.mIIsEditorNumberOf ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.edited_many_resources.toString());
			}
			if(resource.mIContributeDiscussionNot ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.no_contribution_to_a_discussion_yet.toString());
			}
			if(resource.mIContributeDiscussion ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.contributed_to_a_discussion.toString());
			}
			if(resource.mIContributeDiscussionNumberOf ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.contributed_to_many_discussions.toString());
			}
			if(resource.mIParticipatedNot ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.no_true_participation_in_social_network.toString());
			}
			if(resource.mIParticipated ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.particpated_in_social_network.toString());
			}
			if(resource.mIParticipatedNumberOf ){
				maturingIndicatorSetterHelper.addIndicator(resource,result,SSModelUEMIEnum.particpates_frequently_in_social_network.toString());
			}
		}
		
		resource.mIs.addAll(result);
	}
	
	/**
	 * 0 calculate independent MI <br>
	 */
	public void calculateIndependentMI(SSModelUEResource resource){
		
		/*
		 * MI of this resource not depending on other MI of this resource
		 */
		setCreateJust                (resource);               //certain number of events 
		setUseWide                   (resource);               //certain number of events
		setActivePeriod              (resource);               //certain number of events
		setChangePerson              (resource);               //certain number of events / person
		setViewPerson                (resource);               //certain number of events / person
		setAssociatePerson           (resource);               //certain number of persons 
		setCollaborateCollection     (resource);               //collaboration events
		setCollaborateDiscussion     (resource);               //collaboration events
		setChange                    (resource);               //changing events
		setChangeByAddOrDelete       (resource);               //changing events
		setGotRated                  (resource);               //rating events
		setRateHigh                  (resource);               //rating events
		setAssess                    (resource);               //rating/tagging events
		setTag                       (resource);               //tagging events
		setView                      (resource);               //viewing events
		setDiscuss                   (resource);               //discussion events
		setContributedDiscussion     (resource);               //discussion events
		setShareCommunity            (resource);               //sharing events
		setSelectFromOther           (resource);               //search events
//		setSearchResults             (resource);               //search events
		setAware                     (resource);               //awareness events
		setRecommend                 (resource);               //recommendation events	    	
		setIsEditor                  (resource);               //associating events
		setParticipated              (resource);               //participation events
		setReferredBy                (resource);               //referring events
		setOrganizeCollection        (resource);               //collection events               
		setCollect                   (resource);               //collection events
		setCollectSimilar            (resource);               //collection events
		setPresentAudience           (resource);               //collection events
		setMadeOutOf                 (resource);               //collection events

		/*
		 * MI depending on other MI of this resource
		 */
		setStandard                  (resource);               //agglomeration of MIs
	}
	
	/**
	 * calculate MI depending on other MI of all resources <br>
	 */	
	public void calculateDependentMI(
			SSModelUEResource                    resource,
			 Map<String,SSModelUEResource>  resources){
		
		resource.mIChangeReputablePersonNot = true;
		resource.mIChangeReputablePerson    = false; 
		
		setChangeReputablePerson(resources, resource);         //agglomeration of MIs and event types
	}

	/**
	 * 0 changed by reputable person <br>
	 * - whether the resource was changed by a reputable person <br>
	 * - whether the resource wasn't changed by a reputable person <br>
	 **/
	private void setChangeReputablePerson(
			Map<String, SSModelUEResource> resources, 
			SSModelUEResource                    resource) {

		if(
				SSEntityEnum.isUser(resource.type) &&
				
				(resource.mIUseWide                                              ||
				 resource.mIAware                                               )){
			
			for(SSModelUEResource otherResource : resources.values()){
				
				if(otherResource.mIChangeReputablePerson == false){
				
					for(SSUE event :	maturingIndicatorSetterHelper.getResourceEventsOfType(resource, SSModelUEU.changingEventTypes)){
						
						if(SSUri.isSame(event.user, resource.resourceUrl)){
							
							otherResource.mIChangeReputablePersonNot = false;
							otherResource.mIChangeReputablePerson    = true;
							
							break;
						}					
					}
				}
			}
		}
	}

	/**
	 * 0 became standard <br>
	 * - whether the resource became standard <br>
	 **/
	private void setStandard(SSModelUEResource resource){
		
		int counter = 0;
		
		if(resource.mIAssociatePersonNumberOf ){
			counter++;
		}
		
		if(resource.mIAware                   ){
			counter++;
		}
		
		if(resource.mIRateHighNumberOf        ){
			counter++;
		}
		
		if(resource.mIRecommendNumberOf       ){
			counter++;
		}
				
//		if(resource.mISearchResultsNumberOf   ){
//			counter++;
//		}
		
		if(resource.mIUseWide                 ){
			counter++;
		}
		
		if(resource.mIViewPersonNumberOf      ){
			counter++;
		}

		
		if(SSNumberU.isGreaterThan(counter,4)){
			
			resource.mIStandardNot = false;
			resource.mIStandard    = true;
			
		}else{
			
			resource.mIStandardNot = true;
			resource.mIStandard    = false;
		}
	}
	
	/**
	 * 0 got recommended <br>
	 * - whether the resource got recommended <br>
	 * - whether the resource got not recommended <br>
	 * - whether the resource got recommended number of times <br>
	 **/
	private void setRecommend(SSModelUEResource resource){
		
		if(
				SSNumberU.isLessThanOrEqual(
						resource.counters.get(SSModelUEResourceCounterEnum.counterRecommend.toString()),
						0)){
			
			resource.mIRecommendNot       = true;
			resource.mIRecommend          = false;
			resource.mIRecommendNumberOf  = false;
			
		}else{
			
			resource.mIRecommendNot       = false;
			
			resource.mIRecommendNumberOf  = 
				maturingIndicatorSetterHelper.getNumberOfTimesMaturingIndicator(
						resource, 
						resource.counters.get(SSModelUEResourceCounterEnum.counterRecommend.toString()), 
						SSModelUEU.THRESHOLD_COLLECTION_RECOMMEND, 
						SSModelUEU.THRESHOLD_DISCUSSION_RECOMMEND, 
						SSModelUEU.THRESHOLD_DIGITAL_RESOURCE_RECOMMEND,
						SSModelUEU.THRESHOLD_PERSON_RECOMMEND);
			
			resource.mIRecommend          = !resource.mIRecommendNumberOf;
		}
	}
	
	/**
	 * 0 is editor <br>
	 * - whether the resource is author/editor of a document <br>
	 * - whether the resource isn't an author/editor of a document <br>
	 * - whether the resource is an author/editor of a number of documents <br>
	 **/
	private void setIsEditor(SSModelUEResource resource){
		
		if(
				SSNumberU.isLessThanOrEqual(
						resource.counters.get(SSModelUEResourceCounterEnum.counterIsEditor.toString()), 
						0)){
			
			resource.mIIsEditorNot        = true;
			resource.mIIsEditor           = false;
			resource.mIIsEditorNumberOf   = false;
			
		}else{
			
			resource.mIIsEditorNot        = false;
			
			resource.mIIsEditorNumberOf   = 
				maturingIndicatorSetterHelper.getNumberOfTimesMaturingIndicator(
						resource, 
						resource.counters.get(SSModelUEResourceCounterEnum.counterIsEditor.toString()), 
						0, 
						0, 
						0,
						SSModelUEU.THRESHOLD_PERSON_IS_EDITOR);
			
			resource.mIIsEditor           = !resource.mIIsEditorNumberOf;
		}
	}
	
	/**
	 * 0 contributed to discussion <br>
	 * - whether the resource contributed to a discussion <br>
	 * - whether the resource didn't contribute to a discussion <br>
	 * - whether the resource contributed to a number of discussions <br>
	 **/
	private void setContributedDiscussion(SSModelUEResource resource){
		
		if(
				SSNumberU.isLessThanOrEqual(
						resource.counters.get(SSModelUEResourceCounterEnum.counterContributedDiscussion.toString()), 
						0)){
			
			resource.mIContributeDiscussionNot        = true;
			resource.mIContributeDiscussion           = false;
			resource.mIContributeDiscussionNumberOf   = false;
			
		}else{
			
			resource.mIContributeDiscussionNot        = false;
			
			resource.mIContributeDiscussionNumberOf   = 
				maturingIndicatorSetterHelper.getNumberOfTimesMaturingIndicator(
						resource, 
						resource.counters.get(SSModelUEResourceCounterEnum.counterContributedDiscussion.toString()), 
						0, 
						0, 
						0,
						SSModelUEU.THRESHOLD_PERSON_CONTRIBUTED_DISCUSSION);
			
			resource.mIContributeDiscussion           = !resource.mIContributeDiscussionNumberOf;
		}
	}
	
	/**
	 * 0 participated through collection, discussion, tags <br>
	 * - whether the resource participated <br>
	 * - whether the resource didn't participate <br>
	 * - whether the resource participated number of times <br>
	 **/
	private void setParticipated(SSModelUEResource resource){
		
		if(
				SSNumberU.isLessThanOrEqual(
						resource.counters.get(SSModelUEResourceCounterEnum.counterParticipated.toString()),
						0)){
			
			resource.mIParticipatedNot      = true;
			resource.mIParticipated         = false;
			resource.mIParticipatedNumberOf = false;
		}else{
			
			resource.mIParticipatedNot        = false;
			
			resource.mIParticipatedNumberOf   = 
				maturingIndicatorSetterHelper.getNumberOfTimesMaturingIndicator(
						resource, 
						resource.counters.get(SSModelUEResourceCounterEnum.counterParticipated.toString()), 
						0, 
						0, 
						0,
						SSModelUEU.THRESHOLD_PERSON_PARTICIPATED);
			
			resource.mIParticipated           = !resource.mIParticipatedNumberOf;
		}
	}

	/**
	 * 0 referred by resources <br>
	 * - whether the resource got referred by another one <br>
	 * - whether the resource got not referred by another one <br>
	 * - whether the resource got referred by a number of resources <br>
	 **/
	private void setReferredBy(SSModelUEResource resource){
		
		if(
				SSNumberU.isLessThanOrEqual(
						resource.counters.get(SSModelUEResourceCounterEnum.counterReferredBy.toString()),
						0)){
			
			resource.mIReferredByNot        = true;
			resource.mIReferredBy           = false;
			resource.mIReferredByNumberOf   = false;
		}else{
			
			resource.mIReferredByNot        = false;
			
			resource.mIReferredByNumberOf   = 
				maturingIndicatorSetterHelper.getNumberOfTimesMaturingIndicator(
						resource, 
						resource.counters.get(SSModelUEResourceCounterEnum.counterReferredBy.toString()), 
						SSModelUEU.THRESHOLD_COLLECTION_REFERRED_BY, 
						SSModelUEU.THRESHOLD_DISCUSSION_REFERRED_BY, 
						SSModelUEU.THRESHOLD_DIGITAL_RESOURCE_REFERRED_BY,
						SSModelUEU.THRESHOLD_PERSON_REFERRED_BY);
			
			resource.mIReferredBy           = !resource.mIReferredByNumberOf;
		}
	}
	
	/**
	 * 0 reached high awareness <br>
	 * - whether the resource reached high awareness <br>
	 * - whether the resource reached no high awareness <br>
	 **/
	private void setAware(SSModelUEResource resource){

		resource.mIAware = 
			maturingIndicatorSetterHelper.getNumberOfTimesMaturingIndicator(
					resource, 
					resource.counters.get(SSModelUEResourceCounterEnum.counterAware.toString()), 
					SSModelUEU.THRESHOLD_COLLECTION_AWARE, 
					SSModelUEU.THRESHOLD_DISCUSSION_AWARE, 
					SSModelUEU.THRESHOLD_DIGITAL_RESOURCE_AWARE,
					SSModelUEU.THRESHOLD_PERSON_AWARE);

		resource.mIAwareNot = !resource.mIAware;
	}
	
	/**
	 * 0 collaborately work on collections <br>
	 * - whether the resource got collaborately worked on <br>
	 * - whether the resource got not collaborately worked on <br>
	 * - whether the resource got collaborately worked on number of times <br>
	 **/
	private void setCollaborateCollection(SSModelUEResource resource){
		
		if(
				SSNumberU.isLessThanOrEqual(
						resource.counters.get(SSModelUEResourceCounterEnum.counterCollaborateCollectionInitial.toString()), 
						0)                                   ||
				SSNumberU.isLessThanOrEqual(
						resource.counters.get(SSModelUEResourceCounterEnum.counterCollaborateCollection.toString()),
						0)                                  ){
			
			resource.mICollaborateCollectionNot                 = true;
			resource.mICollaborateCollection                    = false;
			resource.mICollaborateCollectionNumberOf            = false;
			
		}else if(
				SSNumberU.isGreaterThan(
						resource.counters.get(SSModelUEResourceCounterEnum.counterCollaborateCollectionInitial.toString()), 
						0)){
			
			resource.mICollaborateCollectionNot                 = false;
			
			resource.mICollaborateCollectionNumberOf  = 
				maturingIndicatorSetterHelper.getNumberOfTimesMaturingIndicator(
						resource, 
						resource.counters.get(SSModelUEResourceCounterEnum.counterCollaborateCollection.toString()), 
						SSModelUEU.THRESHOLD_COLLECTION_COLLABORATE_COLLECTION, 
						SSModelUEU.THRESHOLD_DISCUSSION_COLLABORATE_COLLECTION, 
						SSModelUEU.THRESHOLD_DIGITAL_RESOURCE_COLLABORATE_COLLECTION,
						0);
			
			resource.mICollaborateCollection                    = !resource.mICollaborateCollectionNumberOf;
		}
	}
	
	/**
	 * 0 collaborately work on discussions <br>
	 * - whether the resource got collaborately worked on <br>
	 * - whether the resource got not collaborately worked on <br>
	 * - whether the resource got collaborately worked on number of times <br>
	 **/
	private void setCollaborateDiscussion(SSModelUEResource resource){
		
		if(
				SSNumberU.isLessThanOrEqual(
						resource.counters.get(SSModelUEResourceCounterEnum.counterCollaborateDiscussionInitial.toString()), 
						0)                                   ||
				SSNumberU.isLessThanOrEqual(
						resource.counters.get(SSModelUEResourceCounterEnum.counterCollaborateDiscussion.toString()),
						0)                                  ){
			
			resource.mICollaborateDiscussionNot                 = true;
			resource.mICollaborateDiscussion                    = false;
			resource.mICollaborateDiscussionNumberOf            = false;
			
		}else if(
				SSNumberU.isGreaterThan(
						resource.counters.get(SSModelUEResourceCounterEnum.counterCollaborateDiscussionInitial.toString()), 
						0)){
			
			resource.mICollaborateDiscussionNot                 = false;
			
			resource.mICollaborateDiscussionNumberOf            = 
				maturingIndicatorSetterHelper.getNumberOfTimesMaturingIndicator(
						resource, 
						resource.counters.get(SSModelUEResourceCounterEnum.counterCollaborateDiscussion.toString()), 
						SSModelUEU.THRESHOLD_COLLECTION_COLLABORATE_DISCUSSION, 
						SSModelUEU.THRESHOLD_DISCUSSION_COLLABORATE_DISCUSSION, 
						SSModelUEU.THRESHOLD_DIGITAL_RESOURCE_COLLABORATE_DISCUSSION,
						0);
			
			resource.mICollaborateDiscussion                    = !resource.mICollaborateDiscussionNumberOf;
		}
	}

	/**
	 * 0 organized collection  <br>
	 * - whether the collection was organized <br>
	 * - whether the collection wasn't organized <br>
	 * - whether the collection was organized number of times <br>
	 **/
	private void setOrganizeCollection(SSModelUEResource resource){
		
		if(
				SSNumberU.isLessThanOrEqual(
						resource.counters.get(SSModelUEResourceCounterEnum.counterOrganizeCollection.toString()), 
						0)){
			
			
			resource.mIOrganizeCollectionNot       = true;
			resource.mIOrganizeCollection          = false;
			resource.mIOrganizeCollectionNumberOf  = false;
			
		}else{
			
			resource.mIOrganizeCollectionNot       = false;
			
			resource.mIOrganizeCollectionNumberOf  = 
				maturingIndicatorSetterHelper.getNumberOfTimesMaturingIndicator(
						resource, 
						resource.counters.get(SSModelUEResourceCounterEnum.counterOrganizeCollection.toString()), 
						SSModelUEU.THRESHOLD_COLLECTION_ORGANIZE_COLLECTION, 
						SSModelUEU.THRESHOLD_DISCUSSION_ORGANIZE_COLLECTION, 
						SSModelUEU.THRESHOLD_DIGITAL_RESOURCE_ORGANIZE_COLLECTION,
						SSModelUEU.THRESHOLD_PERSON_ORGANIZE_COLLECTION);
			
			resource.mIOrganizeCollection          = !resource.mIOrganizeCollectionNumberOf;
		}
	}
	
	/**
	 * 0 has number of associated persons <br>
	 * - whether the resource has a number of persons associated <br>
	 **/
	private void setAssociatePerson(SSModelUEResource resource){
		
		resource.mIAssociatePersonNumberOf = 
			maturingIndicatorSetterHelper.getNumberOfTimesMaturingIndicator(
				resource, 
				resource.counters.get(SSModelUEResourceCounterEnum.counterAssociatePerson.toString()), 
				SSModelUEU.THRESHOLD_COLLECTION_ASSOCIATE_PERSON, 
				SSModelUEU.THRESHOLD_DISCUSSION_ASSOCIATE_PERSON, 
				SSModelUEU.THRESHOLD_DIGITAL_RESOURCE_ASSOCIATE_PERSON,
				0);
	}
	
	/**
	 * 0 changed (or not recently after intensive editing) <br>
	 * - whether the resource changed <br>
	 * - whether the resource changed not <br>
	 * - whether the resource changed number of times <br>
	 * (- whether the resource changed not recently after intensive editing) <br>
	 **/
	private void setChange(SSModelUEResource resource){
		
		if(
				SSNumberU.isLessThanOrEqual(
						resource.counters.get(SSModelUEResourceCounterEnum.counterChange.toString()),
						0)){
			
			resource.mIChangeNot                          = true;
			resource.mIChange                             = false;
			resource.miChangeNumberOf                     = false;
			
//			resource.mIChangeRecentAfterIntensiveChangeNot = 
//				maturingIndicatorSetterHelper.getNumberOfTimesMaturingIndicator(
//					resource, 
//					resource.counterChange, 
//					RMGlobalConstants.THRESHOLD_COLLECTION_CHANGE_INTENSIVE, 
//					RMGlobalConstants.THRESHOLD_DISCUSSION_CHANGE_INTENSIVE, 
//					RMGlobalConstants.THRESHOLD_DIGITAL_RESOURCE_CHANGE_INTENSIVE);

		}else{
		
			resource.mIChangeNot                           = false;
//			resource.mIChangeRecentAfterIntensiveChangeNot = false;
			
			resource.miChangeNumberOf                = 
				maturingIndicatorSetterHelper.getNumberOfTimesMaturingIndicator(
						resource, 
						resource.counters.get(SSModelUEResourceCounterEnum.counterChange.toString()), 
						SSModelUEU.THRESHOLD_COLLECTION_CHANGE, 
						SSModelUEU.THRESHOLD_DISCUSSION_CHANGE, 
						SSModelUEU.THRESHOLD_DIGITAL_RESOURCE_CHANGE,
						0);
			
			resource.mIChange                              = !resource.miChangeNumberOf;
		}
	}
	
	/**
	 * 0 tagged <br>
	 * - whether the resource was tagged <br>
	 * - whether the resource was not tagged <br>
	 * - whether the resource was tagged number of times <br>
	 **/ 
	private void setTag(SSModelUEResource resource){
		
		if(
				SSNumberU.isLessThanOrEqual(
						resource.counters.get(SSModelUEResourceCounterEnum.counterTag.toString()),
						0)){
			
			resource.mITagNot       = true;
			resource.mITag          = false;
			resource.mITagNumberOf  = false;
		}else{
			
			resource.mITagNot       = false;
		
			resource.mITagNumberOf  =
				maturingIndicatorSetterHelper.getNumberOfTimesMaturingIndicator(
					resource, 
					resource.counters.get(SSModelUEResourceCounterEnum.counterTag.toString()), 
					SSModelUEU.THRESHOLD_COLLECTION_TAG, 
					SSModelUEU.THRESHOLD_DISCUSSION_TAG, 
					SSModelUEU.THRESHOLD_DIGITAL_RESOURCE_TAG,
					SSModelUEU.THRESHOLD_PERSON_TAG);
			
			resource.mITag          = !resource.mITagNumberOf;
		}
	}
	
	
	/**
	 * 0 just created <br>
	 * - whether the resource was just created (has just a number of events so far) <br>
	 * - whether the resource wasn't just created <br>
	 **/
	private void setCreateJust(SSModelUEResource resource){
		
		resource.mICreateJust =
			maturingIndicatorSetterHelper.getLessNumberOfTimesMaturityIndicator(
					resource,
					resource.counters.get(SSModelUEResourceCounterEnum.counterEvent.toString()),
					SSModelUEU.THRESHOLD_COLLECTION_CREATE_JUST,
					SSModelUEU.THRESHOLD_DISCUSSION_CREATE_JUST,
					SSModelUEU.THRESHOLD_DIGITAL_RESOURCE_CREATE_JUST);
	}
	
	/**
	 * 0 used widely <br>
	 * - whether the resource is used widely (has average number of events) <br>
	 * - whether the resource isn't used widely (has average number of events) <br>
	 **/
	private void setUseWide(SSModelUEResource resource){
		
		resource.mIUseWide =
			maturingIndicatorSetterHelper.getNumberOfTimesMaturingIndicator(
				resource, 
				resource.counters.get(SSModelUEResourceCounterEnum.counterEvent.toString()), 
				SSModelUEU.THRESHOLD_COLLECTION_USE_WIDE, 
				SSModelUEU.THRESHOLD_DISCUSSION_USE_WIDE, 
				SSModelUEU.THRESHOLD_DIGITAL_RESOURCE_USE_WIDE,
				SSModelUEU.THRESHOLD_PERSON_USE_WIDE);
		
		resource.mIUseWideNot = !resource.mIUseWide;	
	}
	
	/**
	 * 0 active periods <br>
	 * - whether the resource has a number of active periods <br>
	 **/
	private void setActivePeriod(SSModelUEResource resource){
		
		resource.mIActivePeriodNumberOf =
			maturingIndicatorSetterHelper.getNumberOfTimesMaturingIndicator(
				resource, 
				resource.counters.get(SSModelUEResourceCounterEnum.counterActivePeriod.toString()), 
				SSModelUEU.THRESHOLD_COLLECTION_ACTIVE_PERIOD, 
				SSModelUEU.THRESHOLD_DISCUSSION_ACTIVE_PERIOD, 
				SSModelUEU.THRESHOLD_DIGITAL_RESOURCE_ACTIVE_PERIOD,
				SSModelUEU.THRESHOLD_PERSON_ACTIVE_PERIOD);
		
		resource.mIActivePeriodNot = !resource.mIActivePeriodNumberOf;	
	}
	
	/**
	 * 0 changed by person <br>
	 * - whether the resource was change by a peson <br>
	 * - whether the resource wasn't changed by a person <br>
	 * - whether the resource was changed by a number of different persons <br>
	 **/
	private void setChangePerson(SSModelUEResource resource){
		
		if(
				SSNumberU.isLessThanOrEqual(
						resource.counters.get(SSModelUEResourceCounterEnum.counterChangePerson.toString()), 
						0)){

			resource.mIChangePersonNot                = true; 
			resource.mIChangePerson                   = false;
			resource.mIChangePersonNumberOf           = false;
			
		}else{
			resource.mIChangePersonNot                = false; 
			
			resource.mIChangePersonNumberOf =
				maturingIndicatorSetterHelper.getNumberOfTimesMaturingIndicator(
					resource, 
					resource.counters.get(SSModelUEResourceCounterEnum.counterChangePerson.toString()), 
					SSModelUEU.THRESHOLD_COLLECTION_CHANGE_PERSON, 
					SSModelUEU.THRESHOLD_DISCUSSION_CHANGE_PERSON, 
					SSModelUEU.THRESHOLD_DIGITAL_RESOURCE_CHANGE_PERSON,
					0);
			
			resource.mIChangePerson                   = !resource.mIChangePersonNumberOf;
		}
	}
	
	
	/**
	 * 0 changed by adding or deleting steps <br>
	 * - whether the resource was changed by one adding/deleting step <br>
	 * - whether the resource wasn't changed by one adding/deleting step <br>
	 * - whether the resource was changed by a number of adding/deleting steps <br>
	 **/
	private void setChangeByAddOrDelete(SSModelUEResource resource){
	
		if(
				SSNumberU.isLessThanOrEqual(
						resource.counters.get(SSModelUEResourceCounterEnum.counterAddAndDelete.toString()), 
						0)){

			resource.mIChangeByAddOrDeleteNot         = true; 
			resource.mIChangeByAddOrDelete            = false;
			resource.mIChangeByAddOrDeleteNumberOf    = false;
			
		}else{
			resource.mIChangeByAddOrDeleteNot         = false; 
			
			resource.mIChangeByAddOrDeleteNumberOf =
				maturingIndicatorSetterHelper.getNumberOfTimesMaturingIndicator(
					resource, 
					resource.counters.get(SSModelUEResourceCounterEnum.counterAddAndDelete.toString()), 
					SSModelUEU.THRESHOLD_COLLECTION_ADD_AND_DELETE, 
					SSModelUEU.THRESHOLD_DISCUSSION_ADD_AND_DELETE, 
					SSModelUEU.THRESHOLD_DIGITAL_RESOURCE_ADD_AND_DELETE,
					SSModelUEU.THRESHOLD_PERSON_ADD_AND_DELETE);
			
			resource.mIChangeByAddOrDelete            = !resource.mIChangeByAddOrDeleteNumberOf;
		}
	}

	
	/**
	 * 0 has discussion <br>
	 * - whether the resource has a discussion <br>
	 * - whether the resource has no discussion <br>
	 **/
	private void setDiscuss(SSModelUEResource resource){
	
		if(
				SSNumberU.isLessThanOrEqual(
						resource.counters.get(SSModelUEResourceCounterEnum.counterDiscussionAbout.toString()),
						0)){

			resource.mIDiscussNot = true;
			resource.mIDiscuss    = false;
						
		}else{
			
			resource.mIDiscussNot = false;
			resource.mIDiscuss    = true;
		}
	}
	
	
	/**
	 * 0 has collection <br>
	 * - whether the resource is in one collection <br>
	 * - whether the resource is in number of collections <br>
	 * - whether the resource is in no collection <br>
	 **/
	private void setCollect(SSModelUEResource resource){

		if(
				SSNumberU.isLessThanOrEqual(
						resource.counters.get(SSModelUEResourceCounterEnum.counterCollection.toString()),
						0)){
			
			resource.mICollectNot                = true;
			resource.mICollect                   = false;
			resource.mICollectNumberOf           = false;
			
		}else{
		
			resource.mICollectNot        = false;
			
			resource.mICollectNumberOf   = 
				maturingIndicatorSetterHelper.getNumberOfTimesMaturingIndicator(
					resource, 
					resource.counters.get(SSModelUEResourceCounterEnum.counterCollection.toString()), 
					SSModelUEU.THRESHOLD_COLLECTION_COLLECTION, 
					SSModelUEU.THRESHOLD_DISCUSSION_COLLECTION, 
					SSModelUEU.THRESHOLD_DIGITAL_RESOURCE_COLLECTION,
					SSModelUEU.THRESHOLD_PERSON_COLLECTION);
			
			resource.mICollect           = !resource.mICollectNumberOf;
		}
	}
	
	/**
	 * 0 has collection with similar content <br>
	 * - whether the resource is in a collection with similar content according to tags <br>
	 * - whether the resource is not in a collection with... <br>
	 * - whether the resource is in a collection with... number of times <br>
	 **/
	private void setCollectSimilar(SSModelUEResource resource){
		
		if(
				SSNumberU.isLessThanOrEqual(
						resource.counters.get(SSModelUEResourceCounterEnum.counterCollectSimilar.toString()),
						0)){
			
			resource.mICollectSimilarNot         = true;
			resource.mICollectSimilar            = false;
			resource.mICollectSimilarNumberOf    = false;
			
		}else{
		
			resource.mICollectSimilarNot         = false;
			
			resource.mICollectSimilarNumberOf   = 
				maturingIndicatorSetterHelper.getNumberOfTimesMaturingIndicator(
					resource, 
					resource.counters.get(SSModelUEResourceCounterEnum.counterCollectSimilar.toString()), 
					SSModelUEU.THRESHOLD_COLLECTION_COLLECTION_SIMILAR, 
					SSModelUEU.THRESHOLD_DISCUSSION_COLLECTION_SIMILAR, 
					SSModelUEU.THRESHOLD_DIGITAL_RESOURCE_COLLECTION_SIMILAR,
					SSModelUEU.THRESHOLD_PERSON_COLLECTION_SIMILAR);
			
			resource.mICollectSimilar            = !resource.mICollectSimilarNumberOf;
		}
	}

	/**
	 * 0 present to influential audience <br>
	 * - whether the resource is presented to influential audience <br>
	 * - whether the resource isn't presented to influential audience <br>
	 * - whether the resource is present to influential audience number of times <br>
	 **/
	private void setPresentAudience(SSModelUEResource resource){
		
		if(
				SSNumberU.isLessThanOrEqual(
						resource.counters.get(SSModelUEResourceCounterEnum.counterPresentAudience.toString()),
						0)){
			
			resource.mIPresentAudienceNot         = true;
			resource.mIPresentAudience            = false;
			resource.mIPresentAudienceNumberOf    = false;
			
		}else{
		
			resource.mIPresentAudienceNot         = false;
			
			resource.mIPresentAudienceNumberOf    = 
				maturingIndicatorSetterHelper.getNumberOfTimesMaturingIndicator(
					resource, 
					resource.counters.get(SSModelUEResourceCounterEnum.counterPresentAudience.toString()), 
					SSModelUEU.THRESHOLD_COLLECTION_PRESENT_AUDIENCE, 
					SSModelUEU.THRESHOLD_DISCUSSION_PRESENT_AUDIENCE, 
					SSModelUEU.THRESHOLD_DIGITAL_RESOURCE_PRESENT_AUDIENCE,
					0);
			
			resource.mIPresentAudience            = !resource.mIPresentAudienceNumberOf;
		}
	}
	
	/**
	 * 0 made out of <br>
	 * - whether the resource is made out of integrating parts <br>
	 * - whether the resource isn't made out of integrating parts <br>
	 * - whether the resource is made out of a number of integrating parts <br>
	 **/
	private void setMadeOutOf(SSModelUEResource resource){
		
		if(
				SSNumberU.isLessThanOrEqual(
						resource.counters.get(SSModelUEResourceCounterEnum.counterMadeOutOf.toString()),
						0)){
			
			resource.mIMadeOutOfOthersNot         = true;
			resource.mIMadeOutOfOthers            = false;
			resource.mIMadeOutOfOthersNumberOf    = false;
			
		}else{
		
			resource.mIMadeOutOfOthersNot         = false;
			
			resource.mIMadeOutOfOthersNumberOf    = 
				maturingIndicatorSetterHelper.getNumberOfTimesMaturingIndicator(
					resource, 
					resource.counters.get(SSModelUEResourceCounterEnum.counterMadeOutOf.toString()), 
					SSModelUEU.THRESHOLD_COLLECTION_MADE_OUT_OF, 
					0, 
					0,
					0);
			
			resource.mIMadeOutOfOthers            = !resource.mIMadeOutOfOthersNumberOf;
		}
	}
	
	/**
	 * 0 shared with community <br>
	 * - whether the resource was shared with community <br>
	 * - whether the resource wasn't shared with community <br>
	 * - whether the resource was shared with community number of times <br>
	 **/
	private void setShareCommunity(SSModelUEResource resource){
		
		if(
				SSNumberU.isLessThanOrEqual(
						resource.counters.get(SSModelUEResourceCounterEnum.counterShareCommunity.toString()),
						0)){
			
			resource.mIShareCommunityNot       = true;
			resource.mIShareCommunity          = false;
			resource.mIShareCommunityNumberOf  = false;
			
		}else{
			resource.mIShareCommunityNot       = false;
			
			resource.mIShareCommunityNumberOf  = 
				maturingIndicatorSetterHelper.getNumberOfTimesMaturingIndicator(
					resource, 
					resource.counters.get(SSModelUEResourceCounterEnum.counterShareCommunity.toString()), 
					SSModelUEU.THRESHOLD_COLLECTION_SHARE_COMMUNITY, 
					SSModelUEU.THRESHOLD_DISCUSSION_SHARE_COMMUNITY, 
					SSModelUEU.THRESHOLD_DIGITAL_RESOURCE_SHARE_COMMUNITY,
					SSModelUEU.THRESHOLD_PERSON_SHARE_COMMUNITY);
			
			resource.mIShareCommunity          = !resource.mIShareCommunityNumberOf;
		}
	}
	
	
	/**
	 * 0 viewed by person <br>
	 * - whether the resource was viewed by a persons <br>
	 * - whether the resource wasn't viewed by a persons <br>
	 * - whether the resource was viewed by number of different persons <br>
	 **/
	private void setViewPerson(SSModelUEResource resource){

		if(
				SSNumberU.isLessThanOrEqual(
						resource.counters.get(SSModelUEResourceCounterEnum.counterViewPerson.toString()),
						0)){
			
			resource.mIViewPersonNot           = true;
			resource.mIViewPerson              = false;
			resource.mIViewPersonNumberOf      = false;
			
		}else{
			resource.mIViewPersonNot           = false;
			
			resource.mIViewPersonNumberOf      = 
				maturingIndicatorSetterHelper.getNumberOfTimesMaturingIndicator(
					resource, 
					resource.counters.get(SSModelUEResourceCounterEnum.counterViewPerson.toString()), 
					SSModelUEU.THRESHOLD_COLLECTION_VIEW_PERSON, 
					SSModelUEU.THRESHOLD_DISCUSSION_VIEW_PERSON, 
					SSModelUEU.THRESHOLD_DIGITAL_RESOURCE_VIEW_PERSON,
					SSModelUEU.THRESHOLD_PERSON_VIEW_PERSON);
			
			resource.mIViewPerson              = !resource.mIViewPersonNumberOf;
		}
	}
	
	/**
	 * 0 viewed <br>
	 * - whether the resource was viewed <br>
	 * - whether the resource wasn't viewed <br>
	 * - whether the resource was viewed number of times <br> 
	 **/
	private void setView(SSModelUEResource resource){
		
		if(
				SSNumberU.isLessThanOrEqual(
						resource.counters.get(SSModelUEResourceCounterEnum.counterView.toString()),
						0)){
			
			resource.mIViewNot         = true;
			resource.mIView            = false;
			resource.mIViewNumberOf    = false;
		}else{
		
			resource.mIViewNot         = false;
			
			resource.mIViewNumberOf    = 
				maturingIndicatorSetterHelper.getNumberOfTimesMaturingIndicator(
					resource, 
					resource.counters.get(SSModelUEResourceCounterEnum.counterView.toString()), 
					SSModelUEU.THRESHOLD_COLLECTION_VIEW, 
					SSModelUEU.THRESHOLD_DISCUSSION_VIEW, 
					SSModelUEU.THRESHOLD_DIGITAL_RESOURCE_VIEW,
					SSModelUEU.THRESHOLD_PERSON_VIEW);
			
			resource.mIView            = !resource.mIViewNumberOf;
		}
	}

	
	/**
	 * 0 rated highly <br>
	 * - whether the resource was rated highly <br>
	 * - whether the resource wasn't rated highly <br>
	 * - whether the resource was rated highly a number of times<br>
	 **/
	private void setRateHigh(SSModelUEResource resource){
		
		if(
				SSNumberU.isLessThanOrEqual(
						resource.counters.get(SSModelUEResourceCounterEnum.counterRateHigh.toString()), 
						0)){
			
			resource.mIRateHighNot      = true;
			resource.mIRateHigh         = false;
			resource.mIRateHighNumberOf = false;
			
		}else{
			
			resource.mIRateHighNot      = false;
			
			resource.mIRateHighNumberOf = 
				maturingIndicatorSetterHelper.getNumberOfTimesMaturingIndicator(
					resource, 
					resource.counters.get(SSModelUEResourceCounterEnum.counterRateHigh.toString()), 
					SSModelUEU.THRESHOLD_COLLECTION_RATE_HIGH,
					SSModelUEU.THRESHOLD_DISCUSSION_RATE_HIGH,
					SSModelUEU.THRESHOLD_DIGITAL_RESOURCE_RATE_HIGH,
					SSModelUEU.THRESHOLD_PERSON_RATE_HIGH);

			resource.mIRateHigh         = !resource.mIRateHighNumberOf;
		}
	}
	
	/**
	 * 0 got rated <br>
	 * - whether the resource was rated <br>
	 * - whether the resource wasn't rated <br>
	 * - whether the resource was rated a number of times<br>
	 **/
	private void setGotRated(SSModelUEResource resource){
		
		if(
				SSNumberU.isLessThanOrEqual(
						resource.counters.get(SSModelUEResourceCounterEnum.counterGotRated.toString()), 
						0)){
			
			resource.mIGotRatedNot      = true;
			resource.mIGotRated         = false;
			resource.mIGotRatedNumberOf = false;
			
		}else{
			
			resource.mIGotRatedNot      = false;
			
			resource.mIGotRatedNumberOf = 
				maturingIndicatorSetterHelper.getNumberOfTimesMaturingIndicator(
					resource, 
					resource.counters.get(SSModelUEResourceCounterEnum.counterGotRated.toString()), 
					SSModelUEU.THRESHOLD_COLLECTION_GOT_RATED,
					SSModelUEU.THRESHOLD_DISCUSSION_GOT_RATED,
					SSModelUEU.THRESHOLD_DIGITAL_RESOURCE_GOT_RATED,
					SSModelUEU.THRESHOLD_PERSON_GOT_RATED);
			
			resource.mIGotRated         = !resource.mIGotRatedNumberOf;
		}
	}
	
	/**
	 * 0 assessed <br>
	 * - whether the resource was assessed by a person <br>
	 * - whether the resource wasn't assessed by a person <br>
	 * - whether the resource was assessed number of times by <br>
	 **/
	private void setAssess(SSModelUEResource resource){
		
		if(
				SSNumberU.isLessThanOrEqual(
						resource.counters.get(SSModelUEResourceCounterEnum.counterAssess.toString()), 
						0)){
			
			resource.mIAssesNot           = true;
			resource.mIAsses              = false;
			resource.mIAssesNumberOf      = false;
			
		}else{
			
			resource.mIAssesNot           = false;
			
			resource.mIAssesNumberOf      = 
				maturingIndicatorSetterHelper.getNumberOfTimesMaturingIndicator(
						resource, 
						resource.counters.get(SSModelUEResourceCounterEnum.counterAssess.toString()), 
						SSModelUEU.THRESHOLD_COLLECTION_ASSESS, 
						SSModelUEU.THRESHOLD_DISCUSSION_ASSESS, 
						SSModelUEU.THRESHOLD_DIGITAL_RESOURCE_ASSESS,
						SSModelUEU.THRESHOLD_PERSON_ASSESS);
			
			resource.mIAsses              = !resource.mIAssesNumberOf; 
		}
	}
	
//	/**
//	 * 0 appears in number of search results <br>
//	 * - whether the resource appears in a search result <br>
//	 * - whether the resource doesn't appear in a search result <br>
//	 * - whether the resource appears in a number of search results <br>
//	 **/
//	private void setSearchResults(SSModelUEResource resource){
//		
//		if(
//				SSNumberU.isLessThanOrEqual(
//						resource.counters.get(SSModelUEResourceCounterEnum.counterSearchResult.toString()), 
//						0)){
//			
//			resource.mISearchResultsNot           = true;
//			resource.mISearchResults              = false;
//			resource.mISearchResultsNumberOf      = false;
//			
//		}else{
//			
//			resource.mISearchResultsNot           = false;
//			
//			resource.mISearchResultsNumberOf      = 
//				maturingIndicatorSetterHelper.getNumberOfTimesMaturingIndicator(
//						resource, 
//						resource.counters.get(SSModelUEResourceCounterEnum.counterSearchResult.toString()), 
//						SSModelUEU.THRESHOLD_COLLECTION_SEARCH_RESULT, 
//						SSModelUEU.THRESHOLD_DISCUSSION_SEARCH_RESULT, 
//						SSModelUEU.THRESHOLD_DIGITAL_RESOURCE_SEARCH_RESULT,
//						SSModelUEU.THRESHOLD_PERSON_SEARCH_RESULT);
//			
//			resource.mISearchResults              = !resource.mISearchResultsNumberOf;
//		}
//	}
	
	
	
	/**
	 * 0 selected from others, or not or number of times <br>
	 * - whether the resource was selected from others <br>
	 * - whether the resource wasn't selected from others <br>
	 * - whether the resource was selected from others number of times <br>
	 **/
	private void setSelectFromOther(SSModelUEResource resource){
		
		if(
				SSNumberU.isLessThanOrEqual(
						resource.counters.get(SSModelUEResourceCounterEnum.counterSelectFromOther.toString()), 
						0)){
			
			resource.mISelectFromOtherNot        = true;
			resource.mISelectFromOther           = false;
			resource.mISelectFromOtherNumberOf   = false;
			
		}else{
			
			resource.mISelectFromOtherNot        = false;
			
			resource.mISelectFromOtherNumberOf   = 
				maturingIndicatorSetterHelper.getNumberOfTimesMaturingIndicator(
						resource, 
						resource.counters.get(SSModelUEResourceCounterEnum.counterSelectFromOther.toString()), 
						SSModelUEU.THRESHOLD_COLLECTION_SELECT_FROM_OTHER, 
						SSModelUEU.THRESHOLD_DISCUSSION_SELECT_FROM_OTHER, 
						SSModelUEU.THRESHOLD_DIGITAL_RESOURCE_SELECT_FROM_OTHER,
						SSModelUEU.THRESHOLD_PERSON_SELECT_FROM_OTHER);
						
			resource.mISelectFromOther           = !resource.mISelectFromOtherNumberOf;
		}
	}	
}
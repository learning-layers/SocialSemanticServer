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

import at.kc.tugraz.ss.datatypes.datatypes.SSEntityEnum;
import at.kc.tugraz.socialserver.utils.*;
import at.kc.tugraz.ss.serv.modeling.ue.datatypes.SSModelUEResource;
import at.kc.tugraz.ss.serv.modeling.ue.datatypes.enums.SSModelUEResourceCounterEnum;
import at.kc.tugraz.ss.serv.modeling.ue.utils.SSModelUEU;
import java.util.*;

public class SSModelUEMIThresholdSetter {

  private final Map<String, SSModelUEResource> resources;
  
  public SSModelUEMIThresholdSetter(Map<String, SSModelUEResource> resources){
    this.resources = resources;
  }
  
	private int getThreshold(
			SSEntityEnum                       resourceType,
			SSModelUEResourceCounterEnum       counterId){
		
		List<Integer>       distinctFrequencies      = new ArrayList<Integer>();
		Integer[]           distinctFrequenciesArray;
		int                 index;
    String              counterIdString = SSStrU.toString(counterId);
		
		for(SSModelUEResource resource : resources.values()){
		
			if(!SSEntityEnum.equals(resource.type, resourceType)){
				
				if(
						SSEntityEnum.isResourceOrFile(resourceType) &&
						SSEntityEnum.isResourceOrFile(resource.type)){
				}else{
					continue;
				}
			}
			
      try{
         if(resource.counters.get(counterIdString) == 0){
           continue;
         }
      }catch(Exception error){
        continue;
      }
      
			if(SSStrU.containsNot(distinctFrequencies, resource.counters.get(counterIdString))){
				distinctFrequencies.add(resource.counters.get(counterIdString));
			}
		}
		
		distinctFrequenciesArray = SSNumberU.toIntegerArray(distinctFrequencies);
		
		java.util.Arrays.sort( distinctFrequenciesArray );
		
		if(distinctFrequenciesArray.length % 2 == 0){
			
			index = distinctFrequenciesArray.length / 2;
			
			if(
					index >= 0 && 
					index  < distinctFrequenciesArray.length){
			
				return distinctFrequenciesArray[index];
			}			
			
		}else{
			
			index = distinctFrequenciesArray.length / 2;
						
			if(
					index + 1  >= 0 && 
					index + 1   < distinctFrequenciesArray.length){
			
				return distinctFrequenciesArray[index + 1];
			}	
		}
		
		return 1;
	}
	
	/**
	 * calculate thresholds for mi calculations 
	 */
	public void calculateThresholds(){

		/*
		 * only available for a certain resource type
		 */
		double averageCollectionMadeOutOf                          = getThreshold(SSEntityEnum.coll,SSModelUEResourceCounterEnum.counterMadeOutOf); //devide(totalCollectionMadeOutOf,           resourceCount);
		double averagePersonIsEditor                               = getThreshold(SSEntityEnum.user,SSModelUEResourceCounterEnum.counterIsEditor); //devide(totalPersonIsEditor,                resourceCount);
		double averagePersonContributedDiscussion                  = getThreshold(SSEntityEnum.user,SSModelUEResourceCounterEnum.counterContributedDiscussion); //devide(totalPersonContributedDiscussion,   resourceCount);
		double averagePersonParticipated                           = getThreshold(SSEntityEnum.user,SSModelUEResourceCounterEnum.counterParticipated); //devide(totalPersonParticipated,            resourceCount);
		
		/*
		 * available for more than one resource type
		 */
		double averageCollectionRateHigh                           = getThreshold(SSEntityEnum.coll,SSModelUEResourceCounterEnum.counterRateHigh);     //devide(totalCollectionRateHigh,            resourceCount);
		double averageDiscussionRateHigh                           = getThreshold(SSEntityEnum.disc,SSModelUEResourceCounterEnum.counterRateHigh);     //devide(totalDiscussionRateHigh,            resourceCount);
		double averageDigitalResourceRateHigh                      = getThreshold(SSEntityEnum.entity,SSModelUEResourceCounterEnum.counterRateHigh);//devide(totalDigitalResourceRateHigh,       resourceCount);
		double averagePersonRateHigh                               = getThreshold(SSEntityEnum.user,SSModelUEResourceCounterEnum.counterRateHigh);         //devide(totalPersonRateHigh,                resourceCount);
		
		double averageCollectionOrganizeCollection                 = getThreshold(SSEntityEnum.coll,SSModelUEResourceCounterEnum.counterOrganizeCollection);//devide(totalCollectionOrganizeCollection,       resourceCount);
		double averageDiscussionOrganizeCollection                 = getThreshold(SSEntityEnum.disc,SSModelUEResourceCounterEnum.counterOrganizeCollection);//devide(totalDiscussionOrganizeCollection,       resourceCount);
		double averageDigitalResourceOrganizeCollection            = getThreshold(SSEntityEnum.entity,SSModelUEResourceCounterEnum.counterOrganizeCollection);//devide(totalDigitalResourceOrganizeCollection,  resourceCount);
		double averagePersonOrganizeCollection                     = getThreshold(SSEntityEnum.user,SSModelUEResourceCounterEnum.counterOrganizeCollection);//devide(totalPersonOrganizeCollection,           resourceCount);
		
		double averageCollectionEventCount                         = getThreshold(SSEntityEnum.coll,SSModelUEResourceCounterEnum.counterEvent); //devide(totalCollectionEventCount,          resourceCount);
		double averageDiscussionEventCount                         = getThreshold(SSEntityEnum.disc,SSModelUEResourceCounterEnum.counterEvent); //devide(totalDiscussionEventCount,          resourceCount);
		double averageDigitalResourceEventCount                    = getThreshold(SSEntityEnum.entity,SSModelUEResourceCounterEnum.counterEvent); //devide(totalDigitalResourceEventCount,     resourceCount);
		double averagePersonEventCount                             = getThreshold(SSEntityEnum.user,SSModelUEResourceCounterEnum.counterEvent); //devide(totalPersonEventCount,              resourceCount);
		
		double averageCollectionCollection                         = getThreshold(SSEntityEnum.coll,SSModelUEResourceCounterEnum.counterCollection);//devide(totalCollectionCollection,          resourceCount);
		double averageDiscussionCollection                         = getThreshold(SSEntityEnum.disc,SSModelUEResourceCounterEnum.counterCollection);//devide(totalDiscussionCollection,          resourceCount);
		double averageDigitalResourceCollection                    = getThreshold(SSEntityEnum.entity,SSModelUEResourceCounterEnum.counterCollection);//devide(totalDigitalResourceCollection,     resourceCount);
		double averagePersonCollection                             = getThreshold(SSEntityEnum.user,SSModelUEResourceCounterEnum.counterCollection);//devide(totalPersonCollection,              resourceCount);
		
		double averageCollectionAssociatePerson                    = getThreshold(SSEntityEnum.coll,SSModelUEResourceCounterEnum.counterAssociatePerson); //devide(totalCollectionAssociatePerson,     resourceCount);
		double averageDiscussionAssociatePerson                    = getThreshold(SSEntityEnum.disc,SSModelUEResourceCounterEnum.counterAssociatePerson); //devide(totalDiscussionAssociatePerson,     resourceCount);
		double averageDigitalResourceAssociatePerson               = getThreshold(SSEntityEnum.entity,SSModelUEResourceCounterEnum.counterAssociatePerson); //devide(totalDigitalResourceAssociatePerson,resourceCount);
		
		double averageCollectionChange                             = getThreshold(SSEntityEnum.coll,SSModelUEResourceCounterEnum.counterChange); //devide(totalCollectionChange,              resourceCount);
		double averageDiscussionChange                             = getThreshold(SSEntityEnum.disc,SSModelUEResourceCounterEnum.counterChange); //devide(totalDiscussionChange,              resourceCount);
		double averageDigitalResourceChange                        = getThreshold(SSEntityEnum.entity,SSModelUEResourceCounterEnum.counterChange); //devide(totalDigitalResourceChange,         resourceCount);
		double averagePersonAddAndDelete                           = getThreshold(SSEntityEnum.user,SSModelUEResourceCounterEnum.counterChange); //devide(totalPersonAddAndDelete,            resourceCount);
		
		double averageCollectionChangePerson                       = getThreshold(SSEntityEnum.coll,SSModelUEResourceCounterEnum.counterChangePerson); //devide(totalCollectionChangePerson,        resourceCount);
		double averageDiscussionChangePerson                       = getThreshold(SSEntityEnum.disc,SSModelUEResourceCounterEnum.counterChangePerson); //devide(totalDiscussionChangePerson,        resourceCount);
		double averageDigitalResourceChangePerson                  = getThreshold(SSEntityEnum.entity,SSModelUEResourceCounterEnum.counterChangePerson); //devide(totalDigitalResourceChangePerson,   resourceCount);
		
		double averageCollectionTag                                = getThreshold(SSEntityEnum.coll,SSModelUEResourceCounterEnum.counterTag); //devide(totalCollectionTag,                 resourceCount);
		double averageDiscussionTag                                = getThreshold(SSEntityEnum.disc,SSModelUEResourceCounterEnum.counterTag); //devide(totalDiscussionTag,                 resourceCount);		
		double averageDigitalResourceTag                           = getThreshold(SSEntityEnum.entity,SSModelUEResourceCounterEnum.counterTag); //devide(totalDigitalResourceTag,            resourceCount);
		double averagePersonTag                                    = getThreshold(SSEntityEnum.user,SSModelUEResourceCounterEnum.counterTag); //devide(totalPersonTag,                     resourceCount);
		
		double averageCollectionViewPerson                         = getThreshold(SSEntityEnum.coll,SSModelUEResourceCounterEnum.counterViewPerson); //devide(totalCollectionViewPerson,          resourceCount);
		double averageDiscussionViewPerson                         = getThreshold(SSEntityEnum.disc,SSModelUEResourceCounterEnum.counterViewPerson); //devide(totalDiscussionViewPerson,          resourceCount);
		double averageDigitalResourceViewPerson                    = getThreshold(SSEntityEnum.entity,SSModelUEResourceCounterEnum.counterViewPerson); //devide(totalDigitalResourceViewPerson,     resourceCount);
		double averagePersonViewPerson                             = getThreshold(SSEntityEnum.user,SSModelUEResourceCounterEnum.counterViewPerson); //devide(totalPersonViewPerson,              resourceCount);
		
		double averageCollectionView                               = getThreshold(SSEntityEnum.coll,SSModelUEResourceCounterEnum.counterView); //devide(totalCollectionView,                resourceCount);
		double averageDiscussionView                               = getThreshold(SSEntityEnum.disc,SSModelUEResourceCounterEnum.counterView); //devide(totalDiscussionView,                resourceCount);
		double averageDigitalResourceView                          = getThreshold(SSEntityEnum.entity,SSModelUEResourceCounterEnum.counterView); //devide(totalDigitalResourceView,           resourceCount);
		double averagePersonView                                   = getThreshold(SSEntityEnum.user,SSModelUEResourceCounterEnum.counterView); //devide(totalPersonView,                    resourceCount);
		
//		double averageCollectionSearchResult                       = getThreshold(SSEntityEnum.coll,SSModelUEResourceCounterEnum.counterSearchResult); //devide(totalCollectionSearchResult,        resourceCount);
//		double averageDiscussionSearchResult                       = getThreshold(SSEntityEnum.disc,SSModelUEResourceCounterEnum.counterSearchResult); //devide(totalDiscussionSearchResult,        resourceCount);
//		double averageDigitalResourceSearchResult                  = getThreshold(SSEntityEnum.entity,SSModelUEResourceCounterEnum.counterSearchResult); //devide(totalDigitalResourceSearchResult,   resourceCount);
//		double averagePersonSearchResult                           = getThreshold(SSEntityEnum.user,SSModelUEResourceCounterEnum.counterSearchResult); //devide(totalPersonSearchResult,            resourceCount);
		
		double averageCollectionSelectFromOther                    = getThreshold(SSEntityEnum.coll,SSModelUEResourceCounterEnum.counterSelectFromOther); //devide(totalCollectionSelectFromOther,     resourceCount);
		double averageDiscussionSelectFromOther                    = getThreshold(SSEntityEnum.disc,SSModelUEResourceCounterEnum.counterSelectFromOther); //devide(totalDiscussionSelectFromOther,     resourceCount);
		double averageDigitalResourceSelectFromOther               = getThreshold(SSEntityEnum.entity,SSModelUEResourceCounterEnum.counterSelectFromOther); //devide(totalDigitalResourceSelectFromOther,resourceCount);
		double averagePersonSelectFromOther                        = getThreshold(SSEntityEnum.user,SSModelUEResourceCounterEnum.counterSelectFromOther); //devide(totalPersonSelectFromOther,         resourceCount);
		
		double averageCollectionCollaborateCollection              = getThreshold(SSEntityEnum.coll,SSModelUEResourceCounterEnum.counterCollaborateCollection); //devide(totalCollectionCollaborateCollection,         resourceCount);
		double averageDiscussionCollaborateCollection              = getThreshold(SSEntityEnum.disc,SSModelUEResourceCounterEnum.counterCollaborateCollection); //devide(totalDiscussionCollaborateCollection,         resourceCount);
		double averageDigitalResourceCollaborateCollection         = getThreshold(SSEntityEnum.entity,SSModelUEResourceCounterEnum.counterCollaborateCollection); //devide(totalDigitalResourceCollaborateCollection,    resourceCount);
		
		double averageCollectionCollaborateDiscussion              = getThreshold(SSEntityEnum.coll,SSModelUEResourceCounterEnum.counterCollaborateDiscussion); //devide(totalCollectionCollaborateDiscussion,         resourceCount);
		double averageDiscussionCollaborateDiscussion              = getThreshold(SSEntityEnum.disc,SSModelUEResourceCounterEnum.counterCollaborateDiscussion); //devide(totalDiscussionCollaborateDiscussion,         resourceCount);
		double averageDigitalResourceCollaborateDiscussion         = getThreshold(SSEntityEnum.entity,SSModelUEResourceCounterEnum.counterCollaborateDiscussion); //devide(totalDigitalResourceCollaborateDiscussion,    resourceCount);
		
		double averageCollectionAware                              = getThreshold(SSEntityEnum.coll,SSModelUEResourceCounterEnum.counterAware); //devide(totalCollectionAware,               resourceCount);		
		double averageDiscussionAware                              = getThreshold(SSEntityEnum.disc,SSModelUEResourceCounterEnum.counterAware); //devide(totalDiscussionAware,               resourceCount);
		double averageDigitalResourceAware                         = getThreshold(SSEntityEnum.entity,SSModelUEResourceCounterEnum.counterAware); //devide(totalDigitalResourceAware,          resourceCount);
		double averagePersonAware                                  = getThreshold(SSEntityEnum.user,SSModelUEResourceCounterEnum.counterAware); //devide(totalPersonAware,                   resourceCount);
		
		double averageCollectionRecommend                          = getThreshold(SSEntityEnum.coll,SSModelUEResourceCounterEnum.counterRecommend); //devide(totalCollectionRecommend,           resourceCount);
		double averageDiscussionRecommend                          = getThreshold(SSEntityEnum.disc,SSModelUEResourceCounterEnum.counterRecommend); //devide(totalDiscussionRecommend,           resourceCount);
		double averageDigitalResourceRecommend                     = getThreshold(SSEntityEnum.entity,SSModelUEResourceCounterEnum.counterRecommend); //devide(totalDigitalResourceRecommend,      resourceCount);
		double averagePersonRecommend                              = getThreshold(SSEntityEnum.user,SSModelUEResourceCounterEnum.counterRecommend); // devide(totalPersonRecommend,               resourceCount);
		
		double averageCollectionAddAndDelete                       = getThreshold(SSEntityEnum.coll,SSModelUEResourceCounterEnum.counterAddAndDelete); //devide(totalCollectionAddAndDelete,        resourceCount);
		double averageDiscussionAddAndDelete                       = getThreshold(SSEntityEnum.disc,SSModelUEResourceCounterEnum.counterAddAndDelete); //devide(totalDiscussionAddAndDelete,        resourceCount);
		double averageDigitalResourceAddAndDelete                  = getThreshold(SSEntityEnum.entity,SSModelUEResourceCounterEnum.counterAddAndDelete); //devide(totalDigitalResourceAddAndDelete,   resourceCount);
		
		double averageCollectionShareCommunity                     = getThreshold(SSEntityEnum.coll,SSModelUEResourceCounterEnum.counterShareCommunity); //devide(totalCollectionShareCommunity,      resourceCount);
		double averageDiscussionShareCommunity                     = getThreshold(SSEntityEnum.disc,SSModelUEResourceCounterEnum.counterShareCommunity); //devide(totalDiscussionShareCommunity,      resourceCount);
		double averageDigitalResourceShareCommunity                = getThreshold(SSEntityEnum.entity,SSModelUEResourceCounterEnum.counterShareCommunity); //devide(totalDigitalResourceShareCommunity, resourceCount);
		double averagePersonShareCommunity                         = getThreshold(SSEntityEnum.user,SSModelUEResourceCounterEnum.counterShareCommunity); //devide(totalPersonShareCommunity,          resourceCount);
		
		double averageCollectionCollectionSimilar                  = getThreshold(SSEntityEnum.coll,SSModelUEResourceCounterEnum.counterCollectSimilar); //devide(totalCollectionCollectionSimilar,      resourceCount);
		double averageDiscussionCollectionSimilar                  = getThreshold(SSEntityEnum.disc,SSModelUEResourceCounterEnum.counterCollectSimilar); //devide(totalDiscussionCollectionSimilar,      resourceCount);
		double averageDigitalResourceCollectionSimilar             = getThreshold(SSEntityEnum.entity,SSModelUEResourceCounterEnum.counterCollectSimilar); //devide(totalDigitalResourceCollectionSimilar, resourceCount);	
		double averagePersonCollectionSimilar                      = getThreshold(SSEntityEnum.user,SSModelUEResourceCounterEnum.counterCollectSimilar); //devide(totalPersonCollectionSimilar,          resourceCount);
		
		double averageCollectionPresentAudience                    = getThreshold(SSEntityEnum.coll,SSModelUEResourceCounterEnum.counterPresentAudience); //devide(totalCollectionPresentAudience,        resourceCount);
		double averageDiscussionPresentAudience                    = getThreshold(SSEntityEnum.disc,SSModelUEResourceCounterEnum.counterPresentAudience); //devide(totalDiscussionPresentAudience,        resourceCount);
		double averageDigitalResourcePresentAudience               = getThreshold(SSEntityEnum.entity,SSModelUEResourceCounterEnum.counterPresentAudience); //devide(totalDigitalResourcePresentAudience,   resourceCount);	

		double averageCollectionAssess                             = getThreshold(SSEntityEnum.coll,SSModelUEResourceCounterEnum.counterAssess); //devide(totalCollectionAssess,                 resourceCount);
		double averageDiscussionAssess                             = getThreshold(SSEntityEnum.disc,SSModelUEResourceCounterEnum.counterAssess); //devide(totalDiscussionAssess,                 resourceCount);
		double averageDigitalResourceAssess                        = getThreshold(SSEntityEnum.entity,SSModelUEResourceCounterEnum.counterAssess); //devide(totalDigitalResourceAssess,            resourceCount);
		double averagePersonAssess                                 = getThreshold(SSEntityEnum.user,SSModelUEResourceCounterEnum.counterAssess); // devide(totalPersonAssess,                     resourceCount);
		
		double averageCollectionGotRated                           = getThreshold(SSEntityEnum.coll,SSModelUEResourceCounterEnum.counterGotRated); //devide(totalCollectionGotRated,               resourceCount);
		double averageDiscussionGotRated                           = getThreshold(SSEntityEnum.disc,SSModelUEResourceCounterEnum.counterGotRated); //devide(totalDiscussionGotRated,               resourceCount);
		double averageDigitalResourceGotRated                      = getThreshold(SSEntityEnum.entity,SSModelUEResourceCounterEnum.counterGotRated); //devide(totalDigitalResourceGotRated,          resourceCount);
		double averagePersonGotRated                               = getThreshold(SSEntityEnum.user,SSModelUEResourceCounterEnum.counterGotRated); // devide(totalPersonGotRated,                   resourceCount);
		
		double averageCollectionReferredBy                         = getThreshold(SSEntityEnum.coll,SSModelUEResourceCounterEnum.counterReferredBy); //devide(totalCollectionReferredBy,                 resourceCount);
		double averageDiscussionReferredBy                         = getThreshold(SSEntityEnum.disc,SSModelUEResourceCounterEnum.counterReferredBy); //devide(totalDiscussionReferredBy,                 resourceCount);
		double averageDigitalResourceReferredBy                    = getThreshold(SSEntityEnum.entity,SSModelUEResourceCounterEnum.counterReferredBy); //devide(totalDigitalResourceReferredBy,            resourceCount);
		double averagePersonReferredBy                             = getThreshold(SSEntityEnum.user,SSModelUEResourceCounterEnum.counterReferredBy); //devide(totalPersonReferredBy,                     resourceCount);

		double averageCollectionActivePeriod                       = getThreshold(SSEntityEnum.coll,SSModelUEResourceCounterEnum.counterActivePeriod); //devide(totalCollectionActivePeriod,               resourceCount);
		double averageDiscussionActivePeriod                       = getThreshold(SSEntityEnum.disc,SSModelUEResourceCounterEnum.counterActivePeriod); //devide(totalDiscussionActivePeriod,               resourceCount);
		double averageDigitalResourceActivePeriod                  = getThreshold(SSEntityEnum.entity,SSModelUEResourceCounterEnum.counterActivePeriod); //devide(totalDigitalResourceActivePeriod,          resourceCount);
		double averagePersonActivePeriod                           = getThreshold(SSEntityEnum.user,SSModelUEResourceCounterEnum.counterActivePeriod); //devide(totalPersonActivePeriod,                   resourceCount);
		
		/*
		 * only available for a certain resource type
		 */
		//collections
		  SSModelUEU.THRESHOLD_COLLECTION_MADE_OUT_OF                    = multiply(averageCollectionMadeOutOf,           SSModelUEU.FACTOR_COLLECTION_MADE_OUT_OF);
		//persons
		SSModelUEU.THRESHOLD_PERSON_IS_EDITOR                          = multiply(averagePersonIsEditor,                SSModelUEU.FACTOR_PERSON_IS_EDITOR);
		SSModelUEU.THRESHOLD_PERSON_CONTRIBUTED_DISCUSSION             = multiply(averagePersonContributedDiscussion,   SSModelUEU.FACTOR_PERSON_CONTRIBUTED_DISCUSSION);
		SSModelUEU.THRESHOLD_PERSON_PARTICIPATED                       = multiply(averagePersonParticipated,            SSModelUEU.FACTOR_PERSON_PARTICIPATED);
				
		/*
		 * available for more than one resource type
		 */
		SSModelUEU.THRESHOLD_COLLECTION_CREATE_JUST                    = multiply(averageCollectionEventCount,          SSModelUEU.FACTOR_COLLECTION_CREATE_JUST);
		SSModelUEU.THRESHOLD_DISCUSSION_CREATE_JUST                    = multiply(averageDiscussionEventCount,          SSModelUEU.FACTOR_DISCUSSION_CREATE_JUST);
		SSModelUEU.THRESHOLD_DIGITAL_RESOURCE_CREATE_JUST              = multiply(averageDigitalResourceEventCount,     SSModelUEU.FACTOR_DIGITAL_RESOURCE_CREATE_JUST);
		
		SSModelUEU.THRESHOLD_COLLECTION_USE_WIDE                       = multiply(averageCollectionEventCount,          SSModelUEU.FACTOR_COLLECTION_USE_WIDE);
		SSModelUEU.THRESHOLD_DISCUSSION_USE_WIDE                       = multiply(averageDiscussionEventCount,          SSModelUEU.FACTOR_DISCUSSION_USE_WIDE);
		SSModelUEU.THRESHOLD_DIGITAL_RESOURCE_USE_WIDE                 = multiply(averageDigitalResourceEventCount,     SSModelUEU.FACTOR_DIGITAL_RESOURCE_USE_WIDE);
		SSModelUEU.THRESHOLD_PERSON_USE_WIDE                           = multiply(averagePersonEventCount,              SSModelUEU.FACTOR_PERSON_USE_WIDE);		
		
		SSModelUEU.THRESHOLD_COLLECTION_COLLECTION                     = multiply(averageCollectionCollection,          SSModelUEU.FACTOR_COLLECTION_COLLECTION);
		SSModelUEU.THRESHOLD_DISCUSSION_COLLECTION                     = multiply(averageDiscussionCollection,          SSModelUEU.FACTOR_DISCUSSION_COLLECTION);
		SSModelUEU.THRESHOLD_DIGITAL_RESOURCE_COLLECTION               = multiply(averageDigitalResourceCollection,     SSModelUEU.FACTOR_DIGITAL_RESOURCE_COLLECTION);
		SSModelUEU.THRESHOLD_PERSON_COLLECTION                         = multiply(averagePersonCollection,              SSModelUEU.FACTOR_PERSON_COLLECTION);
		
		SSModelUEU.THRESHOLD_COLLECTION_CHANGE                         = multiply(averageCollectionChange,              SSModelUEU.FACTOR_COLLECTION_CHANGE);
		SSModelUEU.THRESHOLD_DISCUSSION_CHANGE                         = multiply(averageDiscussionChange,              SSModelUEU.FACTOR_DISCUSSION_CHANGE);
		SSModelUEU.THRESHOLD_DIGITAL_RESOURCE_CHANGE                   = multiply(averageDigitalResourceChange,         SSModelUEU.FACTOR_DIGITAL_RESOURCE_CHANGE);
		
		SSModelUEU.THRESHOLD_COLLECTION_ASSOCIATE_PERSON               = multiply(averageCollectionAssociatePerson,     SSModelUEU.FACTOR_COLLECTION_ASSOCIATE_PERSON);
		SSModelUEU.THRESHOLD_DISCUSSION_ASSOCIATE_PERSON               = multiply(averageDiscussionAssociatePerson,     SSModelUEU.FACTOR_DISCUSSION_ASSOCIATE_PERSON);
		SSModelUEU.THRESHOLD_DIGITAL_RESOURCE_ASSOCIATE_PERSON         = multiply(averageDigitalResourceAssociatePerson,SSModelUEU.FACTOR_DIGITAL_RESOURCE_ASSOCIATE_PERSON);
		
		SSModelUEU.THRESHOLD_COLLECTION_CHANGE_PERSON                  = multiply(averageCollectionChangePerson,        SSModelUEU.FACTOR_COLLECTION_CHANGE_PERSON);
		SSModelUEU.THRESHOLD_DISCUSSION_CHANGE_PERSON                  = multiply(averageDiscussionChangePerson,        SSModelUEU.FACTOR_DISCUSSION_CHANGE_PERSON);
		SSModelUEU.THRESHOLD_DIGITAL_RESOURCE_CHANGE_PERSON            = multiply(averageDigitalResourceChangePerson,   SSModelUEU.FACTOR_DIGITAL_RESOURCE_CHANGE_PERSON);
		
		SSModelUEU.THRESHOLD_COLLECTION_TAG                            = multiply(averageCollectionTag,                 SSModelUEU.FACTOR_COLLECTION_TAG);
		SSModelUEU.THRESHOLD_DISCUSSION_TAG                            = multiply(averageDiscussionTag,                 SSModelUEU.FACTOR_DISCUSSION_TAG);
		SSModelUEU.THRESHOLD_DIGITAL_RESOURCE_TAG                      = multiply(averageDigitalResourceTag,            SSModelUEU.FACTOR_DIGITAL_RESOURCE_TAG);
		SSModelUEU.THRESHOLD_PERSON_TAG                                = multiply(averagePersonTag,                     SSModelUEU.FACTOR_PERSON_TAG);
		
		SSModelUEU.THRESHOLD_COLLECTION_VIEW_PERSON                    = multiply(averageCollectionViewPerson,          SSModelUEU.FACTOR_COLLECTION_VIEW_PERSON);
		SSModelUEU.THRESHOLD_DISCUSSION_VIEW_PERSON                    = multiply(averageDiscussionViewPerson,          SSModelUEU.FACTOR_DISCUSSION_VIEW_PERSON);
		SSModelUEU.THRESHOLD_DIGITAL_RESOURCE_VIEW_PERSON              = multiply(averageDigitalResourceViewPerson,     SSModelUEU.FACTOR_DIGITAL_RESOURCE_VIEW_PERSON);
		SSModelUEU.THRESHOLD_PERSON_VIEW_PERSON                        = multiply(averagePersonViewPerson,              SSModelUEU.FACTOR_PERSON_VIEW_PERSON);
		
		SSModelUEU.THRESHOLD_COLLECTION_VIEW                           = multiply(averageCollectionView,                SSModelUEU.FACTOR_COLLECTION_VIEW);
		SSModelUEU.THRESHOLD_DISCUSSION_VIEW                           = multiply(averageDiscussionView,                SSModelUEU.FACTOR_DISCUSSION_VIEW);
		SSModelUEU.THRESHOLD_DIGITAL_RESOURCE_VIEW                     = multiply(averageDigitalResourceView,           SSModelUEU.FACTOR_DIGITAL_RESOURCE_VIEW);
		SSModelUEU.THRESHOLD_PERSON_VIEW                               = multiply(averagePersonView,                    SSModelUEU.FACTOR_PERSON_VIEW);
		
//		SSModelUEU.THRESHOLD_COLLECTION_SEARCH_RESULT                  = multiply(averageCollectionSearchResult,        SSModelUEU.FACTOR_COLLECTION_SEARCH_RESULT);
////		SSModelUEU.THRESHOLD_DISCUSSION_SEARCH_RESULT                  = multiply(averageDiscussionSearchResult,        SSModelUEU.FACTOR_DISCUSSION_SEARCH_RESULT);
//		SSModelUEU.THRESHOLD_DIGITAL_RESOURCE_SEARCH_RESULT            = multiply(averageDigitalResourceSearchResult,   SSModelUEU.FACTOR_DIGITAL_RESOURCE_SEARCH_RESULT);
//		SSModelUEU.THRESHOLD_PERSON_SEARCH_RESULT                      = multiply(averagePersonSearchResult,            SSModelUEU.FACTOR_PERSON_SEARCH_RESULT);
		
		SSModelUEU.THRESHOLD_COLLECTION_SELECT_FROM_OTHER              = multiply(averageCollectionSelectFromOther,      SSModelUEU.FACTOR_COLLECTION_SELECT_FROM_OTHER);
		SSModelUEU.THRESHOLD_DISCUSSION_SELECT_FROM_OTHER              = multiply(averageDiscussionSelectFromOther,      SSModelUEU.FACTOR_DISCUSSION_SELECT_FROM_OTHER);
		SSModelUEU.THRESHOLD_DIGITAL_RESOURCE_SELECT_FROM_OTHER        = multiply(averageDigitalResourceSelectFromOther, SSModelUEU.FACTOR_DIGITAL_RESOURCE_SELECT_FROM_OTHER);
		SSModelUEU.THRESHOLD_PERSON_SELECT_FROM_OTHER                  = multiply(averagePersonSelectFromOther,          SSModelUEU.FACTOR_PERSON_SELECT_FROM_OTHER);
		
		SSModelUEU.THRESHOLD_COLLECTION_COLLABORATE_COLLECTION         = multiply(averageCollectionCollaborateCollection,          SSModelUEU.FACTOR_COLLECTION_COLLABORATE_COLLECTION);		
		SSModelUEU.THRESHOLD_DISCUSSION_COLLABORATE_COLLECTION         = multiply(averageDiscussionCollaborateCollection,          SSModelUEU.FACTOR_DISCUSSION_COLLABORATE_COLLECTION);
		SSModelUEU.THRESHOLD_DIGITAL_RESOURCE_COLLABORATE_COLLECTION   = multiply(averageDigitalResourceCollaborateCollection,     SSModelUEU.FACTOR_DIGITAL_RESOURCE_COLLABORATE_COLLECTION);
		
		SSModelUEU.THRESHOLD_COLLECTION_COLLABORATE_DISCUSSION         = multiply(averageCollectionCollaborateDiscussion,          SSModelUEU.FACTOR_COLLECTION_COLLABORATE_DISCUSSION);		
		SSModelUEU.THRESHOLD_DISCUSSION_COLLABORATE_DISCUSSION         = multiply(averageDiscussionCollaborateDiscussion,          SSModelUEU.FACTOR_DISCUSSION_COLLABORATE_DISCUSSION);
		SSModelUEU.THRESHOLD_DIGITAL_RESOURCE_COLLABORATE_DISCUSSION   = multiply(averageDigitalResourceCollaborateDiscussion,     SSModelUEU.FACTOR_DIGITAL_RESOURCE_COLLABORATE_DISCUSSION);
		
		SSModelUEU.THRESHOLD_COLLECTION_AWARE                          = multiply(averageCollectionAware,                   SSModelUEU.FACTOR_COLLECTION_AWARE);
		SSModelUEU.THRESHOLD_DISCUSSION_AWARE                          = multiply(averageDiscussionAware,                   SSModelUEU.FACTOR_DISCUSSION_AWARE);
		SSModelUEU.THRESHOLD_DIGITAL_RESOURCE_AWARE                    = multiply(averageDigitalResourceAware,              SSModelUEU.FACTOR_DIGITAL_RESOURCE_AWARE);
		SSModelUEU.THRESHOLD_PERSON_AWARE                              = multiply(averagePersonAware,                       SSModelUEU.FACTOR_PERSON_AWARE);
		
		SSModelUEU.THRESHOLD_COLLECTION_RECOMMEND                      = multiply(averageCollectionRecommend,               SSModelUEU.FACTOR_COLLECTION_RECOMMEND);
		SSModelUEU.THRESHOLD_DISCUSSION_RECOMMEND                      = multiply(averageDiscussionRecommend,               SSModelUEU.FACTOR_DISCUSSION_RECOMMEND);
		SSModelUEU.THRESHOLD_DIGITAL_RESOURCE_RECOMMEND                = multiply(averageDigitalResourceRecommend,          SSModelUEU.FACTOR_DIGITAL_RESOURCE_RECOMMEND);
		SSModelUEU.THRESHOLD_PERSON_RECOMMEND                          = multiply(averagePersonRecommend,                   SSModelUEU.FACTOR_PERSON_RECOMMEND);
		
		SSModelUEU.THRESHOLD_COLLECTION_ADD_AND_DELETE                 = multiply(averageCollectionAddAndDelete,            SSModelUEU.FACTOR_COLLECTION_ADD_AND_DELETE);
		SSModelUEU.THRESHOLD_DISCUSSION_ADD_AND_DELETE                 = multiply(averageDiscussionAddAndDelete,            SSModelUEU.FACTOR_DISCUSSION_ADD_AND_DELETE);
		SSModelUEU.THRESHOLD_DIGITAL_RESOURCE_ADD_AND_DELETE           = multiply(averageDigitalResourceAddAndDelete,       SSModelUEU.FACTOR_DIGITAL_RESOURCE_ADD_AND_DELETE);
		SSModelUEU.THRESHOLD_PERSON_ADD_AND_DELETE                     = multiply(averagePersonAddAndDelete,                SSModelUEU.FACTOR_PERSON_ADD_AND_DELETE);
		
		SSModelUEU.THRESHOLD_COLLECTION_RATE_HIGH                      = multiply(averageCollectionRateHigh,                SSModelUEU.FACTOR_COLLECTION_RATE_HIGH);
		SSModelUEU.THRESHOLD_DISCUSSION_RATE_HIGH                      = multiply(averageDiscussionRateHigh,                SSModelUEU.FACTOR_DISCUSSION_RATE_HIGH);
		SSModelUEU.THRESHOLD_DIGITAL_RESOURCE_RATE_HIGH                = multiply(averageDigitalResourceRateHigh,           SSModelUEU.FACTOR_DIGITAL_RESOURCE_RATE_HIGH);
		SSModelUEU.THRESHOLD_PERSON_RATE_HIGH                          = multiply(averagePersonRateHigh,                    SSModelUEU.FACTOR_PERSON_RATE_HIGH);
		
		SSModelUEU.THRESHOLD_COLLECTION_SHARE_COMMUNITY                = multiply(averageCollectionShareCommunity,          SSModelUEU.FACTOR_COLLECTION_SHARE_COMMUNITY);
		SSModelUEU.THRESHOLD_DISCUSSION_SHARE_COMMUNITY                = multiply(averageDiscussionShareCommunity,          SSModelUEU.FACTOR_DISCUSSION_SHARE_COMMUNITY);
		SSModelUEU.THRESHOLD_DIGITAL_RESOURCE_SHARE_COMMUNITY          = multiply(averageDigitalResourceShareCommunity,     SSModelUEU.FACTOR_DIGITAL_RESOURCE_SHARE_COMMUNITY);
		SSModelUEU.THRESHOLD_PERSON_SHARE_COMMUNITY                    = multiply(averagePersonShareCommunity,              SSModelUEU.FACTOR_PERSON_SHARE_COMMUNITY); 
		
		SSModelUEU.THRESHOLD_COLLECTION_COLLECTION_SIMILAR             = multiply(averageCollectionCollectionSimilar,       SSModelUEU.FACTOR_COLLECTION_COLLECTION_SIMILAR);
		SSModelUEU.THRESHOLD_DISCUSSION_COLLECTION_SIMILAR             = multiply(averageDiscussionCollectionSimilar,       SSModelUEU.FACTOR_DISCUSSION_COLLECTION_SIMILAR);
		SSModelUEU.THRESHOLD_DIGITAL_RESOURCE_COLLECTION_SIMILAR       = multiply(averageDigitalResourceCollectionSimilar,  SSModelUEU.FACTOR_DIGITAL_RESOURCE_COLLECTION_SIMILAR);
		SSModelUEU.THRESHOLD_PERSON_COLLECTION_SIMILAR                 = multiply(averagePersonCollectionSimilar,           SSModelUEU.FACTOR_PERSON_COLLECTION_SIMILAR);
		
		SSModelUEU.THRESHOLD_COLLECTION_PRESENT_AUDIENCE               = multiply(averageCollectionPresentAudience,         SSModelUEU.FACTOR_COLLECTION_PRESENT_AUDIENCE);
		SSModelUEU.THRESHOLD_DISCUSSION_PRESENT_AUDIENCE               = multiply(averageDiscussionPresentAudience,         SSModelUEU.FACTOR_DISCUSSION_PRESENT_AUDIENCE);
		SSModelUEU.THRESHOLD_DIGITAL_RESOURCE_PRESENT_AUDIENCE         = multiply(averageDigitalResourcePresentAudience,    SSModelUEU.FACTOR_DIGITAL_RESOURCE_PRESENT_AUDIENCE);

		SSModelUEU.THRESHOLD_COLLECTION_ASSESS                         = multiply(averageCollectionAssess,                  SSModelUEU.FACTOR_COLLECTION_ASSESS);
		SSModelUEU.THRESHOLD_DISCUSSION_ASSESS                         = multiply(averageDiscussionAssess,                  SSModelUEU.FACTOR_DISCUSSION_ASSESS);
		SSModelUEU.THRESHOLD_DIGITAL_RESOURCE_ASSESS                   = multiply(averageDigitalResourceAssess,             SSModelUEU.FACTOR_DIGITAL_RESOURCE_ASSESS);
		SSModelUEU.THRESHOLD_PERSON_ASSESS                             = multiply(averagePersonAssess,                      SSModelUEU.FACTOR_PERSON_ASSESS);
		
		SSModelUEU.THRESHOLD_COLLECTION_GOT_RATED                      = multiply(averageCollectionGotRated,                SSModelUEU.FACTOR_COLLECTION_GOT_RATED);
		SSModelUEU.THRESHOLD_DISCUSSION_GOT_RATED                      = multiply(averageDiscussionGotRated,                SSModelUEU.FACTOR_DISCUSSION_GOT_RATED);
		SSModelUEU.THRESHOLD_DIGITAL_RESOURCE_GOT_RATED                = multiply(averageDigitalResourceGotRated,           SSModelUEU.FACTOR_DIGITAL_RESOURCE_GOT_RATED);
		SSModelUEU.THRESHOLD_PERSON_GOT_RATED                          = multiply(averagePersonGotRated,                    SSModelUEU.FACTOR_PERSON_GOT_RATED);
		
		SSModelUEU.THRESHOLD_COLLECTION_REFERRED_BY                    = multiply(averageCollectionReferredBy,              SSModelUEU.FACTOR_COLLECTION_REFERRED_BY);
		SSModelUEU.THRESHOLD_DISCUSSION_REFERRED_BY                    = multiply(averageDiscussionReferredBy,              SSModelUEU.FACTOR_DISCUSSION_REFERRED_BY);
		SSModelUEU.THRESHOLD_DIGITAL_RESOURCE_REFERRED_BY              = multiply(averageDigitalResourceReferredBy,         SSModelUEU.FACTOR_DIGITAL_RESOURCE_REFERRED_BY);
		SSModelUEU.THRESHOLD_PERSON_REFERRED_BY                        = multiply(averagePersonReferredBy,                  SSModelUEU.FACTOR_PERSON_REFERRED_BY);		
		
		SSModelUEU.THRESHOLD_COLLECTION_ACTIVE_PERIOD                  = multiply(averageCollectionActivePeriod,            SSModelUEU.FACTOR_COLLECTION_ACTIVE_PERIOD);
		SSModelUEU.THRESHOLD_DISCUSSION_ACTIVE_PERIOD                  = multiply(averageDiscussionActivePeriod,            SSModelUEU.FACTOR_DISCUSSION_ACTIVE_PERIOD);
		SSModelUEU.THRESHOLD_DIGITAL_RESOURCE_ACTIVE_PERIOD            = multiply(averageDigitalResourceActivePeriod,       SSModelUEU.FACTOR_DIGITAL_RESOURCE_ACTIVE_PERIOD);
		SSModelUEU.THRESHOLD_PERSON_ACTIVE_PERIOD                      = multiply(averagePersonActivePeriod,                SSModelUEU.FACTOR_PERSON_ACTIVE_PERIOD);
		
		SSModelUEU.THRESHOLD_COLLECTION_ORGANIZE_COLLECTION            = multiply(averageCollectionOrganizeCollection,      SSModelUEU.FACTOR_COLLECTION_ORGANIZE_COLLECTION);
		SSModelUEU.THRESHOLD_DISCUSSION_ORGANIZE_COLLECTION            = multiply(averageDiscussionOrganizeCollection,      SSModelUEU.FACTOR_DISCUSSION_ORGANIZE_COLLECTION);
		SSModelUEU.THRESHOLD_DIGITAL_RESOURCE_ORGANIZE_COLLECTION      = multiply(averageDigitalResourceOrganizeCollection, SSModelUEU.FACTOR_DIGITAL_RESOURCE_ORGANIZE_COLLECTION);
		SSModelUEU.THRESHOLD_PERSON_ORGANIZE_COLLECTION                = multiply(averagePersonOrganizeCollection,          SSModelUEU.FACTOR_PERSON_ORGANIZE_COLLECTION);
	}

//	private double devide(double what, double by){
//		
//		if(by != 0){
//		
//			return what/by;
//		}else{
//			return 0;
//		}
//	}
	
	private double multiply(double what, double by){
		
		return what * by;
	}
}

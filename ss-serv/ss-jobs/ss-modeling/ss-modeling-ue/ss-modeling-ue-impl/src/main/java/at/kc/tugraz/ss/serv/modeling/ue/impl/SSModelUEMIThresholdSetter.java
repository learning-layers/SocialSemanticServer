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

import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.socialserver.utils.*;
import at.kc.tugraz.ss.serv.modeling.ue.datatypes.SSModelUEEntity;
import at.kc.tugraz.ss.serv.modeling.ue.datatypes.enums.SSModelUEResourceCounterEnum;
import at.kc.tugraz.ss.serv.modeling.ue.utils.SSModelUEU;
import java.util.*;

public class SSModelUEMIThresholdSetter {

  private final Map<String, SSModelUEEntity> resources;
  
  public SSModelUEMIThresholdSetter(Map<String, SSModelUEEntity> resources){
    this.resources = resources;
  }
  
	private int getThreshold(
			SSEntityE                       resourceType,
			SSModelUEResourceCounterEnum       counterId){
		
		List<Integer>       distinctFrequencies      = new ArrayList<>();
		Integer[]           distinctFrequenciesArray;
		int                 index;
    String              counterIdString = SSStrU.toStr(counterId);
		
		for(SSModelUEEntity resource : resources.values()){
		
			if(!SSEntityE.equals(resource.type, resourceType)){
				
				if(
						SSEntityE.isResourceOrFile(resourceType) &&
						SSEntityE.isResourceOrFile(resource.type)){
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
      
			if(!distinctFrequencies.contains(resource.counters.get(counterIdString))){
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
		double averageCollectionMadeOutOf                          = getThreshold(SSEntityE.coll,SSModelUEResourceCounterEnum.counterMadeOutOf); //devide(totalCollectionMadeOutOf,           resourceCount);
		double averagePersonIsEditor                               = getThreshold(SSEntityE.user,SSModelUEResourceCounterEnum.counterIsEditor); //devide(totalPersonIsEditor,                resourceCount);
		double averagePersonContributedDiscussion                  = getThreshold(SSEntityE.user,SSModelUEResourceCounterEnum.counterContributedDiscussion); //devide(totalPersonContributedDiscussion,   resourceCount);
		double averagePersonParticipated                           = getThreshold(SSEntityE.user,SSModelUEResourceCounterEnum.counterParticipated); //devide(totalPersonParticipated,            resourceCount);
		
		/*
		 * available for more than one resource type
		 */
		double averageCollectionRateHigh                           = getThreshold(SSEntityE.coll,SSModelUEResourceCounterEnum.counterRateHigh);     //devide(totalCollectionRateHigh,            resourceCount);
		double averageDiscussionRateHigh                           = getThreshold(SSEntityE.disc,SSModelUEResourceCounterEnum.counterRateHigh);     //devide(totalDiscussionRateHigh,            resourceCount);
		double averageDigitalResourceRateHigh                      = getThreshold(SSEntityE.entity,SSModelUEResourceCounterEnum.counterRateHigh);//devide(totalDigitalResourceRateHigh,       resourceCount);
		double averagePersonRateHigh                               = getThreshold(SSEntityE.user,SSModelUEResourceCounterEnum.counterRateHigh);         //devide(totalPersonRateHigh,                resourceCount);
		
		double averageCollectionOrganizeCollection                 = getThreshold(SSEntityE.coll,SSModelUEResourceCounterEnum.counterOrganizeCollection);//devide(totalCollectionOrganizeCollection,       resourceCount);
		double averageDiscussionOrganizeCollection                 = getThreshold(SSEntityE.disc,SSModelUEResourceCounterEnum.counterOrganizeCollection);//devide(totalDiscussionOrganizeCollection,       resourceCount);
		double averageDigitalResourceOrganizeCollection            = getThreshold(SSEntityE.entity,SSModelUEResourceCounterEnum.counterOrganizeCollection);//devide(totalDigitalResourceOrganizeCollection,  resourceCount);
		double averagePersonOrganizeCollection                     = getThreshold(SSEntityE.user,SSModelUEResourceCounterEnum.counterOrganizeCollection);//devide(totalPersonOrganizeCollection,           resourceCount);
		
		double averageCollectionEventCount                         = getThreshold(SSEntityE.coll,SSModelUEResourceCounterEnum.counterEvent); //devide(totalCollectionEventCount,          resourceCount);
		double averageDiscussionEventCount                         = getThreshold(SSEntityE.disc,SSModelUEResourceCounterEnum.counterEvent); //devide(totalDiscussionEventCount,          resourceCount);
		double averageDigitalResourceEventCount                    = getThreshold(SSEntityE.entity,SSModelUEResourceCounterEnum.counterEvent); //devide(totalDigitalResourceEventCount,     resourceCount);
		double averagePersonEventCount                             = getThreshold(SSEntityE.user,SSModelUEResourceCounterEnum.counterEvent); //devide(totalPersonEventCount,              resourceCount);
		
		double averageCollectionCollection                         = getThreshold(SSEntityE.coll,SSModelUEResourceCounterEnum.counterCollection);//devide(totalCollectionCollection,          resourceCount);
		double averageDiscussionCollection                         = getThreshold(SSEntityE.disc,SSModelUEResourceCounterEnum.counterCollection);//devide(totalDiscussionCollection,          resourceCount);
		double averageDigitalResourceCollection                    = getThreshold(SSEntityE.entity,SSModelUEResourceCounterEnum.counterCollection);//devide(totalDigitalResourceCollection,     resourceCount);
		double averagePersonCollection                             = getThreshold(SSEntityE.user,SSModelUEResourceCounterEnum.counterCollection);//devide(totalPersonCollection,              resourceCount);
		
		double averageCollectionAssociatePerson                    = getThreshold(SSEntityE.coll,SSModelUEResourceCounterEnum.counterAssociatePerson); //devide(totalCollectionAssociatePerson,     resourceCount);
		double averageDiscussionAssociatePerson                    = getThreshold(SSEntityE.disc,SSModelUEResourceCounterEnum.counterAssociatePerson); //devide(totalDiscussionAssociatePerson,     resourceCount);
		double averageDigitalResourceAssociatePerson               = getThreshold(SSEntityE.entity,SSModelUEResourceCounterEnum.counterAssociatePerson); //devide(totalDigitalResourceAssociatePerson,resourceCount);
		
		double averageCollectionChange                             = getThreshold(SSEntityE.coll,SSModelUEResourceCounterEnum.counterChange); //devide(totalCollectionChange,              resourceCount);
		double averageDiscussionChange                             = getThreshold(SSEntityE.disc,SSModelUEResourceCounterEnum.counterChange); //devide(totalDiscussionChange,              resourceCount);
		double averageDigitalResourceChange                        = getThreshold(SSEntityE.entity,SSModelUEResourceCounterEnum.counterChange); //devide(totalDigitalResourceChange,         resourceCount);
		double averagePersonAddAndDelete                           = getThreshold(SSEntityE.user,SSModelUEResourceCounterEnum.counterChange); //devide(totalPersonAddAndDelete,            resourceCount);
		
		double averageCollectionChangePerson                       = getThreshold(SSEntityE.coll,SSModelUEResourceCounterEnum.counterChangePerson); //devide(totalCollectionChangePerson,        resourceCount);
		double averageDiscussionChangePerson                       = getThreshold(SSEntityE.disc,SSModelUEResourceCounterEnum.counterChangePerson); //devide(totalDiscussionChangePerson,        resourceCount);
		double averageDigitalResourceChangePerson                  = getThreshold(SSEntityE.entity,SSModelUEResourceCounterEnum.counterChangePerson); //devide(totalDigitalResourceChangePerson,   resourceCount);
		
		double averageCollectionTag                                = getThreshold(SSEntityE.coll,SSModelUEResourceCounterEnum.counterTag); //devide(totalCollectionTag,                 resourceCount);
		double averageDiscussionTag                                = getThreshold(SSEntityE.disc,SSModelUEResourceCounterEnum.counterTag); //devide(totalDiscussionTag,                 resourceCount);		
		double averageDigitalResourceTag                           = getThreshold(SSEntityE.entity,SSModelUEResourceCounterEnum.counterTag); //devide(totalDigitalResourceTag,            resourceCount);
		double averagePersonTag                                    = getThreshold(SSEntityE.user,SSModelUEResourceCounterEnum.counterTag); //devide(totalPersonTag,                     resourceCount);
		
		double averageCollectionViewPerson                         = getThreshold(SSEntityE.coll,SSModelUEResourceCounterEnum.counterViewPerson); //devide(totalCollectionViewPerson,          resourceCount);
		double averageDiscussionViewPerson                         = getThreshold(SSEntityE.disc,SSModelUEResourceCounterEnum.counterViewPerson); //devide(totalDiscussionViewPerson,          resourceCount);
		double averageDigitalResourceViewPerson                    = getThreshold(SSEntityE.entity,SSModelUEResourceCounterEnum.counterViewPerson); //devide(totalDigitalResourceViewPerson,     resourceCount);
		double averagePersonViewPerson                             = getThreshold(SSEntityE.user,SSModelUEResourceCounterEnum.counterViewPerson); //devide(totalPersonViewPerson,              resourceCount);
		
		double averageCollectionView                               = getThreshold(SSEntityE.coll,SSModelUEResourceCounterEnum.counterView); //devide(totalCollectionView,                resourceCount);
		double averageDiscussionView                               = getThreshold(SSEntityE.disc,SSModelUEResourceCounterEnum.counterView); //devide(totalDiscussionView,                resourceCount);
		double averageDigitalResourceView                          = getThreshold(SSEntityE.entity,SSModelUEResourceCounterEnum.counterView); //devide(totalDigitalResourceView,           resourceCount);
		double averagePersonView                                   = getThreshold(SSEntityE.user,SSModelUEResourceCounterEnum.counterView); //devide(totalPersonView,                    resourceCount);
		
//		double averageCollectionSearchResult                       = getThreshold(SSEntityEnum.coll,SSModelUEResourceCounterEnum.counterSearchResult); //devide(totalCollectionSearchResult,        resourceCount);
//		double averageDiscussionSearchResult                       = getThreshold(SSEntityEnum.disc,SSModelUEResourceCounterEnum.counterSearchResult); //devide(totalDiscussionSearchResult,        resourceCount);
//		double averageDigitalResourceSearchResult                  = getThreshold(SSEntityEnum.entity,SSModelUEResourceCounterEnum.counterSearchResult); //devide(totalDigitalResourceSearchResult,   resourceCount);
//		double averagePersonSearchResult                           = getThreshold(SSEntityEnum.user,SSModelUEResourceCounterEnum.counterSearchResult); //devide(totalPersonSearchResult,            resourceCount);
		
		double averageCollectionSelectFromOther                    = getThreshold(SSEntityE.coll,SSModelUEResourceCounterEnum.counterSelectFromOther); //devide(totalCollectionSelectFromOther,     resourceCount);
		double averageDiscussionSelectFromOther                    = getThreshold(SSEntityE.disc,SSModelUEResourceCounterEnum.counterSelectFromOther); //devide(totalDiscussionSelectFromOther,     resourceCount);
		double averageDigitalResourceSelectFromOther               = getThreshold(SSEntityE.entity,SSModelUEResourceCounterEnum.counterSelectFromOther); //devide(totalDigitalResourceSelectFromOther,resourceCount);
		double averagePersonSelectFromOther                        = getThreshold(SSEntityE.user,SSModelUEResourceCounterEnum.counterSelectFromOther); //devide(totalPersonSelectFromOther,         resourceCount);
		
		double averageCollectionCollaborateCollection              = getThreshold(SSEntityE.coll,SSModelUEResourceCounterEnum.counterCollaborateCollection); //devide(totalCollectionCollaborateCollection,         resourceCount);
		double averageDiscussionCollaborateCollection              = getThreshold(SSEntityE.disc,SSModelUEResourceCounterEnum.counterCollaborateCollection); //devide(totalDiscussionCollaborateCollection,         resourceCount);
		double averageDigitalResourceCollaborateCollection         = getThreshold(SSEntityE.entity,SSModelUEResourceCounterEnum.counterCollaborateCollection); //devide(totalDigitalResourceCollaborateCollection,    resourceCount);
		
		double averageCollectionCollaborateDiscussion              = getThreshold(SSEntityE.coll,SSModelUEResourceCounterEnum.counterCollaborateDiscussion); //devide(totalCollectionCollaborateDiscussion,         resourceCount);
		double averageDiscussionCollaborateDiscussion              = getThreshold(SSEntityE.disc,SSModelUEResourceCounterEnum.counterCollaborateDiscussion); //devide(totalDiscussionCollaborateDiscussion,         resourceCount);
		double averageDigitalResourceCollaborateDiscussion         = getThreshold(SSEntityE.entity,SSModelUEResourceCounterEnum.counterCollaborateDiscussion); //devide(totalDigitalResourceCollaborateDiscussion,    resourceCount);
		
		double averageCollectionAware                              = getThreshold(SSEntityE.coll,SSModelUEResourceCounterEnum.counterAware); //devide(totalCollectionAware,               resourceCount);		
		double averageDiscussionAware                              = getThreshold(SSEntityE.disc,SSModelUEResourceCounterEnum.counterAware); //devide(totalDiscussionAware,               resourceCount);
		double averageDigitalResourceAware                         = getThreshold(SSEntityE.entity,SSModelUEResourceCounterEnum.counterAware); //devide(totalDigitalResourceAware,          resourceCount);
		double averagePersonAware                                  = getThreshold(SSEntityE.user,SSModelUEResourceCounterEnum.counterAware); //devide(totalPersonAware,                   resourceCount);
		
		double averageCollectionRecommend                          = getThreshold(SSEntityE.coll,SSModelUEResourceCounterEnum.counterRecommend); //devide(totalCollectionRecommend,           resourceCount);
		double averageDiscussionRecommend                          = getThreshold(SSEntityE.disc,SSModelUEResourceCounterEnum.counterRecommend); //devide(totalDiscussionRecommend,           resourceCount);
		double averageDigitalResourceRecommend                     = getThreshold(SSEntityE.entity,SSModelUEResourceCounterEnum.counterRecommend); //devide(totalDigitalResourceRecommend,      resourceCount);
		double averagePersonRecommend                              = getThreshold(SSEntityE.user,SSModelUEResourceCounterEnum.counterRecommend); // devide(totalPersonRecommend,               resourceCount);
		
		double averageCollectionAddAndDelete                       = getThreshold(SSEntityE.coll,SSModelUEResourceCounterEnum.counterAddAndDelete); //devide(totalCollectionAddAndDelete,        resourceCount);
		double averageDiscussionAddAndDelete                       = getThreshold(SSEntityE.disc,SSModelUEResourceCounterEnum.counterAddAndDelete); //devide(totalDiscussionAddAndDelete,        resourceCount);
		double averageDigitalResourceAddAndDelete                  = getThreshold(SSEntityE.entity,SSModelUEResourceCounterEnum.counterAddAndDelete); //devide(totalDigitalResourceAddAndDelete,   resourceCount);
		
		double averageCollectionShareCommunity                     = getThreshold(SSEntityE.coll,SSModelUEResourceCounterEnum.counterShareCommunity); //devide(totalCollectionShareCommunity,      resourceCount);
		double averageDiscussionShareCommunity                     = getThreshold(SSEntityE.disc,SSModelUEResourceCounterEnum.counterShareCommunity); //devide(totalDiscussionShareCommunity,      resourceCount);
		double averageDigitalResourceShareCommunity                = getThreshold(SSEntityE.entity,SSModelUEResourceCounterEnum.counterShareCommunity); //devide(totalDigitalResourceShareCommunity, resourceCount);
		double averagePersonShareCommunity                         = getThreshold(SSEntityE.user,SSModelUEResourceCounterEnum.counterShareCommunity); //devide(totalPersonShareCommunity,          resourceCount);
		
		double averageCollectionCollectionSimilar                  = getThreshold(SSEntityE.coll,SSModelUEResourceCounterEnum.counterCollectSimilar); //devide(totalCollectionCollectionSimilar,      resourceCount);
		double averageDiscussionCollectionSimilar                  = getThreshold(SSEntityE.disc,SSModelUEResourceCounterEnum.counterCollectSimilar); //devide(totalDiscussionCollectionSimilar,      resourceCount);
		double averageDigitalResourceCollectionSimilar             = getThreshold(SSEntityE.entity,SSModelUEResourceCounterEnum.counterCollectSimilar); //devide(totalDigitalResourceCollectionSimilar, resourceCount);	
		double averagePersonCollectionSimilar                      = getThreshold(SSEntityE.user,SSModelUEResourceCounterEnum.counterCollectSimilar); //devide(totalPersonCollectionSimilar,          resourceCount);
		
		double averageCollectionPresentAudience                    = getThreshold(SSEntityE.coll,SSModelUEResourceCounterEnum.counterPresentAudience); //devide(totalCollectionPresentAudience,        resourceCount);
		double averageDiscussionPresentAudience                    = getThreshold(SSEntityE.disc,SSModelUEResourceCounterEnum.counterPresentAudience); //devide(totalDiscussionPresentAudience,        resourceCount);
		double averageDigitalResourcePresentAudience               = getThreshold(SSEntityE.entity,SSModelUEResourceCounterEnum.counterPresentAudience); //devide(totalDigitalResourcePresentAudience,   resourceCount);	

		double averageCollectionAssess                             = getThreshold(SSEntityE.coll,SSModelUEResourceCounterEnum.counterAssess); //devide(totalCollectionAssess,                 resourceCount);
		double averageDiscussionAssess                             = getThreshold(SSEntityE.disc,SSModelUEResourceCounterEnum.counterAssess); //devide(totalDiscussionAssess,                 resourceCount);
		double averageDigitalResourceAssess                        = getThreshold(SSEntityE.entity,SSModelUEResourceCounterEnum.counterAssess); //devide(totalDigitalResourceAssess,            resourceCount);
		double averagePersonAssess                                 = getThreshold(SSEntityE.user,SSModelUEResourceCounterEnum.counterAssess); // devide(totalPersonAssess,                     resourceCount);
		
		double averageCollectionGotRated                           = getThreshold(SSEntityE.coll,SSModelUEResourceCounterEnum.counterGotRated); //devide(totalCollectionGotRated,               resourceCount);
		double averageDiscussionGotRated                           = getThreshold(SSEntityE.disc,SSModelUEResourceCounterEnum.counterGotRated); //devide(totalDiscussionGotRated,               resourceCount);
		double averageDigitalResourceGotRated                      = getThreshold(SSEntityE.entity,SSModelUEResourceCounterEnum.counterGotRated); //devide(totalDigitalResourceGotRated,          resourceCount);
		double averagePersonGotRated                               = getThreshold(SSEntityE.user,SSModelUEResourceCounterEnum.counterGotRated); // devide(totalPersonGotRated,                   resourceCount);
		
		double averageCollectionReferredBy                         = getThreshold(SSEntityE.coll,SSModelUEResourceCounterEnum.counterReferredBy); //devide(totalCollectionReferredBy,                 resourceCount);
		double averageDiscussionReferredBy                         = getThreshold(SSEntityE.disc,SSModelUEResourceCounterEnum.counterReferredBy); //devide(totalDiscussionReferredBy,                 resourceCount);
		double averageDigitalResourceReferredBy                    = getThreshold(SSEntityE.entity,SSModelUEResourceCounterEnum.counterReferredBy); //devide(totalDigitalResourceReferredBy,            resourceCount);
		double averagePersonReferredBy                             = getThreshold(SSEntityE.user,SSModelUEResourceCounterEnum.counterReferredBy); //devide(totalPersonReferredBy,                     resourceCount);

		double averageCollectionActivePeriod                       = getThreshold(SSEntityE.coll,SSModelUEResourceCounterEnum.counterActivePeriod); //devide(totalCollectionActivePeriod,               resourceCount);
		double averageDiscussionActivePeriod                       = getThreshold(SSEntityE.disc,SSModelUEResourceCounterEnum.counterActivePeriod); //devide(totalDiscussionActivePeriod,               resourceCount);
		double averageDigitalResourceActivePeriod                  = getThreshold(SSEntityE.entity,SSModelUEResourceCounterEnum.counterActivePeriod); //devide(totalDigitalResourceActivePeriod,          resourceCount);
		double averagePersonActivePeriod                           = getThreshold(SSEntityE.user,SSModelUEResourceCounterEnum.counterActivePeriod); //devide(totalPersonActivePeriod,                   resourceCount);
		
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

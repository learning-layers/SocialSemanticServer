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
 package at.kc.tugraz.ss.serv.modeling.ue.datatypes;

import at.tugraz.sss.serv.SSObjU;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSEntityE;
import at.kc.tugraz.ss.serv.modeling.ue.datatypes.enums.SSModelUERelationEnum;
import at.kc.tugraz.ss.serv.modeling.ue.datatypes.enums.SSModelUEResourceCounterEnum;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntityA;
import at.kc.tugraz.ss.service.userevent.datatypes.*;
import java.util.*;

public class SSModelUEEntity extends SSEntityA{

  /*
	 * resource and user/user common properties
	 */
	public final SSUri                               entity;
	public final List<SSUri>                         relatedPersons            = new ArrayList<>();
	public final List<SSUE>                          events                    = new ArrayList<>();
	public final List<String>                        mIs                       = new ArrayList<>();
	public final Map<String,Integer>                 mIDayCounts               = new HashMap<>();
	public final List<SSUri>                         editors                   = new ArrayList<>();
  public SSEntityE                                 type;
	
	/*
	 * user/user related properties
	 */
	public final  List<SSUE>                         personsEvents                      = new ArrayList<>();
	public final  List<SSUri>                        personsDiscussions                 = new ArrayList<>();
	public final  List<SSUri>                        personsRelatedResources            = new ArrayList<>();
	public final  List<SSUri>                        personsRelatedPersons              = new ArrayList<>();
	public final  List<SSModelUETopicScore>          personsTopicScores                 = new ArrayList<>();
  public final  Map<String,Integer>                personsTopicFrequencies            = new HashMap<>();
  public final  Map<String,List<SSUE>>             personsCreatedTopicEvents          = new HashMap<>();
  public final  Map<String,List<SSUE>>             personsUsingTopicEvents            = new HashMap<>();
  public final  Map<String,Double>                 personsRawTopicFrequencies         = new HashMap<>();
  public SSUri                                     personsRecentArtifact              = null;
	public String                                    personsRecentTopic                 = null;
  
  /*
	 * mi calculation helpers
	 */
  public final Map<String, Integer>                counters                           = new HashMap<>();
	public final List<SSUri>                         personsChanged                     = new ArrayList<>();
	public final List<SSUri>                         personsView                        = new ArrayList<>();
	
	/**
 	 * mis
	 */
	public boolean mIChangeRecentAfterIntensiveChangeNot      = false;

	public boolean mIStandardNot                              = true;
	public boolean mIStandard                                 = false;
//	public boolean mIStandardNumberOf                         = false; [not available: being standard is a status without count]
	
//	public boolean mICreateJustNot                            = true;  [not available: not just created doesn't make sense]
	public boolean mICreateJust                               = false;
//	public boolean mICreateJustNumberOf                       = false; [not available: just created is a status without count]
	
	public boolean mIUseWideNot                               = true;
	public boolean mIUseWide                                  = false;
//	public boolean mIUseWideNumberOf                          = false; [not available: used wide is a status without count]
	
	public boolean mIAwareNot                                 = false;
	public boolean mIAware                                    = false;
//	public boolean mIAwareNumberOf                            = false; [not available: being aware is a status without count]
	
//	public boolean mIAssociatePersonNot                       = true;  [not available: resource has to be associated to at least one user]
//	public boolean mIAssociatePerson                          = false; [not available: it's always the case here]
	public boolean mIAssociatePersonNumberOf                  = false;
	
	public boolean mIDiscussNot                               = true;
	public boolean mIDiscuss                                  = false;
//	public boolean mIDiscussNumberOf                          = false; [not available: a resource can only be in one disc]
	
	public boolean mIChangeNot                                = true;
	public boolean mIChange                                   = false;
	public boolean miChangeNumberOf                           = false;
	
	public boolean mIChangeByAddOrDeleteNot                   = true;
	public boolean mIChangeByAddOrDelete                      = false;
	public boolean mIChangeByAddOrDeleteNumberOf              = false;

	public boolean mITagNot                                   = true;
	public boolean mITag                                      = false;
	public boolean mITagNumberOf                              = false;
	
	public boolean mIViewNot                                  = true;
	public boolean mIView                                     = false;
	public boolean mIViewNumberOf                             = false;

	public boolean mICollaborateCollectionNot                 = true;
	public boolean mICollaborateCollection                    = false;
	public boolean mICollaborateCollectionNumberOf            = false;
	
	public boolean mICollaborateDiscussionNot                 = true;
	public boolean mICollaborateDiscussion                    = false;
	public boolean mICollaborateDiscussionNumberOf            = false;
	
	public boolean mIShareCommunityNot                        = true;
	public boolean mIShareCommunity                           = false;
	public boolean mIShareCommunityNumberOf                   = false;
	
	public boolean mISelectFromOtherNot                       = true;
	public boolean mISelectFromOther                          = false;
	public boolean mISelectFromOtherNumberOf                  = false;
	
	public boolean mICollectNot                               = true;
	public boolean mICollect                                  = false;
	public boolean mICollectNumberOf                          = false;

	public boolean mIRateHighNot                              = true;
	public boolean mIRateHigh                                 = false;
	public boolean mIRateHighNumberOf                         = false;

	public boolean mIRecommendNot                             = true;
	public boolean mIRecommend                                = false;
	public boolean mIRecommendNumberOf                        = false;

	public boolean mIOrganizeCollectionNot                    = true;
	public boolean mIOrganizeCollection                       = false;
	public boolean mIOrganizeCollectionNumberOf               = false;
	
//	public boolean mISearchResultsNot                         = true;
//	public boolean mISearchResults                            = false;
//	public boolean mISearchResultsNumberOf                    = false;
	
	public boolean mIChangePersonNot                          = true;
	public boolean mIChangePerson                             = false;
	public boolean mIChangePersonNumberOf                     = false;

	public boolean mIViewPersonNot                            = true;
	public boolean mIViewPerson                               = false;
	public boolean mIViewPersonNumberOf                       = false;
	
	public boolean mICollectSimilarNot                        = true;
	public boolean mICollectSimilar                           = false;
	public boolean mICollectSimilarNumberOf                   = false;
	
	public boolean mIPresentAudienceNot                       = true;
	public boolean mIPresentAudience                          = false;
	public boolean mIPresentAudienceNumberOf                  = false;
	
	public boolean mIAssesNot                                 = true;
	public boolean mIAsses                                    = false;
	public boolean mIAssesNumberOf                            = false;

	public boolean mIIsEditorNot                              = true;
	public boolean mIIsEditor                                 = false;
	public boolean mIIsEditorNumberOf                         = false;
	
	public boolean mIContributeDiscussionNot                  = true;
	public boolean mIContributeDiscussion                     = false;
	public boolean mIContributeDiscussionNumberOf             = false;
	
	public boolean mIGotRatedNot                              = true;
	public boolean mIGotRated                                 = false;
	public boolean mIGotRatedNumberOf                         = false;

	public boolean mIParticipatedNot                          = true;
	public boolean mIParticipated                             = false;
	public boolean mIParticipatedNumberOf                     = false;
	
	public boolean mIReferredByNot                            = true;
	public boolean mIReferredBy                               = false;
	public boolean mIReferredByNumberOf                       = false;
	
	public boolean mIActivePeriodNot                          = true;
//	public boolean mIActivePeriod                             = false; [not available: each resource by events is automatically active in some period]
	public boolean mIActivePeriodNumberOf                     = false;
	
	public boolean mIMadeOutOfOthersNot                       = true;
	public boolean mIMadeOutOfOthers                          = false;
	public boolean mIMadeOutOfOthersNumberOf                  = false;
	
	public boolean mIChangeReputablePersonNot                 = true;
	public boolean mIChangeReputablePerson                    = false;
//	public boolean mIChangeReputablePersonNumberOf            = false; [not available: cause currently there exists not threshold for]
  
	public SSModelUEEntity(final SSUri uri) throws Exception{
		
    super(uri);
    
		this.entity = uri;
		
		resetCalculatedProperties();
	}
	
	private void resetCalculatedProperties(){
    
		/*
		 * resource and user/user common properties
		 */
		relatedPersons.clear();
		events.clear();
		mIs.clear();
		editors.clear();

		/*
		 * user/user related properties
		 */
		personsRecentArtifact               = null;
		personsRecentTopic                  = null;
		personsEvents.clear();
		personsDiscussions.clear();
		personsRelatedResources.clear();
		personsRelatedPersons.clear();
		personsTopicScores.clear();
		personsTopicFrequencies.clear();
		personsCreatedTopicEvents.clear();
		personsUsingTopicEvents.clear();
		personsRawTopicFrequencies.clear();

	  /*
		 * mi calculation helpers
		 */
		personsChanged.clear();
		personsView.clear();

		resetCounters();
	}
  
  @Override
  public Object jsonLDDesc(){
    return "dtheiler";
  }
	
	public List<SSModelUERelation> getRelationsForType(
			SSModelUERelationEnum relationType) {
	
		switch (relationType) {
		
		case relatedPersons:
			
			return getRelatedPersonsRelations(
					relationType);

		case relatedResources:
			
			return getRelatedResourcesForRelations(
					relationType);
			
		case editors:
			
			return getEditorsRelations(
					relationType);
			
		case maturingIndicators:
			
			return getMaturingIndicatorsForRelations(
					relationType);
			
		case topics:
			
			return getTopicsForRelations(
					relationType);
			
		case createdTopics:
			
			return getCreatedTopicsForRelations(
					relationType);
			
		case usedTopics:
			
			return getUsedTopicsForRelations(
					relationType);
		
		case highTopicScores:
			
			return getTopicScoresForRelations(
					relationType,
					1); 
			
		case middleTopicScores:
			
			return getTopicScoresForRelations(
					relationType,
					2);
			
		case lowTopicScores:
			
			return getTopicScoresForRelations(
					relationType,
					3);
			
		case ownedDiscussions:
			
			return getOwnedDiscussionsForRelation(
					relationType);
			
		default:
			return new ArrayList<>();
		}
	}
	
	private List<SSModelUERelation> getTopicScoresForRelations(
			SSModelUERelationEnum relationType,
			int               score) {

		List<SSModelUERelation> result = new ArrayList<>();
		
		for(SSModelUETopicScore topicScore : personsTopicScores){
			
			if(topicScore.level == score){
				
				result.add(
          new SSModelUERelation(
          SSStrU.toStr(entity),
          null,
          topicScore.topic,
          null,
          relationType));
      }
		}

		return result;
	}

	private List<SSModelUERelation> getOwnedDiscussionsForRelation(
			SSModelUERelationEnum relationType) {
		
		List<SSModelUERelation> result = new ArrayList<>();
		
		for(SSUri discussion : personsDiscussions){
			
			result.add(
					new SSModelUERelation(
							SSStrU.toStr(entity),
							null,
							discussion.toString(),
							null,
							relationType));
		}

		return result;
	}

	private List<SSModelUERelation> getCreatedTopicsForRelations(
			SSModelUERelationEnum relationType) {
		
		List<SSModelUERelation>  result = new ArrayList<>();
		
		for(String createdTopic : personsCreatedTopicEvents.keySet()){
			
			result.add(
					new SSModelUERelation(
							SSStrU.toStr(entity),
							null,
							createdTopic,
							null,
							relationType));
		}

		return result;
	}
	
	private List<SSModelUERelation> getUsedTopicsForRelations(
			SSModelUERelationEnum relationType) {
		
		List<SSModelUERelation>  result = new ArrayList<>();
		
		for(String usedTopic : personsUsingTopicEvents.keySet()){
			
			result.add(
					new SSModelUERelation(
							SSStrU.toStr(entity),
							null,
							usedTopic,
							null,
							relationType));
		}

		return result;
	}

	private List<SSModelUERelation> getTopicsForRelations(
			SSModelUERelationEnum relationType){
		
		List<SSModelUERelation>  result = new ArrayList<>();
		
		for(SSModelUETopicScore topicScore : personsTopicScores){
			
			result.add(
					new SSModelUERelation(
							SSStrU.toStr(entity),
							null,
							topicScore.topic,
							null,
							relationType));
		}

		return result;
	}
	
	private List<SSModelUERelation> getRelatedResourcesForRelations(
			SSModelUERelationEnum relationType) {

		List<SSModelUERelation>  result = new ArrayList<>();
		
		for(SSUri relatedDigitalResource : personsRelatedResources){
			
			result.add(
					new SSModelUERelation(
							SSStrU.toStr(entity),
							null,
							relatedDigitalResource.toString(),
							null,
							relationType));
		}

		return result;
	}

	private List<SSModelUERelation> getMaturingIndicatorsForRelations(
			SSModelUERelationEnum relationType){

		List<SSModelUERelation>  result = new ArrayList<>();

		for(String maturingIndicator : mIs){
			
			result.add(
					new SSModelUERelation(
							SSStrU.toStr(entity),
							null,
							maturingIndicator,
							null,
							relationType));
		}

		return result;
	}
	
	
	private List<SSModelUERelation> getEditorsRelations(
			SSModelUERelationEnum relationType){
		
		List<SSModelUERelation>  result = new ArrayList<>();
		
		for(SSUri editor : editors){

			result.add(
					new SSModelUERelation(
							SSStrU.toStr(entity),
							null,
							editor.toString(),
							null,
							relationType));
		}
		
		return result;
	}
	
	private List<SSModelUERelation> getRelatedPersonsRelations(
			SSModelUERelationEnum relationType){
		
		List<SSModelUERelation>  result              = new ArrayList<>();
		List<String>         alreadyAddedPersons = new ArrayList<>();
		
		for(SSUri relatedPerson : personsRelatedPersons){

      if(relatedPerson != null){
        
        result.add(
            new SSModelUERelation(
                SSStrU.toStr(entity),
                null,
                relatedPerson.toString(),
                null,
                relationType));

        alreadyAddedPersons.add(relatedPerson.toString());
      }
		}

    for(SSUri relatedPerson : relatedPersons){
      
      if(
        SSObjU.isNull(relatedPerson) ||
        alreadyAddedPersons.contains(relatedPerson.toString())){
        continue;
      }
      
      result.add(
        new SSModelUERelation(
          SSStrU.toStr(entity),
          null,
          relatedPerson.toString(),
          null,
          relationType));
    }
		
		return result;
	}

	private void resetCounters(){
		
		counters.clear();

		counters.put(SSModelUEResourceCounterEnum.counterAssociatePerson.toString(),                     0);
		counters.put(SSModelUEResourceCounterEnum.counterChangePerson.toString(),                        0);
		counters.put(SSModelUEResourceCounterEnum.counterViewPerson.toString(),                          0);
		counters.put(SSModelUEResourceCounterEnum.counterCollection.toString(),                          0);
//		counters.put(SSModelUEResourceCounterEnum.counterSearchResult.toString(),                        0);
		counters.put(SSModelUEResourceCounterEnum.counterAddAndDelete.toString(),                        0);
		counters.put(SSModelUEResourceCounterEnum.counterChange.toString(),                              0);
		counters.put(SSModelUEResourceCounterEnum.counterOrganizeCollection.toString(),                  0);
		counters.put(SSModelUEResourceCounterEnum.counterCollaborateCollectionInitial.toString(),        0);
		counters.put(SSModelUEResourceCounterEnum.counterCollaborateDiscussionInitial.toString(),        0);
		counters.put(SSModelUEResourceCounterEnum.counterCollaborateCollection.toString(),               0);
		counters.put(SSModelUEResourceCounterEnum.counterCollaborateDiscussion.toString(),               0);
		counters.put(SSModelUEResourceCounterEnum.counterEvent.toString(),                               0);
		counters.put(SSModelUEResourceCounterEnum.counterAware.toString(),                               0);
		counters.put(SSModelUEResourceCounterEnum.counterRecommend.toString(),                           0);
		counters.put(SSModelUEResourceCounterEnum.counterDiscussionAbout.toString(),                     0);
		counters.put(SSModelUEResourceCounterEnum.counterRateHigh.toString(),                            0);
		counters.put(SSModelUEResourceCounterEnum.counterGotRated.toString(),                            0);
		counters.put(SSModelUEResourceCounterEnum.counterShareCommunity.toString(),                      0);
		counters.put(SSModelUEResourceCounterEnum.counterSelectFromOther.toString(),                     0);
		counters.put(SSModelUEResourceCounterEnum.counterTag.toString(),                                 0);
		counters.put(SSModelUEResourceCounterEnum.counterView.toString(),                                0);
		counters.put(SSModelUEResourceCounterEnum.counterCollectSimilar.toString(),                      0);
		counters.put(SSModelUEResourceCounterEnum.counterPresentAudience.toString(),                     0);
		counters.put(SSModelUEResourceCounterEnum.counterAssess.toString(),                              0);
		counters.put(SSModelUEResourceCounterEnum.counterIsEditor.toString(),                            0);
		counters.put(SSModelUEResourceCounterEnum.counterContributedDiscussion.toString(),               0);
		counters.put(SSModelUEResourceCounterEnum.counterParticipated.toString(),                        0);
		counters.put(SSModelUEResourceCounterEnum.counterReferredBy.toString(),                          0);
		counters.put(SSModelUEResourceCounterEnum.counterActivePeriod.toString(),                        0);
		counters.put(SSModelUEResourceCounterEnum.counterMadeOutOf.toString(),                           0);
		counters.put(SSModelUEResourceCounterEnum.counterPersonsRelatedResources.toString(),             0);
	}
}


//collection: 
//export 
//Focussed Searching


//3 to 4  
//popular choices for relevant artifacts 
//publishing

/*
 * 4 ad-hoc training(instruction) / piloting(implementation)
 */

//distributed to the wider organizational context


/*
 * 5a formal training / institutionalizing
 */

//formal training (instruction)
//preparation of learning material (collections for learning (tags, material, arrangement/order)) 
//export for collections
                                   
//institutionalizing(introduction):
//knowledge is being shared
//used widely


/*
 * 5b standardizing (incorporation)
 */
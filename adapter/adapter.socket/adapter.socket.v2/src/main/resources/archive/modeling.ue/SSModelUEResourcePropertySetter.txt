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

import at.kc.tugraz.ss.serv.modeling.ue.datatypes.SSModelUEEntity;
import at.kc.tugraz.ss.service.userevent.datatypes.SSUE;
import java.util.Map;

public class SSModelUEResourcePropertySetter {

  private final Map<String, SSModelUEEntity>        resources;
	private final SSModelUEResourcePropertySetterHelper propertySetterHelper;

  public SSModelUEResourcePropertySetter(Map<String, SSModelUEEntity> resources){
    
    this.resources            = resources;
    this.propertySetterHelper = new SSModelUEResourcePropertySetterHelper(resources);
  }
  
//	/**
//	 * o sets resource type (according to the stored type in graph or resource's events)
//	 */
//	public void setResourceType(ModelResource resource) {
//
//		
//		resource.
//		
//		if(GM.isTypeNotTheSame(resource.type,ModelResourceType.digitalResource)){
//			return;
//		}
//
//		if(
//				GM.isTypeUserOrPerson(
//						GM.getResourceTypeFromUri(resource.resourceUrl))){
//
//			//			resource.type = 
//			//			propertySetterHelper.getTypeIfUser(resource);
//
//		}else{
//			resource.type = 
//				propertySetterHelper.getTypeIfCollectionDiscussionOrDigitalResource(resource);
//		}
//	}
	
	/**
	 * 0 set/increase/decrease various event counts on the resource <br>
	 * 0 where possible also add values like editors, associated persons, etc. <br> 
	 * 0 counters and properties are needed for calculating thresholds for MI calculation and MI calculation itself <br>
	 */
	public void setResourceIndependentProperties(
			SSModelUEEntity resource) throws Exception{
		
		propertySetterHelper.setCounterEvent         (resource);
		propertySetterHelper.increaseActivePeriod    (resource);
		
		for(SSUE event : resource.events){
			
			propertySetterHelper.increaseCountersChanging                             (resource, event);
			propertySetterHelper.increaseCountersTagging                              (resource, event);
//			propertySetterHelper.increaseCountersCollection                           (resource, event);
//			propertySetterHelper.increaseCountersDiscussionAbout                      (resource, event);
			propertySetterHelper.increaseCountersView                                 (resource, event);
//			propertySetterHelper.increaseCountersSearchResult                         (resource, event);
			propertySetterHelper.increaseCountersOrganizeCollection                   (resource, event);
			propertySetterHelper.increaseCountersCollaborationDiscussionInitial       (resource, event);
			propertySetterHelper.increaseCountersCollaborationCollectionInitial       (resource, event);
			propertySetterHelper.increaseCountersCollaborationDiscussion              (resource, event);
			propertySetterHelper.increaseCountersCollaborationCollection              (resource, event);
			propertySetterHelper.increaseCountersAwareness                            (resource, event);
			propertySetterHelper.increaseCountersRecommendation                       (resource, event);
			propertySetterHelper.increaseCountersEditor                               (resource, event);
			propertySetterHelper.increaseCountersAssociated                           (resource, event);
			propertySetterHelper.increaseCountersAddAndDelete                         (resource, event);
			propertySetterHelper.increaseCountersRateHigh                             (resource, event);
			propertySetterHelper.increaseCountersShareCommunity                       (resource, event);
			propertySetterHelper.increaseCountersSelectFromOther                      (resource, event);
			propertySetterHelper.increaseCountersPresentAudience                      (resource, event);
			propertySetterHelper.increaseCountersAssess                               (resource, event);
			propertySetterHelper.increaseCountersGotRated                             (resource, event);
			
//			propertySetterHelper.decreaseCountersCollection                           (resource, event);
			propertySetterHelper.decreaseCountersInitialCollectionsCollaboration      (resource, event);
		}
	}

	/**
	 * 0 fill out calculation properties for resource which depend on certain characteristics of other resources <br>
	 */
	public void setResourceDependentProperties() throws Exception {
		
		for (SSModelUEEntity resource : resources.values()){
			
			propertySetterHelper.increaseCountersCollectionSimilar     (resource);
			propertySetterHelper.increaseCountersContributedDiscussion (resource);
			propertySetterHelper.increaseCounterParticipated           (resource);

//			propertySetterHelper.setCountersMadeOutOf                  (resource);
			propertySetterHelper.setCountersIsEditor                   (resource);
			propertySetterHelper.setCountersReferredBy                 (resource);
		}
	}
}
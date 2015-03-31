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

import at.kc.tugraz.ss.serv.modeling.ue.datatypes.SSModelUETopicScore;
import at.kc.tugraz.ss.serv.modeling.ue.datatypes.SSModelUEEntity;
import at.kc.tugraz.ss.serv.modeling.ue.datatypes.SSModelUERelation;
import at.kc.tugraz.ss.serv.modeling.ue.api.SSModelUEServerI;
import at.kc.tugraz.ss.serv.modeling.ue.api.SSModelUEClientI;
import at.kc.tugraz.ss.serv.modeling.ue.datatypes.rets.SSModelUEResourceDetailsRet;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.*;
import at.tugraz.sss.serv.SSSocketCon;
import at.tugraz.sss.serv.SSEntityE;
import at.kc.tugraz.ss.serv.modeling.ue.datatypes.pars.SSModelUEEditorsPar;
import at.kc.tugraz.ss.serv.modeling.ue.datatypes.pars.SSModelUEMIsForEntityGetPar;
import at.kc.tugraz.ss.serv.modeling.ue.datatypes.pars.SSModelUEModelRelationsPar;
import at.kc.tugraz.ss.serv.modeling.ue.datatypes.pars.SSModelUERelatedPersonsPar;
import at.kc.tugraz.ss.serv.modeling.ue.datatypes.pars.SSModelUEEntityDetailsPar;
import at.kc.tugraz.ss.serv.modeling.ue.datatypes.pars.SSModelUEEntityRecentPar;
import at.kc.tugraz.ss.serv.modeling.ue.datatypes.pars.SSModelUEEntitiesContributedPar;
import at.kc.tugraz.ss.serv.modeling.ue.datatypes.pars.SSModelUEEntitiesForForMiGetPar;
import at.kc.tugraz.ss.serv.modeling.ue.datatypes.pars.SSModelUETopicRecentPar;
import at.kc.tugraz.ss.serv.modeling.ue.datatypes.pars.SSModelUETopicScoresPar;
import at.kc.tugraz.ss.serv.modeling.ue.utils.SSModelUEU;
import at.tugraz.sss.serv.SSServPar;

import at.kc.tugraz.ss.serv.modeling.ue.datatypes.enums.SSModelUEMIEnum;
import at.kc.tugraz.ss.serv.modeling.ue.datatypes.rets.SSModelUEMIsForEntityGetRet;
import at.kc.tugraz.ss.serv.modeling.ue.datatypes.rets.SSModelUERelatedPersonsRet;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSServImplMiscA;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.tugraz.sss.serv.caller.SSServCallerU;
import at.kc.tugraz.ss.service.userevent.datatypes.SSUE;
import java.util.*;

public class SSModelUEImpl extends SSServImplMiscA implements SSModelUEClientI, SSModelUEServerI{
  
  private final Map<String, SSModelUEEntity> resources;
  
  public SSModelUEImpl(final SSConfA conf, final Map<String, SSModelUEEntity> resources) throws Exception{
    
    super(conf);
    
    this.resources = resources;
  }

  /* SSModelUserEventClientI  */
  
  @Override
  public void modelUEResourceDetails(SSSocketCon sSCon, SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    sSCon.writeRetFullToClient(modelUEResourceDetails(parA));
  }
  
  @Override
  public void modelUERelatedPersons(SSSocketCon sSCon, SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSModelUERelatedPersonsRet.get(modelUERelatedPersons(parA), parA.op));
  }
  
  @Override
  public void modelUEMIsForEntityGet(final SSSocketCon sSCon, final SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);
    
    sSCon.writeRetFullToClient(SSModelUEMIsForEntityGetRet.get(modelUEMIsForEntityGet(parA), parA.op));
  }
  
  /* SSModelUEServerI  */
  
  @Override
  public SSModelUEResourceDetailsRet modelUEResourceDetails(SSServPar parI) throws Exception {
    
    SSModelUEEntityDetailsPar par = new SSModelUEEntityDetailsPar(parI);
    
    return new SSModelUEResourceDetailsRet(
      par.entity,
      modelUERelatedPersons       (new SSModelUERelatedPersonsPar(par)),
      modelUEMIsForEntityGet      (new SSModelUEMIsForEntityGetPar(par)),
      modelUEEditors              (new SSModelUEEditorsPar(par)),
      modelUEResourceRecent       (new SSModelUEEntityRecentPar(par)),
      modelUETopicRecent          (new SSModelUETopicRecentPar(par)),
      modelUEResourcesContributed (new SSModelUEEntitiesContributedPar(par)),
      modelUETopicScores          (new SSModelUETopicScoresPar(par)), 
      par.op);
  }
  
  @Override
  public List<SSUri> modelUERelatedPersons(SSServPar parI) throws Exception {
    
    SSModelUERelatedPersonsPar par = new SSModelUERelatedPersonsPar(parI);
    
    List<SSUri>       result   = new ArrayList<>();
    
    if(SSObjU.isNull(par.entity)){
      return result;
    }
    
    SSModelUEEntity resource = resources.get(par.entity.toString());
    
    if(resource != null){
      
      if(SSEntityE.isUser(resource.type)){
        
        result.addAll(resource.personsRelatedPersons);
        result.addAll(resource.relatedPersons);
      }else{
        result.addAll(resource.relatedPersons);
      }
    }
    
    return result;
  }
  
  @Override
  public List<String> modelUEMIsForEntityGet(final SSServPar parA) throws Exception {
    
    final SSModelUEMIsForEntityGetPar par = new SSModelUEMIsForEntityGetPar(parA);
    
    if(SSObjU.isNull(par.entity, resources.get(par.entity.toString()))){
      return new ArrayList<>();
    }
    
    return resources.get(par.entity.toString()).mIs;
  }
  
  @Override
  public void modelUEUpdate(final SSServPar parA) throws Exception {
    
    final SSModelUEResourcePropertySetter resourcePropertySetter        = new SSModelUEResourcePropertySetter (resources);
    final SSModelUEMIThresholdSetter      thresholdSetter               = new SSModelUEMIThresholdSetter      (resources);
    final SSModelUEMISetter               maturingIndicatorSetter       = new SSModelUEMISetter               ();
    final SSModelUEUESetter               eventSetter                   = new SSModelUEUESetter               (this, resources);
    final List<SSUE>                      sortedEventsSinceLastUpdate;
    final SSModelUEPersonPropertySetter   personPropertySetter;
    
    synchronized(resources){
     
      eventSetter.removeOldEventsFromModel();
      
      sortedEventsSinceLastUpdate = eventSetter.setNewEventsToModel   (SSModelUEU.lastUpdateTime);
      personPropertySetter        = new SSModelUEPersonPropertySetter (this, sortedEventsSinceLastUpdate, resources);

      SSModelUEU.lastUpdateTime = new Date().getTime();

      for (SSModelUEEntity resource : resources.values()){

        try{
          resource.type = SSServCaller.entityGet(resource.entity).type;
        }catch(Exception error){
          resource.type = SSEntityE.entity;
          
          SSServErrReg.reset();
        }

        resourcePropertySetter.setResourceIndependentProperties                 (resource);

        personPropertySetter.setPersonIndependentProperties                     (resource);
      }

      personPropertySetter.setSomething();

      resourcePropertySetter.setResourceDependentProperties();

      thresholdSetter.calculateThresholds();

      for(SSModelUEEntity resource : resources.values()){
        maturingIndicatorSetter.calculateIndependentMI(resource);
      }

      for(SSModelUEEntity resource : resources.values()){

        maturingIndicatorSetter.calculateDependentMI(resource, resources);

        maturingIndicatorSetter.setTextualMI(resource);
      }
    }
    
    //		maturingIndicatorTripleStoreSetter.saveMI();
  }
  
  @Override
  public List<SSUri> modelUEEntitiesForMiGet(final SSServPar parA) throws Exception {
    
    final SSModelUEEntitiesForForMiGetPar par         = new SSModelUEEntitiesForForMiGetPar(parA);
    final List<SSUri>                     entityUris  = new ArrayList<>();
    
    if(
      SSObjU.isNull(par.mi) ||
      !SSModelUEMIEnum.contains(SSStrU.toStr(par.mi))){
      return entityUris;
    }
    
    for(SSModelUEEntity resource : resources.values()){
      
      if(SSServCaller.modelUEMIsForEntityGet(par.user, resource.entity).contains(par.mi.toString())){
        entityUris.add(resource.entity);
      }
    }
    
    return entityUris;
  }
  
  @Override
  public List<String> modelUEResourcesAll(SSServPar parA) throws Exception {
    
    List<String> allResources = new ArrayList<>();
    
    for(String resourceUrl : resources.keySet()){
      
      allResources.add(resourceUrl);
    }
    
    return allResources;
  }
  
  @Override
  public List<SSUri> modelUEEditors(SSServPar parI) throws Exception {
    
    SSModelUEEditorsPar par = new SSModelUEEditorsPar(parI);
    
    if(SSObjU.isNull(par.entity)){
      return new ArrayList<>();
    }
    
    if(!SSObjU.isNull(resources.get(SSStrU.toStr(par.entity)))){
      return resources.get(SSStrU.toStr(par.entity)).editors;
    }else{
      return new ArrayList<>();
    }
  }
  
  @Override
  public SSUri modelUEResourceRecent(SSServPar parI) throws Exception {
    
    SSModelUEEntityRecentPar par = new SSModelUEEntityRecentPar(parI);
    
    if(SSObjU.isNull(par.entity)){
      return null;
    }
    
    SSModelUEEntity resource = resources.get(SSStrU.toStr(par.entity));
    
    if(!SSObjU.isNull(resource)){
      return resource.personsRecentArtifact;
    }
    
    return null;
  }
  
  @Override
  public String modelUETopicRecent(SSServPar parI) throws Exception {
    
    final SSModelUETopicRecentPar par = new SSModelUETopicRecentPar(parI);
    
    if(SSObjU.isNull(par.entity)){
      return null;
    }
    
    SSModelUEEntity resource = resources.get(SSStrU.toStr(par.entity));
    
    if(!SSObjU.isNull(resource)){
      return resource.personsRecentTopic;
    }
    
    return null;
  }
  
  @Override
  public List<SSUri> modelUEResourcesContributed(SSServPar parI) throws Exception {
    
    SSModelUEEntitiesContributedPar par = new SSModelUEEntitiesContributedPar(parI);
    
    List<SSUri>   result    = new ArrayList<>();
    
    if(SSObjU.isNull(par.entity)){
      return result;
    }
    
    SSModelUEEntity resource = resources.get(SSStrU.toStr(par.entity));
    
    if(!SSObjU.isNull(resource)){
      result = resource.personsRelatedResources;
    }
    
    return result;
  }
  
  @Override
  public List<SSModelUETopicScore> modelUETopicScores(SSServPar parI) throws Exception {
    
    SSModelUETopicScoresPar par = new SSModelUETopicScoresPar(parI);
    
    List<SSModelUETopicScore> result    = new ArrayList<>();
    
    if(SSObjU.isNull(par.entity)){
      return result;
    }
    
    SSModelUEEntity resource = resources.get(SSStrU.toStr(par.entity));
    
    if(!SSObjU.isNull(resource)){
      result = resource.personsTopicScores;
    }
    
    return result;
  }
  
  @Override
  public List<SSModelUERelation> modelUEModelRelations(SSServPar parI) throws Exception {
    
    SSModelUEModelRelationsPar par = new SSModelUEModelRelationsPar(parI);
    
    List<SSModelUERelation>  result        = new ArrayList<>();
    SSModelUEEntity        modelResource;
    
    if(SSObjU.isNull(par.entity)){
      return result;
    }
    
    modelResource = resources.get(SSStrU.toStr(par.entity));
    
    if(!SSObjU.isNull(par.entity)){
      result = modelResource.getRelationsForType(par.type);
    }
    
    return fillModelRelationLabels(result);
  }
  
  
  private List<SSModelUERelation> fillModelRelationLabels(
    List<SSModelUERelation>      modelRelations) throws Exception {
    
    int    counter      = 0;
    String subjectLabel = null;
    
    for(SSModelUERelation relation : modelRelations){
      
      if(counter == 0){
        subjectLabel = SSStrU.toStr(SSServCaller.entityGet(SSUri.get(relation.subject)).label);
      }
      
      relation.subjectLabel = subjectLabel;
      relation.objectLabel  = SSStrU.toStr(SSServCaller.entityGet(SSUri.get(relation.object)));      
      
      counter++;
    }
    
    return modelRelations;
  }  
}


//  @Override
//	public  List<SSUri> getOwnedSharedColls(
//    SSUri resourceURI) {
//
//		List<SSUri>       result    = new ArrayList<>();
//
//    if(SSObjectUtils.isNull(resourceURI)){
//      return result;
//    }
//
//		ModelResource     resource  = resources.get(resourceURI.toString());
//
//		if(resource != null){
//
//			for(SSColl coll : resource.personsSharedCollections){
//
//				result.add(coll.uri);
//			}
//		}
//
//		return result;
//	}
//
//  @Override
//	public  List<SSUri> getOwnedFollowedColls(
//			SSUri resourceURI) {
//
//		List<SSUri>       result    = new ArrayList<>();
//
//    if(SSObjectUtils.isNull(resourceURI)){
//      return result;
//    }
//
//		ModelResource     resource  = resources.get(resourceURI.toString());
//
//		if(resource != null){
//
//			for(SSColl collection : resource.personsFollowedCollections){
//
//				result.add(collection.uri);
//			}
//		}
//
//		return result;
//	}
//
//  @Override
//	public  List<SSUri> getContributedDiscs(
//			SSUri resourceURI) {
//
//		List<SSUri>       result    = new ArrayList<>();
//
//    if(SSObjectUtils.isNull(resourceURI)){
//      return result;
//    }
//
//		ModelResource     resource  = resources.get(resourceURI.toString());
//
//		if(resource != null){
//			result = resource.personsDiscussions;
//		}
//
//		return result;
//	}

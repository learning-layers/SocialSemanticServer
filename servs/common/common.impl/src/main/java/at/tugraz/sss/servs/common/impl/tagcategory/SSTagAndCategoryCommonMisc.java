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
package at.tugraz.sss.servs.common.impl.tagcategory;

import at.kc.tugraz.ss.category.datatypes.SSCategory;
import at.kc.tugraz.ss.category.datatypes.SSCategoryFrequ;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityFromTypeAndLabelGetPar;
import at.kc.tugraz.ss.service.tag.datatypes.SSTag;
import at.kc.tugraz.ss.service.tag.datatypes.SSTagFrequ;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSEntityA;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSLabel;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.SSSpaceE;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.util.SSServCallerU;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSTagAndCategoryCommonMisc {
  
  private final SSTagAndCategoryCommonSQL sqlFct;
  private final SSEntityE                 metadataType;
  
  public SSTagAndCategoryCommonMisc(
    final SSDBSQLI         dbSQL,
    final SSEntityE        metadataType) throws Exception{

    this.metadataType = metadataType;
    this.sqlFct       = new SSTagAndCategoryCommonSQL(dbSQL, metadataType);
  }
  
  public List<SSUri> getEntitiesForMetadataIfSpaceSet(
    final SSUri        user,
    final List<String> labels,
    final SSSpaceE     space,
    final SSUri        userToUse) throws Exception{
    
    final List<SSUri> entities = new ArrayList<>();
    SSEntity          metadataEntity;
    
    if(labels.isEmpty()){
      
      entities.addAll(
        sqlFct.getEntities(
          userToUse,
          space,
          null));
    }
    
    //TODO dtheiler: handle loop in db
    for(String label : labels){
    
      metadataEntity =
        ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityFromTypeAndLabelGet(
          new SSEntityFromTypeAndLabelGetPar(
            null,
            null,
            user,
            SSLabel.get(label), //label,
            metadataType, //type,
            false)); //withUserRestriction
      
      if(metadataEntity == null){
        continue;
      }
      
      entities.addAll(
        sqlFct.getEntities(
          userToUse,
          space,
          metadataEntity.id));
    }
    
    SSStrU.distinctWithoutEmptyAndNull2(entities);
    
    return entities;
  }
  
  public List<SSUri> getEntitiesForMetadataIfSpaceNotSet(
    final SSUri        user,
    final SSUri        forUser,
    final List<String> labels)throws Exception{
    
    final List<SSUri> entities = new ArrayList<>();
    SSEntity          metadataEntity;
    
    if(labels.isEmpty()){
      
      entities.addAll(
        sqlFct.getEntities(
          user,
          SSSpaceE.privateSpace, 
          null));
      
      entities.addAll(
        sqlFct.getEntities(
          forUser,
          SSSpaceE.sharedSpace,
          null));
    }
    
    //TODO dtheiler: handle loop in db
    for(String label : labels){
      
      metadataEntity =
        ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityFromTypeAndLabelGet(
          new SSEntityFromTypeAndLabelGetPar(
            null,
            null,
            user,
            SSLabel.get(label), //label,
            metadataType, //type,
            false)); //withUserRestriction
      
      if(metadataEntity == null){
        continue;
      }
      
      entities.addAll(
        sqlFct.getEntities(
          user,
          SSSpaceE.privateSpace,
          metadataEntity.id));
      
      entities.addAll(
        sqlFct.getEntities(
          forUser,
          SSSpaceE.sharedSpace,
          metadataEntity.id));
    }
    
    SSStrU.distinctWithoutEmptyAndNull2(entities);
    
    return entities;
  }
    
  public List<SSEntity> getMetadataIfSpaceNotSet(
    final SSUri        user,
    final SSUri        forUser,
    final List<SSUri>  entities,
    final List<String> labels,
    final List<SSUri>  circles, 
    final Long         startTime) throws Exception{
    
    final List<SSEntity> metadata    = new ArrayList<>();
    SSEntity             metadataEntity;
    
    if(
      entities.isEmpty() &&
      labels.isEmpty() &&
      circles.isEmpty()){
      
      metadata.addAll (sqlFct.getMetadataAsss(forUser, null, SSSpaceE.sharedSpace,  startTime, null));
      metadata.addAll (sqlFct.getMetadataAsss(forUser, null, SSSpaceE.circleSpace,  startTime, null));
      metadata.addAll (sqlFct.getMetadataAsss(user,    null, SSSpaceE.privateSpace, startTime, null));
    }
    
    if(
      entities.isEmpty() &&
      labels.isEmpty()   &&
      !circles.isEmpty()){
      
      metadata.addAll(
        sqlFct.getMetadataAsss(
          SSUri.asListWithoutNullAndEmpty(user),
          null,
          SSSpaceE.privateSpace,
          startTime,
          null,
          circles));
      
      metadata.addAll(
        sqlFct.getMetadataAsss(
          SSUri.asListWithoutNullAndEmpty(forUser),
          null,
          SSSpaceE.sharedSpace,
          startTime,
          null,
          circles));
      
      metadata.addAll(
        sqlFct.getMetadataAsss(
          SSUri.asListWithoutNullAndEmpty(forUser),
          null,
          SSSpaceE.circleSpace,
          startTime,
          null,
          circles));
    }
    
    if(
      entities.isEmpty() &&
      !labels.isEmpty()){
      
      final List<SSUri> metadataURIs    = new ArrayList<>();
      
      for(String label : labels){
        
        metadataEntity =
          ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityFromTypeAndLabelGet(
            new SSEntityFromTypeAndLabelGetPar(
              null,
              null,
              user,
              SSLabel.get(label), //label,
              metadataType, //type,
              false)); //withUserRestriction
        
        if(metadataEntity == null){
          continue;
        }
        
        metadataURIs.add(metadataEntity.id);
      }
      
      metadata.addAll (
        sqlFct.getMetadataAsss(
          SSUri.asListWithoutNullAndEmpty(user),
          null,
          SSSpaceE.privateSpace,
          startTime,
          metadataURIs,
          circles));
      
      metadata.addAll (
        sqlFct.getMetadataAsss(
          SSUri.asListWithoutNullAndEmpty(forUser),
          null,
          SSSpaceE.sharedSpace,
          startTime,
          metadataURIs,
          circles));
      
      metadata.addAll (
        sqlFct.getMetadataAsss(
          SSUri.asListWithoutNullAndEmpty(forUser),
          null,
          SSSpaceE.circleSpace,
          startTime,
          metadataURIs,
          circles));
    }
    
    if(
      !entities.isEmpty() &&
      labels.isEmpty()){
      
      metadata.addAll (
        sqlFct.getMetadataAsss(
          SSUri.asListWithoutNullAndEmpty(forUser), 
          entities, 
          SSSpaceE.sharedSpace,  
          startTime, 
          null,
          circles));
      
      metadata.addAll (
        sqlFct.getMetadataAsss(
          SSUri.asListWithoutNullAndEmpty(forUser), 
          entities, 
          SSSpaceE.circleSpace,  
          startTime, 
          null,
          circles));
      
      metadata.addAll(
        sqlFct.getMetadataAsss(
          SSUri.asListWithoutNullAndEmpty(user),    
          entities, 
          SSSpaceE.privateSpace, 
          startTime, 
          null,
          circles));
    }
    
    if(
      !entities.isEmpty() &&
      !labels.isEmpty()){
      
      for(String label : labels){
        
        metadataEntity =
          ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityFromTypeAndLabelGet(
            new SSEntityFromTypeAndLabelGetPar(
              null,
              null,
              user,
              SSLabel.get(label), //label,
              metadataType, //type,
              false)); //withUserRestriction
        
        if(metadataEntity == null){
          continue;
        }
        
        metadata.addAll (
          sqlFct.getMetadataAsss(
            SSUri.asListWithoutNullAndEmpty(forUser),     
            entities, 
            SSSpaceE.sharedSpace,  
            startTime, 
            SSUri.asListWithoutNullAndEmpty(metadataEntity.id),
            circles));
        
        metadata.addAll (
          sqlFct.getMetadataAsss(
            SSUri.asListWithoutNullAndEmpty(forUser),     
            entities, 
            SSSpaceE.circleSpace,  
            startTime, 
            SSUri.asListWithoutNullAndEmpty(metadataEntity.id), 
            circles));
        
        metadata.addAll (
          sqlFct.getMetadataAsss(
            SSUri.asListWithoutNullAndEmpty(user),
            entities, 
            SSSpaceE.privateSpace, 
            startTime, 
            SSUri.asListWithoutNullAndEmpty(metadataEntity.id), 
            circles));
      }
    }
    
    return metadata;
  }
  
  public List<SSEntity> getMetadataIfSpaceSet(
    final SSUri        userToUse,
    final List<SSUri>  entities,
    final List<String> labels,
    final List<SSUri>  circles, 
    final SSSpaceE     space,
    final Long         startTime) throws Exception{
    
    final List<SSEntity>   metadata   = new ArrayList<>();
    SSEntity               metadataEntity;
    
    if(
      entities.isEmpty() &&
      labels.isEmpty()   &&
      circles.isEmpty()){
      
      metadata.addAll (sqlFct.getMetadataAsss(userToUse, null, space, startTime, null));
    }
    
    if(
      entities.isEmpty() &&
      labels.isEmpty()   &&
      !circles.isEmpty()){
      
      metadata.addAll(
        sqlFct.getMetadataAsss(
          SSUri.asListWithoutNullAndEmpty(userToUse), 
          null, 
          space, 
          startTime,
          null,
          circles));
    }
      
    if(
      entities.isEmpty() &&
      !labels.isEmpty()){
      
      final List<SSUri> metadataURIs    = new ArrayList<>();
        
      for(String label : labels){
        
        metadataEntity =
          ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityFromTypeAndLabelGet(
            new SSEntityFromTypeAndLabelGetPar(
              null,
              null,
              userToUse,
              SSLabel.get(label), //label,
              metadataType, //type,
              false)); //withUserRestriction
        
        if(metadataEntity == null){
          continue;
        }
        
        metadataURIs.add(metadataEntity.id);
      }
      
      metadata.addAll(
        sqlFct.getMetadataAsss(
          SSUri.asListWithoutNullAndEmpty(userToUse), 
          null, 
          space, 
          startTime,
          metadataURIs,
          circles));
    }
    
    if(
      !entities.isEmpty() &&
      labels.isEmpty()){
      
      metadata.addAll(
        sqlFct.getMetadataAsss(
          SSUri.asListWithoutNullAndEmpty(userToUse), 
          entities, 
          space, 
          startTime, 
          null,
          circles));
    }
    
    if(
      !entities.isEmpty() &&
      !labels.isEmpty()){
    
      for(String label : labels){
        
        metadataEntity =
          ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityFromTypeAndLabelGet(
            new SSEntityFromTypeAndLabelGetPar(
              null,
              null,
              userToUse,
              SSLabel.get(label), //label,
              metadataType, //type,
              false)); //withUserRestriction
        
        if(metadataEntity == null){
          continue;
        }
        
        metadata.addAll (
          sqlFct.getMetadataAsss(
            SSUri.asListWithoutNullAndEmpty(userToUse), 
            entities, 
            space, 
            startTime, 
            SSUri.asListWithoutNullAndEmpty(metadataEntity.id),
            circles));
      }
    }
    
    return metadata;
  }
  
  public List<SSEntityA> getMetadataFrequsFromMetadata(
    final List<SSEntity> metadata,
    final SSSpaceE       space) throws Exception{
    
    final Map<String, SSEntityA> metadataFrequs = new HashMap<>();
    
    String metaLabel;
    
    for (SSEntity meta : metadata) {
      
      switch(metadataType){
        
        case tag:{
          
          metaLabel  = SSStrU.toStr(((SSTag)meta).tagLabel);
          
          if(metadataFrequs.containsKey(metaLabel)){
            ((SSTagFrequ) metadataFrequs.get(metaLabel)).frequ += 1;
          }else{
            metadataFrequs.put(metaLabel, SSTagFrequ.get(((SSTag)meta).tagLabel, space, 1));
          }
          
          break;
        }
        
        case category:{
          
          metaLabel  = SSStrU.toStr(((SSCategory) meta).categoryLabel);
          
          if(metadataFrequs.containsKey(metaLabel)){
            ((SSCategoryFrequ) metadataFrequs.get(metaLabel)).frequ += 1;
          }else{
            metadataFrequs.put(metaLabel, SSCategoryFrequ.get(((SSCategory) meta).categoryLabel, space, 1));
          }
          break;
        }
        
        default: throw new UnsupportedOperationException();
      }
    }
      
    return new ArrayList(metadataFrequs.values());
  }
      
  public List<SSEntity> filterMetadataByEntitiesUserCanAccess(
    final List<SSEntity>   metadata, 
    final Boolean          withUserRestriction, 
    final SSUri            user, 
    final SSUri            forUser) throws Exception{
    
    final List<SSEntity> filtered = new ArrayList<>();
    
    //because its supposed that a user can read all entities he attached tags to,
    //but not that he necessarly can read the entities another user tagged
    
    if(
      withUserRestriction &&
      !SSStrU.equals(user, forUser)){
      
      for(SSEntity meta : metadata){
        
        switch(metadataType){
          
          case tag:{
            
            if(!SSServCallerU.canUserRead(user, ((SSTag) meta).entity)){
              continue;
            }
            
            if(((SSTag) meta).circle != null){
              
              if(!SSServCallerU.canUserRead(user, ((SSTag) meta).circle)){
                continue;
              }
            }

            filtered.add(meta);
            break;
          }
          
          case category:{
            
            if(!SSServCallerU.canUserRead(user, ((SSCategory) meta).entity)){
              continue;
            }
            
//            if(((SSTag) meta).circle != null){
//              
//              if(!SSServCallerU.canUserRead(user, ((SSCategory) meta).circle)){
//                continue;
//              }
//            }
            
            filtered.add(meta);
            break;
          }
          
          default:{
            throw new UnsupportedOperationException();
          }
        }
      }
      
      return filtered;
    }
    
    return metadata;
  }
  
  public List<SSUri> filterEntitiesUserCanAccess(
    final List<SSUri> entityURIs, 
    final Boolean     withUserRestriction, 
    final SSUri       user, 
    final SSUri       forUser) throws Exception{
    
    final List<SSUri> filtered     = new ArrayList<>();
    
    //because its supposed that a user can read all entities he attached tags to,
    //but not that he necessarly can read the entities another user tagged
    
    if(
      withUserRestriction &&
      !SSStrU.equals(user,  forUser)){
      
      for(SSUri entityURI : entityURIs){
        
        if(!SSServCallerU.canUserRead(user, entityURI)){
          continue;
        }
          
        filtered.add(entityURI);
      }
      
      return filtered;
    }
    
    return entityURIs;
  }
}

//public static List<SSUri> getEntitiesForTagsIfSpaceSet(
//    final SSTagSQLFct                               sqlFct,
//    final SSTagEntitiesForTagsGetPar            par,
//    final SSUri                                     userToUse) throws Exception{
//    
//    final List<SSUri> entities = new ArrayList<>();
//    SSEntity          tagEntity;
//    SSLabel           slabel;
//    
//    if(par.labels.isEmpty()){
//      
//      entities.addAll(
//        sqlFct.getEntities(
//          userToUse,
//          par.space,
//          null));
//    }
//    
//    //TODO dtheiler: handle loop in db
//    for(SSTagLabel label : par.labels){
//    
//      slabel    = SSLabel.get(SSStrU.toStr(label));
//      tagEntity =
//        ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityFromTypeAndLabelGet(
//          new SSEntityFromTypeAndLabelGetPar(
//            null,
//            null,
//            par.user,
//            slabel, //label,
//            SSEntityE.tag, //type,
//            false)); //withUserRestriction
//      
//      if(tagEntity == null){
//        continue;
//      }
//      
//      entities.addAll(
//        sqlFct.getEntities(
//          userToUse,
//          par.space,
//          tagEntity.id));
//    }
//    
//    SSStrU.distinctWithoutEmptyAndNull2(entities);
//    
//    return entities;
//  }
//  
//  public static List<SSTag> getTagsIfSpaceNotSet(
//    final SSTagSQLFct        sqlFct,
//    final SSTagsGetPar       par) throws Exception{
//    
//    final List<SSTag> tags    = new ArrayList<>();
//    SSLabel           slabel;
//    SSEntity          tagEntity;
//    
//    if(
//      par.entities.isEmpty() &&
//      par.labels.isEmpty() &&
//      par.circles.isEmpty()){
//      
//      tags.addAll (sqlFct.getTagAsss(par.forUser, null, SSSpaceE.sharedSpace,  par.startTime, null));
//      tags.addAll (sqlFct.getTagAsss(par.forUser, null, SSSpaceE.circleSpace,  par.startTime, null));
//      tags.addAll (sqlFct.getTagAsss(par.user,    null, SSSpaceE.privateSpace, par.startTime, null));
//    }
//    
//    if(
//      par.entities.isEmpty() &&
//      par.labels.isEmpty()   &&
//      !par.circles.isEmpty()){
//      
//      tags.addAll(
//        sqlFct.getTagAsss(
//          SSUri.asListWithoutNullAndEmpty(par.user),
//          null,
//          SSSpaceE.privateSpace,
//          par.startTime,
//          null,
//          par.circles));
//      
//      tags.addAll(
//        sqlFct.getTagAsss(
//          SSUri.asListWithoutNullAndEmpty(par.forUser),
//          null,
//          SSSpaceE.sharedSpace,
//          par.startTime,
//          null,
//          par.circles));
//      
//      tags.addAll(
//        sqlFct.getTagAsss(
//          SSUri.asListWithoutNullAndEmpty(par.forUser),
//          null,
//          SSSpaceE.circleSpace,
//          par.startTime,
//          null,
//          par.circles));
//    }
//    
//    if(
//      par.entities.isEmpty() &&
//      !par.labels.isEmpty()){
//      
//      final List<SSUri> tagURIs    = new ArrayList<>();
//      
//      for(SSTagLabel label : par.labels){
//        
//        slabel    = SSLabel.get(SSStrU.toStr(label));
//        tagEntity =
//          ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityFromTypeAndLabelGet(
//          new SSEntityFromTypeAndLabelGetPar(
//            null,
//            null,
//            par.user,
//            slabel, //label,
//            SSEntityE.tag, //type,
//            false)); //withUserRestriction
//        
//        if(tagEntity == null){
//          continue;
//        }
//        
//        tagURIs.add(tagEntity.id);
//      }
//      
//      tags.addAll (
//        sqlFct.getTagAsss(
//          SSUri.asListWithoutNullAndEmpty(par.user),
//          null,
//          SSSpaceE.privateSpace,
//          par.startTime,
//          tagURIs,
//          par.circles));
//      
//      tags.addAll (
//        sqlFct.getTagAsss(
//          SSUri.asListWithoutNullAndEmpty(par.forUser),
//          null,
//          SSSpaceE.sharedSpace,
//          par.startTime,
//          tagURIs,
//          par.circles));
//      
//      tags.addAll (
//        sqlFct.getTagAsss(
//          SSUri.asListWithoutNullAndEmpty(par.forUser),
//          null,
//          SSSpaceE.circleSpace,
//          par.startTime,
//          tagURIs,
//          par.circles));
//    }
//    
//    if(
//      !par.entities.isEmpty() &&
//      par.labels.isEmpty()){
//      
//      tags.addAll (
//        sqlFct.getTagAsss(
//          SSUri.asListWithoutNullAndEmpty(par.forUser), 
//          par.entities, 
//          SSSpaceE.sharedSpace,  
//          par.startTime, 
//          null,
//          par.circles));
//      
//      tags.addAll (
//        sqlFct.getTagAsss(
//          SSUri.asListWithoutNullAndEmpty(par.forUser), 
//          par.entities, 
//          SSSpaceE.circleSpace,  
//          par.startTime, 
//          null,
//          par.circles));
//      
//      tags.addAll(
//        sqlFct.getTagAsss(
//          SSUri.asListWithoutNullAndEmpty(par.user),    
//          par.entities, 
//          SSSpaceE.privateSpace, 
//          par.startTime, 
//          null,
//          par.circles));
//    }
//    
//    if(
//      !par.entities.isEmpty() &&
//      !par.labels.isEmpty()){
//      
//      for(SSTagLabel label : par.labels){
//        
//        slabel    = SSLabel.get(SSStrU.toStr(label));
//        tagEntity =
//          ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityFromTypeAndLabelGet(
//            new SSEntityFromTypeAndLabelGetPar(
//              null,
//              null,
//              par.user,
//              slabel, //label,
//              SSEntityE.tag, //type,
//              false)); //withUserRestriction
//        
//        if(tagEntity == null){
//          continue;
//        }
//        
//        tags.addAll (
//          sqlFct.getTagAsss(
//            SSUri.asListWithoutNullAndEmpty(par.forUser),     
//            par.entities, 
//            SSSpaceE.sharedSpace,  
//            par.startTime, 
//            SSUri.asListWithoutNullAndEmpty(tagEntity.id),
//            par.circles));
//        
//        tags.addAll (
//          sqlFct.getTagAsss(
//            SSUri.asListWithoutNullAndEmpty(par.forUser),     
//            par.entities, 
//            SSSpaceE.circleSpace,  
//            par.startTime, 
//            SSUri.asListWithoutNullAndEmpty(tagEntity.id), 
//            par.circles));
//        
//        tags.addAll (
//          sqlFct.getTagAsss(
//            SSUri.asListWithoutNullAndEmpty(par.user),
//            par.entities, 
//            SSSpaceE.privateSpace, 
//            par.startTime, 
//            SSUri.asListWithoutNullAndEmpty(tagEntity.id), 
//            par.circles));
//      }
//    }
//    
//    return tags;
//  }
//  
//  public static List<SSTag> getTagsIfSpaceSet(
//    final SSTagSQLFct        sqlFct, 
//    final SSTagsGetPar       par,
//    final SSUri              userToUse) throws Exception{
//    
//    final List<SSTag>      tags   = new ArrayList<>();
//    SSLabel                slabel;
//    SSEntity               tagEntity;
//    
//    if(
//      par.entities.isEmpty() &&
//      par.labels.isEmpty()   &&
//      par.circles.isEmpty()){
//      
//      tags.addAll (sqlFct.getTagAsss(userToUse, null, par.space, par.startTime, null));
//    }
//    
//    if(
//      par.entities.isEmpty() &&
//      par.labels.isEmpty()   &&
//      !par.circles.isEmpty()){
//      
//      tags.addAll(
//        sqlFct.getTagAsss(
//          SSUri.asListWithoutNullAndEmpty(userToUse), 
//          null, 
//          par.space, 
//          par.startTime,
//          null,
//          par.circles));
//    }
//      
//    if(
//      par.entities.isEmpty() &&
//      !par.labels.isEmpty()){
//      
//      final List<SSUri> tagURIs    = new ArrayList<>();
//        
//      for(SSTagLabel label : par.labels){
//        
//        slabel    = SSLabel.get(SSStrU.toStr(label));
//        tagEntity =
//          ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityFromTypeAndLabelGet(
//          new SSEntityFromTypeAndLabelGetPar(
//            null,
//            null,
//            par.user,
//            slabel, //label,
//            SSEntityE.tag, //type,
//            false)); //withUserRestriction
//        
//        if(tagEntity == null){
//          continue;
//        }
//        
//        tagURIs.add(tagEntity.id);
//      }
//      
//      tags.addAll(
//        sqlFct.getTagAsss(
//          SSUri.asListWithoutNullAndEmpty(userToUse), 
//          null, 
//          par.space, 
//          par.startTime,
//          tagURIs,
//          par.circles));
//    }
//    
//    if(
//      !par.entities.isEmpty() &&
//      par.labels.isEmpty()){
//      
//      tags.addAll(
//        sqlFct.getTagAsss(
//          SSUri.asListWithoutNullAndEmpty(userToUse), 
//          par.entities, 
//          par.space, 
//          par.startTime, 
//          null,
//          par.circles));
//    }
//    
//    if(
//      !par.entities.isEmpty() &&
//      !par.labels.isEmpty()){
//    
//      for(SSTagLabel label : par.labels){
//        
//        slabel    = SSLabel.get(SSStrU.toStr(label));
//        tagEntity =
//          ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityFromTypeAndLabelGet(
//            new SSEntityFromTypeAndLabelGetPar(
//              null,
//              null,
//              par.user,
//              slabel, //label,
//              SSEntityE.tag, //type,
//              false)); //withUserRestriction
//        
//        if(tagEntity == null){
//          continue;
//        }
//        
//        tags.addAll (
//          sqlFct.getTagAsss(
//            SSUri.asListWithoutNullAndEmpty(userToUse), 
//            par.entities, 
//            par.space, 
//            par.startTime, 
//            SSUri.asListWithoutNullAndEmpty(tagEntity.id),
//            par.circles));
//      }
//    }
//    
//    return tags;
//  }
//  
//  public static List<SSUri> getEntitiesForTagsIfSpaceNotSet(
//    final SSTagSQLFct                    sqlFct,
//    final SSTagEntitiesForTagsGetPar     par) throws Exception{
//    
//    final List<SSUri> entities = new ArrayList<>();
//    SSLabel           slabel;
//    SSEntity          tagEntity;
//    
//    if(par.labels.isEmpty()){
//      
//      entities.addAll(
//        sqlFct.getEntities(
//          par.user,
//          SSSpaceE.privateSpace, 
//          null));
//      
//      entities.addAll(
//        sqlFct.getEntities(
//          par.forUser,
//          SSSpaceE.sharedSpace,
//          null));
//    }
//    
//    //TODO dtheiler: handle loop in db
//    for(SSTagLabel label : par.labels){
//      
//      slabel    = SSLabel.get(SSStrU.toStr(label));
//      tagEntity =
//        ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityFromTypeAndLabelGet(
//          new SSEntityFromTypeAndLabelGetPar(
//            null,
//            null,
//            par.user,
//            slabel, //label,
//            SSEntityE.tag, //type,
//            false)); //withUserRestriction
//      
//      if(tagEntity == null){
//        continue;
//      }
//      
//      entities.addAll(
//        sqlFct.getEntities(
//          par.user,
//          SSSpaceE.privateSpace,
//          tagEntity.id));
//      
//      entities.addAll(
//        sqlFct.getEntities(
//          par.forUser,
//          SSSpaceE.sharedSpace,
//          tagEntity.id));
//    }
//    
//    SSStrU.distinctWithoutEmptyAndNull2(entities);
//    
//    return entities;
//  }

//    final List<SSTagFrequ> outList = new ArrayList<>(counterPerTags.size());
//    
//    for(Map.Entry<String, Integer> entry : counterPerTags.entrySet()){
//      
//      outList.add(
//        SSTagFrequ.get(
//          SSTagLabel.get(entry.getKey()),
//          space,
//          counterPerTags.get(entry.getKey())));
//    }
//  

//  private void saveUETagAdd(SSServPar parA) throws Exception {
//    
//    Map<String, Object> opPars = new HashMap<>();
//    SSTagAddPar par = new SSTagAddPar(parA);
//    
//    opPars.put(SSVarU.shouldCommit, true);
//    opPars.put(SSVarU.user,         par.user);
//    opPars.put(SSVarU.resource,     par.resource);
//    opPars.put(SSVarU.eventType,    SSUEEnum.useTag);
//    opPars.put(SSVarU.content,      SSStrU.toStr(par.tagString));
//    
//    SSServReg.callServServer(new SSServPar(SSServOpE.uEAdd, opPars));
//    
//    opPars = new HashMap<>();
//    opPars.put(SSVarU.shouldCommit, true);
//    opPars.put(SSVarU.user,         par.user);
//    opPars.put(SSVarU.resource,     par.resource);
//    opPars.put(SSVarU.content,      SSStrU.toStr(par.tagString));
//    
//    if(SSSpaceEnum.isShared(par.space)) {
//      opPars.put(SSVarU.eventType,    SSUEEnum.addSharedTag);
//    } else {
//      opPars.put(SSVarU.eventType,    SSUEEnum.addPrivateTag);
//    }
//    
//    SSServReg.callServServer(new SSServPar(SSServOpE.uEAdd, opPars));
//  }
  
//  private void saveUETagDelete(SSServPar parA) throws Exception{
//    
//    Map<String, Object> opPars = new HashMap<>();
//    SSTagAssRemovePar par = new SSTagAssRemovePar(parA);
//    
//    opPars.put(SSVarU.shouldCommit, true);
//    opPars.put(SSVarU.user,         par.user);
//    opPars.put(SSVarU.resource,     par.resource);
//    opPars.put(SSVarU.content,      SSStrU.toStr(par.tagString));
//    
//    if (SSSpaceEnum.isShared(par.space)) {
//      opPars.put(SSVarU.eventType,    SSUEEnum.removeSharedTag);
//    } else {
//      opPars.put(SSVarU.eventType,    SSUEEnum.removePrivateTag);
//    }
//    
//    SSServReg.callServServer(new SSServPar(SSServOpE.uEAdd, opPars));
//  }
//  public static String[] getStringArrayFromList(
//          List    list,
//          boolean deleteNamespace)  {
//
//    String[] outString = new String[list.size()];
//    int      i         = 0;
//    String   namespace = Vocabulary.getInstance().getNamespace(VocNamespace.EMPTY, false).toString();
//    Iterator iterator  = list.iterator();
//    String   tagString = null;
//    while(iterator.hasNext()) {
//      
//      tagString = (String) iterator.next();
//      
//      if (deleteNamespace) {
////        tagString = tagString.replaceAll("http://tug.mature-ip.eu/", strU.strEmpty);
//        tagString = tagString.replaceAll(namespace, strU.strEmpty);
//      }
//
//      outString[i] = tagString;
//
//      System.out.print(outString[i] + "|");
//
//      i++;
//    }
//
//    return outString;
//  }

//  public static Map<String, Long> getCreationTimePerTagFromTags(final List<SSTag> tags) throws Exception{
//    
//    final Map<String, Long> creationTimesPerTag = new HashMap<>();
//    
//    for(SSTag tag : tags){
//      creationTimesPerTag.put(tag.label.toString(), entityCreationTimeGet(tag.uri));
//    }
//    
//    return creationTimesPerTag;
//  }
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
package at.kc.tugraz.ss.category.impl.fct.misc;

import at.tugraz.sss.serv.SSStrU;
import at.kc.tugraz.ss.category.datatypes.par.SSCategoriesGetPar;
import at.kc.tugraz.ss.category.datatypes.SSCategory;
import at.kc.tugraz.ss.category.datatypes.SSCategoryFrequ;
import at.kc.tugraz.ss.category.datatypes.SSCategoryLabel;
import at.kc.tugraz.ss.category.datatypes.par.SSCategoryEntitiesForCategoriesGetPar;
import at.kc.tugraz.ss.category.impl.fct.sql.SSCategorySQLFct;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityGetPar;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSSpaceE;
import at.tugraz.sss.serv.SSLabel;
import at.tugraz.sss.serv.SSServReg;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSCategoryMiscFct {

  public static List<SSUri> getEntitiesForCategoriesIfSpaceNotSet(
    final SSCategorySQLFct                          sqlFct,
    final SSCategoryEntitiesForCategoriesGetPar par) throws Exception{
    
    final List<SSUri> entities = new ArrayList<>();
    SSLabel           slabel;
    SSEntity          categoryEntity;
    
    if(par.labels.isEmpty()){
      
      entities.addAll(
        sqlFct.getEntities(
          par.user,
          SSSpaceE.privateSpace, 
          null));
      
      entities.addAll(
        sqlFct.getEntities(
          par.forUser,
          SSSpaceE.sharedSpace,
          null));
    }
    
    //TODO dtheiler: handle loop in db
    for(SSCategoryLabel label : par.labels){
      
      slabel         = SSLabel.get(SSStrU.toStr(label));
      categoryEntity =
        ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityGet(
          new SSEntityGetPar(
            null,
            null,
            par.user,
            null, //entity
            null, //forUser,
            slabel, //label,
            SSEntityE.category, //type,
            false, //withUserRestriction
            false, //invokeEntityHandlers
            null,  //descPar
            true)); //logErr
            
      if(categoryEntity == null){
        continue;
      }
      
      entities.addAll(
        sqlFct.getEntities(
          par.user,
          SSSpaceE.privateSpace,
          categoryEntity.id)); 
            
      entities.addAll(
        sqlFct.getEntities(
          par.forUser,
          SSSpaceE.sharedSpace,
          categoryEntity.id));
    }
    
    SSStrU.distinctWithoutEmptyAndNull2(entities);
    
    return entities;
  }
  
  public static List<SSUri> getEntitiesForCategoriesIfSpaceSet(
    final SSCategorySQLFct                          sqlFct,
    final SSCategoryEntitiesForCategoriesGetPar par,
    final SSUri                                     userToUse) throws Exception{
    
    final List<SSUri> entities = new ArrayList<>();
    SSLabel           slabel;
    SSEntity          categoryEntity;
    
    if(par.labels.isEmpty()){
      
      entities.addAll(
        sqlFct.getEntities(
          userToUse,
          par.space,
          null));
    }
    
    //TODO dtheiler: handle loop in db
    for(SSCategoryLabel label : par.labels){
    
      slabel         = SSLabel.get(SSStrU.toStr(label));
      categoryEntity =
        ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityGet(
          new SSEntityGetPar(
            null,
            null,
            par.user,
            null, //entity
            null, //forUser,
            slabel, //label,
            SSEntityE.category, //type,
            false, //withUserRestriction
            false, //invokeEntityHandlers
            null,  //descPar
            true)); //logErr
      
      if(categoryEntity == null){
        continue;
      }
      
      entities.addAll(
        sqlFct.getEntities(
          userToUse,
          par.space,
          categoryEntity.id));
    }
    
    SSStrU.distinctWithoutEmptyAndNull2(entities);
    
    return entities;
  }
  
  public static List<SSCategory> getCategoriesIfSpaceNotSet(
    final SSCategorySQLFct       sqlFct,
    final SSCategoriesGetPar par) throws Exception{
    
    final List<SSCategory> categories = new ArrayList<>();
    SSLabel                slabel;
    SSEntity               categoryEntity;
    
    if(par.entities.isEmpty()){
      
      if(par.labels.isEmpty()){
        categories.addAll (sqlFct.getCategoryAsss(par.forUser, null, SSSpaceE.sharedSpace,  par.startTime, null));
        categories.addAll (sqlFct.getCategoryAsss(par.user,    null, SSSpaceE.privateSpace, par.startTime, null));
      }
      
      for(SSCategoryLabel label : par.labels){
        
        slabel         = SSLabel.get(SSStrU.toStr(label));
        categoryEntity =
          ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityGet(
            new SSEntityGetPar(
              null,
              null,
              par.user,
              null, //entity
              null, //forUser,
              slabel, //label,
              SSEntityE.category, //type,
              false, //withUserRestriction
              false, //invokeEntityHandlers
              null,  //descPar
              true)); //logErr
        
        if(categoryEntity == null){
          continue;
        }
        
        categories.addAll (sqlFct.getCategoryAsss(par.forUser, null, SSSpaceE.sharedSpace,  par.startTime, categoryEntity.id));
        categories.addAll (sqlFct.getCategoryAsss(par.user,    null, SSSpaceE.privateSpace, par.startTime, categoryEntity.id));
      }
    }
    
    //TODO dtheiler: handle loops in db
    for(SSUri entity : par.entities){
      
      if(par.labels.isEmpty()){
        categories.addAll (sqlFct.getCategoryAsss(par.forUser, entity, SSSpaceE.sharedSpace,  par.startTime, null));
        categories.addAll (sqlFct.getCategoryAsss(par.user,    entity, SSSpaceE.privateSpace, par.startTime, null));
      }
      
      for(SSCategoryLabel label : par.labels){
        
        slabel         = SSLabel.get(SSStrU.toStr(label));
        categoryEntity =
          ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityGet(
            new SSEntityGetPar(
              null,
              null,
              par.user,
              null, //entity
              null, //forUser,
              slabel, //label,
              SSEntityE.category, //type,
              false, //withUserRestriction
              false, //invokeEntityHandlers
              null,  //descPar
              true)); //logErr
        
        if(categoryEntity == null){
          continue;
        }
        
        categories.addAll (sqlFct.getCategoryAsss(par.forUser,     entity, SSSpaceE.sharedSpace,  par.startTime, categoryEntity.id));
        categories.addAll (sqlFct.getCategoryAsss(par.user,        entity, SSSpaceE.privateSpace, par.startTime, categoryEntity.id));
      }
    }
    
    return categories;
  }
  
  public static List<SSCategory> getCategoriesIfSpaceSet(
    final SSCategorySQLFct       sqlFct, 
    final SSCategoriesGetPar par,
    final SSUri                  userToUse) throws Exception{
    
    final List<SSCategory> categories = new ArrayList<>();
    SSLabel                slabel;
    SSEntity               categoryEntity;
    
    if(par.entities.isEmpty()){
      
      if(par.labels.isEmpty()){
        categories.addAll (sqlFct.getCategoryAsss(userToUse, null, par.space, par.startTime, null));
      }
      
      for(SSCategoryLabel label : par.labels){
        
        slabel         = SSLabel.get(SSStrU.toStr(label));
        categoryEntity =
          ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityGet(
            new SSEntityGetPar(
              null,
              null,
              par.user,
              null, //entity
              null, //forUser,
              slabel, //label,
              SSEntityE.category, //type,
              false, //withUserRestriction
              false, //invokeEntityHandlers
              null,  //descPar
              true)); //logErr
        
        if(categoryEntity == null){
          continue;
        }
        
        categories.addAll (sqlFct.getCategoryAsss(userToUse, null, par.space, par.startTime, categoryEntity.id));
      }
    }
    
    //TODO dtheiler: handle loops in db
    for(SSUri entity : par.entities){
      
      if(par.labels.isEmpty()){
        categories.addAll (sqlFct.getCategoryAsss(userToUse, entity, par.space, par.startTime, null));
      }
      
      for(SSCategoryLabel label : par.labels){
        
        slabel         = SSLabel.get(SSStrU.toStr(label));
        categoryEntity =
          ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityGet(
            new SSEntityGetPar(
              null,
              null,
              par.user,
              null, //entity
              null, //forUser,
              slabel, //label,
              SSEntityE.category, //type,
              false, //withUserRestriction
              false, //invokeEntityHandlers
              null,  //descPar
              true)); //logErr
        
        if(categoryEntity == null){
          continue;
        }
        
        categories.addAll (sqlFct.getCategoryAsss(userToUse, entity, par.space, par.startTime, categoryEntity.id));
      }
    }
    
    return categories;
  }
  
  public static List<SSCategoryFrequ> getCategoryFrequsFromCategories(
    final List<SSCategory> categories,
    final SSSpaceE         space) throws Exception{
    
    final Map<String, Integer> counterPerCategories = new HashMap<>();
    
    String categoryLabel;
    
    for (SSCategory category : categories) {
      
      categoryLabel = SSStrU.toStr(category.label);
      
      if(counterPerCategories.containsKey(categoryLabel)){
        counterPerCategories.put(categoryLabel, counterPerCategories.get(categoryLabel) + 1);
      } else {
        counterPerCategories.put(categoryLabel, 1);
      }
    }
    
    final List<SSCategoryFrequ> outList = new ArrayList<>(counterPerCategories.size());
    
    for(String key : counterPerCategories.keySet()){
      
      outList.add(
        SSCategoryFrequ.get(
          SSCategoryLabel.get(key),
          space,
          counterPerCategories.get(key)));
    }
    
    return outList;
  }
}

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
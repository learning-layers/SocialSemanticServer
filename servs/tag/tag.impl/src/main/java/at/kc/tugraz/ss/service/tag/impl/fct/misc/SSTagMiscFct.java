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
package at.kc.tugraz.ss.service.tag.impl.fct.misc;

import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSSpaceE;
import at.tugraz.sss.serv.SSLabel;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.kc.tugraz.ss.service.tag.datatypes.SSTagLabel;
import at.kc.tugraz.ss.service.tag.datatypes.SSTag;
import at.kc.tugraz.ss.service.tag.datatypes.SSTagFrequ;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagUserEntitiesForTagsGetPar;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagsUserGetPar;
import at.kc.tugraz.ss.service.tag.impl.fct.sql.SSTagSQLFct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSTagMiscFct {

  public static List<SSUri> getEntitiesForTagsIfSpaceSet(
    final SSTagSQLFct                               sqlFct,
    final SSTagUserEntitiesForTagsGetPar            par,
    final SSUri                                     userToUse) throws Exception{
    
    final List<SSUri> entities = new ArrayList<>();
    SSLabel           slabel;
    
    if(par.labels.isEmpty()){
      
      entities.addAll(
        sqlFct.getEntities(
          userToUse,
          par.space,
          null));
    }
    
    //TODO dtheiler: handle loop in db
    for(SSTagLabel label : par.labels){
    
      slabel = SSLabel.get(SSStrU.toStr(label));
      
      if(!SSServCaller.entityExists(SSEntityE.tag, slabel)){
        continue;
      }
      
      entities.addAll(
        sqlFct.getEntities(
          userToUse,
          par.space,
          SSServCaller.entityGet(
            SSEntityE.tag,
            slabel).id));
    }
    
    SSStrU.distinctWithoutEmptyAndNull2(entities);
    
    return entities;
  }
  
  public static List<SSTag> getTagsIfSpaceNotSet(
    final SSTagSQLFct            sqlFct,
    final SSTagsUserGetPar       par) throws Exception{
    
    final List<SSTag> categories = new ArrayList<>();
    SSLabel slabel;
    SSUri   categoryURI;
    
    if(par.entities.isEmpty()){
      
      if(par.labels.isEmpty()){
        categories.addAll (sqlFct.getTagAsss(par.forUser, null, SSSpaceE.sharedSpace,  par.startTime, null));
        categories.addAll (sqlFct.getTagAsss(par.user,    null, SSSpaceE.privateSpace, par.startTime, null));
      }
      
      for(SSTagLabel label : par.labels){
        
        slabel = SSLabel.get(SSStrU.toStr(label));
        
        if(!SSServCaller.entityExists(SSEntityE.tag, slabel)){
          continue;
        }
        
        categoryURI = 
          SSServCaller.entityGet(
            SSEntityE.tag,
            slabel).id;
          
        categories.addAll (sqlFct.getTagAsss(par.forUser, null, SSSpaceE.sharedSpace,  par.startTime, categoryURI));
        categories.addAll (sqlFct.getTagAsss(par.user,    null, SSSpaceE.privateSpace, par.startTime, categoryURI));
      }
    }
      
    if(par.labels.isEmpty()){
      categories.addAll (sqlFct.getTagAsss(SSUri.asListWithoutNullAndEmpty(par.forUser), par.entities, SSSpaceE.sharedSpace,  par.startTime, null));
      categories.addAll (sqlFct.getTagAsss(SSUri.asListWithoutNullAndEmpty(par.user),    par.entities, SSSpaceE.privateSpace, par.startTime, null));
    }
    
    //TODO dtheiler: handle loops in db
    for(SSTagLabel label : par.labels){
      
      slabel = SSLabel.get(SSStrU.toStr(label));
      
      if(!SSServCaller.entityExists(SSEntityE.tag, slabel)){
        continue;
      }
      
      categoryURI =
        SSServCaller.entityGet(
          SSEntityE.tag,
          slabel).id;
      
      categories.addAll (sqlFct.getTagAsss(SSUri.asListWithoutNullAndEmpty(par.forUser),     par.entities, SSSpaceE.sharedSpace,  par.startTime, SSUri.asListWithoutNullAndEmpty(categoryURI)));
      categories.addAll (sqlFct.getTagAsss(SSUri.asListWithoutNullAndEmpty(par.user),        par.entities, SSSpaceE.privateSpace, par.startTime, SSUri.asListWithoutNullAndEmpty(categoryURI)));
    }
    
    return categories;
  }
  
  public static List<SSTag> getTagsIfSpaceSet(
    final SSTagSQLFct            sqlFct, 
    final SSTagsUserGetPar       par,
    final SSUri                  userToUse) throws Exception{
    
    final List<SSTag>      categories = new ArrayList<>();
    SSLabel                slabel;
    SSUri                  categoryURI;
    
    if(par.entities.isEmpty()){
      
      if(par.labels.isEmpty()){
        categories.addAll (sqlFct.getTagAsss(userToUse, null, par.space, par.startTime, null));
      }
      
      for(SSTagLabel label : par.labels){
        
        slabel = SSLabel.get(SSStrU.toStr(label));
        
        if(!SSServCaller.entityExists(SSEntityE.tag, slabel)){
          continue;
        }
        
        categoryURI = 
          SSServCaller.entityGet(
            SSEntityE.tag,
            slabel).id;
        
        categories.addAll (sqlFct.getTagAsss(userToUse, null, par.space, par.startTime, categoryURI));
      }
    }
    
    if(par.labels.isEmpty()){
      categories.addAll (sqlFct.getTagAsss(SSUri.asListWithoutNullAndEmpty(userToUse), par.entities, par.space, par.startTime, null));
    }

    //TODO dtheiler: handle loops in db
    for(SSTagLabel label : par.labels){
      
      slabel = SSLabel.get(SSStrU.toStr(label));
      
      if(!SSServCaller.entityExists(SSEntityE.tag, slabel)){
        continue;
      }
      
      categoryURI =
        SSServCaller.entityGet(
          SSEntityE.tag,
          slabel).id;
      
      categories.addAll (sqlFct.getTagAsss(SSUri.asListWithoutNullAndEmpty(userToUse), par.entities, par.space, par.startTime, SSUri.asListWithoutNullAndEmpty(categoryURI)));
    }
    
    return categories;
  }
  
  public static List<SSUri> getEntitiesForTagsIfSpaceNotSet(
    final SSTagSQLFct                    sqlFct,
    final SSTagUserEntitiesForTagsGetPar par) throws Exception{
    
    final List<SSUri> entities = new ArrayList<>();
    SSLabel slabel;
    
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
    for(SSTagLabel label : par.labels){
      
      slabel = SSLabel.get(SSStrU.toStr(label));
      
      if(!SSServCaller.entityExists(SSEntityE.tag, slabel)){
        continue;
      }
      
      entities.addAll(
        sqlFct.getEntities(
          par.user,
          SSSpaceE.privateSpace,
          SSServCaller.entityGet(
            SSEntityE.tag,
            slabel).id));
      
      entities.addAll(
        sqlFct.getEntities(
          par.forUser,
          SSSpaceE.sharedSpace,
          SSServCaller.entityGet(
            SSEntityE.tag,
            slabel).id));
    }
    
    SSStrU.distinctWithoutEmptyAndNull2(entities);
    
    return entities;
  }
  
  public static List<SSTagFrequ> getTagFrequsFromTags(
    final List<SSTag> tags,
    final SSSpaceE    space) throws Exception{
    
    final Map<String, SSTagFrequ> tagFrequs = new HashMap<>();
    
    String tagLabel;
    
    for (SSTag tag : tags) {
      
      tagLabel = SSStrU.toStr(tag.tagLabel);
      
      if(tagFrequs.containsKey(tagLabel)){
        tagFrequs.get(tagLabel).frequ += 1;
      }else{
        tagFrequs.put(tagLabel, SSTagFrequ.get(tag.tagLabel, space, 1));
      }
    }
    
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
    return new ArrayList(tagFrequs.values());
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
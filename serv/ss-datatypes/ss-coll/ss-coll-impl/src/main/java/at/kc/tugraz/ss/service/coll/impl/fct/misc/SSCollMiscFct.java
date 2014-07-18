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
package at.kc.tugraz.ss.service.coll.impl.fct.misc;

import at.kc.tugraz.socialserver.utils.SSObjU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityA;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.service.coll.datatypes.SSColl;
import at.kc.tugraz.ss.service.coll.datatypes.SSCollEntry;
import at.kc.tugraz.ss.service.coll.impl.fct.sql.SSCollSQLFct;
import java.util.ArrayList;
import java.util.List;

public class SSCollMiscFct{

  public static Boolean ownsUserASubColl(
    final SSCollSQLFct sqlFct, 
    final SSUri        userUri,
    final SSUri        collUri) throws Exception{
    
    final List<String> subCollUris = new ArrayList<>();
    
    getAllChildCollURIs(sqlFct, collUri.toString(), collUri.toString(), subCollUris);
    
    for(String subCollUri : subCollUris){
      
      if(sqlFct.ownsUserColl(userUri, SSUri.get(subCollUri))){
        return true;
      }
    }
    
    return false;
  }
  
  public static void getAllChildCollURIs(
    final SSCollSQLFct sqlFct,
    final String       startCollUri, 
    final String       currentCollUri, 
    final List<String> subCollUris) throws Exception{
    
    for(String directSubCollUri : sqlFct.getDirectChildCollURIs(currentCollUri)){
      getAllChildCollURIs(sqlFct, startCollUri, directSubCollUri, subCollUris);
    }
    
    if(startCollUri.equals(currentCollUri)){
      return;
    }
    
    if(!subCollUris.contains(currentCollUri)){
      subCollUris.add(currentCollUri);
    }
  }
   
  public static SSColl getCollWithEntriesWithCircleTypes(
    final SSCollSQLFct sqlFct,
    final SSUri userUri,
    final SSUri collUri) throws Exception{

    SSCollEntry collEntry;
    
    try{

      final SSColl coll
        = sqlFct.getCollWithEntries(
          collUri,
          SSServCaller.entityUserEntityCircleTypesGet(userUri, collUri));

      for(SSEntityA entry : coll.entries){
        
        collEntry = (SSCollEntry) entry;
        
        collEntry.circleTypes.clear();
        collEntry.circleTypes.addAll(SSServCaller.entityUserEntityCircleTypesGet(userUri, collEntry.id));
      }

      return coll;

    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  public static void addCollAndSubCollsWithEntriesToCircle(
    final SSCollSQLFct sqlFct,
    final SSUri userUri,
    final SSColl startColl,
    final SSUri circleUri) throws Exception{

    try{
      SSServCaller.entityEntitiesToCircleAdd(
        userUri,
        circleUri,
        getCollSubCollAndEntryURIs(sqlFct, startColl),
        false);

    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public static List<SSUri> getCollSubCollAndEntryURIs(
    final SSCollSQLFct sqlFct,
    final SSColl       startColl) throws Exception{

    SSCollEntry collEntry;
    
    try{

      final List<String> subCollUris          = new ArrayList<>();
      final List<SSUri>  collAndCollEntryUris = new ArrayList<>();

      //add coll and coll direct entry uris
      collAndCollEntryUris.add(startColl.id);

      for(SSEntityA entry : startColl.entries){
        collAndCollEntryUris.add(((SSCollEntry)entry).id);
      }

      //add all coll sub coll und entry uris
      getAllChildCollURIs(sqlFct, SSStrU.toStr(startColl.id), SSStrU.toStr(startColl.id), subCollUris);

      for(String subCollUri : subCollUris){

        if(!SSStrU.contains(collAndCollEntryUris, SSUri.get(subCollUri))){
          collAndCollEntryUris.add(SSUri.get(subCollUri));
        }

        for(SSEntityA entry : sqlFct.getCollWithEntries(SSUri.get(subCollUri), new ArrayList<>()).entries){

          collEntry = (SSCollEntry) entry;
            
          if(!SSStrU.contains(collAndCollEntryUris, collEntry.id)){
            collAndCollEntryUris.add(collEntry.id);
          }
        }
      }

      return collAndCollEntryUris;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public static SSUri getDirectParentCollURIForUser(
    final SSCollSQLFct sqlFct, 
    final SSUri        userUri, 
    final SSUri        childColl) throws Exception{
    
    try{
      
      if(sqlFct.isCollRoot(childColl)){
        return childColl;
      }
      
      for(String parentCollUri : sqlFct.getDirectParentCollURIs(childColl.toString())){
        
        if(sqlFct.ownsUserColl(userUri, SSUri.get(parentCollUri))){
          return SSUri.get(parentCollUri);
        }
      }
      
      throw new Exception("user doesnt own coll");
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
//  public static void getAllParentCollURIs(
//    final SSCollSQLFct sqlFct, 
//    final String       startCollUri, 
//    final String       currentCollUri, 
//    final List<String> parentCollUris) throws Exception{
//    
//    for(String directParentCollUri : sqlFct.getDirectParentCollURIs(currentCollUri)){
//      getAllParentCollURIs(sqlFct, startCollUri, directParentCollUri, parentCollUris);
//    }
//    
//    if(startCollUri.equals(currentCollUri)){
//      return;
//    }
//    
//    if(!parentCollUris.contains(currentCollUri)){
//      parentCollUris.add(currentCollUri);
//    }
//  }

  public static void shareCollWithUser(
    final SSCollSQLFct sqlFct,
    final SSUri userUri,
    final SSUri userUriToShareWith,
    final SSUri rootCollUri,
    final SSUri collUri,
    final SSUri circleUri) throws Exception{

    try{

      if(SSObjU.isNull(sqlFct, userUri, userUriToShareWith, rootCollUri, collUri, circleUri)){
        throw new Exception("pars null");
      }
      
      sqlFct.addCollToColl(
        userUriToShareWith,
        rootCollUri,
        collUri,
        false,
        true);

      addCollAndSubCollsWithEntriesToCircle(
        sqlFct,
        userUri,
        sqlFct.getCollWithEntries(
          collUri,
          new ArrayList<>()),
        circleUri);

      SSServCaller.entityUsersToCircleAdd(
        userUri,
        circleUri,
        sqlFct.getCollUserURIs(collUri),
        false);

    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public static void shareEntityWithUser(
    final SSCollSQLFct sqlFct, 
    final SSUri        sharedWithMeFilesCollUri,
    final SSUri        entityUri) throws Exception{
    
    try{
      
      if(SSObjU.isNull(sqlFct, sharedWithMeFilesCollUri, entityUri)){
        throw new Exception("pars null");
      }
      
      sqlFct.addCollEntry(sharedWithMeFilesCollUri, entityUri);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}

//  public static void addCollAndSubEntitiesToCircle(
//    final SSCollSQLFct sqlFct,
//    final SSUri        userUri,
//    final SSColl       coll,
//    final SSUri        circleUri) throws Exception{
//
//    try{
//
//      final List<String> subCollUris = new ArrayList<>();
//      SSColl             subColl;
//
//      SSServCaller.entityEntitiesToCircleAdd(
//        userUri,
//        circleUri,
//        coll.uri,
//        false);
//      
//      for(SSCollEntry collEntry : coll.entries){
//        
//        SSServCaller.entityEntitiesToCircleAdd(
//          userUri,
//          circleUri,
//          collEntry.uri,
//          false);
//      }
//      
//      sqlFct.getAllChildCollURIs(SSStrU.toStr(coll.uri), SSStrU.toStr(coll.uri), subCollUris);
//      
//      for(String subCollUri : subCollUris){
//        
//        subColl = sqlFct.getCollWithEntries(SSUri.get(subCollUri), new ArrayList<SSEntityCircleTypeE>());
//        
//        for(SSCollEntry collEntry : subColl.entries){
//          
//          SSServCaller.entityEntitiesToCircleAdd(
//            userUri,
//            circleUri,
//            collEntry.uri,
//            false);
//        }
//      }
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//    }
//  }

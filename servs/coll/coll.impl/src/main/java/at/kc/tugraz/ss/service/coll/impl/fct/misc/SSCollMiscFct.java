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

import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.*;
import at.kc.tugraz.ss.service.coll.datatypes.SSColl;
import at.kc.tugraz.ss.service.coll.impl.fct.sql.SSCollSQLFct;
import at.tugraz.sss.serv.datatype.SSEntity;
import at.tugraz.sss.serv.datatype.SSErr;
import at.tugraz.sss.serv.datatype.enums.SSErrE;
import at.tugraz.sss.serv.datatype.par.*;
import at.tugraz.sss.serv.reg.SSServErrReg;
import java.util.ArrayList;
import java.util.List;

public class SSCollMiscFct{

  public static boolean ownsUserASubColl(
    final SSServPar servPar,
    final SSCollSQLFct sqlFct, 
    final SSUri        userUri,
    final SSUri        collUri) throws SSErr{
    
    final List<SSUri> subCollUris = new ArrayList<>();
    
    getAllChildCollURIs(servPar, sqlFct, collUri, collUri, subCollUris);
    
    for(SSUri subCollUri : subCollUris){
      
      if(sqlFct.ownsUserColl(servPar, userUri, subCollUri)){
        return true;
      }
    }
    
    return false;
  }
  
  public static void getAllChildCollURIs(
    final SSServPar servPar,
    final SSCollSQLFct sqlFct,
    final SSUri        startCollUri, 
    final SSUri        currentCollUri, 
    final List<SSUri>  subCollUris) throws SSErr{
    
    for(SSUri directSubCollUri : sqlFct.getDirectChildCollURIs(servPar, currentCollUri)){
      getAllChildCollURIs(servPar, sqlFct, startCollUri, directSubCollUri, subCollUris);
    }
    
    if(SSStrU.equals(startCollUri, currentCollUri)){
      return;
    }
    
    if(!SSStrU.contains(subCollUris, currentCollUri)){
      subCollUris.add(currentCollUri);
    }
  }
   
  public static List<SSUri> getCollSubCollAndEntryURIs(
    final SSServPar servPar,
    final SSCollSQLFct sqlFct,
    final SSColl       startColl) throws SSErr{

    try{

      final List<SSUri>  subCollUris          = new ArrayList<>();
      final List<SSUri>  collAndCollEntryUris = new ArrayList<>();

      //add coll and coll direct entry uris
      collAndCollEntryUris.add(startColl.id);

      collAndCollEntryUris.addAll(SSUri.getDistinctNotNullFromEntities(startColl.entries));

      //add all coll sub coll und entry uris
      getAllChildCollURIs(servPar, sqlFct, startColl.id, startColl.id, subCollUris);

      for(SSUri subCollUri : subCollUris){

        if(!SSStrU.contains(collAndCollEntryUris, subCollUri)){
          collAndCollEntryUris.add(subCollUri);
        }

        for(SSEntity collEntry : sqlFct.getCollWithEntries(servPar, subCollUri).entries){

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
    final SSServPar servPar,
    final SSCollSQLFct sqlFct, 
    final SSUri        userUri, 
    final SSUri        childColl) throws SSErr{
    
    try{
      
      if(sqlFct.isCollRoot(servPar, childColl)){
        return childColl;
      }
      
      for(SSUri parentCollUri : sqlFct.getDirectParentCollURIs(servPar, childColl)){
        
        if(sqlFct.ownsUserColl(servPar, userUri, parentCollUri)){
          return parentCollUri;
        }
      }
      
      throw SSErr.get(SSErrE.userDoesntOwnColl);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
//  public static void getAllParentCollURIs(
//    final SSCollSQLFct sqlFct, 
//    final String       startCollUri, 
//    final String       currentCollUri, 
//    final List<String> parentCollUris) throws SSErr{
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
}

//  public static void addCollAndSubEntitiesToCircle(
//    final SSCollSQLFct sqlFct,
//    final SSUri        userUri,
//    final SSColl       coll,
//    final SSUri        circleUri) throws SSErr{
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

//public static SSColl getCollWithEntriesWithCircleTypes(
//    final SSCollSQLFct sqlFct,
//    final SSUri userUri,
//    final SSUri collUri) throws SSErr{
//
//    SSCollEntry collEntry;
//    
//    try{
//      
//      final SSColl coll =
//        sqlFct.getCollWithEntries(
//          collUri,
//          ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).circleTypesGet(
//            new SSCircleTypesGetPar(
//              null,
//              null,
//              userUri,
//              userUri,
//              collUri,
//              false)));
//      
//      for(Object entry : coll.entries){
//        
//        collEntry = (SSCollEntry) entry;
//        
//        collEntry.circleTypes.clear();
//        collEntry.circleTypes.addAll(
//          ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).circleTypesGet(
//            new SSCircleTypesGetPar(
//              null,
//              null,
//              userUri,
//              userUri,
//              collEntry.id,
//              false)));
//      }
//
//      return coll;
//
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }
//  }
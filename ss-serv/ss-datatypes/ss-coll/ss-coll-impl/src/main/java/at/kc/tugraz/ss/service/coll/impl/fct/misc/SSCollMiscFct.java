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

import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSEntityCircleTypeE;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.service.coll.datatypes.SSColl;
import at.kc.tugraz.ss.service.coll.datatypes.SSCollEntry;
import at.kc.tugraz.ss.service.coll.impl.fct.sql.SSCollSQLFct;
import java.util.ArrayList;
import java.util.List;

public class SSCollMiscFct{
  
  public static SSColl getCollWithEntriesWithCircleTypes(
    final SSCollSQLFct sqlFct,
    final SSUri        userUri,
    final SSUri        collUri) throws Exception{
    
    try{
      
      final SSColl coll =
        sqlFct.getCollWithEntries(
          collUri,
          SSServCaller.entityUserEntityCircleTypesGet(userUri, collUri));
      
      for(SSCollEntry entry : coll.entries){
        
        if(sqlFct.isColl(entry.uri)){ //coll entry is a coll itself
          entry.circleTypes.clear();
          entry.circleTypes.addAll(SSServCaller.entityUserEntityCircleTypesGet(userUri, entry.uri));
        }
      }
      
      return coll;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public static void addCollAndSubCollsWithEntriesToCircle(
    final SSCollSQLFct               sqlFct,
    final SSUri                      userUri,
    final SSColl                     startColl,
    final SSUri                      circleUri) throws Exception{
    
    try{
      final List<String>  subCollUris          = new ArrayList<String>();
      final List<SSUri>   collAndCollEntryUris = new ArrayList<SSUri>();
      
      //add coll and coll direct entry uris
      collAndCollEntryUris.add(startColl.uri);
      
      for(SSCollEntry collEntry : startColl.entries){
        collAndCollEntryUris.add(collEntry.uri);
      }
      
      //add all coll sub coll und entry uris
      sqlFct.getAllChildCollURIs(SSUri.toStr(startColl.uri), SSUri.toStr(startColl.uri), subCollUris);
      
      for(String subCollUri : subCollUris){
        
        if(!SSUri.contains(collAndCollEntryUris, SSUri.get(subCollUri))){
          collAndCollEntryUris.add(SSUri.get(subCollUri));
        }
        
        for(SSCollEntry collEntry : sqlFct.getCollWithEntries(SSUri.get(subCollUri), new ArrayList<SSEntityCircleTypeE>()).entries){
          
          if(!SSUri.contains(collAndCollEntryUris, collEntry.uri)){
            collAndCollEntryUris.add(collEntry.uri);
          }
        }
      }
      
      SSServCaller.entityEntitiesToCircleAdd(
        userUri,
        circleUri,
        collAndCollEntryUris,
        false);
      
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
//      final List<String> subCollUris = new ArrayList<String>();
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
//      sqlFct.getAllChildCollURIs(SSUri.toStr(coll.uri), SSUri.toStr(coll.uri), subCollUris);
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

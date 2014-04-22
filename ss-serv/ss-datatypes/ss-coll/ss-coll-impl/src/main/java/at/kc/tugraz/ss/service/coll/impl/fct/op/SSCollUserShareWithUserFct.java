/**
 * Copyright 2013 Graz University of Technology - KTI (Knowledge Technologies Institute)
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
package at.kc.tugraz.ss.service.coll.impl.fct.op;

import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.service.coll.datatypes.SSCollEntry;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserShareWithUserPar;
import at.kc.tugraz.ss.service.coll.impl.fct.sql.SSCollSQLFct;
import java.util.ArrayList;
import java.util.List;

public class SSCollUserShareWithUserFct{

  public static void addCollToRootColl(
    final SSCollSQLFct               sqlFct, 
    final SSCollUserShareWithUserPar par, 
    final SSUri                      rootCollUri) throws Exception{
    
    try{
      
      sqlFct.addCollToUserColl(
        par.userUriToShareWith,
        rootCollUri,
        par.collUri,
        false,
        true);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public static void addSubCollsWithEntriesToSharedCircle(
    final SSCollSQLFct               sqlFct, 
    final SSCollUserShareWithUserPar par) throws Exception{
    
    try{
      final List<String>  subCollUris          = new ArrayList<String>();
      final List<SSUri>   collAndCollEntryUris = new ArrayList<SSUri>();
      
      sqlFct.getAllChildCollURIs(SSUri.toStr(par.collUri), SSUri.toStr(par.collUri), subCollUris);
      
      for(String subCollUri : subCollUris){
        
        if(!SSUri.contains(collAndCollEntryUris, SSUri.get(subCollUri))){
          collAndCollEntryUris.add(SSUri.get(subCollUri));
        }
        
        for(SSCollEntry collEntry : SSServCaller.collUserWithEntries(par.user, SSUri.get(subCollUri)).entries){
          
          if(!SSUri.contains(collAndCollEntryUris, collEntry.uri)){
            collAndCollEntryUris.add(collEntry.uri);
          }
        }
      }
      
      SSServCaller.entityUserEntitiesToCircleAdd(
        par.user,
        par.collCircleUri,
        collAndCollEntryUris,
        false);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public static void addCollUsersToSharedCircle(
    final SSCollSQLFct               sqlFct, 
    final SSCollUserShareWithUserPar par) throws Exception{
    
    try{
      SSServCaller.entityUserUsersToCircleAdd(
          par.user,
          par.collCircleUri,
          sqlFct.getCollUsers(par.collUri),
          false);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}
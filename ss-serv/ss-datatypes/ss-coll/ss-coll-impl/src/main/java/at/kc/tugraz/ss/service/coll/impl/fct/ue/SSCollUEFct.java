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
package at.kc.tugraz.ss.service.coll.impl.fct.ue;

import at.kc.tugraz.socialserver.utils.SSLogU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.datatypes.datatypes.SSUEEnum;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserEntryAddPar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserEntryDeletePar;
import at.kc.tugraz.ss.service.coll.datatypes.pars.SSCollUserSharePar;

public class SSCollUEFct {
  
  public static void collUserEntryDelete(final SSCollUserEntryDeletePar par){
    
    if(!par.saveUE){
      return;
    }
    
    try{
      
      SSServCaller.ueAdd(
        par.user,
        par.collEntry,
        SSUEEnum.removeCollectionItem,
        SSUri.toStr(par.coll),
        par.shouldCommit);
      
      SSServCaller.ueAdd(
        par.user,
        par.coll,
        SSUEEnum.changeCollectionByRemoveCollectionItem,
        SSUri.toStr(par.collEntry),
        par.shouldCommit);
      
    }catch(Exception error){
      SSLogU.warn("storing ue failed");
    }
  }
  
  public static void collUserUnSubscribeColl(final SSCollUserEntryDeletePar par){
    
    if(!par.saveUE){
      return;
    }
    
    try{
      
      SSServCaller.ueAdd(
        par.user,
        par.collEntry,
        SSUEEnum.unSubscribeCollection,
        SSStrU.empty,
        par.shouldCommit);
      
    }catch(Exception error){
      SSLogU.warn("storing ue failed");
    }
  }
  
  public static void collUserDeleteColl(final SSCollUserEntryDeletePar par){
    
    if(!par.saveUE){
      return;
    }
    
    try{
      
      SSServCaller.ueAdd(
        par.user,
        par.collEntry,
        SSUEEnum.removeCollection,
        SSStrU.empty,
        par.shouldCommit);
      
    }catch(Exception error){
      SSLogU.warn("storing ue failed");
    }
  }
  
  public static void collUserShareColl(final SSCollUserSharePar par){
    
    if(!par.saveUE){
      return;
    }
        
    try{
      
      SSServCaller.ueAdd(
        par.user,
        par.coll,
        SSUEEnum.shareCollection,
        SSStrU.empty,
        par.shouldCommit);
      
    }catch(Exception error){
      SSLogU.warn("storing ue failed");
    }    
  }

  public static void collUserEntryAdd(final SSCollUserEntryAddPar par){
    
    //TODO dtheiler: re-implement this
//    if(!par.saveUE){
//      return;
//    }
//        
//    try{
//      
//      if(par.addNewColl){
//
//        if(SSSpaceEnum.isPrivate(par.space)){
//          SSServCaller.ueAdd(
//          par.user,
//          par.collEntry,
//          SSUEEnum.createPrivateCollection,
//          SSStrU.empty,
//          par.shouldCommit);
//        }
//                
//        if(SSSpaceEnum.isShared(par.space)){
//        
//          SSServCaller.ueAdd(
//            par.user,
//            par.collEntry,
//            SSUEEnum.createSharedCollection,
//            SSStrU.empty,
//            par.shouldCommit);
//        }
//      }
//      
//      if(SSSpaceEnum.isFollow(par.space)){
//        
//        SSServCaller.ueAdd(
//          par.user,
//          par.collEntry,
//          SSUEEnum.subscribeCollection,
//          SSStrU.empty,
//          par.shouldCommit);
//      }
//      
//      if(SSSpaceEnum.isSharedOrFollow(par.space)){
//        
//        SSServCaller.ueAdd(
//          par.user,
//          par.collEntry,
//          SSUEEnum.addSharedCollectionItem,
//          SSUri.toStr(par.coll),
//          par.shouldCommit);
//        
//        SSServCaller.ueAdd(
//          par.user,
//          par.coll,
//          SSUEEnum.changeCollectionByAddSharedCollectionItem,
//          SSUri.toStr(par.collEntry),
//          par.shouldCommit);
//      }
//      
//      if(SSSpaceEnum.isPrivate(par.space)){
//        
//        SSServCaller.ueAdd(
//          par.user,
//          par.collEntry,
//          SSUEEnum.addPrivateCollectionItem,
//          SSUri.toStr(par.coll),
//          par.shouldCommit);
//        
//        SSServCaller.ueAdd(
//          par.user,
//          par.coll,
//          SSUEEnum.changeCollectionByAddPrivateCollectionItem,
//          SSUri.toStr(par.collEntry),
//          par.shouldCommit);
//      }
//      
//    }catch(Exception error){
//      SSLogU.warn("storing ue failed");
//    }
  }
}

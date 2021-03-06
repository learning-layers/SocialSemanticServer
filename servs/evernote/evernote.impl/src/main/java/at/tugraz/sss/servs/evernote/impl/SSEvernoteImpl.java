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
package at.tugraz.sss.servs.evernote.impl;

import at.tugraz.sss.serv.util.SSDateU;
import at.tugraz.sss.serv.util.SSLogU;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.SSEntity;
import at.tugraz.sss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.serv.jobs.evernote.api.SSEvernoteClientI;
import at.kc.tugraz.ss.serv.jobs.evernote.api.SSEvernoteServerI;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteInfo;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteNote;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteNoteAddPar;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteNoteGetPar;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteNoteStoreGetPar;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteNoteTagNamesGetPar;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteNotebookGetPar;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteResource;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteResourceAddPar;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteResourceByHashGetPar;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteResourceGetPar;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteUSNSetPar;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteUserAddPar;
import at.tugraz.sss.servs.file.api.SSFileServerI;
import at.tugraz.sss.serv.conf.api.SSConfA;
import at.tugraz.sss.serv.db.api.SSDBNoSQLI;
import at.tugraz.sss.serv.entity.api.SSDescribeEntityI;
import at.tugraz.sss.serv.datatype.par.SSEntityDescriberPar;
import at.tugraz.sss.serv.datatype.SSErr;
import at.tugraz.sss.serv.impl.api.SSServImplWithDBA;
import com.evernote.auth.EvernoteAuth;
import com.evernote.auth.EvernoteService;
import com.evernote.clients.ClientFactory;
import com.evernote.clients.NoteStoreClient;
import com.evernote.clients.UserStoreClient;
import com.evernote.edam.error.EDAMErrorCode;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.notestore.SyncChunk;
import com.evernote.edam.notestore.SyncChunkFilter;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.Notebook;
import com.evernote.edam.type.Resource;
import com.evernote.edam.type.SharedNotebook;
import java.util.List;
import at.tugraz.sss.serv.reg.SSServErrReg;
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.serv.datatype.par.*;
import at.tugraz.sss.servs.file.datatype.par.SSEntityFilesGetPar;

public class SSEvernoteImpl
extends SSServImplWithDBA
implements
  SSEvernoteClientI,
  SSEvernoteServerI,
  SSDescribeEntityI{
  
  private final SSEvernoteSQL sql;
  
  public SSEvernoteImpl(final SSConfA conf) throws SSErr{
    
    super(conf, (SSDBSQLI) SSServReg.getServ(SSDBSQLI.class), (SSDBNoSQLI) SSServReg.getServ(SSDBNoSQLI.class));
    
    this.sql = new SSEvernoteSQL(dbSQL);
  }
  
  @Override
  public SSEntity describeEntity(
    final SSServPar servPar,
    final SSEntity             entity,
    final SSEntityDescriberPar par) throws SSErr{
    
    try{
      
      switch(entity.type){
        
        case evernoteNote:{
          
          if(SSStrU.isEqual(entity, par.recursiveEntity)){
            return entity;
          }
          
          //TODO use (implement) evernoteEvernoteNoteGet instead
          final SSEvernoteNote evernoteNote =
            SSEvernoteNote.get(
              sql.getNote(servPar, entity.id),
              entity);
          
          for(SSEntity file :
            ((SSFileServerI) SSServReg.getServ(SSFileServerI.class)).filesGet(
              new SSEntityFilesGetPar(
                servPar,
                par.user,
                evernoteNote.id,
                par.withUserRestriction,
                false))){ //invokeEntityHandlers
            
            evernoteNote.file = file;
            break;
          }
          
          return evernoteNote;
        }
        
        case evernoteResource:{
          
          if(SSStrU.isEqual(entity, par.recursiveEntity)){
            return entity;
          }
          
          //TODO use (implement) evernoteEvernoteResourceGet instead
          final SSEvernoteResource evernoteResource =
            SSEvernoteResource.get(
              sql.getResource(servPar, entity.id),
              entity);
          
          for(SSEntity file :
            ((SSFileServerI) SSServReg.getServ(SSFileServerI.class)).filesGet(
              new SSEntityFilesGetPar(
                servPar,
                par.user,
                evernoteResource.id,
                par.withUserRestriction,
                false))){ //invokeEntityHandlers
            
            evernoteResource.file = file;
            break;
          }
          
          return evernoteResource;
        }
      }
      
      return entity;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSEvernoteInfo evernoteNoteStoreGet(final SSEvernoteNoteStoreGetPar par) throws SSErr {
    
    try{
      final EvernoteAuth              evernoteAuth       = new EvernoteAuth   (EvernoteService.PRODUCTION, par.authToken);
      final ClientFactory             clientFactory      = new ClientFactory  (evernoteAuth);
      final UserStoreClient           userStore          = clientFactory.createUserStoreClient();
      final NoteStoreClient           noteStore          = clientFactory.createNoteStoreClient();
      final SSUri                     shardUri           = SSUri.get(userStore.getPublicUserInfo(userStore.getUser().getUsername()).getWebApiUrlPrefix());
      final SyncChunkFilter           filter             = new SyncChunkFilter();
      SyncChunk                       noteStoreSyncChunk;
      Integer                         lastUSN;
      
      filter.setIncludeNotes                               (true);
      filter.setIncludeNoteResources	                     (true);
      filter.setIncludeNoteAttributes	                     (true);
      filter.setIncludeNotebooks                           (true);
      filter.setIncludeTags	                               (true);
      filter.setIncludeSearches                            (false);
      filter.setIncludeResources	                         (true);
      filter.setIncludeLinkedNotebooks                     (true);
      filter.setIncludeExpunged                            (false);
      filter.setIncludeNoteApplicationDataFullMap	         (true);
      filter.setIncludeResourceApplicationDataFullMap	     (true);
      filter.setIncludeNoteResourceApplicationDataFullMap	 (true);
      
      noteStoreSyncChunk =
        noteStore.getFilteredSyncChunk(
          sql.getUSN(par, par.authToken),
          1000000,
          filter);
      
      if(noteStoreSyncChunk.isSetChunkHighUSN()){
        lastUSN = noteStoreSyncChunk.getChunkHighUSN();
      }else{
        lastUSN = noteStoreSyncChunk.getUpdateCount();
      }
      
      if(
        !noteStoreSyncChunk.isSetUpdateCount()  ||
        !noteStoreSyncChunk.isSetChunkHighUSN() ||
        (lastUSN >= noteStoreSyncChunk.getUpdateCount()) && lastUSN >= sql.getUSN(par, par.authToken)){
        
        SSLogU.debug(par.authEmail + " received full evernote content");
      }else{
        SSLogU.info(par.authEmail + " needs further syncing to retrieve full evernote content");
      }
      
//      if(lastUSN != noteStoreSyncChunk.getUpdateCount()){
//        SSLogU.warn(par.authEmail + " didnt receive latest information from evernote | more than 1.000.000 (new) entries available");
//      }
      
      return SSEvernoteInfo.get(
        userStore,
        noteStore,
        shardUri,
        noteStoreSyncChunk,
        par.authToken);
      
//      https://sandbox.evernote.com/shard/s1/sh/72ddd50f-5d13-46e3-b32d-d2b314ced5c1/ea77ae0587d735f39a94868ce3ddab5f
      
    }catch(EDAMSystemException edamError){
      
      if(gotToSleep(edamError, par.authEmail)){
        return evernoteNoteStoreGet(par);
      }else{
        SSServErrReg.regErrThrow(edamError);
        return null;
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public Notebook evernoteNotebookGet(final SSEvernoteNotebookGetPar par) throws SSErr {
    
    try{
      return par.noteStore.getNotebook(par.notebookGUID);
    }catch(EDAMSystemException edamError){
      
      if(gotToSleep(edamError, SSStrU.empty)){
        return evernoteNotebookGet (par);
      }else{
        SSServErrReg.regErrThrow(edamError);
        return null;
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public Resource evernoteResourceByHashGet(final SSEvernoteResourceByHashGetPar par) throws SSErr {
    
    try{
      
//      short      a     = Short.parseShort  (par.resourceHash, 2);
      
//      byte[] array =  par.resourceHash.getBytes();
      
//      ByteBuffer bytes = ByteBuffer.wrap(array);
      
      //allocate(2).putShort(a);
      
//      int numberChars = par.resourceHash.length();
//      byte[] bytes = new byte[numberChars / 2];
//
//      for (int i = 0; i < numberChars; i += 2){
//        bytes[i / 2] = Byte.parseByte(par.resourceHash.substring(i, i + 1), 16);
//      }
      
      byte[] result = new byte[par.resourceHash.length() / 2];
      for (int i = 0; i < result.length; ++i) {
        int offset = i * 2;
        result[i] = (byte) Integer.parseInt(par.resourceHash.substring(offset, offset + 2), 16);
      }
      
      return par.noteStore.getResourceByHash(par.noteGUID, result, true, false, false);
    }catch(EDAMSystemException edamError){
      
      if(gotToSleep(edamError, SSStrU.empty)){
        return evernoteResourceByHashGet (par);
      }else{
        SSServErrReg.regErrThrow(edamError);
        return null;
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<String> evernoteNoteTagNamesGet(final SSEvernoteNoteTagNamesGetPar par) throws SSErr{
    
    try{
      return SSStrU.distinctWithoutEmptyAndNull(par.noteStore.getNoteTagNames(par.noteGUID));
      
    }catch(EDAMSystemException edamError){
      
      if(gotToSleep(edamError, SSStrU.empty)){
        return evernoteNoteTagNamesGet(par);
      }else{
        SSServErrReg.regErrThrow(edamError);
        return null;
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public Note evernoteNoteGet(final SSEvernoteNoteGetPar par) throws SSErr{
    
    try{
      return par.noteStore.getNote(par.noteGUID, par.includeContent, false, false, false);
      
    }catch(EDAMSystemException edamError){
      
      if(gotToSleep(edamError, SSStrU.empty)){
        return evernoteNoteGet (par);
      }else{
        SSServErrReg.regErrThrow(edamError);
        return null;
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public Resource evernoteResourceGet(final SSEvernoteResourceGetPar par) throws SSErr{
    
    try{
      return par.noteStore.getResource(par.resourceGUID, par.includeContent, false, true, false);
      
    }catch(EDAMSystemException edamError){
      
      if(gotToSleep(edamError, SSStrU.empty)){
        return evernoteResourceGet (par);
      }else{
        SSServErrReg.regErrThrow(edamError);
        return null;
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public boolean evernoteUserAdd(final SSEvernoteUserAddPar par) throws SSErr{
    
    try{
      dbSQL.startTrans(par, par.shouldCommit);
      
      sql.addUserIfNotExists(par, par.user, par.authToken);
      
      dbSQL.commit(par, par.shouldCommit);
      
      return true;
      
    }catch(Exception error){
      
      try{
        dbSQL.rollBack(par, par.shouldCommit);
        return false;
      }catch(Exception error2){
        SSLogU.err(error2);
      }
      
      SSServErrReg.regErrThrow(error);
      return false;
    }
  }
  
  @Override
  public boolean evernoteNoteAdd(final SSEvernoteNoteAddPar par) throws SSErr{
    
    try{
      dbSQL.startTrans(par, par.shouldCommit);
      
      sql.addNoteIfNotExists(par, par.notebook, par.note);
      
      dbSQL.commit(par, par.shouldCommit);
      
      return true;
      
    }catch(Exception error){
      
      try{
        dbSQL.rollBack(par, par.shouldCommit);
        return false;
      }catch(Exception error2){
        SSLogU.err(error2);
      }
      
      SSServErrReg.regErrThrow(error);
      return false;
    }
  }
  
  @Override
  public boolean evernoteResourceAdd(final SSEvernoteResourceAddPar par) throws SSErr{
    
    try{
      dbSQL.startTrans(par, par.shouldCommit);
      
      sql.addResourceIfNotExists(par, par.note, par.resource);
      
      dbSQL.commit(par, par.shouldCommit);
      
      return true;
      
    }catch(Exception error){
      
      try{
        dbSQL.rollBack(par, par.shouldCommit);
        return false;
      }catch(Exception error2){
        SSLogU.err(error2);
      }
      
      SSServErrReg.regErrThrow(error);
      return false;
    }
  }
  
  @Override
  public boolean evernoteUSNSet(final SSEvernoteUSNSetPar par) throws SSErr{
    
    try{
      dbSQL.startTrans(par, par.shouldCommit);
      
      sql.setUSN(par, par.authToken, par.usn);
      
      dbSQL.commit(par, par.shouldCommit);
      
      return true;
      
    }catch(Exception error){
      
      try{
        dbSQL.rollBack(par, par.shouldCommit);
        return false;
      }catch(Exception error2){
        SSLogU.err(error2);
      }
      
      SSServErrReg.regErrThrow(error);
      return false;
    }
  }
  
  private boolean gotToSleep(
    final EDAMSystemException edamError,
    final String              authEmail) throws SSErr{
    
    try{
      
      if(edamError.getErrorCode().compareTo(EDAMErrorCode.RATE_LIMIT_REACHED) != 0){
        throw edamError;
      }
      
      try{
        
        SSLogU.info("evernote API goes to sleep for " + edamError.getRateLimitDuration() + " seconds for RATE EXCEPTION for " + authEmail);
        
        Thread.sleep(edamError.getRateLimitDuration() * SSDateU.secondInMilliseconds  + SSDateU.secondInMilliseconds * 10) ;
      }catch(Exception error){
        SSServErrReg.regErrThrow(error);
        return false;
      }
      
      return true;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return false;
    }
  }
}
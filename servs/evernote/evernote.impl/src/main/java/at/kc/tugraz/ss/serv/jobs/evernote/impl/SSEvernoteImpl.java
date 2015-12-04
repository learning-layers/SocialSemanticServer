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
package at.kc.tugraz.ss.serv.jobs.evernote.impl;

import at.tugraz.sss.serv.SSDateU;
import at.tugraz.sss.serv.SSLogU;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSDBSQLI;
import at.kc.tugraz.ss.serv.jobs.evernote.api.SSEvernoteClientI;
import at.kc.tugraz.ss.serv.jobs.evernote.api.SSEvernoteServerI;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteInfo;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteNote;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteNoteAddPar;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteNoteGetPar;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteNoteStoreGetPar;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteNoteTagNamesGetPar;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteNotebookGetPar;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteNotebooksSharedGetPar;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteResource;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteResourceAddPar;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteResourceByHashGetPar;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteResourceGetPar;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteUSNSetPar;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteUserAddPar;
import at.kc.tugraz.ss.serv.jobs.evernote.impl.fct.sql.SSEvernoteSQLFct;
import at.kc.tugraz.ss.service.filerepo.api.SSFileRepoServerI;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSDescribeEntityI;
import at.tugraz.sss.serv.SSEntityDescriberPar;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSServImplWithDBA;
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
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.servs.file.datatype.par.SSEntityFilesGetPar;

public class SSEvernoteImpl 
extends SSServImplWithDBA
implements 
  SSEvernoteClientI, 
  SSEvernoteServerI, 
  SSDescribeEntityI{
  
  private final SSEvernoteSQLFct sqlFct;
  
  public SSEvernoteImpl(final SSConfA conf) throws SSErr{
    
    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());
    
    this.sqlFct = new SSEvernoteSQLFct(dbSQL);
  }
  
  @Override
  public SSEntity describeEntity(
    final SSEntity             entity,
    final SSEntityDescriberPar par) throws Exception{
    
    try{
      
      switch(entity.type){
        
        case evernoteNote:{
          
          if(SSStrU.equals(entity, par.recursiveEntity)){
            return entity;
          }
          
          //TODO use (implement) evernoteEvernoteNoteGet instead
          final SSEvernoteNote evernoteNote =
            SSEvernoteNote.get(
              sqlFct.getNote(entity.id),
              entity);
          
          for(SSEntity file : 
            ((SSFileRepoServerI) SSServReg.getServ(SSFileRepoServerI.class)).filesGet(
              new SSEntityFilesGetPar(
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
          
          if(SSStrU.equals(entity, par.recursiveEntity)){
            return entity;
          }
          
          //TODO use (implement) evernoteEvernoteResourceGet instead
          final SSEvernoteResource evernoteResource =
            SSEvernoteResource.get(
              sqlFct.getResource(entity.id),
              entity);
          
          for(SSEntity file :
            ((SSFileRepoServerI) SSServReg.getServ(SSFileRepoServerI.class)).filesGet(
              new SSEntityFilesGetPar(
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
  public SSEvernoteInfo evernoteNoteStoreGet(final SSEvernoteNoteStoreGetPar par) throws Exception {
    
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
          sqlFct.getUSN(par.authToken),
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
        (lastUSN >= noteStoreSyncChunk.getUpdateCount()) && lastUSN >= sqlFct.getUSN(par.authToken)){
        
        SSLogU.debug(par.authEmail + " received full evernote content");
      }else{
        SSLogU.warn(par.authEmail + " needs further syncing to retrieve full evernote content");
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
      
    }catch(Exception error){
      
      if(
        error instanceof EDAMSystemException &&
        ((EDAMSystemException)error).getErrorCode().compareTo(EDAMErrorCode.RATE_LIMIT_REACHED) == 0){
        
        SSServErrReg.reset();
        
        SSLogU.info("evernoteNoteStoreGet goes to sleep for " + ((EDAMSystemException)error).getRateLimitDuration() + " seconds for RATE EXCEPTION for " + par.authEmail);
        
        Thread.sleep(((EDAMSystemException)error).getRateLimitDuration() * SSDateU.secondInMilliseconds  + SSDateU.secondInMilliseconds * 10) ;
        
        return evernoteNoteStoreGet (par);
      }
      
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public Notebook evernoteNotebookGet(final SSServPar parA) throws Exception {
    
    try{
      
      final SSEvernoteNotebookGetPar par       = new SSEvernoteNotebookGetPar(parA);
      
      return par.noteStore.getNotebook(par.notebookGUID);
    }catch(Exception error){
      
      if(
        error instanceof EDAMSystemException &&
        ((EDAMSystemException)error).getErrorCode().compareTo(EDAMErrorCode.RATE_LIMIT_REACHED) == 0){
        
        SSServErrReg.reset();
        
        SSLogU.info("evernoteNotebookGet goes to sleep for " + ((EDAMSystemException)error).getRateLimitDuration() + " seconds for RATE EXCEPTION");
        
        Thread.sleep(((EDAMSystemException)error).getRateLimitDuration() * SSDateU.secondInMilliseconds  + SSDateU.secondInMilliseconds * 10) ;
        
        return evernoteNotebookGet (parA);
      }
      
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public Resource evernoteResourceByHashGet(final SSServPar parA) throws Exception {
    
    try{
      
      final SSEvernoteResourceByHashGetPar par       = new SSEvernoteResourceByHashGetPar(parA);
      
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
    }catch(Exception error){
      
      if(
        error instanceof EDAMSystemException &&
        ((EDAMSystemException)error).getErrorCode().compareTo(EDAMErrorCode.RATE_LIMIT_REACHED) == 0){
        
        SSServErrReg.reset();
        
        SSLogU.info("evernoteResourceByHashGet goes to sleep for " + ((EDAMSystemException)error).getRateLimitDuration() + " seconds for RATE EXCEPTION");
        
        Thread.sleep(((EDAMSystemException)error).getRateLimitDuration() * SSDateU.secondInMilliseconds  + SSDateU.secondInMilliseconds * 10) ;
        
        return evernoteResourceByHashGet (parA);
      }
      
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SharedNotebook> evernoteNotebooksSharedGet(SSServPar parA) throws Exception {
    
    try{
      SSEvernoteNotebooksSharedGetPar par             = new SSEvernoteNotebooksSharedGetPar(parA);
      
			return par.noteStore.listSharedNotebooks();
    }catch(Exception error){
      
      if(
        error instanceof EDAMSystemException &&
        ((EDAMSystemException)error).getErrorCode().compareTo(EDAMErrorCode.RATE_LIMIT_REACHED) == 0){
        
        SSServErrReg.reset();
        
        SSLogU.info("evernoteNotebooksSharedGet goes to sleep for " + ((EDAMSystemException)error).getRateLimitDuration() + " seconds for RATE EXCEPTION");
        
        Thread.sleep(((EDAMSystemException)error).getRateLimitDuration() * SSDateU.secondInMilliseconds  + SSDateU.secondInMilliseconds * 10) ;
        
        return evernoteNotebooksSharedGet(parA);
      }
      
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<String> evernoteNoteTagNamesGet(final SSServPar parA) throws Exception{
    
    try{
      final SSEvernoteNoteTagNamesGetPar par       = new SSEvernoteNoteTagNamesGetPar(parA);
      
      return SSStrU.distinctWithoutEmptyAndNull(par.noteStore.getNoteTagNames(par.noteGUID));
      
    }catch(Exception error){
      
      if(
        error instanceof EDAMSystemException &&
        ((EDAMSystemException)error).getErrorCode().compareTo(EDAMErrorCode.RATE_LIMIT_REACHED) == 0){
        
        SSServErrReg.reset();
        
        SSLogU.info("evernoteNoteTagNamesGet goes to sleep for " + ((EDAMSystemException)error).getRateLimitDuration() + " seconds for RATE EXCEPTION");
        
        Thread.sleep(((EDAMSystemException)error).getRateLimitDuration() * SSDateU.secondInMilliseconds  + SSDateU.secondInMilliseconds * 10) ;
        
        return evernoteNoteTagNamesGet (parA);
      }
      
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public Note evernoteNoteGet(final SSServPar parA) throws Exception{
    
    try{
      final SSEvernoteNoteGetPar par = new SSEvernoteNoteGetPar(parA);
      
      return par.noteStore.getNote(par.noteGUID, par.includeContent, false, false, false);
      
    }catch(Exception error){
      
      if(
        error instanceof EDAMSystemException &&
        ((EDAMSystemException)error).getErrorCode().compareTo(EDAMErrorCode.RATE_LIMIT_REACHED) == 0){
        
        SSServErrReg.reset();
        
        SSLogU.info("evernoteNoteGet goes to sleep for " + ((EDAMSystemException)error).getRateLimitDuration() + " seconds for RATE EXCEPTION");
        
        Thread.sleep(((EDAMSystemException)error).getRateLimitDuration() * SSDateU.secondInMilliseconds  + SSDateU.secondInMilliseconds * 10) ;
        
        return evernoteNoteGet (parA);
      }
      
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public Resource evernoteResourceGet(final SSServPar parA) throws Exception{
    
    try{
      final SSEvernoteResourceGetPar par = new SSEvernoteResourceGetPar(parA);
      
      return par.noteStore.getResource(par.resourceGUID, par.includeContent, false, true, false);
      
    }catch(Exception error){
      
      if(
        error instanceof EDAMSystemException &&
        ((EDAMSystemException)error).getErrorCode().compareTo(EDAMErrorCode.RATE_LIMIT_REACHED) == 0){
        
        SSServErrReg.reset();
        
        SSLogU.info("evernoteResourceGet goes to sleep for " + ((EDAMSystemException)error).getRateLimitDuration() + " seconds for RATE EXCEPTION");
        
        Thread.sleep(((EDAMSystemException)error).getRateLimitDuration() * SSDateU.secondInMilliseconds  + SSDateU.secondInMilliseconds * 10) ;
        
        return evernoteResourceGet (parA);
      }
      
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public Boolean evernoteUserAdd(final SSServPar parA) throws Exception{
     
    try{
      final SSEvernoteUserAddPar par = new SSEvernoteUserAddPar(parA);
      
      dbSQL.startTrans(par.shouldCommit);
      
      sqlFct.addUserIfNotExists(par.user, par.authToken);
      
      dbSQL.commit(par.shouldCommit);
      
      return true;
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(parA.shouldCommit)){
          
          SSServErrReg.reset();
          
          return evernoteUserAdd(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return false;
        }
      }
      
      dbSQL.rollBack(parA.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return false;
    }
  }
  
  @Override
  public Boolean evernoteNoteAdd(final SSServPar parA) throws Exception{
     
    try{
      final SSEvernoteNoteAddPar par = new SSEvernoteNoteAddPar(parA);
      
      dbSQL.startTrans(par.shouldCommit);
      
      sqlFct.addNoteIfNotExists(par.notebook, par.note);
      
      dbSQL.commit(par.shouldCommit);
      
      return true;
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(parA.shouldCommit)){
          
          SSServErrReg.reset();
          
          return evernoteNoteAdd(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return false;
        }
      }
      
      dbSQL.rollBack(parA.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public Boolean evernoteResourceAdd(final SSServPar parA) throws Exception{
     
    try{
      final SSEvernoteResourceAddPar par = new SSEvernoteResourceAddPar(parA);
      
      dbSQL.startTrans(par.shouldCommit);
      
      sqlFct.addResourceIfNotExists(par.note, par.resource);
      
      dbSQL.commit(par.shouldCommit);
      
      return true;
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(parA.shouldCommit)){
          
          SSServErrReg.reset();
          
          return evernoteResourceAdd(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return false;
        }
      }
      
      dbSQL.rollBack(parA.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return false;
    }
  }
  
  @Override
  public Boolean evernoteUSNSet(final SSServPar parA) throws Exception{
     
    try{
      final SSEvernoteUSNSetPar par = new SSEvernoteUSNSetPar(parA);
      
      dbSQL.startTrans(par.shouldCommit);
      
      sqlFct.setUSN(par.authToken, par.usn);
      
      dbSQL.commit(par.shouldCommit);
      
      return true;
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(parA.shouldCommit)){
          
          SSServErrReg.reset();
          
          return evernoteUSNSet(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return false;
        }
      }
      
      dbSQL.rollBack(parA.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return false;
    }
  }
  
//    @Override 
//  public List<LinkedNotebook> evernoteNotebooksLinkedGet(SSServPar parA) throws Exception{
//    
//    try{
//      SSEvernoteNotebooksLinkedGetPar par             = new SSEvernoteNotebooksLinkedGetPar(parA);
//      
//			return par.noteStore.listLinkedNotebooks();
//    }catch (Exception error){
//			SSServErrReg.regErrThrow(error);
//      return null;
//		}
//  }
  
  //  @Override
//  public List<Notebook> evernoteNotebooksGet(SSServPar parA) throws Exception {
//    
//    try{
//      
//      SSEvernoteNotebooksGetPar par       = new SSEvernoteNotebooksGetPar(parA);
//      
//      return par.noteStore.listNotebooks();
//    }catch (Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }
//  }
  
//  @Override
//  public List<Note> evernoteNotesGet(SSServPar parA) throws Exception {
//    
//    try{
//      
//      SSEvernoteNotesGetPar   par        = new SSEvernoteNotesGetPar(parA);
//      List<Note>              notes      = new ArrayList<>();
//      NotesMetadataResultSpec resultSpec = new NotesMetadataResultSpec();
//      NoteFilter              noteFilter = new NoteFilter();
//      NotesMetadataList       noteList;
////    NoteCollectionCounts    noteCount;
//      
//      resultSpec.setIncludeAttributes(true);
//      resultSpec.setIncludeCreated(true);
//      resultSpec.setIncludeDeleted(true);
//      resultSpec.setIncludeNotebookGuid(true);
//      resultSpec.setIncludeTitle(true);
//      resultSpec.setIncludeUpdated(true);
//      
//      noteFilter.setNotebookGuid(par.notebookGuid);
//      
////      noteCount = par.noteStore.findNoteCounts    (noteFilter, false);
//      noteList  = par.noteStore.findNotesMetadata (noteFilter, 0, 100, resultSpec);
//      
//      for (NoteMetadata note : noteList.getNotes()) {
//        notes.add(par.noteStore.getNote(note.getGuid(), true, true, false, false));
//      }
//      
//      return notes;
//    }catch(Exception error){
//      
//      if(
//        error instanceof EDAMSystemException &&
//        ((EDAMSystemException)error).getErrorCode().compareTo(EDAMErrorCode.RATE_LIMIT_REACHED) == 0){
//        
//        SSServErrReg.reset();
//        
//        SSLogU.info("evernoteNotesGet goes to sleep for " + ((EDAMSystemException)error).getRateLimitDuration() + " seconds for RATE EXCEPTION");
//        
//        Thread.sleep(((EDAMSystemException)error).getRateLimitDuration() * SSDateU.secondInMilliseconds  + SSDateU.secondInMilliseconds * 10) ;
//        
//        return evernoteNotesGet (parA);
//      }
//      
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }
//  }
//  
//  @Override
//  public List<Note> evernoteNotesLinkedGet(final SSServPar parA) throws Exception{
//    
//    try{
//      final SSEvernoteNotesLinkedGetPar par       = new SSEvernoteNotesLinkedGetPar(parA);
//      final SyncChunk                   synChunk  = par.noteStore.getLinkedNotebookSyncChunk(par.linkedNotebook, 0, 256, true);
//      
//      return synChunk.getNotes();
//      
//    }catch (Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }
//  }
}

// @Override
//  public String evernoteUsersAuthTokenGet(final SSServPar parA) throws Exception{
//     
//    try{
//      final SSEvernoteUsersAuthTokenGetPar par = new SSEvernoteUsersAuthTokenGetPar(parA);
//      
//      return sqlFct.getAuthToken(par.user);
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }
//  }
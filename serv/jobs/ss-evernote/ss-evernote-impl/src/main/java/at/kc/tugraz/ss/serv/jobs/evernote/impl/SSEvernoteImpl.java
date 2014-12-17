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

import at.kc.tugraz.socialserver.utils.SSDateU;
import at.kc.tugraz.socialserver.utils.SSLogU;
import at.kc.tugraz.socialserver.utils.SSMimeTypeU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntity;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityDescGetPar;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.jobs.evernote.api.SSEvernoteClientI;
import at.kc.tugraz.ss.serv.jobs.evernote.api.SSEvernoteServerI;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteInfo;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteNote;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteNoteGetPar;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteNoteStoreGetPar;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteNoteTagNamesGetPar;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteNotebookGetPar;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteNotebooksGetPar;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteNotebooksLinkedGetPar;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteNotebooksSharedGetPar;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteNotesGetPar;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteNotesLinkedGetPar;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteResource;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteResourceGetPar;
import at.kc.tugraz.ss.serv.jobs.evernote.impl.fct.sql.SSEvernoteSQLFct;
import at.kc.tugraz.ss.serv.jobs.evernote.impl.helper.SSEvernoteHelper;
import at.kc.tugraz.ss.serv.serv.api.SSConfA;
import at.kc.tugraz.ss.serv.serv.api.SSEntityDescriberI;
import at.kc.tugraz.ss.serv.serv.api.SSEntityHandlerImplI;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import com.evernote.auth.EvernoteAuth;
import com.evernote.auth.EvernoteService;
import com.evernote.clients.ClientFactory;
import com.evernote.clients.NoteStoreClient;
import com.evernote.clients.UserStoreClient;
import com.evernote.edam.error.EDAMErrorCode;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.notestore.NoteMetadata;
import com.evernote.edam.notestore.NotesMetadataList;
import com.evernote.edam.notestore.NotesMetadataResultSpec;
import com.evernote.edam.notestore.SyncChunk;
import com.evernote.edam.notestore.SyncChunkFilter;
import com.evernote.edam.type.LinkedNotebook;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.Notebook;
import com.evernote.edam.type.Resource;
import com.evernote.edam.type.SharedNotebook;
import java.util.ArrayList;
import java.util.List;

public class SSEvernoteImpl extends SSServImplWithDBA implements SSEvernoteClientI, SSEvernoteServerI, SSEntityHandlerImplI, SSEntityDescriberI{
  
  private final SSEvernoteSQLFct sqlFct;
  
  public SSEvernoteImpl(final SSConfA conf, final SSDBSQLI dbSQL) throws Exception{
    
    super(conf, null, dbSQL);
    
    this.sqlFct = new SSEvernoteSQLFct(dbSQL);
  }
  
  @Override
  public SSEntity getUserEntity(
    final SSUri              user,
    final SSEntity           entity) throws Exception{
    
    return entity;
  }
  
  @Override
  public SSEntity getDescForEntity(
    final SSEntityDescGetPar par,
    final SSEntity           desc) throws Exception{
    
    switch(desc.type){
      case evernoteNote:
      case evernoteResource:
        
        if(par.getThumb){
          
          desc.thumb =
            SSEvernoteHelper.getThumbBase64(
              par.user,
              par.entity);
        }
    }
    
    switch(desc.type){
      
      case evernoteNote:{
        
        return SSEvernoteNote.get(
          desc,
          sqlFct.getNote(par.entity).notebook);
      }
      
      case evernoteResource:{
        
        String      fileExt  = null;
        String      mimeType = null;
        
        try{
          
          final List<SSUri> filesForEntity = SSServCaller.entityFilesGet    (par.user, par.entity);
          
          fileExt        = SSServCaller.fileExtGet        (par.user, filesForEntity.get(0));
          mimeType       = SSMimeTypeU.mimeTypeForFileExt (fileExt);
          
        }catch(Exception error){
          SSLogU.warn("mime type cannot be retrieved from evernoteResource as it has no file attached");
          
          SSServErrReg.reset();
        }
        
        return SSEvernoteResource.get(
          desc,
          sqlFct.getResource(par.entity).note,
          fileExt,
          mimeType);
      }
    }
    
    return desc;
  }
  
  @Override
  public Boolean copyUserEntity(
    final SSUri        user,
    final List<SSUri>  users,
    final SSUri        entity,
    final List<SSUri>  entitiesToExclude,
    final SSEntityE    entityType) throws Exception{
    
    return false;
  }
  
  @Override
  public List<SSUri> getSubEntities(
    final SSUri         user,
    final SSUri         entity,
    final SSEntityE     type) throws Exception{

    return null;
  }
  
  @Override
  public List<SSUri> getParentEntities(
    final SSUri         user,
    final SSUri         entity,
    final SSEntityE     type) throws Exception{
    
    return new ArrayList<>();
  }
    
  @Override
  public Boolean setUserEntityPublic(
    final SSUri          userUri,
    final SSUri          entityUri, 
    final SSEntityE   entityType,
    final SSUri          publicCircleUri) throws Exception{

    return false;
  }
  
  @Override
  public void shareUserEntity(
    final SSUri          userUri, 
    final List<SSUri>    userUrisToShareWith,
    final SSUri          entityUri, 
    final SSUri          entityCircleUri,
    final SSEntityE      entityType,
    final Boolean        saveActivity) throws Exception{
  }
  
  @Override
  public void shareUserEntityWithCircle(
    final SSUri        userUri, 
    final SSUri        circleUri, 
    final SSUri        entityUri, 
    final SSEntityE entityType) throws Exception{
  }
  
  @Override
  public void removeDirectlyAdjoinedEntitiesForUser(
    final SSUri       userUri, 
    final SSEntityE   entityType,
    final SSUri       entityUri,
    final Boolean     removeUserTags,
    final Boolean     removeUserRatings,
    final Boolean     removeFromUserColls,
    final Boolean     removeUserLocations) throws Exception{
  }
  
  @Override
  public SSEvernoteInfo evernoteNoteStoreGet(SSServPar parA) throws Exception {
    
    try{
      final SSEvernoteNoteStoreGetPar par                = new SSEvernoteNoteStoreGetPar(parA);
      final EvernoteAuth              evernoteAuth       = new EvernoteAuth   (EvernoteService.PRODUCTION, par.authToken);
      final ClientFactory             clientFactory      = new ClientFactory  (evernoteAuth);
      final UserStoreClient           userStore          = clientFactory.createUserStoreClient();
      final NoteStoreClient           noteStore          = clientFactory.createNoteStoreClient();
      final SSUri                     shardUri           = SSUri.get(userStore.getPublicUserInfo(userStore.getUser().getUsername()).getWebApiUrlPrefix());
      final SyncChunkFilter           filter             = new SyncChunkFilter();
      SyncChunk                       noteStoreSyncChunk;
      
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
          100000,
          filter);
      
      if(noteStoreSyncChunk.getChunkHighUSN() != noteStoreSyncChunk.getUpdateCount()){
        SSLogU.warn(par.authToken + " didnt receive latest information from evernote | more than 100.000 (new) entries available");
      }
      
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
        
        SSLogU.info("evernoteNoteStoreGet goes to sleep for " + ((EDAMSystemException)error).getRateLimitDuration() + " seconds for RATE EXCEPTION");
        
        Thread.sleep(((EDAMSystemException)error).getRateLimitDuration() * SSDateU.secondInMilliseconds  + SSDateU.secondInMilliseconds * 10) ;
        
        return evernoteNoteStoreGet (parA);
      }
      
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<Notebook> evernoteNotebooksGet(SSServPar parA) throws Exception {
    
    try{
      
      SSEvernoteNotebooksGetPar par       = new SSEvernoteNotebooksGetPar(parA);
      
      return par.noteStore.listNotebooks();
    }catch (Exception error){
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
  public List<SharedNotebook> evernoteNotebooksSharedGet(SSServPar parA) throws Exception {
    
    try{
      SSEvernoteNotebooksSharedGetPar par             = new SSEvernoteNotebooksSharedGetPar(parA);
      
			return par.noteStore.listSharedNotebooks();
    }catch (Exception error){
			SSServErrReg.regErrThrow(error);
      return null;
		}
  }
  
  @Override 
  public List<LinkedNotebook> evernoteNotebooksLinkedGet(SSServPar parA) throws Exception{
    
    try{
      SSEvernoteNotebooksLinkedGetPar par             = new SSEvernoteNotebooksLinkedGetPar(parA);
      
			return par.noteStore.listLinkedNotebooks();
    }catch (Exception error){
			SSServErrReg.regErrThrow(error);
      return null;
		}
  }
  
  @Override
  public List<Note> evernoteNotesGet(SSServPar parA) throws Exception {
    
    try{
      
      SSEvernoteNotesGetPar   par        = new SSEvernoteNotesGetPar(parA);
      List<Note>              notes      = new ArrayList<>();
      NotesMetadataResultSpec resultSpec = new NotesMetadataResultSpec();
      NoteFilter              noteFilter = new NoteFilter();
      NotesMetadataList       noteList;
//    NoteCollectionCounts    noteCount;
      
      resultSpec.setIncludeAttributes(true);
      resultSpec.setIncludeCreated(true);
      resultSpec.setIncludeDeleted(true);
      resultSpec.setIncludeNotebookGuid(true);
      resultSpec.setIncludeTitle(true);
      resultSpec.setIncludeUpdated(true);
      
      noteFilter.setNotebookGuid(par.notebookGuid);
      
//      noteCount = par.noteStore.findNoteCounts    (noteFilter, false);
      noteList  = par.noteStore.findNotesMetadata (noteFilter, 0, 100, resultSpec);
      
      for (NoteMetadata note : noteList.getNotes()) {
        notes.add(par.noteStore.getNote(note.getGuid(), true, true, false, false));
      }
      
      return notes;
    }catch(Exception error){
      
      if(
        error instanceof EDAMSystemException &&
        ((EDAMSystemException)error).getErrorCode().compareTo(EDAMErrorCode.RATE_LIMIT_REACHED) == 0){
        
        SSServErrReg.reset();
        
        SSLogU.info("evernoteNotesGet goes to sleep for " + ((EDAMSystemException)error).getRateLimitDuration() + " seconds for RATE EXCEPTION");
        
        Thread.sleep(((EDAMSystemException)error).getRateLimitDuration() * SSDateU.secondInMilliseconds  + SSDateU.secondInMilliseconds * 10) ;
        
        return evernoteNotesGet (parA);
      }
      
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<Note> evernoteNotesLinkedGet(final SSServPar parA) throws Exception{
    
    try{
      final SSEvernoteNotesLinkedGetPar par       = new SSEvernoteNotesLinkedGetPar(parA);
      final SyncChunk                   synChunk  = par.noteStore.getLinkedNotebookSyncChunk(par.linkedNotebook, 0, 256, true);
      
      return synChunk.getNotes();
      
    }catch (Exception error){
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
}
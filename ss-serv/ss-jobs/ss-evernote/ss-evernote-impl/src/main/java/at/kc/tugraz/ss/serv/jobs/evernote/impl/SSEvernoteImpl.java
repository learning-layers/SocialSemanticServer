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

import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.serv.serv.api.SSServConfA;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityEnum;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityDescA;
import at.kc.tugraz.ss.datatypes.datatypes.SSLabelStr;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSEntityDesc;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserDirectlyAdjoinedEntitiesRemovePar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.jobs.evernote.api.SSEvernoteClientI;
import at.kc.tugraz.ss.serv.jobs.evernote.api.SSEvernoteServerI;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteInfo;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteNoteStoreGetPar;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteNotebooksGetPar;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteNotebooksLinkedGetPar;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteNotebooksSharedGetPar;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteNotesGetPar;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteNotesLinkedGetPar;
import at.kc.tugraz.ss.serv.serv.api.SSEntityHandlerImplI;
import at.kc.tugraz.ss.serv.serv.api.SSServImplMiscA;
import at.kc.tugraz.ss.service.rating.datatypes.SSRatingOverall;
import at.kc.tugraz.ss.service.tag.datatypes.SSTag;
import com.evernote.auth.EvernoteAuth;
import com.evernote.auth.EvernoteService;
import com.evernote.clients.ClientFactory;
import com.evernote.clients.NoteStoreClient;
import com.evernote.clients.UserStoreClient;
import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.notestore.NoteMetadata;
import com.evernote.edam.notestore.NotesMetadataList;
import com.evernote.edam.notestore.NotesMetadataResultSpec;
import com.evernote.edam.notestore.SyncChunk;
import com.evernote.edam.type.LinkedNotebook;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.Notebook;
import com.evernote.edam.type.SharedNotebook;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class SSEvernoteImpl extends SSServImplMiscA implements SSEvernoteClientI, SSEvernoteServerI, SSEntityHandlerImplI{
  
  public SSEvernoteImpl(final SSServConfA conf) throws Exception{
    super(conf);
  }
  
  /****** SSEntityHandlerImplI ******/
  @Override
  public void removeDirectlyAdjoinedEntitiesForUser(
    final SSEntityEnum                                  entityType,
    final SSEntityUserDirectlyAdjoinedEntitiesRemovePar par,
    final Boolean                                       shouldCommit) throws Exception{
  }
  
  @Override
  public SSEntityDescA getDescForEntity(
    final SSEntityEnum    entityType,
    final SSUri           userUri, 
    final SSUri           entityUri, 
    final SSLabelStr      label,
    final Long            creationTime,
    final List<SSTag>     tags, 
    final SSRatingOverall overallRating,
    final List<SSUri>     discUris,
    final SSUri           author)throws Exception{
    
    //TODO dtheiler fix this: set desc for different evernote entity types
    return SSEntityDesc.get(entityUri, label, creationTime, tags, overallRating, discUris, author);
  }
  
  /****** SSServRegisterableImplI ******/
  
  @Override
  public List<SSMethU> publishClientOps() throws Exception{
    
    List<SSMethU> clientOps = new ArrayList<SSMethU>();
      
    Method[] methods = SSEvernoteClientI.class.getMethods();
    
    for(Method method : methods){
      clientOps.add(SSMethU.get(method.getName()));
    }
    
    return clientOps;
  }
  
  @Override
  public List<SSMethU> publishServerOps() throws Exception{
    
    List<SSMethU> serverOps = new ArrayList<SSMethU>();
    
    Method[] methods = SSEvernoteServerI.class.getMethods();
    
    for(Method method : methods){
      serverOps.add(SSMethU.get(method.getName()));
    }
    
    return serverOps;
  }
  
  @Override
  public void handleClientOp(SSSocketCon sSCon, SSServPar par) throws Exception{
    SSEvernoteClientI.class.getMethod(SSMethU.toStr(par.op), SSSocketCon.class, SSServPar.class).invoke(this, sSCon, par);
  }
  
  @Override
  public Object handleServerOp(SSServPar par) throws Exception{
    return SSEvernoteServerI.class.getMethod(SSMethU.toStr(par.op), SSServPar.class).invoke(this, par);
  }

  /****** SSServRegisterableImplI ******/
  @Override
  public SSEvernoteInfo evernoteNoteStoreGet(SSServPar parA) throws Exception {
    
    SSEvernoteNoteStoreGetPar par    = new SSEvernoteNoteStoreGetPar(parA);
    SSEvernoteInfo            result = null;
    EvernoteAuth              evernoteAuth;
    ClientFactory             clientFactory;
    
    try{
      
      evernoteAuth   = new EvernoteAuth   (EvernoteService.PRODUCTION, par.authToken);
      clientFactory  = new ClientFactory  (evernoteAuth);
     
      UserStoreClient userStore = clientFactory.createUserStoreClient();
      NoteStoreClient noteStore = clientFactory.createNoteStoreClient();
      SSUri           shardUri  = SSUri.get(userStore.getPublicUserInfo(userStore.getUser().getUsername()).getWebApiUrlPrefix());
      
      result         = SSEvernoteInfo.get (userStore, noteStore, shardUri);
      
//      https://sandbox.evernote.com/shard/s1/sh/72ddd50f-5d13-46e3-b32d-d2b314ced5c1/ea77ae0587d735f39a94868ce3ddab5f
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
    
    return result;
  }
  
  @Override 
  public List<Notebook> evernoteNotebooksGet(SSServPar parA) throws Exception {
    
    SSEvernoteNotebooksGetPar par       = new SSEvernoteNotebooksGetPar(parA);
    List<Notebook>            notebooks = null;
    
    try{
			notebooks = par.noteStore.listNotebooks();
    }catch (Exception error){
			SSServErrReg.regErrThrow(error);
		}
		
		return notebooks;
  }
  
  @Override 
  public List<SharedNotebook> evernoteNotebooksSharedGet(SSServPar parA) throws Exception {
    
    SSEvernoteNotebooksSharedGetPar par             = new SSEvernoteNotebooksSharedGetPar(parA);
    List<SharedNotebook>            sharedNotebooks = null;
    
    try{
			sharedNotebooks = par.noteStore.listSharedNotebooks();
    }catch (Exception error){
			SSServErrReg.regErrThrow(error);
		}
		
		return sharedNotebooks;
  }
  
  @Override 
  public List<LinkedNotebook> evernoteNotebooksLinkedGet(SSServPar parA) throws Exception{
    
    SSEvernoteNotebooksLinkedGetPar par             = new SSEvernoteNotebooksLinkedGetPar(parA);
    List<LinkedNotebook>            linkedNotebooks = null;
    
    try{
			linkedNotebooks = par.noteStore.listLinkedNotebooks();
    }catch (Exception error){
			SSServErrReg.regErrThrow(error);
		}
		
		return linkedNotebooks;
  }
  
  @Override
  public List<Note> evernoteNotesGet(SSServPar parA) throws Exception {
    
    SSEvernoteNotesGetPar   par        = new SSEvernoteNotesGetPar(parA);
    List<Note>              notes      = new ArrayList<Note>();
    NotesMetadataResultSpec resultSpec = new NotesMetadataResultSpec();
    NoteFilter              noteFilter = new NoteFilter();
    NotesMetadataList       noteList;
//    NoteCollectionCounts    noteCount;
    
    try{
      
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
        notes.add(par.noteStore.getNote(note.getGuid(), true, false, false, false));
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
    
    return notes;
  }
  
  @Override
  public List<Note> evernoteNotesLinkedGet(final SSServPar parA) throws Exception{
    
    SSEvernoteNotesLinkedGetPar par       = new SSEvernoteNotesLinkedGetPar(parA);
    List<Note>                  notes     = new ArrayList<Note>();
    SyncChunk                   synChunk;
    
    try{
			synChunk = par.noteStore.getLinkedNotebookSyncChunk(par.linkedNotebook, 0, 256, true);
      notes    = synChunk.getNotes();
      
    }catch (Exception error){
      SSServErrReg.regErrThrow(error);
		}
		
		return notes;
  }
}
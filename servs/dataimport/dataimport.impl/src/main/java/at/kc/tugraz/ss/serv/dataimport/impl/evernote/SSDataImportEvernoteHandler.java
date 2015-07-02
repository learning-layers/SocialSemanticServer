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
package at.kc.tugraz.ss.serv.dataimport.impl.evernote;

import at.tugraz.sss.serv.SSDateU;
import at.tugraz.sss.serv.SSLinkU;
import at.tugraz.sss.serv.SSObjU;
import at.tugraz.sss.serv.SSStrU;
import at.kc.tugraz.ss.conf.conf.SSCoreConf;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSLabel;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSSpaceE;
import at.tugraz.sss.serv.SSToolContextE;
import at.kc.tugraz.ss.serv.dataimport.datatypes.pars.SSDataImportEvernotePar;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUpdatePar;
import at.tugraz.sss.serv.SSDBSQLI;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteInfo;
import at.kc.tugraz.ss.service.tag.api.SSTagServerI;
import at.kc.tugraz.ss.service.tag.datatypes.SSTagLabel;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagsAddPar;
import at.kc.tugraz.ss.service.userevent.api.SSUEServerI;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.kc.tugraz.ss.service.userevent.datatypes.SSUE;
import at.kc.tugraz.ss.service.userevent.datatypes.SSUEE;
import at.kc.tugraz.ss.service.userevent.datatypes.pars.SSUEAddPar;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServReg;
import com.evernote.edam.type.LinkedNotebook;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.NoteAttributes;
import com.evernote.edam.type.Notebook;
import com.evernote.edam.type.Resource;
import com.evernote.edam.type.SharedNotebook;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import sss.serv.eval.datatypes.SSEvalLogE;

public class SSDataImportEvernoteHandler {
  
  private final  String                  localWorkPath;
  private        SSEvernoteInfo          evernoteInfo             = null;
  private        SSUri                   userUri                  = null;
  private        List<SharedNotebook>    sharedNotebooks          = null;
  private        List<String>            sharedNotebookGuids      = null;
//  private        long                    april01                  = new Date().getTime() - SSDateU.dayInMilliSeconds * 109;
  
  public SSDataImportEvernoteHandler(final SSDBSQLI dbSQL) throws Exception{
    this.localWorkPath   = SSCoreConf.instGet().getSss().getLocalWorkPath();
  }
  
  public void setBasicEvernoteInfo(final SSDataImportEvernotePar par) throws Exception{
    
    if(par.authToken == null){
      par.authToken = SSServCaller.evernoteUsersAuthTokenGet(par.user);
    }
    
    evernoteInfo          = SSServCaller.evernoteNoteStoreGet (par.user, par.authToken);
    evernoteInfo.userName = SSLabel.get(evernoteInfo.userStore.getUser().getUsername());
    
    this.userUri         = 
      SSServCaller.authRegisterUser(
        par.user, 
        SSLabel.get(par.authEmail),//evernoteInfo.userName,
        par.authEmail,
        "1234", 
        false,
        false,
        false);
    
    SSServCaller.evernoteUserAdd(
      this.userUri,  
      par.authToken, 
      false);
  }
  
  public void setUSN() throws Exception{
    
    SSServCaller.evernoteUSNSet(
      this.userUri,
      evernoteInfo.authToken, 
      evernoteInfo.noteStoreSyncChunk.getUpdateCount(),
      false);
  }
  
  public void setSharedNotebooks() throws Exception{
    
    sharedNotebooks     = SSServCaller.evernoteNotebooksSharedGet (evernoteInfo.noteStore);
    sharedNotebookGuids = getSharedNotebookGuids   (sharedNotebooks);
  }
  
  public void handleNotebooks() throws Exception{
    
    final List<Notebook> notebooks      = evernoteInfo.noteStoreSyncChunk.getNotebooks();
    SSUri                notebookUri;
    SSLabel              notebookLabel;
    
    if(notebooks == null){
      return;
    }
    
    for(Notebook notebook : notebooks){
      
      notebookUri      = getNormalOrSharedNotebookUri         (evernoteInfo.userName,    notebook, sharedNotebookGuids);
      notebookLabel    = getNormalOrSharedNotebookLabel       (notebook);
      
      addNotebook(
        notebookUri, 
        notebookLabel,  
        notebook.getServiceCreated());
      
      addNotebookUEs(
        notebookUri, 
        notebook);
    }
  }
  
  private void addNotebook(
    final SSUri    notebookUri,
    final SSLabel  notebookLabel,
    final Long     notebookCreationTime) throws Exception{
    
    ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityUpdate(
      new SSEntityUpdatePar(
        null,
        null,
        userUri,
        notebookUri,
        null, //uriAlternative,
        SSEntityE.evernoteNotebook, //type,
        notebookLabel, //label
        null, //description,
        null, //comments,
        null, //downloads,
        null, //screenShots,
        null, //images,
        null, //videos,
        null, //entitiesToAttach,
        notebookCreationTime, //creationTime,
        null, //read,
        false, //withUserRestriction
        false)); //shouldCommit)
            
    SSServCaller.evalLog(
      userUri, 
      SSToolContextE.evernoteImport, 
      userUri, 
      SSEvalLogE.addNotebook, 
      notebookUri, 
      null, 
      SSUri.asListWithoutNullAndEmpty(), 
      SSUri.asListWithoutNullAndEmpty());
  }
  
  private void addNotebookUEs(
    final SSUri    notebookUri,
    final Notebook notebook) throws Exception{
    
    final List<SSUE> existingCreationUEs =
      SSServCaller.uEsGet(
        userUri,
        userUri,
        notebookUri,
        SSUEE.asListWithoutEmptyAndNull(SSUEE.evernoteNotebookCreate),
        null,
        null);
    
    if(existingCreationUEs.isEmpty()){
      
      ((SSUEServerI) SSServReg.getServ(SSUEServerI.class)).uEAdd(
        new SSUEAddPar(
          null,
          null,
          userUri,
          notebookUri,
          SSUEE.evernoteNotebookCreate,
          SSStrU.empty,
          notebook.getServiceCreated(),
          false)); //shouldCommit
    }
    
    final List<SSUE> existingUpdatingUEs =
      SSServCaller.uEsGet(
        userUri,
        userUri,
        notebookUri,
        SSUEE.asListWithoutEmptyAndNull(SSUEE.evernoteNotebookUpdate),
        notebook.getServiceUpdated(),
        notebook.getServiceUpdated());
    
    if(existingUpdatingUEs.isEmpty()){
      
      ((SSUEServerI) SSServReg.getServ(SSUEServerI.class)).uEAdd(
        new SSUEAddPar(
          null,
          null,
          userUri,
          notebookUri,
          SSUEE.evernoteNotebookUpdate,
          SSStrU.empty,
          notebook.getServiceUpdated(),
          false)); //shouldCommit
    }
  }
  
  public void handleLinkedNotebooks() throws Exception{

    final List<LinkedNotebook> linkedNotebooks = evernoteInfo.noteStoreSyncChunk.getLinkedNotebooks();
    int                        timeCounter     = 1;
    SSUri                      notebookUri;
    Long                       creationTimeForLinkedNotebook;
    
    if(linkedNotebooks == null){
      return;
    }
    
    for(LinkedNotebook linkedNotebook : linkedNotebooks){
      
      notebookUri                   = getLinkedNotebookUri     (linkedNotebook);
      creationTimeForLinkedNotebook = new Date().getTime() - (SSDateU.dayInMilliSeconds * timeCounter);
      timeCounter++;
      
      addNotebook(
        notebookUri, 
        getLinkedNotebookLabel(
          linkedNotebook), 
        creationTimeForLinkedNotebook);
      
      addLinkedNotebookUEs(
        notebookUri, 
        creationTimeForLinkedNotebook);
    }
  }
  
  private void addLinkedNotebookUEs(
    final SSUri notebookUri,
    final Long  creationTimeForLinkedNotebook) throws Exception {
    
    final List<SSUE> existingUEs =
      SSServCaller.uEsGet(
        userUri,
        userUri,
        notebookUri,
        SSUEE.asListWithoutEmptyAndNull(SSUEE.evernoteNotebookFollow),
        creationTimeForLinkedNotebook,
        creationTimeForLinkedNotebook);
    
    if(!existingUEs.isEmpty()){
      return;
    }
    
    ((SSUEServerI) SSServReg.getServ(SSUEServerI.class)).uEAdd(
      new SSUEAddPar(
        null,
        null,
        userUri,
        notebookUri,
        SSUEE.evernoteNotebookFollow,
        SSStrU.empty,
        creationTimeForLinkedNotebook,
        false)); //shouldCommit
  }
  
  public void handleNotes() throws Exception{
    
    final List<Note>     notes = evernoteInfo.noteStoreSyncChunk.getNotes();
    Note                 noteWithContent;
    Notebook             notebook;
    SSUri                notebookUri;
    SSUri                noteUri;
    List<String>         noteTagNames;
    
    if(notes == null){
      return;
    }
    
    for(Note note : notes){
      
      noteUri          = getNormalOrSharedNoteUri        (evernoteInfo,           note);
      notebook         = SSServCaller.evernoteNotebookGet                         (evernoteInfo.noteStore, note.getNotebookGuid());
      noteWithContent  = SSServCaller.evernoteNoteGet                             (evernoteInfo.noteStore, note.getGuid(), true);
      notebookUri      = getNormalOrSharedNotebookUri    (evernoteInfo.userName,  notebook, sharedNotebookGuids);
      
      addNote(
        noteUri, 
        getNoteLabel(
          note), 
        note, 
        notebookUri);
      
      noteTagNames = SSServCaller.evernoteNoteTagNamesGet(evernoteInfo.noteStore, note.getGuid());
        
      ((SSTagServerI) SSServReg.getServ(SSTagServerI.class)).tagsAdd(
        new SSTagsAddPar(
          null,
          null,
          userUri,
          SSTagLabel.get(noteTagNames), //labels
          noteUri, //entity
          SSSpaceE.sharedSpace, //space
          note.getUpdated(), //creationTime
          false, //withUserRestriction
          false)); //shouldCommit
      
      for(String noteTag : noteTagNames){
       
        SSServCaller.evalLog(
          userUri,
          SSToolContextE.evernoteImport,
          userUri,
          SSEvalLogE.addTag,
          noteUri,
          noteTag,
          SSUri.asListWithoutNullAndEmpty(notebookUri),
          SSUri.asListWithoutNullAndEmpty());
      }
      
      addNoteUEs(
        note,
        noteUri);
      
      new SSDataImportEvernoteNoteContentHandler(
        userUri,
        noteWithContent,
        noteUri,
        evernoteInfo.noteStore,
        localWorkPath).handleNoteContent();
    }
  }
  
  private void addNoteUEs(
    final Note         note,
    final SSUri        noteUri) throws Exception {
    
    final List<SSUE> existingCreationUEs =
      SSServCaller.uEsGet(
        userUri,
        userUri,
        noteUri,
        SSUEE.asListWithoutEmptyAndNull(SSUEE.evernoteNoteCreate),
        null,
        null);
    
    if(existingCreationUEs.isEmpty()){
      
      ((SSUEServerI) SSServReg.getServ(SSUEServerI.class)).uEAdd(
        new SSUEAddPar(
          null,
          null,
          userUri,
          noteUri,
          SSUEE.evernoteNoteCreate,
          SSStrU.empty,
          note.getCreated(),
          false)); //shouldCommit
    }
    
    final List<SSUE> existingUpdateUEs =
      SSServCaller.uEsGet(
        userUri,
        userUri,
        noteUri,
        SSUEE.asListWithoutEmptyAndNull(SSUEE.evernoteNoteUpdate),
        note.getUpdated(),
        note.getUpdated());
    
    if(existingUpdateUEs.isEmpty()){
      
      ((SSUEServerI) SSServReg.getServ(SSUEServerI.class)).uEAdd(
        new SSUEAddPar(
          null,
          null,
          userUri,
          noteUri,
          SSUEE.evernoteNoteUpdate,
          SSStrU.empty,
          note.getUpdated(),
          false)); //shouldCommit
    }

    if(note.getDeleted() != 0L){
      
      final List<SSUE> existingDeleteUEs = 
        SSServCaller.uEsGet(
          userUri,
          userUri,
          noteUri,
          SSUEE.asListWithoutEmptyAndNull(SSUEE.evernoteNoteDelete),
          null,
          null);
      
      if(existingDeleteUEs.isEmpty()){
        
        ((SSUEServerI) SSServReg.getServ(SSUEServerI.class)).uEAdd(
          new SSUEAddPar(
            null,
            null,
            userUri,
            noteUri,
            SSUEE.evernoteNoteDelete,
            SSStrU.empty,
            note.getDeleted(),
            false)); //shouldCommit
      }
    }
    
    final NoteAttributes noteAttr = note.getAttributes();
    
    if(noteAttr == null){
      return;
    }
    
    if(noteAttr.getShareDate() != 0L){
  
      final List<SSUE> existingShareUEs =
        SSServCaller.uEsGet(
          userUri,
          userUri,
          noteUri,
          SSUEE.asListWithoutEmptyAndNull(SSUEE.evernoteNoteShare),
          noteAttr.getShareDate(),
          noteAttr.getShareDate());
      
      if(existingShareUEs.isEmpty()){
        
        ((SSUEServerI) SSServReg.getServ(SSUEServerI.class)).uEAdd(
          new SSUEAddPar(
            null,
            null,
            userUri,
            noteUri,
            SSUEE.evernoteNoteShare,
            SSStrU.empty,
            noteAttr.getShareDate(),
            false)); //shouldCommit
      }
    }
    
    if(noteAttr.getReminderDoneTime() != 0L){
      
      final List<SSUE> existingReminderUEs = 
        SSServCaller.uEsGet(
          userUri,
          userUri,
          noteUri,
          SSUEE.asListWithoutEmptyAndNull(SSUEE.evernoteReminderDone),
          noteAttr.getReminderDoneTime(),
          noteAttr.getReminderDoneTime());
      
      if(existingReminderUEs.isEmpty()){
       
        ((SSUEServerI) SSServReg.getServ(SSUEServerI.class)).uEAdd(
          new SSUEAddPar(
            null,
            null,
            userUri,
            noteUri,
            SSUEE.evernoteReminderDone,
            SSStrU.empty,
            noteAttr.getReminderDoneTime(),
            false)); //shouldCommit
      }
    }
    
    if(noteAttr.getReminderTime() != 0L){
      
      final List<SSUE> existingReminder2UEs = 
        SSServCaller.uEsGet(
          userUri,
          userUri,
          noteUri,
          SSUEE.asListWithoutEmptyAndNull(SSUEE.evernoteReminderCreate),
          noteAttr.getReminderTime(),
          noteAttr.getReminderTime());
      
      if(existingReminder2UEs.isEmpty()){
        
        ((SSUEServerI) SSServReg.getServ(SSUEServerI.class)).uEAdd(
          new SSUEAddPar(
            null,
            null,
            userUri,
            noteUri,
            SSUEE.evernoteReminderCreate,
            SSStrU.empty,
            noteAttr.getReminderTime(),
            false)); //shouldCommit
      }
    }
  }
  
  private void addNote(
    final SSUri   noteUri,
    final SSLabel noteLabel,
    final Note    note,
    final SSUri   notebookUri) throws Exception{
    
    final SSEntity nootebookEntity;
      
    try{
      
      ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityUpdate(
        new SSEntityUpdatePar(
          null,
          null,
          userUri,
          noteUri,
          null, //uriAlternative,
          SSEntityE.evernoteNote, //type,
          noteLabel, //label
          null, //description,
          null, //comments,
          null, //downloads,
          null, //screenShots,
          null, //images,
          null, //videos,
          null, //entitiesToAttach,
          note.getCreated(), //creationTime,
          null, //read,
          false, //withUserRestriction
          false)); //shouldCommit)
          
      nootebookEntity =
        ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityGet(
          new SSEntityGetPar(
            null,
            null,
            null,
            notebookUri, //entity
            null,  //forUser
            null, //label
            null, //type
            false, //withUserRestriction
            false, //invokeEntityHandlers
            null, //descPar
            true)); //logErr
      
      if(nootebookEntity == null){
        
        addNotebook(
          notebookUri, 
          null, 
          null);
      }
      
      SSServCaller.evernoteNoteAdd(
        this.userUri,
        notebookUri,
        noteUri,
        false);
      
      SSServCaller.evalLog(
        userUri,
        SSToolContextE.evernoteImport,
        userUri,
        SSEvalLogE.addNote,
        noteUri,
        null,
        SSUri.asListWithoutNullAndEmpty(notebookUri),
        SSUri.asListWithoutNullAndEmpty());
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void handleResources() throws Exception{
    
    final List<Resource> resources = evernoteInfo.noteStoreSyncChunk.getResources();
    Resource             resourceWithContent;
    SSUri                resourceUri;
    Note                 note;
    SSUri                noteUri;
    
    if(resources == null){
      return;
    }
    
    for(Resource resource : resources){

      resourceWithContent = SSServCaller.evernoteResourceGet                  (evernoteInfo.noteStore, resource.getGuid(), false);
      
      if(
        resourceWithContent.isSetHeight() &&
        resourceWithContent.isSetWidth()){
        
        if(
          resourceWithContent.getWidth()  <= 200||
          resourceWithContent.getHeight() <= 200){
          continue;
        }
      }else{
      }
      
      resourceWithContent = SSServCaller.evernoteResourceGet                  (evernoteInfo.noteStore, resource.getGuid(), true);
      resourceUri         = getResourceUri           (evernoteInfo, resource);
      note                = SSServCaller.evernoteNoteGet                      (evernoteInfo.noteStore, resource.getNoteGuid(), false);
      noteUri             = getNormalOrSharedNoteUri (evernoteInfo, note);
      
      addResource(
        resourceUri,
        getResourceLabel(
          resource,
          note),
        note.getUpdated(),
        noteUri);
      
      new SSDataImportEvernoteResourceContentHandler(
        userUri,
        resourceWithContent,
        resourceUri,
        localWorkPath).handleResourceContent();
      
      addResourceUEs(
        resourceUri, 
        note.getUpdated());
    }
  }
  
  private void addResourceUEs(
    final SSUri resourceUri,
    final Long  resourceAddTime) throws Exception{
    
    final List<SSUE> existingUEs =
      SSServCaller.uEsGet(
        userUri,
        userUri,
        resourceUri,
        SSUEE.asListWithoutEmptyAndNull(SSUEE.evernoteResourceAdd),
        resourceAddTime,
        resourceAddTime);
    
    if(existingUEs.isEmpty()){
      
      ((SSUEServerI) SSServReg.getServ(SSUEServerI.class)).uEAdd(
        new SSUEAddPar(
          null,
          null,
          userUri,
          resourceUri,
          SSUEE.evernoteResourceAdd,
          SSStrU.empty,
          resourceAddTime,
          false)); //shouldCommit
    }
  }

  private void addResource(
    final SSUri   resourceUri,
    final SSLabel resourceLabel,
    final Long    resourceAddTime,
    final SSUri   noteUri) throws Exception{
    
    ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityUpdate(
      new SSEntityUpdatePar(
        null,
        null,
        userUri,
        resourceUri,
        null, //uriAlternative,
        SSEntityE.evernoteResource, //type,
        resourceLabel, //label
        null, //description,
        null, //comments,
        null, //downloads,
        null, //screenShots,
        null, //images,
        null, //videos,
        null, //entitiesToAttach,
        resourceAddTime, //creationTime,
        null, //read,
        false, //withUserRestriction
        false)); //shouldCommit)
    
    SSServCaller.evernoteResourceAdd(
      this.userUri,
      noteUri, 
      resourceUri,
      false);
    
    SSServCaller.evalLog(
      userUri,
      SSToolContextE.evernoteImport,
      userUri,
      SSEvalLogE.addResource,
      resourceUri,
      null,
      SSUri.asListWithoutNullAndEmpty(noteUri),
      SSUri.asListWithoutNullAndEmpty());
  }
  
  private static List<String> getSharedNotebookGuids(final List<SharedNotebook> sharedNotebooks) {
    
    final List<String> sharedNotebookGuids = new ArrayList<>();
    
    if(sharedNotebooks == null){
      return sharedNotebookGuids;
    }
    
    for(SharedNotebook sharedNotebook : sharedNotebooks){
      sharedNotebookGuids.add(sharedNotebook.getNotebookGuid());
    }
    
    return sharedNotebookGuids;
  }
  
  private static SSUri getNormalOrSharedNotebookUri(SSLabel userName, Notebook notebook, List<String> sharedNotebookGuids) throws Exception{
    
    try{
     
      if(
        sharedNotebookGuids.contains   (notebook.getGuid()) &&
        !SSStrU.isEmpty                (notebook.getName())){
        return SSUri.get(createSharedNotebookUriStr(userName, notebook));
      }
      
    }catch(Exception error){}
    
    return getNotebookDefaultUri(notebook);
  }
  
  private static SSUri getNormalOrSharedNoteUri(SSEvernoteInfo evernoteInfo, Note note) throws Exception {
    return SSUri.get(evernoteInfo.shardUri + "view/notebook/" + note.getGuid());
  }
  
  private static SSUri getLinkedNotebookUri(LinkedNotebook linkedNotebook) throws Exception {
    return SSUri.get(linkedNotebook.getWebApiUrlPrefix() + "share/" + linkedNotebook.getShareKey());
  }
  
  private static SSUri getResourceUri(SSEvernoteInfo evernoteInfo, Resource resource) throws Exception{
    return SSUri.get(evernoteInfo.shardUri + "res/" + resource.getGuid());
  }
  
  private static String createSharedNotebookUriStr(SSLabel userName, Notebook notebook) throws Exception{
    
    //TODO dtheiler: check evernote environment to use here
    return SSLinkU.httpsEvernote + "pub/" + SSStrU.toStr(userName) + SSStrU.slash + notebook.getPublishing().getUri(); //7SSStrU.replaceAllBlanksSpecialCharactersDoubleDots(notebook.getName(), SSStrU.empty)
  }
  
  private static SSUri getNotebookDefaultUri(Notebook notebook) throws Exception{
    
    if(
      notebook                  == null ||
      SSStrU.isEmpty(notebook.getGuid())){
      return SSUri.get(SSLinkU.httpsEvernote);
    }
    
    return SSUri.get(SSLinkU.httpsEvernote + "Home.action#b=" + notebook.getGuid());
  }
  
  private static SSLabel getNormalOrSharedNotebookLabel(
    final Notebook notebook) throws Exception{
    
    try{
      final SSLabel tmpLabel = SSLabel.get(notebook.getName());

      if(tmpLabel == null){
        return getDefaultLabel();
      }else{
        return tmpLabel;
      }
    }catch(Exception error){
      return getDefaultLabel();
    }
  }
  
  private static SSLabel getLinkedNotebookLabel(
    final LinkedNotebook linkedNotebook) throws Exception{
    
    try{
      final SSLabel tmpLabel = SSLabel.get(linkedNotebook.getShareName());
      
      if(tmpLabel == null){
        return getDefaultLabel();
      }else{
        return tmpLabel;
      }
    }catch(Exception error){
      return getDefaultLabel();
    }
  }
  
  public static SSLabel getNoteLabel(
    final Note  note) throws Exception {
    
    try{
      final SSLabel tmpLabel = SSLabel.get(note.getTitle());
      
      if(tmpLabel == null){
        return getDefaultLabel();
      }else{
        return tmpLabel;
      }
    }catch(Exception error){
      return getDefaultLabel();
    }
  }
  
  private static SSLabel getResourceLabel(
    final Resource resource,
    final Note     note) throws Exception{
    
    try{
      
      if(
        SSObjU.isNull (resource, resource.getAttributes()) ||
        SSStrU.isEmpty(resource.getAttributes().getFileName())){
        
        return getNoteLabel(note);
      }
      
      return SSLabel.get(resource.getAttributes().getFileName());
    }catch(Exception error){
      return getDefaultLabel();
    }
  }
  
  private static SSLabel getDefaultLabel() throws Exception{
    return SSLabel.get("no label");
  }
}

  
//  public Boolean isSharedNootebook(SSUri notebookUri, SSLabel userName, Notebook notebook) {
//    return uriHelper.isSharedNotebookUri(userName, notebook, notebookUri);
//  }

  

//  public String getUserEmail(final SSEvernoteInfo evernoteInfo) throws Exception{
//    return evernoteInfo.userStore.getUser().getEmail();
//  }

//  private static SSLabel getLinkedNoteLabel(
//    final Note  note) throws Exception {
//    
//    try{
//      final SSLabel tmpLabel = SSLabel.get(note.getTitle());
//      
//      if(tmpLabel == null){
//        return getDefaultLabel();
//      }else{
//        return tmpLabel;
//      }
//    }catch(Exception error){
//      return getDefaultLabel();
//    }
//  }

//private static SSUri getLinkedNoteUri(LinkedNotebook linkedNotebook, Note note) throws Exception{
//    return SSUri.get(linkedNotebook.getWebApiUrlPrefix() + "share/view/" + linkedNotebook.getShareKey() + "?#n=" + note.getGuid());
//  }
//  
//  private static Boolean isSharedNotebookUri(SSLabel userName, Notebook notebook, SSUri notebookUri){
//    
//    String sharedNotebookUriStr;
//    
//    try{
//      sharedNotebookUriStr = createSharedNotebookUriStr(userName, notebook);
//    }catch(Exception error){
//      return false;
//    }
//    
//    if(
//      notebookUri == null || 
//      SSStrU.isEmpty(notebookUri.toString())){
//      return false;
//    }
//    
//    return notebookUri.toString().equals(sharedNotebookUriStr);
//  }
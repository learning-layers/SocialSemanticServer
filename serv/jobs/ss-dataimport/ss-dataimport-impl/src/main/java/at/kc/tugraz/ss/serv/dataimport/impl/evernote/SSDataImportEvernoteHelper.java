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

import at.kc.tugraz.socialserver.utils.SSDateU;
import at.kc.tugraz.socialserver.utils.SSFileExtU;
import at.kc.tugraz.socialserver.utils.SSFileU;
import at.kc.tugraz.socialserver.utils.SSLogU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.conf.conf.SSCoreConf;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSSpaceE;
import at.kc.tugraz.ss.serv.dataimport.datatypes.pars.SSDataImportEvernotePar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSCircleE;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteInfo;
import at.kc.tugraz.ss.serv.jobs.evernote.impl.helper.SSEvernoteHelper;
import at.kc.tugraz.ss.serv.jobs.evernote.impl.helper.SSEvernoteLabelHelper;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.serv.voc.serv.SSVoc;
import at.kc.tugraz.ss.service.userevent.datatypes.SSUEE;
import com.evernote.edam.type.LinkedNotebook;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.NoteAttributes;
import com.evernote.edam.type.Notebook;
import com.evernote.edam.type.Resource;
import com.evernote.edam.type.SharedNotebook;
import java.util.Date;
import java.util.List;

public class SSDataImportEvernoteHelper {
  
  private final  SSEvernoteHelper        evernoteHelper;
  private final  String                  localWorkPath;
  private        SSEvernoteInfo          evernoteInfo           = null;
  private        SSLabel                 userName               = null;
  private        SSUri                   userUri                = null;
  private        List<SharedNotebook>    sharedNotebooks        = null;
  private        List<String>            sharedNotebookGuids    = null;
  private        SSUri                   userCircle             = null;
  
  public SSDataImportEvernoteHelper(final SSDBSQLI dbSQL) throws Exception{
    this.localWorkPath   = SSCoreConf.instGet().getSsConf().getLocalWorkPath();
    this.evernoteHelper  = new SSEvernoteHelper(dbSQL);
  }
  
  public void setBasicEvernoteInfo(final SSDataImportEvernotePar par) throws Exception{
    
    if(par.authToken == null){
      par.authToken = evernoteHelper.sqlFct.getAuthToken(par.user);
    }
    
    this.evernoteInfo    = SSServCaller.evernoteNoteStoreGet (par.user, par.authToken);
    this.userName        = evernoteHelper.getUserName        (evernoteInfo);
    this.userUri         = SSServCaller.authRegisterUser     (par.user, userName, "1234", false);
    
    evernoteHelper.sqlFct.addUserIfNotExists(this.userUri, par.authToken);
    
    this.userCircle      =
      SSServCaller.entityCircleCreate(
        userUri,
        SSUri.asListWithoutNullAndEmpty(),
        SSUri.asListWithoutNullAndEmpty(),
        SSCircleE.priv,
        null,
        SSVoc.systemUserUri,
        null,
        false);
  }
  
  public void setSharedNotebooks() throws Exception{
    sharedNotebooks     = SSServCaller.evernoteNotebooksSharedGet (evernoteInfo.noteStore);
    sharedNotebookGuids = evernoteHelper.getSharedNotebookGuids   (sharedNotebooks);
  }
  
  public void handleNotebooks(final SSDataImportEvernotePar par) throws Exception{
    
    SSUri       notebookUri;
    SSLabel     notebookLabel;
    
    for(Notebook notebook : SSServCaller.evernoteNotebooksGet(evernoteInfo.noteStore)){
      
      notebookUri      = evernoteHelper.uriHelper.getNormalOrSharedNotebookUri      (userName,    notebook, sharedNotebookGuids);
//      isSharedNotebook = evernoteHelper.isSharedNootebook                           (notebookUri, userName, notebook);
      notebookLabel    = SSEvernoteLabelHelper.getNormalOrSharedNotebookLabel       (notebookUri, notebook);
      
      addNotebook(
        notebookUri, 
        notebookLabel,  
        notebook.getServiceCreated());
      
      addNotebookUEs(
        notebookUri, 
        notebook);
      
      handleNotes(
        userUri,
        notebookUri,
        notebookLabel,
        evernoteInfo,
        notebook);
    }
  }
  
  private void addNotebook(
    final SSUri    notebookUri,
    final SSLabel  notebookLabel,
    final Long     notebookCreationTime) throws Exception{
    
    SSServCaller.entityAddAtCreationTime(
      userUri,
      notebookUri,
      notebookLabel,
      notebookCreationTime,
      SSEntityE.evernoteNotebook,
      null,
      false);
    
    SSServCaller.entityUpdate(
      notebookUri,
      notebookLabel,
      null,
      false);
    
    SSServCaller.entityEntitiesToCircleAdd(
      userUri,
      userCircle,
      notebookUri,
      false);
  }
  
  private void addNotebookUEs(
    final SSUri    notebookUri,
    final Notebook notebook) throws Exception{
    
    SSServCaller.uEsRemove(
      userUri,
      notebookUri,
      false);
    
    SSServCaller.uEAddAtCreationTime(
      userUri,
      notebookUri,
      SSUEE.evernoteNotebookUpdate,
      SSStrU.empty,
      notebook.getServiceUpdated(),
      false);
  }
  
  public void handleLinkedNotebooks() throws Exception{

    int                     timeCounter = 1;
    SSUri                   notebookUri;
    Long                    creationTimeForLinkedNotebook;
    
    for(LinkedNotebook linkedNotebook : SSServCaller.evernoteNotebooksLinkedGet(evernoteInfo.noteStore)){
      
      notebookUri                   = evernoteHelper.uriHelper.getLinkedNotebookUri     (linkedNotebook);
      creationTimeForLinkedNotebook = new Date().getTime() - (SSDateU.dayInMilliSeconds * timeCounter);
      timeCounter++;
      
      addNotebook(
        notebookUri, 
        SSEvernoteLabelHelper.getLinkedNotebookLabel(
          linkedNotebook, 
          notebookUri), 
        creationTimeForLinkedNotebook);
      
      addLinkedNotebookUEs(
        notebookUri, 
        creationTimeForLinkedNotebook);
      
//      for(Note note : SSServCaller.getEvernoteLinkedNotes(evernoteInfo.noteStore, linkedNotebook)){
//        
//        noteUri   = evernoteHelper.uriHelper.getLinkedNoteUri     (linkedNotebook, note);
//        noteLabel = evernoteHelper.labelHelper.getLinkedNoteLabel (note, noteUri);
//        
//        SSServCaller.addEntityAtCreationTime(
//          userUri,
//          noteUri,
//          noteLabel,
//          creationTimeForLinkedNotebook,
//          SSEntityEnum.evernoteNote);
//        
//        SSServCaller.uEAddAtCreationTime(
//          userUri,
//          noteUri,
//          SSUEEnum.evernoteNoteFollow,
//          SSStrU.empty,
//          creationTimeForLinkedNotebook,
//          shouldCommit);
//        
//        if(note.getResources() != null){
//          
//          for(Resource resource : note.getResources()){
//            
//            resourceUri   = SSUri.get     (linkedNotebook.getWebApiUrlPrefix() + "share/" + linkedNotebook.getShareKey() + "/res/" + resource.getGuid());
//            resourceLabel = SSLabelStr.get(SSStrU.toStr(resourceUri));
//            
//            SSServCaller.addEntityAtCreationTime(
//              userUri,
//              resourceUri,
//              resourceLabel,
//              note.getUpdated(),
//              SSEntityEnum.evernoteResource);
//            
//            SSServCaller.uEAddAtCreationTime(
//              userUri,
//              resourceUri,
//              SSUEEnum.evernoteResourceFollow,
//              SSStrU.empty,
//              creationTimeForLinkedNotebook,
//              shouldCommit);
//          }
//        }
//      }
    }
  }
  
  private void addLinkedNotebookUEs(
    final SSUri notebookUri,
    final Long  creationTimeForLinkedNotebook) throws Exception {
    
    SSServCaller.uEsRemove(
      userUri,
      notebookUri,
      false);
    
    SSServCaller.uEAddAtCreationTime(
      userUri,
      notebookUri,
      SSUEE.evernoteNotebookFollow,
      SSStrU.empty,
      creationTimeForLinkedNotebook,
      false);
  }
  
  private void handleNotes(
    final SSUri                userUri,
    final SSUri                notebookUri, 
    final SSLabel              notebookLabel, 
    final SSEvernoteInfo       evernoteInfo,
    final Notebook             notebook) throws Exception{
    
    SSUri   noteUri;
    
    for(Note note : SSServCaller.evernoteNotesGet(evernoteInfo.noteStore, notebook.getGuid())){
      
      noteUri = evernoteHelper.uriHelper.getNormalOrSharedNoteUri   (evernoteInfo, note);
      
      addNote(
        noteUri, 
        SSEvernoteLabelHelper.getNoteLabel(
          note,         
          noteUri), 
        note, 
        notebookUri);
      
      addNoteTags(
        note, 
        noteUri,
        notebookLabel);
      
      addNoteAndTagUEs(
        note, 
        noteUri);
      
      handleNoteContent(
        userUri, 
        note,    
        noteUri);
      
      handleResources(
        userUri,
        noteUri, 
        notebookLabel, 
        evernoteInfo, 
        note);
    }
  }
  
  private void addNoteAndTagUEs(
    final Note  note,
    final SSUri noteUri) throws Exception {
    
    SSServCaller.uEsRemove(
      userUri,
      noteUri,
      false);
    
    SSServCaller.uEAddAtCreationTime(
      userUri,
      noteUri,
      SSUEE.evernoteNoteUpdate,
      SSStrU.empty,
      note.getUpdated(),
      false);
    
    if(note.getDeleted() != 0L){
      
      SSServCaller.uEAddAtCreationTime(
        userUri,
        noteUri,
        SSUEE.evernoteNoteDelete,
        SSStrU.empty,
        note.getDeleted(),
        false);
    }
    
    for(String tag : evernoteInfo.noteStore.getNoteTagNames(note.getGuid())){
      
      SSServCaller.uEAddAtCreationTime(
        userUri,
        noteUri,
        SSUEE.addPrivateTag,
        tag,
        note.getUpdated(),
        false);
    }
    
    final NoteAttributes noteAttr = note.getAttributes();
    
    if(noteAttr == null){
      return;
    }
    
    if(noteAttr.getShareDate() != 0L){
  
      SSServCaller.uEAddAtCreationTime(
        userUri,
        noteUri,
        SSUEE.evernoteNoteShare,
        SSStrU.empty,
        noteAttr.getShareDate(),
        false);
    }
    
    if(noteAttr.getReminderDoneTime() != 0L){
      
      SSServCaller.uEAddAtCreationTime(
        userUri,
        noteUri,
        SSUEE.evernoteReminderDone,
        SSStrU.empty,
        noteAttr.getReminderDoneTime(),
        false);
    }
    
    if(noteAttr.getReminderTime() != 0L){
      
      SSServCaller.uEAddAtCreationTime(
        userUri,
        noteUri,
        SSUEE.evernoteReminderCreate,
        SSStrU.empty,
        noteAttr.getReminderTime(),
        false);
    }
  }
  
  private void addNoteTags(
    final Note    note, 
    final SSUri   noteUri,
    final SSLabel notebookLabel) throws Exception{
    
    SSServCaller.tagsAddAtCreationTime(
      userUri,
      noteUri,
      evernoteInfo.noteStore.getNoteTagNames(note.getGuid()),
      SSSpaceE.privateSpace,
      note.getUpdated(),
      false);
    
    if(!SSStrU.isEmpty(notebookLabel)){
      
      SSServCaller.tagAdd(
        userUri,
        noteUri,
        SSStrU.toStr(notebookLabel),
        SSSpaceE.privateSpace,
        false);
    }
  }
  
  private void addNote(
    final SSUri   noteUri,
    final SSLabel noteLabel,
    final Note    note,
    final SSUri   notebookUri) throws Exception{
    
    SSServCaller.entityAddAtCreationTime(
      userUri,
      noteUri,
      noteLabel,
      note.getCreated(),
      SSEntityE.evernoteNote,
      null,
      false);
    
    SSServCaller.entityUpdate(
      noteUri,
      noteLabel,
      null,
      false);
    
    SSServCaller.entityEntitiesToCircleAdd(
      userUri,
      userCircle,
      noteUri,
      false);
    
    evernoteHelper.sqlFct.addNoteIfNotExists (notebookUri, noteUri);
  }
  
  private void handleResources(
    final SSUri                userUri,
    final SSUri                noteUri,
    final SSLabel              notebookLabel,
    final SSEvernoteInfo       evernoteInfo,
    final Note                 note) throws Exception{
    
    SSUri      resourceUri;
    
    if(
      note                == null ||
      note.getResources() == null){
      return;
    }
    
    for(Resource resource : note.getResources()){
      
      resourceUri = evernoteHelper.uriHelper.getResourceUri     (evernoteInfo, resource);
      
      addResource(
        resourceUri,
        SSEvernoteLabelHelper.getResourceLabel(
          resource,
          resourceUri),
        note.getUpdated(),
        noteUri);
      
      addResourceTags(
        resourceUri,
        notebookLabel);
      
      addResourceUEs(
        resourceUri, 
        note.getUpdated());
    }
  }
  
  private void addResourceUEs(
    final SSUri resourceUri,
    final Long  resourceAddTime) throws Exception{
    
    SSServCaller.uEsRemove(
      userUri,
      resourceUri,
      false);
    
    SSServCaller.uEAddAtCreationTime(
      userUri,
      resourceUri,
      SSUEE.evernoteResourceAdd,
      SSStrU.empty,
      resourceAddTime,
      false);
  }
  
  private void addResource(
    final SSUri   resourceUri,
    final SSLabel resourceLabel,
    final Long    resourceAddTime,
    final SSUri   noteUri) throws Exception{
    
    SSServCaller.entityAddAtCreationTime(
      userUri,
      resourceUri,
      resourceLabel,
      resourceAddTime,
      SSEntityE.evernoteResource,
      null,
      false);
    
    SSServCaller.entityUpdate(
      resourceUri,
      resourceLabel,
      null,
      false);
    
    SSServCaller.entityEntitiesToCircleAdd(
      userUri,
      userCircle,
      resourceUri,
      false);
    
    evernoteHelper.sqlFct.addResourceIfNotExists(
      noteUri, 
      resourceUri);
  }
  
  private void addResourceTags(
    final SSUri   resourceUri,
    final SSLabel notebookLabel) throws Exception{
    
    if(!SSStrU.isEmpty(notebookLabel)){
      
      SSServCaller.tagAdd(
        userUri,
        resourceUri,
        SSStrU.toStr(notebookLabel),
        SSSpaceE.privateSpace,
        false);
    }    
  }
  
  //TODO dtheiler: currently works with local file repository only (not web dav or any remote stuff; even ont if localWorkPath != local file repo path)
  private void handleNoteContent(
    final SSUri user,
    final Note  note,
    final SSUri noteUri) throws Exception{
    
    try{
      final SSUri  pngFileUri        = SSServCaller.fileCreateUri                 (userUri, SSFileExtU.png);
      final String pngFilePath       = localWorkPath + SSServCaller.fileIDFromURI (user,    pngFileUri);
      final String xhtmlFilePath     = localWorkPath + SSServCaller.fileIDFromURI (userUri, SSServCaller.fileCreateUri     (userUri, SSFileExtU.xhtml));   //localWorkPath + SSEntityE.evernoteNote + SSStrU.underline + note.getGuid() + SSStrU.dot + SSFileExtU.xhtml;
      final String pdfFilePath       = localWorkPath + SSServCaller.fileIDFromURI (userUri, SSServCaller.fileCreateUri     (userUri, SSFileExtU.pdf));     //localWorkPath + SSEntityE.evernoteNote + SSStrU.underline + note.getGuid() + SSStrU.dot + SSFileExtU.pdf;
      
      try{
        
        SSFileU.writeStr              (note.getContent(), xhtmlFilePath);
        SSFileU.writePDFFromXHTML     (pdfFilePath,       xhtmlFilePath);
        SSFileU.writeScaledPNGFromPDF (pdfFilePath,       pngFilePath);
      }catch(Exception error){
        SSLogU.warn("thumbnail for evernote note couldnt be created");
        return;
      }finally{
        
        try{
          SSFileU.delFile(xhtmlFilePath);
          SSFileU.delFile(pdfFilePath);
        }catch(Exception error){}
      }
        
      SSServCaller.entityAdd(
        user,
        pngFileUri,
        null,
        SSEntityE.thumbnail,
        null,
        false);
      
      SSServCaller.entityThumbsRemove(
        noteUri,
        false);
      
      SSServCaller.entityThumbAdd(
        user,
        noteUri,
        pngFileUri,
        false);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}
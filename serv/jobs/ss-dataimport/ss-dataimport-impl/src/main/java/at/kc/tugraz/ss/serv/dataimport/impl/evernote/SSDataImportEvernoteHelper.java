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
import at.kc.tugraz.ss.conf.conf.SSCoreConf;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.dataimport.datatypes.pars.SSDataImportEvernotePar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSCircleE;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteInfo;
import at.kc.tugraz.ss.serv.jobs.evernote.impl.helper.SSEvernoteHelper;
import at.kc.tugraz.ss.serv.jobs.evernote.impl.helper.SSEvernoteLabelHelper;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.serv.voc.serv.SSVoc;
import com.evernote.edam.type.LinkedNotebook;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.Notebook;
import com.evernote.edam.type.Resource;
import com.evernote.edam.type.SharedNotebook;
import java.util.Date;
import java.util.List;

public class SSDataImportEvernoteHelper {
  
  private final SSEvernoteHelper         evernoteHelper;
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
    
    this.evernoteInfo    = SSServCaller.evernoteNoteStoreGet (par.user, par.authToken);
    this.userName        = evernoteHelper.getUserName        (evernoteInfo);
    this.userUri         = SSServCaller.authRegisterUser     (par.user, userName, "1234", false);
    this.userCircle      = SSServCaller.entityCircleCreate(
      userUri,
      SSUri.asListWithoutNullAndEmpty(),
      SSUri.asListWithoutNullAndEmpty(),
      SSCircleE.priv,
      null,
      SSVoc.systemUserUri,
      null,
      false);
  }
  
  public void handleSharedNotebooks() throws Exception{
    sharedNotebooks     = SSServCaller.evernoteNotebooksSharedGet (evernoteInfo.noteStore);
    sharedNotebookGuids = evernoteHelper.getSharedNotebookGuids   (sharedNotebooks);
  }
  
  public void handleNotebooks(final SSDataImportEvernotePar par) throws Exception{
    
    SSUri       notebookUri;
    SSLabel     notebookLabel;
    Boolean     isSharedNotebook;
    
    for(Notebook notebook : SSServCaller.evernoteNotebooksGet(evernoteInfo.noteStore)){
      
      notebookUri      = evernoteHelper.uriHelper.getNormalOrSharedNotebookUri      (userName,    notebook, sharedNotebookGuids);
      isSharedNotebook = evernoteHelper.isSharedNootebook                           (notebookUri, userName, notebook);
      notebookLabel    = SSEvernoteLabelHelper.getNormalOrSharedNotebookLabel       (notebookUri, notebook);
      
      SSServCaller.entityAddAtCreationTime(
        userUri,
        notebookUri,
        notebookLabel,
        notebook.getServiceCreated(),
        SSEntityE.evernoteNotebook,
        null,
        false);
      
      SSServCaller.entityEntitiesToCircleAdd(
        userUri, 
        userCircle, 
        notebookUri,
        false);
      
      evernoteHelper.ueHelper.addUEsForNormalNotebook (userUri,          notebookUri, notebook);
      evernoteHelper.ueHelper.addUEsForSharedNotebook (isSharedNotebook, userUri,     notebook, notebookUri, sharedNotebooks);
      
      handleEvernoteNotes(
        userUri,
        notebookUri,
        evernoteInfo,
        notebook,
        isSharedNotebook);
    }
  }
  
  public void handleLinkedNotebooks() throws Exception{

    int                     timeCounter = 1;
    SSUri                   notebookUri;
    SSLabel              notebookLabel;
    SSLabel              noteLabel;
    Long                    creationTimeForLinkedNotebook;
    SSUri                   noteUri;
    SSUri                   resourceUri;
    SSLabel              resourceLabel;
    
    for(LinkedNotebook linkedNotebook : SSServCaller.evernoteNotebooksLinkedGet(evernoteInfo.noteStore)){
      
      notebookUri   = evernoteHelper.uriHelper.getLinkedNotebookUri     (linkedNotebook);
      notebookLabel = SSEvernoteLabelHelper.getLinkedNotebookLabel      (linkedNotebook, notebookUri);
      
      creationTimeForLinkedNotebook = new Date().getTime() - (SSDateU.dayInMilliSeconds * timeCounter);
      timeCounter++;
      
      SSServCaller.entityAddAtCreationTime(
        userUri,
        notebookUri,
        notebookLabel,
        creationTimeForLinkedNotebook,
        SSEntityE.evernoteNotebook,
        null,
        false);
      
      SSServCaller.entityEntitiesToCircleAdd(
        userUri, 
        userCircle, 
        notebookUri,
        false);
      
      evernoteHelper.ueHelper.addUEsForLinkedNotebook(
        userUri,
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
  
  private void handleEvernoteNotes(
    final SSUri                userUri,
    final SSUri                notebookUri, 
    final SSEvernoteInfo       evernoteInfo,
    final Notebook             notebook,
    final Boolean              isSharedNotebook) throws Exception{
    
    SSUri   noteUri;
    SSLabel noteLabel;
    
    for(Note note : SSServCaller.evernoteNotesGet(evernoteInfo.noteStore, notebook.getGuid())){
      
      noteUri   = evernoteHelper.uriHelper.getNormalOrSharedNoteUri   (evernoteInfo, note);
      noteLabel = SSEvernoteLabelHelper.getNoteLabel                  (note,         noteUri);
      
      SSServCaller.entityAddAtCreationTime(
        userUri,
        noteUri,
        noteLabel,
        note.getCreated(),
        SSEntityE.evernoteNote,
        null,
        false);
      
      SSServCaller.entityEntitiesToCircleAdd(
        userUri, 
        userCircle, 
        noteUri,
        false);
      
      evernoteHelper.sqlFct.addNote                      (notebookUri,      noteUri);
      evernoteHelper.ueHelper.addUEsAndTagsForNormalNote (userUri,          note,    noteUri);
      evernoteHelper.ueHelper.addUEsForSharedNote        (isSharedNotebook, userUri, notebook, noteUri, sharedNotebooks);
      evernoteHelper.ueHelper.addUEsForNoteAttrs         (userUri,          note,    noteUri);
      
      handleEvernoteNoteContent (userUri, note, noteUri);
      handleEvernoteResources   (userUri, noteUri, notebook, evernoteInfo, note, isSharedNotebook, sharedNotebooks);
    }
  }
  
  private void handleEvernoteResources(
    final SSUri                userUri,
    final SSUri                noteUri,
    final Notebook             notebook,
    final SSEvernoteInfo       evernoteInfo,
    final Note                 note,
    final Boolean              isSharedNotebook,
    final List<SharedNotebook> sharedNotebooks) throws Exception{
    
    SSUri      resourceUri;
    SSLabel resourceLabel;
    
    if(
      note                == null ||
      note.getResources() == null){
      return;
    }
    
    for(Resource resource : note.getResources()){
      
      resourceUri   = evernoteHelper.uriHelper.getResourceUri     (evernoteInfo, resource);
      resourceLabel = SSEvernoteLabelHelper.getResourceLabel      (resource,     resourceUri);
      
      SSServCaller.entityAddAtCreationTime(
        userUri,
        resourceUri,
        resourceLabel,
        note.getUpdated(),
        SSEntityE.evernoteResource,
        null,
        false);
      
      SSServCaller.entityEntitiesToCircleAdd(
        userUri,
        userCircle,
        resourceUri,
        false);
      
      evernoteHelper.sqlFct.addResource               (noteUri, resourceUri);
      evernoteHelper.ueHelper.addUEsForResource       (userUri, resourceUri, note);
      evernoteHelper.ueHelper.addUEsForSharedResource (userUri, resourceUri, notebook, sharedNotebooks, isSharedNotebook);
    }
  }
  
  //TODO dtheiler: currently works with local file repository only (not web dav or any remote stuff; even ont if localWorkPath != local file repo path)
  private void handleEvernoteNoteContent(
    final SSUri user,
    final Note  note,
    final SSUri noteUri){
    
    try{
      final SSUri  pngFileUri        = SSServCaller.fileCreateUri                 (userUri, SSFileExtU.png);
      final String pngFilePath       = localWorkPath + SSServCaller.fileIDFromURI (user, pngFileUri);
      final String xhtmlFilePath     = localWorkPath + SSServCaller.fileIDFromURI (userUri, SSServCaller.fileCreateUri     (userUri, SSFileExtU.xhtml));   //localWorkPath + SSEntityE.evernoteNote + SSStrU.underline + note.getGuid() + SSStrU.dot + SSFileExtU.xhtml;
      final String pdfFilePath       = localWorkPath + SSServCaller.fileIDFromURI (userUri, SSServCaller.fileCreateUri     (userUri, SSFileExtU.pdf));     //localWorkPath + SSEntityE.evernoteNote + SSStrU.underline + note.getGuid() + SSStrU.dot + SSFileExtU.pdf;
      
      SSFileU.writeStr              (note.getContent(), xhtmlFilePath);
      SSFileU.writePDFFromXHTML     (pdfFilePath,       xhtmlFilePath);
      SSFileU.writeScaledPNGFromPDF (pdfFilePath,       pngFilePath);
        
      SSServCaller.entityAdd(
        user,
        pngFileUri,
        null,
        SSEntityE.thumbnail,
        null,
        false);
      
      SSServCaller.entityThumbAdd(
        user,
        noteUri,
        pngFileUri,
        false);
      
    }catch(Exception error){
      SSLogU.warn("thumbnail for evernote note couldnt be created");
    }
  }
}

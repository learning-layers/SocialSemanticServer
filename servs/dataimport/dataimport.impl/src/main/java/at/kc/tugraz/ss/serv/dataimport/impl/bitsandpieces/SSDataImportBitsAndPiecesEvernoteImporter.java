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
package at.kc.tugraz.ss.serv.dataimport.impl.bitsandpieces;

import at.kc.tugraz.ss.serv.dataimport.conf.*;
import at.tugraz.sss.serv.util.SSDateU;
import at.tugraz.sss.serv.util.SSLinkU;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.enums.SSSpaceE;
import at.tugraz.sss.serv.datatype.enums.SSToolContextE;
import at.kc.tugraz.ss.serv.dataimport.datatypes.pars.SSDataImportBitsAndPiecesPar;
import at.kc.tugraz.ss.serv.dataimport.impl.SSDataImportImpl;
import at.kc.tugraz.ss.serv.dataimport.impl.evernote.SSDataImportEvernoteNoteContentHandler;
import at.tugraz.sss.serv.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.serv.jobs.evernote.api.SSEvernoteServerI;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteInfo;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteNoteGetPar;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteNoteStoreGetPar;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteNoteTagNamesGetPar;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteNotebookGetPar;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteNotebooksSharedGetPar;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteResourceGetPar;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteUSNSetPar;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteUserAddPar;
import at.kc.tugraz.ss.service.filerepo.api.SSFileRepoServerI;
import at.kc.tugraz.ss.service.tag.api.SSTagServerI;
import at.kc.tugraz.ss.service.tag.datatypes.SSTagLabel;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagsAddPar;
import at.kc.tugraz.ss.service.userevent.api.SSUEServerI;
import at.kc.tugraz.ss.service.userevent.datatypes.SSUEE;
import at.kc.tugraz.ss.service.userevent.datatypes.pars.SSUEAddPar;
import at.kc.tugraz.ss.service.userevent.datatypes.pars.SSUEsGetPar;
import at.tugraz.sss.serv.datatype.SSEntity;
import at.tugraz.sss.serv.util.SSFileExtE;
import at.tugraz.sss.serv.util.SSLogU;
import at.tugraz.sss.serv.util.SSMimeTypeE;
import at.tugraz.sss.serv.reg.SSServErrReg;
import at.tugraz.sss.servs.file.datatype.par.SSEntityFileAddPar;
import com.evernote.edam.type.LinkedNotebook;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.Notebook;
import com.evernote.edam.type.Resource;
import com.evernote.edam.type.SharedNotebook;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import sss.serv.eval.api.SSEvalServerI;
import sss.serv.eval.datatypes.SSEvalLogE;
import sss.serv.eval.datatypes.par.SSEvalLogPar;

public class SSDataImportBitsAndPiecesEvernoteImporter {
  
  private final SSDataImportBitsAndPiecesPar     par;
  private final SSFileRepoServerI                fileServ;
  private final SSEvernoteServerI                evernoteServ;
  private final SSUEServerI                      ueServ;
  private final SSTagServerI                     tagServ;
  private final SSEvalServerI                    evalServ;
  private final SSUri                            userUri;
  private final SSDataImportBitsAndPiecesMiscFct miscFct;
  private final SSDataImportConf                 conf;
  private final List<String>                     sharedNotebookGuids      = new ArrayList<>();
  private       SSEvernoteInfo                   evernoteInfo             = null;
  private       List<SharedNotebook>             sharedNotebooks          = null;
  
  public SSDataImportBitsAndPiecesEvernoteImporter(
    final SSDataImportConf             conf,
    final SSDataImportBitsAndPiecesPar par, 
    final SSEntityServerI              entityServ,
    final SSFileRepoServerI            fileServ,
    final SSEvernoteServerI            evernoteServ,
    final SSUEServerI                  ueServ,
    final SSTagServerI                 tagServ,
    final SSEvalServerI                evalServ,
    final SSUri                        userUri) throws Exception{
    
    this.conf            = conf;
    this.par             = par;
    this.fileServ        = fileServ;
    this.evernoteServ    = evernoteServ;
    this.ueServ          = ueServ;
    this.tagServ         = tagServ;
    this.evalServ        = evalServ;
    this.userUri         = userUri;
    
    this.miscFct =
      new SSDataImportBitsAndPiecesMiscFct(
        par,
        entityServ,
        evernoteServ,
        ueServ,
        evalServ,
        userUri);
  }
  
  public void handle() throws Exception{
    
    try{
  
      SSLogU.info("start B&P evernote import for " +  par.authEmail);
      
      setBasicEvernoteInfo  ();
      
      handleLinkedNotebooks ();
      setSharedNotebooks    ();
      handleNotebooks       ();
      handleNotes           ();
      handleResources       ();
      
      setUSN();
      
      SSLogU.info("end B&P evernote import for evernote account " + par.authEmail);
      
    }catch(Exception error){
      SSLogU.err("B&P evernote import failed for " + par.authEmail);
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private void setBasicEvernoteInfo() throws Exception{
    
    evernoteInfo = 
      evernoteServ.evernoteNoteStoreGet(
        new SSEvernoteNoteStoreGetPar(
          par.user, 
          par.authToken, 
          par.authEmail));
    
    evernoteInfo.userName = SSLabel.get(evernoteInfo.userStore.getUser().getUsername());
    
    evernoteServ.evernoteUserAdd(
      new SSEvernoteUserAddPar(
        this.userUri,
        par.authToken, 
        false));
  }
  
  private void setUSN() throws Exception{
    
    final Integer usn;
    
    if(evernoteInfo.noteStoreSyncChunk.isSetChunkHighUSN()){
      usn = evernoteInfo.noteStoreSyncChunk.getChunkHighUSN();
    }else{
      if(evernoteInfo.noteStoreSyncChunk.isSetUpdateCount()){
        usn = evernoteInfo.noteStoreSyncChunk.getUpdateCount();
      }else{
        SSLogU.warn("couldnt set USN as Evernote didnt provide one");
        usn = 0;
      }
    }
    
    evernoteServ.evernoteUSNSet(
      new SSEvernoteUSNSetPar(
        this.userUri,
        evernoteInfo.authToken,
        usn,
        false));
  }
  
  private void setSharedNotebooks() throws Exception{
    
    sharedNotebooks     = 
      evernoteServ.evernoteNotebooksSharedGet (
        new SSEvernoteNotebooksSharedGetPar(
          userUri, 
          evernoteInfo.noteStore));
    
    sharedNotebookGuids.clear();

    if(sharedNotebooks == null){
      return;
    }
    
    sharedNotebooks.stream().forEach((sharedNotebook)->{
      sharedNotebookGuids.add(sharedNotebook.getNotebookGuid());
    });
  }
  
  private void handleNotebooks() throws Exception{
    
    final List<Notebook> notebooks      = evernoteInfo.noteStoreSyncChunk.getNotebooks();
    SSUri                notebookUri;
    SSLabel              notebookLabel;
    
    if(notebooks == null){
      return;
    }
    
    for(Notebook notebook : notebooks){
      
      notebookUri      = getNormalOrSharedNotebookUri         (evernoteInfo.userName,    notebook, sharedNotebookGuids);
      notebookLabel    = getNormalOrSharedNotebookLabel       (notebook);
      
      miscFct.addNotebook(
        notebookUri,
        notebookLabel,
        notebook.getServiceCreated());
      
      addNotebookUEs(
        notebookUri,
        notebook);
    }
  }
  
  private void addNotebookUEs(
    final SSUri    notebookUri,
    final Notebook notebook) throws Exception{
    
    final List<SSEntity> existingCreationUEs =
      ueServ.userEventsGet(
        new SSUEsGetPar(
          userUri, //user 
          null, //userEvents
          userUri, //forUser
          notebookUri, //entity
          SSUEE.asListWithoutEmptyAndNull(SSUEE.evernoteNotebookCreate), //types
          null, //startTime,
          null, //endTime, 
          true, //withUserRestriction, 
          false)); //invokeEntityHandlers
    
    if(existingCreationUEs.isEmpty()){
      
      ueServ.userEventAdd(
        new SSUEAddPar(
          userUri,
          notebookUri,
          SSUEE.evernoteNotebookCreate,
          SSStrU.empty,
          notebook.getServiceCreated(),
          true, 
          false)); //shouldCommit
    }
    
    final List<SSEntity> existingUpdatingUEs =
      ueServ.userEventsGet(
        new SSUEsGetPar(
          userUri, //user
          null, //userEvents
          userUri, //forUser
          notebookUri, //entity
          SSUEE.asListWithoutEmptyAndNull(SSUEE.evernoteNotebookUpdate), //types
          notebook.getServiceUpdated(),
          notebook.getServiceUpdated(),
          true, //withUserRestriction,
          false)); //invokeEntityHandlers
    
    if(existingUpdatingUEs.isEmpty()){
      
      ueServ.userEventAdd(
        new SSUEAddPar(
          userUri,
          notebookUri,
          SSUEE.evernoteNotebookUpdate,
          SSStrU.empty,
          notebook.getServiceUpdated(),
          true,
          false)); //shouldCommit
    }
  }
  
  private void handleLinkedNotebooks() throws Exception{
    
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
      
      miscFct.addNotebook(
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
    
    final List<SSEntity> existingUEs =
      ueServ.userEventsGet(
        new SSUEsGetPar(
          userUri, //user
          null, //userEvents
          userUri, //forUser
          notebookUri, //entity
          SSUEE.asListWithoutEmptyAndNull(SSUEE.evernoteNotebookFollow), //types
          creationTimeForLinkedNotebook,
          creationTimeForLinkedNotebook,
          true, //withUserRestriction,
          false)); //invokeEntityHandlers
    
    if(!existingUEs.isEmpty()){
      return;
    }
    
    ueServ.userEventAdd(
      new SSUEAddPar(
        userUri,
        notebookUri,
        SSUEE.evernoteNotebookFollow,
        SSStrU.empty,
        creationTimeForLinkedNotebook,
        true,
        false)); //shouldCommit
  }
  
  private void handleNotes() throws Exception{
    
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
      notebook         =
        evernoteServ.evernoteNotebookGet(
          new SSEvernoteNotebookGetPar(
            userUri,
            evernoteInfo.noteStore,
            note.getNotebookGuid()));
        
      noteWithContent  = 
        evernoteServ.evernoteNoteGet(
          new SSEvernoteNoteGetPar(
            userUri, 
            evernoteInfo.noteStore, 
            note.getGuid(), 
            true));
        
      notebookUri      = 
        getNormalOrSharedNotebookUri(
          evernoteInfo.userName,  
          notebook, 
          sharedNotebookGuids);
      
      miscFct.addNote(
        noteUri,
        getNoteLabel(note),
        notebookUri,
        note.getCreated());
      
      noteTagNames = 
        evernoteServ.evernoteNoteTagNamesGet(
          new SSEvernoteNoteTagNamesGetPar(
            userUri, 
            evernoteInfo.noteStore, 
            note.getGuid()));
      
      tagServ.tagsAdd(
        new SSTagsAddPar(
          userUri,
          SSTagLabel.get(noteTagNames), //labels
          SSUri.asListNotNull(noteUri), //entities
          SSSpaceE.sharedSpace, //space
          null, //circles
          note.getUpdated(), //creationTime
          true, //withUserRestriction
          false)); //shouldCommit
      
      for(String noteTag : noteTagNames){
        
        evalServ.evalLog(
          new SSEvalLogPar(
            userUri, //user
            SSToolContextE.evernoteImport, //toolContext
            SSEvalLogE.addTag, //type
            noteUri, //entity
            noteTag, //content
            SSUri.asListNotNull(notebookUri), //entities
            null, //users
            false)); //shouldCommit
      }
      
      miscFct.addNoteUEs(
        note,
        noteUri, 
        note.getCreated(), 
        note.getUpdated());
      
      new SSDataImportEvernoteNoteContentHandler(
        conf,
        fileServ,
        evernoteServ,
        userUri,
        noteWithContent,
        noteUri,
        evernoteInfo.noteStore).handleNoteContent();
    }
  }
  
  private void handleResources() throws Exception{
    
    final List<Resource> resources = evernoteInfo.noteStoreSyncChunk.getResources();
    Resource             resourceWithContent;
    SSUri                resourceUri;
    Note                 note;
    SSUri                noteUri;
    SSFileExtE           fileExt;
    
    if(resources == null){
      return;
    }
    
    for(Resource resource : resources){
      
      resourceWithContent = 
        evernoteServ.evernoteResourceGet(
          new SSEvernoteResourceGetPar(
            userUri, 
            evernoteInfo.noteStore, 
            resource.getGuid(), 
            false));
        
      try{
        fileExt = SSMimeTypeE.fileExtForMimeType1(resourceWithContent.getMime());
      }catch(Exception error){
        SSLogU.warn("resource with mimetype " + resourceWithContent.getMime() + " not stored", error);
        continue;
      }
        
      try{
        
        if(SSFileExtE.isImageFileExt(fileExt)){
          
          if(
            !resourceWithContent.isSetHeight() ||
            !resourceWithContent.isSetWidth()){
            
//            SSLogU.info("evernote image resource height or width not set");
            continue;
          }
             
          if(
            resourceWithContent.getWidth()  <= SSDataImportImpl.bitsAndPiecesImageMinWidth ||
            resourceWithContent.getHeight() <= SSDataImportImpl.bitsAndPiecesImageMinHeight){
            
//            SSLogU.info("evernote image resource height or width < " + SSDataImportImpl.bitsAndPiecesImageMinWidth);
            continue;
          }
          
//          if(resourceWithContent.getAttributes().isSetSourceURL()){
////            SSLogU.info("evernote image with source url ignored: " + resourceWithContent.getAttributes().getSourceURL());
//              continue;
//          }
        }
      }catch(Exception error){
      }
      
      resourceWithContent = 
        evernoteServ.evernoteResourceGet(
          new SSEvernoteResourceGetPar(
            userUri, 
            evernoteInfo.noteStore, 
            resource.getGuid(), 
            true));
        
      resourceUri         = getResourceUri           (evernoteInfo, resource);
      note                = 
        evernoteServ.evernoteNoteGet(
          new SSEvernoteNoteGetPar(
            userUri, 
            evernoteInfo.noteStore, 
            resource.getNoteGuid(), 
            false));
      
      noteUri             = getNormalOrSharedNoteUri (evernoteInfo, note);
      
      miscFct.addResource(
        resourceUri,
        getResourceLabel(resource, note),
        note.getUpdated(),
        noteUri);
      
      fileServ.fileAdd(
        new SSEntityFileAddPar(
          userUri,
          resourceWithContent.getData().getBody(), //fileBytes, 
          resourceWithContent.getData().getSize(), //fileLength
          fileExt, //fileExt
          null, //file
          SSEntityE.file, //type,
          null, //label
          resourceUri, //entity
          true, //createThumb,
          resourceUri, //entityToAddThumbTo
          true, //removeExistingFilesForEntity
          true, //withUserRestriction
          false));//shouldCommit
      
      miscFct.addResourceUEs(
        resourceUri,
        note.getUpdated());
    }
  }
  
  private SSUri getNormalOrSharedNotebookUri(SSLabel userName, Notebook notebook, List<String> sharedNotebookGuids) throws Exception{
    
    try{
      
      if(
        sharedNotebookGuids.contains   (notebook.getGuid()) &&
        !SSStrU.isEmpty                (notebook.getName())){
        return SSUri.get(createSharedNotebookUriStr(userName, notebook));
      }
      
    }catch(Exception error){}
    
    return getNotebookDefaultUri(notebook);
  }
  
  private SSUri getNormalOrSharedNoteUri(SSEvernoteInfo evernoteInfo, Note note) throws Exception {
    return SSUri.get(evernoteInfo.shardUri + "view/notebook/" + note.getGuid());
  }
  
  private SSUri getLinkedNotebookUri(LinkedNotebook linkedNotebook) throws Exception {
    return SSUri.get(linkedNotebook.getWebApiUrlPrefix() + "share/" + linkedNotebook.getShareKey());
  }
  
  private SSUri getResourceUri(SSEvernoteInfo evernoteInfo, Resource resource) throws Exception{
    return SSUri.get(evernoteInfo.shardUri + "res/" + resource.getGuid());
  }
  
  private String createSharedNotebookUriStr(SSLabel userName, Notebook notebook) throws Exception{
    
    //TODO dtheiler: check evernote environment to use here
    return SSLinkU.httpsEvernote + "pub/" + SSStrU.toStr(userName) + SSStrU.slash + notebook.getPublishing().getUri(); //7SSStrU.replaceAllBlanksSpecialCharactersDoubleDots(notebook.getName(), SSStrU.empty)
  }
  
  private SSUri getNotebookDefaultUri(Notebook notebook) throws Exception{
    
    if(
      notebook                  == null ||
      SSStrU.isEmpty(notebook.getGuid())){
      return SSUri.get(SSLinkU.httpsEvernote);
    }
    
    return SSUri.get(SSLinkU.httpsEvernote + "Home.action#b=" + notebook.getGuid());
  }
  
  private SSLabel getNormalOrSharedNotebookLabel(
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
  
  private SSLabel getLinkedNotebookLabel(
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
  
  public SSLabel getNoteLabel(
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
  
  private SSLabel getResourceLabel(
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
  
  private SSLabel getDefaultLabel() throws Exception{
    return SSLabel.get("no label");
  }
}


//  public boolean isSharedNootebook(SSUri notebookUri, SSLabel userName, Notebook notebook) {
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
//  private static boolean isSharedNotebookUri(SSLabel userName, Notebook notebook, SSUri notebookUri){
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
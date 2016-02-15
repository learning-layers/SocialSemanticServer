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
package at.kc.tugraz.ss.serv.dataimport.impl.bnp;

import at.kc.tugraz.ss.serv.dataimport.impl.SSDataImportActAndLog;
import at.tugraz.sss.serv.util.SSDateU;
import at.tugraz.sss.serv.util.SSLinkU;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.enums.SSSpaceE;
import at.tugraz.sss.serv.datatype.enums.SSToolContextE;
import at.kc.tugraz.ss.serv.dataimport.datatypes.pars.SSDataImportBitsAndPiecesPar;
import at.kc.tugraz.ss.serv.dataimport.impl.evernote.SSDataImportEvernoteNoteContentHandler;
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
import at.tugraz.sss.servs.file.api.SSFileServerI;
import at.kc.tugraz.ss.service.tag.api.SSTagServerI;
import at.kc.tugraz.ss.service.tag.datatypes.SSTagLabel;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagsAddPar;
import at.kc.tugraz.ss.service.userevent.api.SSUEServerI;
import at.kc.tugraz.ss.service.userevent.datatypes.SSUEE;
import at.kc.tugraz.ss.service.userevent.datatypes.pars.SSUEAddPar;
import at.kc.tugraz.ss.service.userevent.datatypes.pars.SSUEsGetPar;
import at.tugraz.sss.serv.datatype.SSEntity;
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.serv.util.SSFileExtE;
import at.tugraz.sss.serv.util.SSLogU;
import at.tugraz.sss.serv.util.SSMimeTypeE;
import at.tugraz.sss.servs.file.datatype.par.SSEntityFileAddPar;
import com.evernote.edam.type.LinkedNotebook;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.Notebook;
import com.evernote.edam.type.Resource;
import com.evernote.edam.type.SharedNotebook;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SSDataImportBNPEvernoteImporter {
  
  private final SSDataImportBNPCommon                  common                     = new SSDataImportBNPCommon();
  private final SSDataImportActAndLog                  actAndLog                  = new SSDataImportActAndLog();
  private final SSDataImportEvernoteNoteContentHandler evernoteNoteContentHandler = new SSDataImportEvernoteNoteContentHandler();
  private final List<String>                           sharedNotebookGuids        = new ArrayList<>();
  private       SSEvernoteInfo                         evernoteInfo             = null;
  private       List<SharedNotebook>                   sharedNotebooks          = null;
  
  public void handle(
    final SSDataImportBitsAndPiecesPar par, 
    final String                       localWorkPath,
    final SSUri                        forUser) throws SSErr{
    
    try{
  
      SSLogU.info("start B&P evernote import for " + par.authEmail);
      
      setBasicEvernoteInfo  (par, forUser);
      
      handleLinkedNotebooks (par, forUser);
      setSharedNotebooks    (par, forUser);
      handleNotebooks       (par, forUser);
      handleNotes           (par, localWorkPath, forUser);
      handleResources       (par, forUser);
      
      setUSN(par, forUser);
      
      SSLogU.info("end B&P evernote import for evernote account " + par.authEmail);
      
    }catch(Exception error){
      SSLogU.err(error, "B&P evernote import failed for " + par.authEmail);
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private void setBasicEvernoteInfo(
    final SSDataImportBitsAndPiecesPar par, 
    final SSUri                        forUser) throws SSErr{
    
    try{
      final SSEvernoteServerI evernoteServ  = (SSEvernoteServerI) SSServReg.getServ(SSEvernoteServerI.class);
      
      evernoteInfo =
        evernoteServ.evernoteNoteStoreGet(
          new SSEvernoteNoteStoreGetPar(
            par,
            par.user,
            par.authToken,
            par.authEmail));
      
      evernoteInfo.userName = SSLabel.get(evernoteInfo.userStore.getUser().getUsername());
      
      evernoteServ.evernoteUserAdd(
        new SSEvernoteUserAddPar(
          par,
          forUser,
          par.authToken,
          false));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private void setUSN(
    final SSDataImportBitsAndPiecesPar par, 
    final SSUri                        forUser) throws SSErr{
    
    try{
      final SSEvernoteServerI evernoteServ  = (SSEvernoteServerI) SSServReg.getServ(SSEvernoteServerI.class);
      
      final Integer usn;
      
      if(evernoteInfo.noteStoreSyncChunk.isSetChunkHighUSN()){
        usn = evernoteInfo.noteStoreSyncChunk.getChunkHighUSN();
      }else{
        if(evernoteInfo.noteStoreSyncChunk.isSetUpdateCount()){
          usn = evernoteInfo.noteStoreSyncChunk.getUpdateCount();
        }else{
          SSLogU.warn("couldnt set USN as Evernote didnt provide one", null);
          usn = 0;
        }
      }
      
      evernoteServ.evernoteUSNSet(
        new SSEvernoteUSNSetPar(
          par,
          forUser,
          evernoteInfo.authToken,
          usn,
          false));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private void setSharedNotebooks(
    final SSDataImportBitsAndPiecesPar par, 
    final SSUri                        forUser) throws SSErr{
    
    try{
      
      final SSEvernoteServerI evernoteServ = (SSEvernoteServerI) SSServReg.getServ(SSEvernoteServerI.class);
      
      sharedNotebooks =
        evernoteServ.evernoteNotebooksSharedGet (
          new SSEvernoteNotebooksSharedGetPar(
            par,
            forUser,
            evernoteInfo.noteStore));
      
      sharedNotebookGuids.clear();
      
      if(sharedNotebooks == null){
        return;
      }
      
      sharedNotebooks.stream().forEach((sharedNotebook)->{
        sharedNotebookGuids.add(sharedNotebook.getNotebookGuid());
      });
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private void handleNotebooks(
    final SSDataImportBitsAndPiecesPar par, 
    final SSUri                        forUser) throws SSErr{
    
    try{
      final List<Notebook> notebooks      = evernoteInfo.noteStoreSyncChunk.getNotebooks();
      SSUri                notebookUri;
      SSLabel              notebookLabel;
      
      if(notebooks == null){
        return;
      }
      
      for(Notebook notebook : notebooks){
        
        notebookUri      = getNormalOrSharedNotebookUri         (evernoteInfo.userName,    notebook, sharedNotebookGuids);
        notebookLabel    = getNormalOrSharedNotebookLabel       (notebook);
        
        common.addNotebook(
          par,
          forUser,
          SSToolContextE.evernoteImport,
          notebookUri,
          notebookLabel,
          notebook.getServiceCreated());
        
        addNotebookUEs(
          par,
          forUser,
          notebookUri,
          notebook);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private void addNotebookUEs(
    final SSDataImportBitsAndPiecesPar par, 
    final SSUri                        forUser,
    final SSUri                        notebookUri,
    final Notebook                     notebook) throws SSErr{
    
    try{
      
      final SSUEServerI ueServ = (SSUEServerI) SSServReg.getServ(SSUEServerI.class);
      
      final List<SSEntity> existingCreationUEs =
        ueServ.userEventsGet(
          new SSUEsGetPar(
            par,
            forUser, //user
            null, //userEvents
            forUser, //forUser
            notebookUri, //entity
            SSUEE.asListWithoutEmptyAndNull(SSUEE.evernoteNotebookCreate), //types
            null, //startTime,
            null, //endTime,
            true, //withUserRestriction,
            false)); //invokeEntityHandlers
      
      if(existingCreationUEs.isEmpty()){
        
        ueServ.userEventAdd(
          new SSUEAddPar(
            par,
            forUser,
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
            par,
            forUser, //user
            null, //userEvents
            forUser, //forUser
            notebookUri, //entity
            SSUEE.asListWithoutEmptyAndNull(SSUEE.evernoteNotebookUpdate), //types
            notebook.getServiceUpdated(),
            notebook.getServiceUpdated(),
            true, //withUserRestriction,
            false)); //invokeEntityHandlers
      
      if(existingUpdatingUEs.isEmpty()){
        
        ueServ.userEventAdd(
          new SSUEAddPar(
            par,
            forUser,
            notebookUri,
            SSUEE.evernoteNotebookUpdate,
            SSStrU.empty,
            notebook.getServiceUpdated(),
            true,
            false)); //shouldCommit
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private void handleLinkedNotebooks(
    final SSDataImportBitsAndPiecesPar par, 
    final SSUri                        forUser) throws SSErr{
    
    try{
      
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
        
        common.addNotebook(
          par,
          forUser,
          SSToolContextE.evernoteImport,
          notebookUri,
          getLinkedNotebookLabel(linkedNotebook),
          creationTimeForLinkedNotebook);
        
        addLinkedNotebookUEs(
          par,
          forUser,
          notebookUri,
          creationTimeForLinkedNotebook);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private void addLinkedNotebookUEs(
    final SSDataImportBitsAndPiecesPar par, 
    final SSUri                        forUser,
    final SSUri                        notebookUri,
    final Long                         creationTimeForLinkedNotebook) throws SSErr {
    
    try{
      final SSUEServerI ueServ = (SSUEServerI) SSServReg.getServ(SSUEServerI.class);
      
      final List<SSEntity> existingUEs =
        ueServ.userEventsGet(
          new SSUEsGetPar(
            par,
            forUser, //user
            null, //userEvents
            forUser, //forUser
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
          par,
          forUser,
          notebookUri,
          SSUEE.evernoteNotebookFollow,
          SSStrU.empty,
          creationTimeForLinkedNotebook,
          true,
          false)); //shouldCommit
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private void handleNotes(
    final SSDataImportBitsAndPiecesPar par, 
    final String                       localWorkPath,
    final SSUri                        forUser) throws SSErr{
    
    try{
      final SSEvernoteServerI evernoteServ = (SSEvernoteServerI) SSServReg.getServ(SSEvernoteServerI.class);
      final SSTagServerI      tagServ      = (SSTagServerI)      SSServReg.getServ(SSTagServerI.class);
      final List<Note>        notes        = evernoteInfo.noteStoreSyncChunk.getNotes();
      Note                    noteWithContent;
      Notebook                notebook;
      SSUri                   notebookUri;
      SSUri                   noteUri;
      List<String>            noteTagNames;
      SSLabel                 noteLabel;
      
      if(notes == null){
        return;
      }
      
      for(Note note : notes){
        
        noteUri          = getNormalOrSharedNoteUri        (evernoteInfo,           note);
        notebook         =
          evernoteServ.evernoteNotebookGet(
            new SSEvernoteNotebookGetPar(
              par,
              forUser,
              evernoteInfo.noteStore,
              note.getNotebookGuid()));
        
        noteWithContent  =
          evernoteServ.evernoteNoteGet(
            new SSEvernoteNoteGetPar(
              par,
              forUser,
              evernoteInfo.noteStore,
              note.getGuid(),
              true));
        
        notebookUri      =
          getNormalOrSharedNotebookUri(
            evernoteInfo.userName,
            notebook,
            sharedNotebookGuids);
        
        noteLabel = getNoteLabel(note);
        
        common.addNote(
          par,
          forUser,
          SSToolContextE.evernoteImport,
          noteUri,
          noteLabel,
          notebookUri,
          note.getCreated());
        
        noteTagNames =
          evernoteServ.evernoteNoteTagNamesGet(
            new SSEvernoteNoteTagNamesGetPar(
              par,
              forUser,
              evernoteInfo.noteStore,
              note.getGuid()));
        
        tagServ.tagsAdd(
          new SSTagsAddPar(
            par,
            forUser,
            SSTagLabel.get(noteTagNames), //labels
            SSUri.asListNotNull(noteUri), //entities
            SSSpaceE.sharedSpace, //space
            null, //circles
            note.getUpdated(), //creationTime
            true, //withUserRestriction
            false)); //shouldCommit
        
        for(String noteTag : noteTagNames){
          
          actAndLog.addTag(
            par,
            SSToolContextE.evernoteImport, //toolContext
            noteUri, //entity,
            noteTag, //content,
            SSUri.asListNotNull(notebookUri), //entities
            note.getUpdated(), //creationTime
            false); //shouldCommit);
        }
        
        common.addNoteUEs(
          par,
          forUser,
          note,
          noteUri,
          note.getCreated(),
          note.getUpdated());
        
        evernoteNoteContentHandler.handleNoteContent(
          par, 
          evernoteInfo.noteStore,
          forUser, 
          localWorkPath, 
          noteWithContent, 
          noteUri, 
          noteLabel);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private void handleResources(
    final SSDataImportBitsAndPiecesPar par, 
    final SSUri                        forUser) throws SSErr{
    
    try{
      final SSEvernoteServerI evernoteServ = (SSEvernoteServerI) SSServReg.getServ(SSEvernoteServerI.class);
      final SSFileServerI fileServ     = (SSFileServerI) SSServReg.getServ(SSFileServerI.class);
      final List<Resource>    resources    = evernoteInfo.noteStoreSyncChunk.getResources();
      Resource                resourceWithContent;
      SSUri                   resourceUri;
      Note                    note;
      SSUri                   noteUri;
      SSLabel                 resourceLabel;
      SSFileExtE              fileExt;
      
      if(resources == null){
        return;
      }
      
      for(Resource resource : resources){
        
        resourceWithContent =
          evernoteServ.evernoteResourceGet(
            new SSEvernoteResourceGetPar(
              par,
              forUser,
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
              resourceWithContent.getWidth()  <= SSDataImportBNPCommon.bitsAndPiecesImageMinWidth ||
              resourceWithContent.getHeight() <= SSDataImportBNPCommon.bitsAndPiecesImageMinHeight){
              
//            SSLogU.info("evernote image resource height or width < " + SSDataImportBNPCommon.bitsAndPiecesImageMinWidth);
              continue;
            }
            
//          if(resourceWithContent.getAttributes().isSetSourceURL()){
////            SSLogU.info("evernote image with source url ignored: " + resourceWithContent.getAttributes().getSourceURL());
//              continue;
//          }
          }
        }catch(Exception error){
          SSLogU.warn(error);
        }
        
        resourceWithContent =
          evernoteServ.evernoteResourceGet(
            new SSEvernoteResourceGetPar(
              par,
              forUser,
              evernoteInfo.noteStore,
              resource.getGuid(),
              true));
        
        resourceUri         = getResourceUri           (evernoteInfo, resource);
        note                =
          evernoteServ.evernoteNoteGet(
            new SSEvernoteNoteGetPar(
              par,
              forUser,
              evernoteInfo.noteStore,
              resource.getNoteGuid(),
              false));
        
        noteUri             = getNormalOrSharedNoteUri (evernoteInfo, note);
        resourceLabel       = getResourceLabel(resource, note);
        
        common.addResource(
          par,
          forUser,
          SSToolContextE.evernoteImport,
          resourceUri,
          resourceLabel,
          note.getUpdated(),
          noteUri);
        
        fileServ.fileAdd(
          new SSEntityFileAddPar(
            par,
            forUser,
            resourceWithContent.getData().getBody(), //fileBytes,
            resourceWithContent.getData().getSize(), //fileLength
            fileExt, //fileExt
            null, //file
            SSEntityE.file, //type,
            resourceLabel, //label
            resourceUri, //entity
            true, //createThumb,
            resourceUri, //entityToAddThumbTo
            true, //removeExistingFilesForEntity
            true, //withUserRestriction
            false));//shouldCommit
        
        common.addResourceUEs(
          par,
          forUser,
          resourceUri,
          note.getUpdated());
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private SSUri getNormalOrSharedNotebookUri(SSLabel userName, Notebook notebook, List<String> sharedNotebookGuids) throws SSErr{
    
    try{
      
      if(
        sharedNotebookGuids.contains   (notebook.getGuid()) &&
        !SSStrU.isEmpty                (notebook.getName())){
        return SSUri.get(createSharedNotebookUriStr(userName, notebook));
      }
      
    }catch(Exception error){
      SSLogU.warn(error);
    }
    
    return getNotebookDefaultUri(notebook);
  }
  
  private SSUri getNormalOrSharedNoteUri(SSEvernoteInfo evernoteInfo, Note note) throws SSErr {
    return SSUri.get(evernoteInfo.shardUri + "view/notebook/" + note.getGuid());
  }
  
  private SSUri getLinkedNotebookUri(LinkedNotebook linkedNotebook) throws SSErr {
    return SSUri.get(linkedNotebook.getWebApiUrlPrefix() + "share/" + linkedNotebook.getShareKey());
  }
  
  private SSUri getResourceUri(SSEvernoteInfo evernoteInfo, Resource resource) throws SSErr{
    return SSUri.get(evernoteInfo.shardUri + "res/" + resource.getGuid());
  }
  
  private String createSharedNotebookUriStr(SSLabel userName, Notebook notebook) throws SSErr{
    
    //TODO dtheiler: check evernote environment to use here
    return SSLinkU.httpsEvernote + "pub/" + SSStrU.toStr(userName) + SSStrU.slash + notebook.getPublishing().getUri(); //7SSStrU.replaceAllBlanksSpecialCharactersDoubleDots(notebook.getName(), SSStrU.empty)
  }
  
  private SSUri getNotebookDefaultUri(Notebook notebook) throws SSErr{
    
    if(
      notebook                  == null ||
      SSStrU.isEmpty(notebook.getGuid())){
      return SSUri.get(SSLinkU.httpsEvernote);
    }
    
    return SSUri.get(SSLinkU.httpsEvernote + "Home.action#b=" + notebook.getGuid());
  }
  
  private SSLabel getNormalOrSharedNotebookLabel(
    final Notebook notebook) throws SSErr{
    
    try{
      final SSLabel tmpLabel = SSLabel.get(notebook.getName());
      
      if(tmpLabel == null){
        return getDefaultLabel();
      }else{
        return tmpLabel;
      }
    }catch(Exception error){
      SSLogU.warn(error);
      
      return getDefaultLabel();
    }
  }
  
  private SSLabel getLinkedNotebookLabel(
    final LinkedNotebook linkedNotebook) throws SSErr{
    
    try{
      final SSLabel tmpLabel = SSLabel.get(linkedNotebook.getShareName());
      
      if(tmpLabel == null){
        return getDefaultLabel();
      }else{
        return tmpLabel;
      }
    }catch(Exception error){
       SSLogU.warn(error);
      return getDefaultLabel();
    }
  }
  
  public SSLabel getNoteLabel(
    final Note  note) throws SSErr {
    
    try{
      final SSLabel tmpLabel = SSLabel.get(note.getTitle());
      
      if(tmpLabel == null){
        return getDefaultLabel();
      }else{
        return tmpLabel;
      }
    }catch(Exception error){
       SSLogU.warn(error);
      return getDefaultLabel();
    }
  }
  
  private SSLabel getResourceLabel(
    final Resource resource,
    final Note     note) throws SSErr{
    
    try{
      
      if(
        SSObjU.isNull (resource, resource.getAttributes()) ||
        SSStrU.isEmpty(resource.getAttributes().getFileName())){
        
        return getNoteLabel(note);
      }
      
      return SSLabel.get(resource.getAttributes().getFileName());
    }catch(Exception error){
       SSLogU.warn(error);
      return getDefaultLabel();
    }
  }
  
  private SSLabel getDefaultLabel() throws SSErr{
    return SSLabel.get("no label");
  }
}
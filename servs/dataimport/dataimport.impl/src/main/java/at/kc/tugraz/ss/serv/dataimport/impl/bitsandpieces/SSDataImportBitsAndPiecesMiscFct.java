/**
  * Code contributed to the Learning Layers project
  * http://www.learning-layers.eu
  * Development is partly funded by the FP7 Programme of the European Commission under
  * Grant Agreement FP7-ICT-318209.
  * Copyright (c) 2015, Graz University of Technology - KTI (Knowledge Technologies Institute).
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

import at.kc.tugraz.ss.serv.dataimport.datatypes.pars.SSDataImportBitsAndPiecesPar;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.service.userevent.api.SSUEServerI;
import at.kc.tugraz.ss.service.userevent.datatypes.SSUEE;
import at.kc.tugraz.ss.service.userevent.datatypes.pars.SSUEAddPar;
import at.kc.tugraz.ss.service.userevent.datatypes.pars.SSUEsGetPar;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSLabel;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSToolContextE;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityGetPar;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityUpdatePar;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.NoteAttributes;
import java.util.List;
import sss.serv.eval.api.SSEvalServerI;
import sss.serv.eval.datatypes.SSEvalLogE;
import sss.serv.eval.datatypes.par.SSEvalLogPar;

public class SSDataImportBitsAndPiecesMiscFct {
 
  private final SSDataImportBitsAndPiecesPar par;
  private final SSEntityServerI              entityServ;
  private final SSUEServerI                  ueServ;
  private final SSEvalServerI                evalServ;
  private final SSUri                        userUri;
  
  public SSDataImportBitsAndPiecesMiscFct(
    final SSDataImportBitsAndPiecesPar par, 
    final SSEntityServerI              entityServ,
    final SSUEServerI                  ueServ,
    final SSEvalServerI                evalServ,
    final SSUri                        userUri) throws Exception{
    
    this.par             = par;
    this.entityServ      = entityServ;
    this.ueServ          = ueServ;
    this.evalServ        = evalServ;
    this.userUri         = userUri;
  }
  
  public void addNotebook(
    final SSUri    notebookUri,
    final SSLabel  notebookLabel,
    final Long     notebookCreationTime) throws Exception{
    
    entityServ.entityUpdate(
      new SSEntityUpdatePar(
        userUri,
        notebookUri,
        SSEntityE.evernoteNotebook, //type,
        notebookLabel, //label
        null, //description,
        notebookCreationTime, //creationTime,
        null, //read,
        false, //setPublic
        true, //withUserRestriction
        false)); //shouldCommit)
    
    evalServ.evalLog(
      new SSEvalLogPar(
        userUri, 
        SSToolContextE.evernoteImport, 
        SSEvalLogE.addNotebook, 
        notebookUri, 
        null,  //content
        null, //entities
        null,  //users
        false)); //shouldCommit
  }
  
  public void addNote(
    final SSUri   noteUri,
    final SSLabel noteLabel,
    final SSUri   notebookUri,
    final Long    creationTime) throws Exception{
    
    try{
      
      entityServ.entityUpdate(
        new SSEntityUpdatePar(
          userUri,
          noteUri,
          SSEntityE.evernoteNote, //type,
          noteLabel, //label
          null, //description,
          creationTime, //creationTime,
          null, //read,
          false, //setPublic
          true, //withUserRestriction
          false)); //shouldCommit)
      
      if(notebookUri != null){
        
        final SSEntity nootebookEntity =
          entityServ.entityGet(
            new SSEntityGetPar(
              userUri,
              notebookUri, //entity
              true, //withUserRestriction
              null)); //descPar
        
        if(nootebookEntity == null){
          
          addNotebook(
            notebookUri,
            null,
            null);
        }
      }
      
      SSServCaller.evernoteNoteAdd(
        this.userUri,
        notebookUri,
        noteUri,
        false);
      
      evalServ.evalLog(
        new SSEvalLogPar(
          userUri,
          SSToolContextE.evernoteImport,
          SSEvalLogE.addNote,
          noteUri,
          null, //content
          SSUri.asListWithoutNullAndEmpty(notebookUri), //entities
          null, //users
          false)); //shouldCommit
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void addNoteUEs(
    final Note         note,
    final SSUri        noteUri,
    final Long         creationTime, 
    final Long         updateTime) throws Exception {
    
    final List<SSEntity> existingCreationUEs =
      ueServ.userEventsGet(
        new SSUEsGetPar(
          userUri, //user
          userUri, //forUser
          noteUri, //entity
          SSUEE.asListWithoutEmptyAndNull(SSUEE.evernoteNoteCreate), //types
          null,
          null,
          true, //withUserRestriction,
          false)); //invokeEntityHandlers
    
    if(existingCreationUEs.isEmpty()){
      
      ueServ.userEventAdd(
        new SSUEAddPar(
          userUri,
          noteUri,
          SSUEE.evernoteNoteCreate,
          SSStrU.empty,
          creationTime,
          true,
          false)); //shouldCommit
    }
    
    final List<SSEntity> existingUpdateUEs =
      ueServ.userEventsGet(
        new SSUEsGetPar(
          userUri, //user
          userUri, //forUser
          noteUri, //entity
          SSUEE.asListWithoutEmptyAndNull(SSUEE.evernoteNoteUpdate), //types
          updateTime,
          updateTime,
          true, //withUserRestriction,
          false)); //invokeEntityHandlers
    
    if(existingUpdateUEs.isEmpty()){
      
      ueServ.userEventAdd(
        new SSUEAddPar(
          userUri,
          noteUri,
          SSUEE.evernoteNoteUpdate,
          SSStrU.empty,
          updateTime,
          true,
          false)); //shouldCommit
    }
    
    if(note != null){
      
      if(note.getDeleted() != 0L){

        final List<SSEntity> existingDeleteUEs =
          ueServ.userEventsGet(
            new SSUEsGetPar(
              userUri, //user
              userUri, //forUser
              noteUri, //entity
              SSUEE.asListWithoutEmptyAndNull(SSUEE.evernoteNoteDelete), //types
              null,
              null,
              true, //withUserRestriction,
              false)); //invokeEntityHandlers

        if(existingDeleteUEs.isEmpty()){

          ueServ.userEventAdd(
            new SSUEAddPar(
              userUri,
              noteUri,
              SSUEE.evernoteNoteDelete,
              SSStrU.empty,
              note.getDeleted(),
              true,
              false)); //shouldCommit
        }
      }

      final NoteAttributes noteAttr = note.getAttributes();

      if(noteAttr == null){
        return;
      }

      if(noteAttr.getShareDate() != 0L){

        final List<SSEntity> existingShareUEs =
          ueServ.userEventsGet(
            new SSUEsGetPar(
              userUri, //user
              userUri, //forUser
              noteUri, //entity
              SSUEE.asListWithoutEmptyAndNull(SSUEE.evernoteNoteShare), //types
              noteAttr.getShareDate(),
              noteAttr.getShareDate(),
              true, //withUserRestriction,
              false)); //invokeEntityHandlers

        if(existingShareUEs.isEmpty()){

          ueServ.userEventAdd(
            new SSUEAddPar(
              userUri,
              noteUri,
              SSUEE.evernoteNoteShare,
              SSStrU.empty,
              noteAttr.getShareDate(),
              true,
              false)); //shouldCommit
        }
      }

      if(noteAttr.getReminderDoneTime() != 0L){

        final List<SSEntity> existingReminderUEs =
          ueServ.userEventsGet(
            new SSUEsGetPar(
              userUri, //user
              userUri, //forUser
              noteUri, //entity
              SSUEE.asListWithoutEmptyAndNull(SSUEE.evernoteReminderDone), //types
              noteAttr.getReminderDoneTime(),
              noteAttr.getReminderDoneTime(),
              true, //withUserRestriction,
              false)); //invokeEntityHandlers

        if(existingReminderUEs.isEmpty()){

          ueServ.userEventAdd(
            new SSUEAddPar(
              userUri,
              noteUri,
              SSUEE.evernoteReminderDone,
              SSStrU.empty,
              noteAttr.getReminderDoneTime(),
              true,
              false)); //shouldCommit
        }
      }

      if(noteAttr.getReminderTime() != 0L){

        final List<SSEntity> existingReminder2UEs =
          ueServ.userEventsGet(
            new SSUEsGetPar(
              userUri, //user
              userUri, //forUser
              noteUri, //entity
              SSUEE.asListWithoutEmptyAndNull(SSUEE.evernoteReminderCreate), //types
              noteAttr.getReminderTime(),
              noteAttr.getReminderTime(),
              true, //withUserRestriction,
              false)); //invokeEntityHandlers

        if(existingReminder2UEs.isEmpty()){

          ueServ.userEventAdd(
            new SSUEAddPar(
              userUri,
              noteUri,
              SSUEE.evernoteReminderCreate,
              SSStrU.empty,
              noteAttr.getReminderTime(),
              true,
              false)); //shouldCommit
        }
      }
    }
  }
  
  public void addResourceUEs(
    final SSUri resourceUri,
    final Long  resourceAddTime) throws Exception{
    
    final List<SSEntity> existingUEs =
      ueServ.userEventsGet(
        new SSUEsGetPar(
          userUri, //user
          userUri, //forUser
          resourceUri, //entity
          SSUEE.asListWithoutEmptyAndNull(SSUEE.evernoteResourceAdd), //types
          resourceAddTime,
          resourceAddTime,
          true, //withUserRestriction,
          false)); //invokeEntityHandlers
    
    if(existingUEs.isEmpty()){
      
      ueServ.userEventAdd(
        new SSUEAddPar(
          userUri,
          resourceUri,
          SSUEE.evernoteResourceAdd,
          SSStrU.empty,
          resourceAddTime,
          true,
          false)); //shouldCommit
    }
  }
  
  public void addResource(
    final SSUri   resourceUri,
    final SSLabel resourceLabel,
    final Long    resourceAddTime,
    final SSUri   noteUri) throws Exception{
    
    entityServ.entityUpdate(
      new SSEntityUpdatePar(
        userUri,
        resourceUri,
        SSEntityE.evernoteResource, //type,
        resourceLabel, //label
        null, //description,
        resourceAddTime, //creationTime,
        null, //read,
        false, //setPublic
        true, //withUserRestriction
        false)); //shouldCommit)
    
    //TODO fix this hack
    try{
      
      SSServCaller.evernoteResourceAdd(
        this.userUri,
        noteUri,
        resourceUri,
        false);
      
    }catch(Exception error){
      
      entityServ.entityUpdate(
        new SSEntityUpdatePar(
          userUri,
          noteUri,
          SSEntityE.evernoteNote, //type,
          null, //label
          null, //description,
          resourceAddTime, //creationTime,
          null, //read,
          false, //setPublic
          true, //withUserRestriction
          false)); //shouldCommit)
      
      SSServCaller.evernoteResourceAdd(
        this.userUri,
        noteUri,
        resourceUri,
        false);
    }
    
    evalServ.evalLog(
      new SSEvalLogPar(
        userUri,
        SSToolContextE.evernoteImport,
        SSEvalLogE.addResource,
        resourceUri,
        null, //content
        SSUri.asListWithoutNullAndEmpty(noteUri), //entitites
        null,  //users
        false)); //shouldCommit
  }
}

/**
 * Copyright 2013 Graz University of Technology - KTI (Knowledge Technologies Institute)
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
package at.kc.tugraz.ss.serv.jobs.evernote.impl.helper;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.datatypes.datatypes.SSSpaceEnum;
import at.kc.tugraz.ss.datatypes.datatypes.SSTagLabel;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.SSUEEnum;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.NoteAttributes;
import com.evernote.edam.type.Notebook;
import com.evernote.edam.type.SharedNotebook;
import java.util.List;

public class SSEvernoteUEHelper {
  
  public void addUEsForNormalNotebook(
    SSUri userUri,
    SSUri notebookUri,
    Notebook notebook,
    Boolean shouldCommit) throws Exception {
    
    SSServCaller.addUEAtCreationTime(
      userUri,
      notebookUri,
      SSUEEnum.evernoteNotebookCreate,
      SSStrU.empty,
      notebook.getServiceCreated() + SSEvernoteTimestampCounter.get(),
      shouldCommit);
    
    SSServCaller.addUEAtCreationTime(
      userUri,
      notebookUri,
      SSUEEnum.evernoteNotebookUpdate,
      SSStrU.empty,
      notebook.getServiceUpdated()  + SSEvernoteTimestampCounter.get(),
      shouldCommit);
  }
  
  public void addUEsForSharedNotebook(
    Boolean isSharedNotebook,
    SSUri userUri,
    Notebook notebook,
    SSUri notebookUri,
    List<SharedNotebook> sharedNotebooks,
    Boolean shouldCommit) throws Exception {
    
    if(!isSharedNotebook){
      return;
    }
    
    for(SharedNotebook sharedNotebook : sharedNotebooks){
      
      if(notebook.getGuid().equals(sharedNotebook.getNotebookGuid())){
        
        SSServCaller.addUEAtCreationTime(
          userUri,
          notebookUri,
          SSUEEnum.evernoteNotebookShare,
          SSStrU.empty,
          sharedNotebook.getServiceCreated() + SSEvernoteTimestampCounter.get(),
          shouldCommit);
      }
    }
  }
  
  public void addUEsAndTagsForNormalNote(
    SSUri userUri,
    Note note,
    SSUri noteUri,
    Boolean shouldCommit) throws Exception {
    
    if(note == null){
      return;
    }
    
    SSServCaller.addUEAtCreationTime(
      userUri,
      noteUri,
      SSUEEnum.evernoteNoteCreate,
      SSStrU.empty,
      note.getCreated() + SSEvernoteTimestampCounter.get(),
      shouldCommit);
    
    SSServCaller.addUEAtCreationTime(
      userUri,
      noteUri,
      SSUEEnum.evernoteNoteUpdate,
      SSStrU.empty,
      note.getUpdated() + SSEvernoteTimestampCounter.get(),
      shouldCommit);
    
    if(note.getDeleted() != 0L){
      
      SSServCaller.addUEAtCreationTime(
        userUri,
        noteUri,
        SSUEEnum.evernoteNoteDelete,
        SSStrU.empty,
        note.getDeleted() + SSEvernoteTimestampCounter.get(),
        shouldCommit);
    }
    
    if(
      note.getTagNames()  != null &&
      !note.getTagNames().isEmpty()){
      
      SSServCaller.addTagsAtCreationTime(
        userUri,
        noteUri,
        SSTagLabel.getDistinct(note.getTagNames()),
        SSSpaceEnum.privateSpace,
        note.getUpdated() + SSEvernoteTimestampCounter.get(),
        shouldCommit);
      
      for(String tagName : note.getTagNames()){
        
        SSServCaller.addUEAtCreationTime(
          userUri,
          noteUri,
          SSUEEnum.addPrivateTag,
          tagName,
          note.getUpdated() + SSEvernoteTimestampCounter.get(),
          shouldCommit);
      }
    }
  }
  
  public void addUEsForSharedNote(
    Boolean isSharedNotebook,
    SSUri userUri,
    Notebook notebook,
    SSUri noteUri,
    List<SharedNotebook> sharedNotebooks,
    Boolean shouldCommit) throws Exception {
    
    if(
      !isSharedNotebook ||
      notebook == null){
      return;
    }
    
    for(SharedNotebook sharedNotebook : sharedNotebooks){
      
      if(notebook.getGuid().equals(sharedNotebook.getNotebookGuid())){
        
        SSServCaller.addUEAtCreationTime(
          userUri,
          noteUri,
          SSUEEnum.evernoteNoteShare,
          SSStrU.empty,
          sharedNotebook.getServiceCreated() + SSEvernoteTimestampCounter.get(),
          shouldCommit);
      }
    }
  }
  
  public void addUEsForNoteAttrs(
    SSUri userUri,
    Note note,
    SSUri noteUri,
    Boolean shouldCommit) throws Exception {
    
    NoteAttributes noteAttr;
    
    if(
      note     == null ||
      noteUri  == null){
      return;
    }
    
    noteAttr = note.getAttributes();
    
    if(noteAttr == null){
      return;
    }
    
    if(noteAttr.getShareDate() != 0L){
      
      SSServCaller.addUEAtCreationTime(
        userUri,
        noteUri,
        SSUEEnum.evernoteNoteShare,
        SSStrU.empty,
        noteAttr.getShareDate(),
        shouldCommit);
    }
    
    if(noteAttr.getReminderDoneTime() != 0L){
      
      SSServCaller.addUEAtCreationTime(
        userUri,
        noteUri,
        SSUEEnum.evernoteReminderDone,
        SSStrU.empty,
        noteAttr.getReminderDoneTime(),
        shouldCommit);
    }
    
    if(noteAttr.getReminderTime() != 0L){
      
      SSServCaller.addUEAtCreationTime(
        userUri,
        noteUri,
        SSUEEnum.evernoteReminderCreate,
        SSStrU.empty,
        noteAttr.getReminderTime(),
        shouldCommit);
    }
  }
  
  public void addUEsForResource(
    SSUri userUri,
    SSUri resourceUri,
    Note note,
    Boolean shouldCommit) throws Exception{
    
    if(
      resourceUri == null ||
      note        == null){
      return;
    }
    
    SSServCaller.addUEAtCreationTime(
      userUri,
      resourceUri,
      SSUEEnum.evernoteResourceAdd,
      SSStrU.empty,
      note.getUpdated(),
      shouldCommit);
  }
  
  public void addUEsForSharedResource(
    SSUri userUri,
    SSUri resourceUri,
    Notebook notebook,
    List<SharedNotebook> sharedNotebooks,
    Boolean isSharedNotebook,
    Boolean shouldCommit) throws Exception{
    
    if(
      !isSharedNotebook ||
      notebook        == null ||
      sharedNotebooks == null){
      return;
    }
    
    for(SharedNotebook sharedNotebook : sharedNotebooks){
      
      if(notebook.getGuid().equals(sharedNotebook.getNotebookGuid())){
        
        SSServCaller.addUEAtCreationTime(
          userUri,
          resourceUri,
          SSUEEnum.evernoteResourceShare,
          SSStrU.empty,
          sharedNotebook.getServiceCreated(),
          shouldCommit);
      }
    }
  }
  
  public void addUEsForLinkedNotebook(
    SSUri userUri,
    SSUri notebookUri,
    Long creationTimeForLinkedNotebook,
    Boolean shouldCommit) throws Exception {
    
    SSServCaller.addUEAtCreationTime(
      userUri,
      notebookUri,
      SSUEEnum.evernoteNotebookFollow,
      SSStrU.empty,
      creationTimeForLinkedNotebook,
      shouldCommit);
  }
}

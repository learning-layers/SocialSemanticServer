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
package at.tugraz.sss.servs.dataimport.impl.bnp;

import at.tugraz.sss.servs.dataimport.impl.SSDataImportActAndLog;
import at.kc.tugraz.ss.serv.jobs.evernote.api.*;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteNoteAddPar;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteResourceAddPar;
import at.tugraz.sss.serv.datatype.SSEntity;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.reg.SSServErrReg;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.enums.SSToolContextE;
import at.tugraz.sss.serv.datatype.par.*;
import at.tugraz.sss.serv.entity.api.*;
import at.tugraz.sss.serv.reg.*;

public class SSDataImportBNPCommon {
  
  public static final Integer                 bitsAndPiecesImageMinWidth          = 250;
  public static final Integer                 bitsAndPiecesImageMinHeight         = 250;

  private final SSDataImportActAndLog actAndLog = new SSDataImportActAndLog();
  
  public void addNotebook(
    final SSServPar      servPar,
    final SSUri          userUri,
    final SSToolContextE toolContext,
    final SSUri          notebookUri,
    final SSLabel        notebookLabel,
    final Long           notebookCreationTime) throws SSErr{
    
    try{
      final SSEntityServerI entityServ  = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
      
      entityServ.entityUpdate(
        new SSEntityUpdatePar(
          servPar,
          userUri,
          notebookUri,
          SSEntityE.evernoteNotebook, //type,
          notebookLabel, //label
          null, //description,
          notebookCreationTime, //creationTime,
          null, //read,
          false, //setPublic
          true, //createIfNotExists
          true, //withUserRestriction
          false)); //shouldCommit)
      
      actAndLog.addNotebook(
        servPar, 
        userUri, //user
        toolContext, 
        notebookUri,  //notebook
        notebookCreationTime, 
        false); //shouldCommit);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void addNote(
    final SSServPar      servPar,
    final SSUri          userUri,
    final SSToolContextE toolContext,
    final SSUri          noteUri,
    final SSLabel        noteLabel,
    final SSUri          notebookUri,
    final Long           creationTime) throws SSErr{
    
    try{
      final SSEntityServerI   entityServ    = (SSEntityServerI)   SSServReg.getServ(SSEntityServerI.class);
      final SSEvernoteServerI evernoteServ  = (SSEvernoteServerI) SSServReg.getServ(SSEvernoteServerI.class);
      
      entityServ.entityUpdate(
        new SSEntityUpdatePar(
          servPar,
          userUri,
          noteUri,
          SSEntityE.evernoteNote, //type,
          noteLabel, //label
          null, //description,
          creationTime, //creationTime,
          null, //read,
          false, //setPublic
          true, //createIfNotExists
          true, //withUserRestriction
          false)); //shouldCommit)
      
      if(notebookUri != null){
        
        final SSEntity nootebookEntity =
          entityServ.entityGet(
            new SSEntityGetPar(
              servPar,
              userUri,
              notebookUri, //entity
              true, //withUserRestriction
              null)); //descPar
        
        if(nootebookEntity == null){
          
          addNotebook(
            servPar,
            userUri,
            toolContext,
            notebookUri,
            null,
            null);
        }
      }
      
      evernoteServ.evernoteNoteAdd(
        new SSEvernoteNoteAddPar(
          servPar,
          userUri,
          notebookUri,
          noteUri,
          false)); //shouldCommit
      
      actAndLog.addNote(
        servPar, 
        userUri, 
        toolContext, 
        noteUri, //note
        SSUri.asListNotNull(notebookUri),//entities, 
        creationTime, 
        false); //shouldCommit);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public void addResource(
    final SSServPar      servPar,
    final SSUri          userUri,
    final SSToolContextE toolContext,
    final SSUri          resourceUri,
    final SSLabel        resourceLabel,
    final Long           resourceAddTime,
    final SSUri          noteUri) throws SSErr{
    
    try{
      final SSEntityServerI   entityServ    = (SSEntityServerI)   SSServReg.getServ(SSEntityServerI.class);
      final SSEvernoteServerI evernoteServ  = (SSEvernoteServerI) SSServReg.getServ(SSEvernoteServerI.class);
      
      entityServ.entityUpdate(
        new SSEntityUpdatePar(
          servPar,
          userUri,
          resourceUri,
          SSEntityE.evernoteResource, //type,
          resourceLabel, //label
          null, //description,
          resourceAddTime, //creationTime,
          null, //read,
          false, //setPublic
          true, //createIfNotExists
          true, //withUserRestriction
          false)); //shouldCommit)
      
      //TODO fix this hack
      try{
        
        evernoteServ.evernoteResourceAdd(
          new SSEvernoteResourceAddPar(
            servPar,
            userUri,
            noteUri,
            resourceUri,
            false)); //shouldCommit
        
      }catch(Exception error){
        SSLogU.warn(error);
        
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            servPar,
            userUri,
            noteUri,
            SSEntityE.evernoteNote, //type,
            null, //label
            null, //description,
            resourceAddTime, //creationTime,
            null, //read,
            false, //setPublic
            true, //createIfNotExists
            true, //withUserRestriction
            false)); //shouldCommit)
        
        evernoteServ.evernoteResourceAdd(
          new SSEvernoteResourceAddPar(
            servPar,
            userUri,
            noteUri,
            resourceUri,
            false)); //shouldCommit
      }
      
      actAndLog.addResource(
        servPar, 
        userUri, 
        toolContext, 
        resourceUri, //resource
        SSUri.asListNotNull(noteUri), //entitites
        resourceAddTime, 
        false); //shouldCommit);
        
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}
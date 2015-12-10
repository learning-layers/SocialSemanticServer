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
package at.kc.tugraz.ss.serv.dataimport.impl;

import at.kc.tugraz.ss.category.api.SSCategoryServerI;
import at.tugraz.sss.serv.SSFileU;
import at.tugraz.sss.serv.SSLogU;
import at.tugraz.sss.serv.SSStrU;
import at.kc.tugraz.ss.category.datatypes.SSCategoryLabel;
import at.kc.tugraz.ss.category.datatypes.par.SSCategoriesAddPar;
import at.kc.tugraz.ss.serv.auth.api.SSAuthServerI;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSSpaceE;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSLabel;
import at.kc.tugraz.ss.serv.dataimport.api.SSDataImportClientI;
import at.kc.tugraz.ss.serv.dataimport.api.SSDataImportServerI;
import at.kc.tugraz.ss.serv.dataimport.conf.SSDataImportConf;
import at.kc.tugraz.ss.serv.dataimport.datatypes.pars.SSDataImportAchsoPar;
import at.tugraz.sss.serv.SSServPar;
import at.kc.tugraz.ss.serv.dataimport.datatypes.pars.SSDataImportBitsAndPiecesPar;
import at.kc.tugraz.ss.serv.dataimport.datatypes.pars.SSDataImportEvalLogFilePar;
import at.kc.tugraz.ss.serv.dataimport.datatypes.pars.SSDataImportKCProjWikiProjectsPar;
import at.kc.tugraz.ss.serv.dataimport.datatypes.pars.SSDataImportKCProjWikiVorgaengePar;
import at.kc.tugraz.ss.serv.dataimport.datatypes.pars.SSDataImportMediaWikiUserPar;
import at.kc.tugraz.ss.serv.dataimport.datatypes.pars.SSDataImportSSSUsersFromCSVFilePar;
import at.kc.tugraz.ss.serv.dataimport.impl.bitsandpieces.SSDataImportBitsAndPiecesEvernoteImporter;
import at.kc.tugraz.ss.serv.dataimport.impl.bitsandpieces.SSDataImportBitsAndPiecesMailImporter;
import at.kc.tugraz.ss.serv.dataimport.impl.fct.op.SSDataImportAchsoFct;
import at.kc.tugraz.ss.serv.dataimport.impl.fct.reader.SSDataImportReaderFct;
import at.kc.tugraz.ss.serv.dataimport.impl.fct.sql.SSDataImportSQLFct;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityUpdatePar;
import at.kc.tugraz.ss.serv.job.i5cloud.datatypes.SSi5CloudAchsoVideo;
import at.kc.tugraz.ss.serv.jobs.evernote.api.SSEvernoteServerI;
import at.kc.tugraz.ss.serv.ss.auth.datatypes.pars.SSAuthRegisterUserPar;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.kc.tugraz.ss.service.filerepo.api.SSFileRepoServerI;
import at.kc.tugraz.ss.service.tag.api.SSTagServerI;
import at.kc.tugraz.ss.service.tag.datatypes.SSTagLabel;
import at.kc.tugraz.ss.service.tag.datatypes.pars.SSTagsAddPar;
import at.kc.tugraz.ss.service.userevent.api.SSUEServerI;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSErr;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.SSToolContextE;
import at.tugraz.sss.servs.kcprojwiki.datatype.SSKCProjWikiProject;
import at.tugraz.sss.servs.kcprojwiki.datatype.SSKCProjWikiVorgang;
import at.tugraz.sss.servs.kcprojwiki.datatype.SSKCProjWikiVorgangEmployeeResource;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import sss.serv.eval.api.SSEvalServerI;
import sss.serv.eval.datatypes.SSEvalLogE;
import sss.serv.eval.datatypes.SSEvalLogEntry;

public class SSDataImportImpl 
extends 
  SSServImplWithDBA 
implements 
  SSDataImportClientI, 
  SSDataImportServerI{
  
  public  static final Integer                 bitsAndPiecesImageMinWidth          = 250;
  public  static final Integer                 bitsAndPiecesImageMinHeight         = 250;
  private static final ReentrantReadWriteLock  bitsAndPiecesEvernoteImportsLock    = new ReentrantReadWriteLock();
  private static final Map<Thread, String>     bitsAndPiecesEvernoteImports        = new HashMap<>();
  private static final ReentrantReadWriteLock  bitsAndPiecesEmailsImportsLock      = new ReentrantReadWriteLock();
  private static final Map<Thread, String>     bitsAndPiecesEmailsImports          = new HashMap<>();

  private final SSDataImportSQLFct sqlFct;
  
  public SSDataImportImpl(final SSConfA conf) throws SSErr{
    
    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());
    
    this.sqlFct        = new SSDataImportSQLFct(dbSQL);
  }
  
  private Boolean addBitsAndPiecesEvernoteImport(
    final String authToken,
    final String authEmail) throws SSErr{
    
    try{
      
      if(!bitsAndPiecesEvernoteImportsLock.isWriteLockedByCurrentThread()){
        bitsAndPiecesEvernoteImportsLock.writeLock().lock();
      }
      
      if(bitsAndPiecesEvernoteImports.containsValue(authToken)){
        SSLogU.warn("attempted to start B&P evernote import concurrently for " + authEmail);
        return false;
      }
      
      bitsAndPiecesEvernoteImports.put(Thread.currentThread(), authToken);
      
      return true;
        
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return false;
    }finally{
      
      if(bitsAndPiecesEvernoteImportsLock.isWriteLockedByCurrentThread()){
        bitsAndPiecesEvernoteImportsLock.writeLock().unlock();
      }
    }
  }
  
  private void removeBitsAndPiecesEvernoteImport(final String authToken) throws SSErr{
    
    try{
      bitsAndPiecesEvernoteImportsLock.writeLock().lock();
      
      if(authToken != null){
        bitsAndPiecesEvernoteImports.remove(Thread.currentThread());
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      
      if(bitsAndPiecesEvernoteImportsLock.isWriteLockedByCurrentThread()){
        bitsAndPiecesEvernoteImportsLock.writeLock().unlock();
      }
    }
  }
  
  private Boolean addBitsAndPiecesEmailImport(
    final String authToken,
    final String authEmail) throws SSErr{
    
    try{
      
      if(!bitsAndPiecesEmailsImportsLock.isWriteLockedByCurrentThread()){
        bitsAndPiecesEmailsImportsLock.writeLock().lock();
      }
      
      if(bitsAndPiecesEmailsImports.containsValue(authToken)){
        SSLogU.warn("attempted to start B&P evernote import concurrently for " + authEmail);
        return false;
      }
      
      bitsAndPiecesEmailsImports.put(Thread.currentThread(), authToken);
      
      return true;
        
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return false;
    }finally{
      
      if(bitsAndPiecesEmailsImportsLock.isWriteLockedByCurrentThread()){
        bitsAndPiecesEmailsImportsLock.writeLock().unlock();
      }
    }
  }
  
  private void removeBitsAndPiecesEmailImport(final String authToken) throws SSErr{
    
    try{
      bitsAndPiecesEmailsImportsLock.writeLock().lock();
      
      if(authToken != null){
        bitsAndPiecesEmailsImports.remove(Thread.currentThread());
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      
      if(bitsAndPiecesEmailsImportsLock.isWriteLockedByCurrentThread()){
        bitsAndPiecesEmailsImportsLock.writeLock().unlock();
      }
    }
  }
  
  @Override
  public Boolean dataImportBitsAndPieces(final SSDataImportBitsAndPiecesPar par) throws SSErr{
    
    try{
      
      if(
        par.importEvernote &&
        !addBitsAndPiecesEvernoteImport(par.authToken, par.authEmail)){
        return false;
      }
      
      if(
        par.importEmails &&
        !addBitsAndPiecesEmailImport(par.authToken, par.authEmail)){
        return false;
      }
            
      final SSEntityServerI   entityServ      = (SSEntityServerI)   SSServReg.getServ(SSEntityServerI.class);
      final SSUEServerI       ueServ          = (SSUEServerI)       SSServReg.getServ(SSUEServerI.class);
      final SSFileRepoServerI fileServ        = (SSFileRepoServerI) SSServReg.getServ(SSFileRepoServerI.class);
      final SSAuthServerI     authServ        = (SSAuthServerI)     SSServReg.getServ(SSAuthServerI.class);
      final SSEvernoteServerI evernoteServ    = (SSEvernoteServerI) SSServReg.getServ(SSEvernoteServerI.class);
      final SSTagServerI      tagServ         = (SSTagServerI)      SSServReg.getServ(SSTagServerI.class);
      final SSEvalServerI     evalServ        = (SSEvalServerI)     SSServReg.getServ(SSEvalServerI.class);
      Boolean                 worked          = true;
      SSUri                   userUri         = null;
      
      try{

        dbSQL.startTrans(par.shouldCommit);

        userUri =
          authServ.authRegisterUser(
            new SSAuthRegisterUserPar(
              par.authEmail, //email
              "1234", //password
              SSLabel.get(par.authEmail),//evernoteInfo.userName,
              false, //updatePassword,
              false, //isSystemUser,
              false, //withUserRestriction,
              false)); //shouldCommit
            
        dbSQL.commit(par.shouldCommit);
      }catch(Exception error){
        
        worked = false;
        
        if(!dbSQL.rollBack(par.shouldCommit)){
          SSLogU.warn("sql rollback failed");
        }
        
        SSServErrReg.reset();
      }
      
      if(
      par.importEvernote &&
      userUri != null){
        
        try{

          dbSQL.startTrans(par.shouldCommit);

          new SSDataImportBitsAndPiecesEvernoteImporter(
            par,
            entityServ,
            fileServ,
            evernoteServ,
            ueServ,
            tagServ,
            evalServ,
            userUri).handle();

          dbSQL.commit(par.shouldCommit);
        }catch(Exception error){

          worked = false;

          if(!dbSQL.rollBack(par.shouldCommit)){
            SSLogU.warn("sql rollback failed");
          }

          SSServErrReg.reset();
        }
      }
      
      if(
        par.importEmails &&
        userUri != null){
        
        try{

          dbSQL.startTrans(par.shouldCommit);

          new SSDataImportBitsAndPiecesMailImporter(
            par,
            entityServ,
            fileServ, 
            evalServ, 
            ueServ,
            userUri).handle();

          dbSQL.commit(par.shouldCommit);

        }catch(Exception error){

          worked = false;

          if(!dbSQL.rollBack(par.shouldCommit)){
            SSLogU.warn("sql rollback failed");
          }

          SSServErrReg.reset();
        }
      }
      
      return worked;
    }catch(Exception error){
      
      if(!dbSQL.rollBack(par.shouldCommit)){
        SSLogU.warn("sql rollback failed");
      }
      
      SSServErrReg.regErrThrow(error);
      return false;
    }finally{
      
      try{
        
        if(par.importEvernote){
          removeBitsAndPiecesEvernoteImport(par.authToken);
        }
        
        if(par.importEmails){
          removeBitsAndPiecesEmailImport(par.authToken);
        }
      }catch(Exception error){
        SSLogU.warn("removing evernote import thread failed");
      }
    }
  }
  
  @Override
  public Map<String, String> dataImportSSSUsersFromCSVFile(final SSServPar parA) throws SSErr{
    
    try{
      final SSDataImportSSSUsersFromCSVFilePar par             = new SSDataImportSSSUsersFromCSVFilePar(parA);
      final List<String[]>                     lines           = SSDataImportReaderFct.readAllFromCSV(SSFileU.dirWorking(), par.fileName);
      final Map<String, String>                passwordPerUser = new HashMap<>();
      
      for(String[] line : lines){
        
        try{
          passwordPerUser.put(line[0].trim(), line[1].trim());
        }catch(Exception error){
        }
      }
      
      return passwordPerUser;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void dataImportMediaWikiUser(final SSServPar parA) throws SSErr{

    try{
      
      final SSDataImportMediaWikiUserPar par   = new SSDataImportMediaWikiUserPar(parA);
      final List<String[]>               lines = SSDataImportReaderFct.readAllFromCSV(SSFileU.dirWorkingDataCsv(), ((SSDataImportConf)conf).fileName);
      String firstName;
      String familyName;
      String password;
      String userName;
      
      dbSQL.startTrans(par.shouldCommit);
      
      for(String[] line : lines){
        
        try{
          firstName  = line[0].trim();
          familyName = line[1].trim();
          password   = line[2].trim();
          userName   = firstName + familyName;
          
          userName = userName.replaceAll("[^a-zA-Z0-9]+", SSStrU.empty);
          
          System.out.println(userName);
          
        }catch(Exception error){
          continue;
        }
        
        sqlFct.addUserWithGroup(userName, password);
      }
      
      dbSQL.commit(par.shouldCommit);
      
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(parA.shouldCommit)){
          
          SSServErrReg.reset();
          
          dataImportMediaWikiUser(parA);
        }else{
          SSServErrReg.regErrThrow(error);
          return;
        }
      }
      
      dbSQL.rollBack(parA.shouldCommit);
      SSServErrReg.regErrThrow(error);
    }
  }
  
  //TODO dtheiler: add transactions here
  @Override
  public void dataImportAchso(final SSServPar parA) throws SSErr {
    
    try{
      final SSAuthServerI             authServ     = (SSAuthServerI) SSServReg.getServ(SSAuthServerI.class);
      final SSDataImportAchsoPar      par          = new SSDataImportAchsoPar(parA);
      final List<SSi5CloudAchsoVideo> videoObjs    = 
        SSDataImportAchsoFct.getVideoObjs(
          par.user, 
          SSServCaller.i5CloudAchsoVideoInformationGet());
      
      SSUri authorUri;

      for(SSi5CloudAchsoVideo video : videoObjs){
        
        authorUri=
          authServ.authRegisterUser(
            new SSAuthRegisterUserPar(
              video.authorLabel + SSStrU.at + SSVocConf.systemEmailPostFix, //email
              "1234", //password
              video.authorLabel,//evernoteInfo.userName,
              false, //updatePassword,
              false, //isSystemUser,
              false, //withUserRestriction,
              true)); //shouldCommit
        
        ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityUpdate(
          new SSEntityUpdatePar(
            authorUri,
            video.id,
            null, //type,
            video.label, //label
            null, //description,
            video.creationTime, //creationTime,
            null, //read,
            false, //setPublic
            true, //createIfNotExists
            true, //withUserRestriction
            true)); //shouldCommit)
                
        ((SSTagServerI) SSServReg.getServ(SSTagServerI.class)).tagsAdd(
          new SSTagsAddPar(
            authorUri,
            SSTagLabel.get(video.keywords), //labels,
            SSUri.asListNotNull(video.id), //entities
            SSSpaceE.sharedSpace, //space
            null, //circles
            video.creationTime,  //creationTime
            true, //withUserRestriction
            true)); //shouldCommit
        
        final List<String> categoryLabels = new ArrayList<>();
        
        for(String annotation : video.annotations){
          
          try{
            SSCategoryLabel.get(annotation);
          }catch(Exception error){
            continue;            
          }
          
          categoryLabels.add(annotation);
        }

        ((SSCategoryServerI) SSServReg.getServ(SSCategoryServerI.class)).categoriesAdd(
          new SSCategoriesAddPar(
            authorUri,
            SSCategoryLabel.asListNotEmpty(SSCategoryLabel.get(categoryLabels)),
            video.id,
            SSSpaceE.sharedSpace,
            null, //circle
            video.creationTime,
            false,
            true));
      }
      
      System.out.println();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public Map<String, SSKCProjWikiVorgang> dataImportKCProjWikiVorgaenge(final SSDataImportKCProjWikiVorgaengePar par) throws SSErr{
    
    try{
      final Map<String, SSKCProjWikiVorgang> vorgaenge = new HashMap<>();
      final List<String[]>                   lines;
      String[]                               line;
      SSKCProjWikiVorgang                    vorgang;
      String                                 vorgangNumber;
      String                                 projectNumber;
      String                                 employee;
      Float                                  usedEmployeeHours;
      Float                                  totalEmployeeHours;
      Float                                  totalResources;
      Float                                  usedResources;
      SSKCProjWikiVorgangEmployeeResource    employeeResource;
      
      lines = SSDataImportReaderFct.readAllFromCSV(par.filePath);
      
//      Projekt;WP;Mitarbeiter;ZusArt ;SAnfang;SEnde;Plan-Std VG;verbrauchte Std. VG;Plan-Std. MA;Sum-h MA;
      for(Integer lineCounter = 1; lineCounter < lines.size(); lineCounter++){
        
        try{
        
          line = lines.get(lineCounter);
          
          projectNumber      = line[0].trim();
          vorgangNumber      = line[2].trim();
          employee           = line[4].trim();
          totalResources     = Float.valueOf(SSStrU.replaceAll(line[9].trim(),  SSStrU.comma, SSStrU.dot));
          usedResources      = Float.valueOf(SSStrU.replaceAll(line[10].trim(), SSStrU.comma, SSStrU.dot));
          totalEmployeeHours = Float.valueOf(SSStrU.replaceAll(line[12].trim(), SSStrU.comma, SSStrU.dot));
          usedEmployeeHours  = Float.valueOf(SSStrU.replaceAll(line[13].trim(), SSStrU.comma, SSStrU.dot));
          
          if(vorgaenge.containsKey(vorgangNumber)){
            
            vorgang = vorgaenge.get(vorgangNumber);
            
            if(!SSStrU.equals(vorgang.projectNumber, projectNumber)){
              SSLogU.warn("line " + (lineCounter + 1) + " project number difference; wont be imported");
              continue;
            }
          }else{
            
            vorgang = 
              new SSKCProjWikiVorgang(
                projectNumber,
                vorgangNumber);
            
            vorgaenge.put(vorgangNumber, vorgang);
          }

          if(
            vorgang.totalResources != null &&
            vorgang.totalResources.compareTo(totalResources) != 0){
           
            SSLogU.warn("line " + (lineCounter + 1) + " total resources difference; wont be imported");
            continue;
          }
          
          if(
            vorgang.usedResources != null &&
            vorgang.usedResources.compareTo(usedResources) != 0){
           
            SSLogU.warn("line " + (lineCounter + 1) + " used resources difference; wont be imported");
            continue;
          }
          
          vorgang.totalResources = totalResources;
          vorgang.usedResources  = usedResources;
          
          employeeResource       = vorgang.employeeResources.get(employee);
          
          if(employeeResource != null){
            SSLogU.warn("line " + (lineCounter + 1) + " employee resource already defined; wont be imported");
            continue;
          }
          
          vorgang.employeeResources.put(
            employee,
            new SSKCProjWikiVorgangEmployeeResource(
              employee,
              usedEmployeeHours,
              totalEmployeeHours));
          
          vorgaenge.put(vorgangNumber, vorgang);
        }catch(Exception error){
          SSLogU.warn("line " + (lineCounter + 1) + " reading from csv failed");
        }
      }
      
      return vorgaenge;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<SSEvalLogEntry> dataImportEvalLogFile(final SSDataImportEvalLogFilePar par) throws SSErr{
    
    try{
      final List<SSEvalLogEntry>             logEntries  = new ArrayList<>();
      final List<String[]>                   lines;
      String[]                               line;
      SSEvalLogEntry                         entry;

      //timestamp;tool context;user label;log type;entity;entity type;entity label;content;tag type;entities' ids;entities' labels;users' labels;episodespace;selected bits measure;not selected entities' ids;not selected entities' labels
      lines = SSDataImportReaderFct.readAllFromCSV(par.filePath);
      
      for(Integer lineCounter = 1; lineCounter < lines.size(); lineCounter++){
        
        try{
        
          line  = lines.get(lineCounter);
          entry = new SSEvalLogEntry();
          
          entry.timestamp                 = Long.valueOf      (line[0].trim());
          
          if(par.startTime > entry.timestamp){
            continue;
          }
          
          try{
            entry.toolContext               = SSToolContextE.get(line[1].trim());
          }catch(Exception error){}
          
          entry.userLabel                 = SSLabel.get       (line[2].trim());
          entry.logType                   = SSEvalLogE.get    (line[3].trim());
          
          try{
            entry.entity                    = SSUri.get         (line[4].trim());
          }catch(Exception error){}
          
          try{
            entry.entityType                = SSEntityE.get     (line[5].trim());
          }catch(Exception error){}
          
          entry.entityLabel               = SSLabel.get       (line[6].trim());
          entry.content                   = line[7].trim();
          entry.tagType                   = line[8].trim();
          
          try{
            entry.entityIDs.addAll            (SSUri.get  (SSStrU.splitDistinctWithoutEmptyAndNull(line[9].trim(), SSStrU.comma)));
          }catch(Exception error){}
          
          entry.entityLabels.addAll         (SSLabel.get(SSStrU.splitDistinctWithoutEmptyAndNull(line[10].trim(), SSStrU.comma)));
          entry.userLabels.addAll           (SSLabel.get(SSStrU.splitDistinctWithoutEmptyAndNull(line[11].trim(), SSStrU.comma)));
          
          try{
            entry.episodeSpace              = SSSpaceE.get(line[12].trim());
          }catch(Exception error){}
          
          entry.selectedBitsMeasure       = line[13].trim();
          
          try{
            entry.notSelectedEntityIds.addAll   (SSUri.get  (SSStrU.splitDistinctWithoutEmptyAndNull(line[14].trim(), SSStrU.comma)));
          }catch(Exception error){}
          
          entry.notSelectedEntityLabels.addAll(SSLabel.get(SSStrU.splitDistinctWithoutEmptyAndNull(line[15].trim(), SSStrU.comma)));
          
          logEntries.add(entry);
        }catch(Exception error){
          SSLogU.warn("line " + (lineCounter + 1) + " reading from csv failed");
        }
      }
      
      return logEntries;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public Map<String, SSKCProjWikiProject> dataImportKCProjWikiProjects(final SSDataImportKCProjWikiProjectsPar par) throws SSErr{
    
    try{
      final List<String[]>      lines;
      final Map<String, SSKCProjWikiProject> passwordPerUser = new HashMap<>();
      
      lines = SSDataImportReaderFct.readAllFromCSV(SSFileU.dirWorking(), par.fileName);
      
      for(String[] line : lines){
        
        try{
          passwordPerUser.put(line[0].trim(), new SSKCProjWikiProject(line[0].trim()));
        }catch(Exception error){
        }
      }
      
      return passwordPerUser;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}

//  private Boolean evernoteNoteUploadPNGToWebDav(String pngFilePath, String pngFileID){
//    
//    FileInputStream in = null;
//    
//    try{
//      SSFileU.openFileForRead(pngFilePath);
//    
//      SardineFactory.begin("wduza", "w!123a").put("http://kedemo.know-center.tugraz.at:80/webdav/knowBrainWeb/" + pngFileID, in);
//      
//      return true;
//      
//    }catch(Exception error){
//      SSLogU.logError(error, "couldnt upload note png to webdav");
//      return false;
//    }finally{
//      
//      if(in != null){
//        
//        try {
//          in.close();
//        } catch (IOException error2) {
//          SSLogU.logError(error2, "couldnt close note png input stream for webdav");
//          return false;
//        }
//      }
//    }
//  }


//    try{
//      
//      String inputFile = SSFileU.dirWorkingTmp() + htmlFileName;
//        String url = new File(inputFile).toURI().toURL().toString();
//        String outputFile = SSFileU.dirWorkingTmp() + pdfFileName;
//        noteOutputStream = new FileOutputStream(outputFile);
//        
//        ITextRenderer renderer = new ITextRenderer();
//        renderer.setDocument(url);
//        renderer.layout();
//        renderer.createPDF(noteOutputStream);
//      
//    } catch (Exception error1) {
//      SSLogU.logError(error1, "couldnt write to pdf");
//    }finally{
//      
//      if(noteOutputStream != null){
//        
//        try {
//          noteOutputStream.close();
//        } catch (IOException error) {
//          SSLogU.logError(error, "couldnt close pdf writer");
//        }
//      }
//    }

//@Override
//  public Boolean dataImportUserResourceTagFromWikipedia(final SSServPar parA) throws SSErr {
//    
//    final SSAuthServerI                               authServ      = (SSAuthServerI) SSServReg.getServ(SSAuthServerI.class);
//    final SSDataImportUserResourceTagFromWikipediaPar par           = new SSDataImportUserResourceTagFromWikipediaPar(parA);
//    int                                               counter       = 1;
//    int                                               tagCounter    = 0;
//    BufferedReader                                    lineReader    = null;
//    List<String>                                      tagList;
//    FileInputStream                                   dataImportFileIn;
//    String                                            line;
//    List<String>                                      lineSplit;
//    String                                            userLabel;
//    SSUri                                             resource;
//    SSUri                                             user;
//    String                                            tags;
//    String                                            categories;
//    Long                                              timestamp;
//    
//    try{
//      ((SSTagServerI) SSServReg.getServ(SSTagServerI.class)).tagsRemove(
//        new SSTagsRemovePar(
//          parA.user,
//          null,
//          null,
//          null,
//          SSSpaceE.sharedSpace,
//          null, //circle
//          false,
//          par.shouldCommit));
//      
//      dataImportFileIn = SSFileU.openFileForRead   (SSFileU.dirWorkingDataCsv() + ((SSDataImportConf)conf).fileName);
//      lineReader       = new BufferedReader        (new InputStreamReader(dataImportFileIn));
//      line             = lineReader.readLine();
//      
//      while(line != null){
//        
//        line       = SSStrU.removeDoubleQuotes(line); //        line       = SSStrU.replaceAll(line, SSStrU.dot,     SSStrU.empty);
//        line       = SSStrU.replaceAll        (line, SSStrU.percent, SSStrU.empty);
//        lineSplit  = SSStrU.split             (line, SSStrU.semiColon);
//        
//        if(
//          lineSplit == null ||
//          lineSplit.size() < 5){
//          
//          line = lineReader.readLine();
//          continue;
//        }
//        
//        categories = lineSplit.get(4);
//        
////        if(!categories.contains("health")){
////          line = lineReader.readLine();
////          continue;
////        }
//        
//        try{
//          resource  = SSUri.get(lineSplit.get(1));
//        }catch(Exception error){
//          line = lineReader.readLine();
//          continue;
//        }
//        
//        userLabel   = lineSplit.get   (0);
//        timestamp   = Long.parseLong  (lineSplit.get(2)) * 1000;
//        tags        = lineSplit.get   (3);
//        
//        user=
//          authServ.authRegisterUser(
//            new SSAuthRegisterUserPar(
//              userLabel + SSStrU.at + SSVocConf.systemEmailPostFix, //email
//              "1234", //password
//              SSLabel.get(userLabel),
//              false, //updatePassword,
//              false, //isSystemUser,
//              false, //withUserRestriction,
//              false)); //shouldCommit
//        
//        tagList     = SSStrU.splitDistinctWithoutEmptyAndNull(tags, SSStrU.comma);
//        tagCounter += tagList.size();
//
//        ((SSTagServerI) SSServReg.getServ(SSTagServerI.class)).tagsAdd(
//          new SSTagsAddPar(
//            user,
//            SSTagLabel.get(tagList), //labels
//            SSUri.asListNotNull(resource), //entities
//            SSSpaceE.sharedSpace, //space
//            null, //circles
//            timestamp, //creationTime
//            true, //withUserRestriction
//            par.shouldCommit)); //shouldCommit
//
//        SSLogU.info("line " + counter++ + " " + tagCounter + " time : " + new Date().getTime() + " user: " + user.toString() + " tags: " + tags);
//
//        line = lineReader.readLine();
//      }
//
//      return true;
//
//    }catch(Exception error){
//      
//      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
//        
//        if(dbSQL.rollBack(parA.shouldCommit)){
//          
//          SSServErrReg.reset();
//          
//          return dataImportUserResourceTagFromWikipedia(parA);
//        }else{
//          SSServErrReg.regErrThrow(error);
//          return null;
//        }
//      }
//      
//      dbSQL.rollBack(parA.shouldCommit);
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }finally{
//      
//      if(lineReader != null){
//        lineReader.close();
//      }
//    }
//  }
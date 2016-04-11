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
package at.tugraz.sss.servs.dataimport.impl;

import at.tugraz.sss.serv.util.SSFileU;
import at.tugraz.sss.serv.util.SSLogU;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.db.api.SSDBSQLI;
import at.tugraz.sss.servs.dataimport.api.SSDataImportClientI;
import at.tugraz.sss.servs.dataimport.api.SSDataImportServerI;
import at.tugraz.sss.servs.dataimport.datatype.SSDataImportBitsAndPiecesPar;
import at.tugraz.sss.servs.dataimport.datatype.SSDataImportEvalLogFilePar;
import at.tugraz.sss.servs.dataimport.datatype.SSDataImportKCProjWikiVorgaengePar;
import at.tugraz.sss.servs.dataimport.datatype.SSDataImportMediaWikiUserPar;
import at.tugraz.sss.servs.dataimport.datatype.SSDataImportSSSUsersFromCSVFilePar;
import at.tugraz.sss.serv.conf.SSConf;
import at.tugraz.sss.serv.db.api.SSDBNoSQLI;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.datatype.SSErr;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.serv.datatype.enums.SSToolContextE;
import at.tugraz.sss.serv.impl.api.*;
import at.tugraz.sss.servs.auth.api.*;
import at.tugraz.sss.servs.auth.datatype.*;
import at.tugraz.sss.servs.dataimport.conf.*;
import at.tugraz.sss.servs.kcprojwiki.datatype.SSKCProjWikiVorgang;
import at.tugraz.sss.servs.kcprojwiki.datatype.SSKCProjWikiVorgangEmployeeResource;
import java.text.SimpleDateFormat;
import java.util.Date;
import at.tugraz.sss.servs.eval.datatype.SSEvalLogE;
import at.tugraz.sss.servs.eval.datatype.SSEvalLogEntry;

public class SSDataImportImpl
extends
  SSServImplA
implements
  SSDataImportClientI,
  SSDataImportServerI{
  
  private final SSDataImportBNPEvernoteImporter       bnpEvernoteImporter = new SSDataImportBNPEvernoteImporter(); 
  private final SSDataImportBNPMailImporter           bnpMailImporter     = new SSDataImportBNPMailImporter();
  private final SSDataImportSQL                       sqlFct;
  private final SSDBSQLI                              dbSQL;
  private final SSDBNoSQLI                            dbNoSQL;
  
  public SSDataImportImpl(final SSDataImportConf conf) throws SSErr{
    
    super(conf);
    
    this.dbSQL         = (SSDBSQLI)   SSServReg.getServ(SSDBSQLI.class);
    this.dbNoSQL       = (SSDBNoSQLI) SSServReg.getServ(SSDBNoSQLI.class);
    this.sqlFct        = new SSDataImportSQL(dbSQL);
  }
  
  @Override
  public boolean dataImportBitsAndPieces(final SSDataImportBitsAndPiecesPar par) throws SSErr{
    
    try{
      
      final SSAuthServerI     authServ        = (SSAuthServerI)     SSServReg.getServ(SSAuthServerI.class);
      SSUri                   forUser         = null;
      
      try{
        
        dbSQL.startTrans(par, par.shouldCommit);
        
        forUser =
          authServ.authRegisterUser(
            new SSAuthRegisterUserPar(
              par,
              par.authEmail, //email
              "1234", //password
              SSLabel.get(par.authEmail),//evernoteInfo.userName,
              false, //updatePassword,
              false, //isSystemUser,
              false, //withUserRestriction,
              false)); //shouldCommit
        
        dbSQL.commit(par, par.shouldCommit);
      }catch(Exception error){
        
        SSLogU.err(error);
        
        try{
          dbSQL.rollBack(par, par.shouldCommit);
        }catch(Exception error2){
          SSLogU.err(error2);
        }
        
        return false;
      }
      
      boolean worked = true;
      
      if(par.importEvernote){
        
        try{
          
          dbSQL.startTrans(par, par.shouldCommit);
          
          bnpEvernoteImporter.handle(
            par, 
            SSConf.getLocalWorkPath(), 
            forUser);
          
          dbSQL.commit(par, par.shouldCommit);
          
        }catch(Exception error){
          
          worked = false;
          
          SSLogU.err(error);
          
          try{
            dbSQL.rollBack(par, par.shouldCommit);
          }catch(Exception error2){
            SSLogU.err(error2);
          }
        }
      }
      
      if(
        par.importEmails &&
        forUser != null){
        
        try{
          
          dbSQL.startTrans(par, par.shouldCommit);
          
          bnpMailImporter.handle(
            par, 
            forUser, 
            SSConf.getLocalWorkPath());
          
          dbSQL.commit(par, par.shouldCommit);
          
        }catch(Exception error){
          
          worked = false;
          
          SSLogU.err(error);
          
          try{
            dbSQL.rollBack(par, par.shouldCommit);
          }catch(Exception error2){
            SSLogU.err(error2);
          }
        }
      }
      
      return worked;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return false;
    }
  }
  
  @Override
  public Map<String, String> dataImportSSSUsersFromCSVFile(final SSDataImportSSSUsersFromCSVFilePar par) throws SSErr{
    
    try{
      final List<String[]>                     lines           = SSFileU.readAllFromCSV(par.filePath);
      final Map<String, String>                passwordPerUser = new HashMap<>();
      
      for(String[] line : lines){
        
        try{
          passwordPerUser.put(line[0].trim(), line[1].trim());
        }catch(Exception error){
          SSLogU.warn(error);
        }
      }
      
      return passwordPerUser;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void dataImportMediaWikiUser(final SSDataImportMediaWikiUserPar par) throws SSErr{
    
    try{
      
      final List<String[]> lines    = SSFileU.readAllFromCSV(SSConf.getSssWorkDirDataCsv(), ((SSDataImportConf) conf).fileName);
      String               userName = null;
      String firstName;
      String familyName;
      String password;
      String userNameStart;
      
      for(String[] line : lines){
        
        try{
          firstName  = line[4].trim();
          familyName = line[3].trim();
          password   = line[6].trim();
          userName   = firstName + familyName;
          
          userName      = userName.replaceAll("[^a-zA-Z0-9]+", SSStrU.empty);
          userNameStart = userName.substring(0, 1);
          userName      = userName.substring(1, userName.length());
          userName      = userName.toLowerCase();
          userName      = userNameStart + userName;
          
          System.out.println(userName);
          
        }catch(Exception error){
          SSLogU.warn(error);
          continue;
        }
        
        try{
          dbSQL.startTrans(par, par.shouldCommit);
          
          sqlFct.addUserWithGroup(
            par,
            userName,
            password);
          
          dbSQL.commit(par, par.shouldCommit);
        
        }catch(Exception error){

          try{
            dbSQL.rollBack(par, par.shouldCommit);
          }catch(Exception error2){
            SSLogU.err(error2, "user import for " + userName + " failed");
          }
        }
      }
      
    }catch(Exception error){
      
      try{
        dbSQL.rollBack(par, par.shouldCommit);
      }catch(Exception error2){
        SSLogU.err(error2);
      }
      
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
      String                                 vorgangName;
      String                                 projectNumber;
      String                                 employee;
      String                                 projectName;
      String                                 projectAcronym;
      String                                 vorgangStart;
      String                                 vorgangEnd;
      Float                                  usedEmployeeHours;
      Float                                  totalEmployeeHours;
      Float                                  totalResources;
      Float                                  usedResources;
      SSKCProjWikiVorgangEmployeeResource    employeeResource;
      
      lines = SSFileU.readAllFromCSV(par.filePath);
      //Projekt;Bezeichnung;ProjAcronym;WP;Vorgang;Mitarbeiter;ZusArt ;Abteilung;SAnfang;SEnde;Plan-Std VG;verbrauchte Std. VG;FertigPlan [Vorgangsplan (Sicht 1)];Plan-Std. MA;Sum-h MA;
      for(Integer lineCounter = 1; lineCounter < lines.size(); lineCounter++){
        
        try{
          
          line = lines.get(lineCounter);
          
          projectNumber      = line[0].trim();
          projectName        = line[1].trim();
          projectAcronym     = line[2].trim();
          vorgangNumber      = line[3].trim();
          vorgangName        = line[4].trim();
          employee           = line[5].trim();
          vorgangStart       = line[8].trim();
          vorgangEnd         = line[9].trim();
          totalResources     = Float.valueOf(SSStrU.replaceAll(line[10].trim(),  SSStrU.comma, SSStrU.dot));
          usedResources      = Float.valueOf(SSStrU.replaceAll(line[11].trim(), SSStrU.comma, SSStrU.dot));
          totalEmployeeHours = Float.valueOf(SSStrU.replaceAll(line[13].trim(), SSStrU.comma, SSStrU.dot));
          usedEmployeeHours  = Float.valueOf(SSStrU.replaceAll(line[14].trim(), SSStrU.comma, SSStrU.dot));
          
          if(vorgaenge.containsKey(vorgangNumber)){
            
            vorgang = vorgaenge.get(vorgangNumber);
            
            if(!SSStrU.isEqual(vorgang.projectNumber, projectNumber)){
              SSLogU.warn("line " + (lineCounter + 1) + " project number difference; wont be imported", null);
              continue;
            }
          }else{
            
            vorgang =
              new SSKCProjWikiVorgang(
                projectName,
                projectNumber,
                projectAcronym,
                vorgangName,
                vorgangNumber);
            
            vorgaenge.put(vorgangNumber, vorgang);
          }
          
          vorgang.vorgangStart = getDateFromBMDDate(vorgangStart);
          vorgang.vorgangEnd   = getDateFromBMDDate(vorgangEnd);

          if(
            vorgang.totalResources != null &&
            vorgang.totalResources.compareTo(totalResources) != 0){
            
            SSLogU.warn("line " + (lineCounter + 1) + " total resources difference; wont be imported", null);
            continue;
          }
          
          if(
            vorgang.usedResources != null &&
            vorgang.usedResources.compareTo(usedResources) != 0){
            
            SSLogU.warn("line " + (lineCounter + 1) + " used resources difference; wont be imported", null);
            continue;
          }
          
          vorgang.totalResources = totalResources;
          vorgang.usedResources  = usedResources;
          
          employeeResource       = vorgang.employeeResources.get(employee);
          
          if(employeeResource != null){
            SSLogU.warn("line " + (lineCounter + 1) + " employee resource already defined; wont be imported", null);
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
          SSLogU.warn("line " + (lineCounter + 1) + " reading from csv failed", error);
        }
      }
      
      return vorgaenge;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  
  private String getDateFromBMDDate(final String bmdDate) throws SSErr{
    
    try{
      
      if(SSStrU.isEmpty(bmdDate)){
        return SSStrU.empty;
      }
      
      final SimpleDateFormat bmdFormat  = new SimpleDateFormat("dd.MM.yyyy");
      final SimpleDateFormat wikiFormat = new SimpleDateFormat("yyyy/MM/dd");
      final Date             parsedDate = bmdFormat.parse(bmdDate);
      
      return wikiFormat.format(parsedDate);
      
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
      lines = SSFileU.readAllFromCSV(par.filePath);
      
      for(Integer lineCounter = 1; lineCounter < lines.size(); lineCounter++){
        
        try{
          
          line  = lines.get(lineCounter);
          entry = new SSEvalLogEntry();

          entry.timestamp                 = Long.valueOf      (line[0].trim());
          
          if(par.startTime > entry.timestamp){
            continue;
          }
          
          try{
            
            if(SSStrU.isEmpty(line[1].trim())){
              entry.toolContext = null;
            }else{
              entry.toolContext = SSToolContextE.get(line[1].trim());
            }
            
          }catch(Exception error){
            SSLogU.warn(error);
          }
          
          entry.userLabel                 = SSLabel.get       (line[2].trim());
          entry.userEmail                 = SSLabel.get       (line[3].trim());
          
          try{
            entry.logType                 = SSEvalLogE.get    (line[4].trim());
          }catch(Exception error){
            SSLogU.warn(error);
          }
          
          try{
            
            if(SSStrU.isEmpty(line[5].trim())){
              entry.entity = null;
            }else{
              entry.entity = SSUri.get (line[5].trim());
            }
            
          }catch(Exception error){
            SSLogU.warn(error);
          }
          
          try{
            if(SSStrU.isEmpty(line[6].trim())){
              entry.entityType = null;
            }else{
              entry.entityType = SSEntityE.get(line[6].trim());
            }
            
          }catch(Exception error){
            SSLogU.warn(error);
          }
          
          entry.entityLabel               = SSLabel.get       (line[7].trim());
          entry.content                   = line[8].trim();
          entry.tagType                   = line[9].trim();
          
          try{
            entry.entityIDs.addAll            (SSUri.get  (SSStrU.splitDistinctWithoutEmptyAndNull(line[10].trim(), SSStrU.comma)));
          }catch(Exception error){
            SSLogU.warn(error);
          }
          
          entry.entityLabels.addAll         (SSLabel.get(SSStrU.splitDistinctWithoutEmptyAndNull(line[11].trim(), SSStrU.comma)));
          entry.userLabels.addAll           (SSLabel.get(SSStrU.splitDistinctWithoutEmptyAndNull(line[12].trim(), SSStrU.comma)));
          
          try{
            
            if(!SSStrU.isEmpty(line[13].trim())){
              entry.circleTypes.addAll(SSCircleE.get(SSStrU.splitDistinctWithoutEmptyAndNull(line[13].trim(), SSStrU.comma)));
            }
            
          }catch(Exception error){
            SSLogU.warn(error);
          }
          
          entry.selectedBitsMeasure       = line[14].trim();
          
          try{
            entry.notSelectedEntityIds.addAll   (SSUri.get  (SSStrU.splitDistinctWithoutEmptyAndNull(line[15].trim(), SSStrU.comma)));
          }catch(Exception error){
            SSLogU.warn(error);
          }
          
          entry.notSelectedEntityLabels.addAll(SSLabel.get(SSStrU.splitDistinctWithoutEmptyAndNull(line[16].trim(), SSStrU.comma)));
          
          logEntries.add(entry);
        }catch(Exception error){
          SSLogU.warn("line " + (lineCounter + 1) + " reading from csv failed", error);
        }
      }
      
      return logEntries;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}
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

import at.kc.tugraz.socialserver.utils.SSFileU;
import at.kc.tugraz.socialserver.utils.SSLogU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.category.datatypes.par.SSCategoryLabel;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSSpaceE;
import at.kc.tugraz.ss.serv.db.api.SSDBGraphI;
import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.serv.dataimport.api.SSDataImportClientI;
import at.kc.tugraz.ss.serv.dataimport.api.SSDataImportServerI;
import at.kc.tugraz.ss.serv.dataimport.conf.SSDataImportConf;
import at.kc.tugraz.ss.serv.dataimport.datatypes.pars.SSDataImportAchsoPar;
import at.kc.tugraz.ss.serv.dataimport.datatypes.pars.SSDataImportUserResourceTagFromWikipediaPar;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.dataimport.datatypes.pars.SSDataImportEvernotePar;
import at.kc.tugraz.ss.serv.dataimport.datatypes.pars.SSDataImportEvernoteRet;
import at.kc.tugraz.ss.serv.dataimport.datatypes.pars.SSDataImportMediaWikiUserPar;
import at.kc.tugraz.ss.serv.dataimport.datatypes.pars.SSDataImportSSSUsersFromCSVFilePar;
import at.kc.tugraz.ss.serv.dataimport.impl.fct.op.SSDataImportAchsoFct;
import at.kc.tugraz.ss.serv.dataimport.impl.fct.reader.SSDataImportReaderFct;
import at.kc.tugraz.ss.serv.dataimport.impl.fct.sql.SSDataImportSQLFct;
import at.kc.tugraz.ss.serv.db.datatypes.sql.err.SSSQLDeadLockErr;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.job.i5cloud.datatypes.SSi5CloudAchsoVideo;
import at.kc.tugraz.ss.serv.serv.api.SSConfA;
import at.kc.tugraz.ss.serv.serv.api.SSServImplWithDBA;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSDataImportImpl extends SSServImplWithDBA implements SSDataImportClientI, SSDataImportServerI{
  
  private final SSDataImportSQLFct sqlFct;
  
  public SSDataImportImpl(final SSConfA conf, final SSDBGraphI dbGraph, final SSDBSQLI dbSQL) throws Exception{
    super(conf, dbGraph, dbSQL);
    
    this.sqlFct = new SSDataImportSQLFct         (dbSQL);
  }
  
  @Override
  public Map<String, String> dataImportSSSUsersFromCSVFile(final SSServPar parA) throws Exception{
    
    final SSDataImportSSSUsersFromCSVFilePar par = new SSDataImportSSSUsersFromCSVFilePar(parA);
    final List<String[]>                     lines;
    
    try{
      lines = SSDataImportReaderFct.readAllFromCSV(SSFileU.dirWorking(), par.fileName);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
      
    final Map<String, String> passwordPerUser = new HashMap<>();
    
    try{
      
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
  public void dataImportEvernote(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
   
    SSServCaller.checkKey(parA);

    sSCon.writeRetFullToClient(SSDataImportEvernoteRet.get(dataImportEvernote(parA), parA.op));
  }
  
  @Override
  public Boolean dataImportEvernote(final SSServPar parA) throws Exception{
    
    try{
      
      final SSDataImportEvernotePar par             = new SSDataImportEvernotePar(parA);
     
      final Thread evernoteImportHandler = 
        new Thread(
          new SSDataImportEvernoteHandler(
            (SSDataImportConf)conf, 
            par,
            this));
      
      evernoteImportHandler.start();
      
      return true;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void dataImportMediaWikiUser(final SSServPar parA) throws Exception{

    final SSDataImportMediaWikiUserPar par         = new SSDataImportMediaWikiUserPar(parA);
    final List<String[]>               lines;
    
    try{
      lines = SSDataImportReaderFct.readAllFromCSV(SSFileU.dirWorkingDataCsv(), ((SSDataImportConf)conf).fileName);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return;
    }
    
    String firstName;
    String familyName;
    String password;
    String userName;
    
    try{
      
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
    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(parA)){
        dataImportMediaWikiUser(parA);
      }else{
        SSServErrReg.regErrThrow(deadLockErr);
      }
      
    }catch(Exception error){
      dbSQL.rollBack(parA);
      SSServErrReg.regErrThrow(error);
    }
  }
  
  //TODO dtheiler: add transactions here
  @Override
  public void dataImportAchso(final SSServPar parA) throws Exception {
    
    try{
      final SSDataImportAchsoPar      par          = new SSDataImportAchsoPar(parA);
      final List<SSi5CloudAchsoVideo> videoObjs    = 
        SSDataImportAchsoFct.getVideoObjs(
          par.user, 
          SSServCaller.i5CloudAchsoVideoInformationGet());
      
      SSUri authorUri;

      for(SSi5CloudAchsoVideo video : videoObjs){
        
        authorUri =
          SSServCaller.authRegisterUser(
            par.user,
            video.author,
            "1234",
            true);
        
        SSServCaller.entityAddAtCreationTime(
          authorUri, 
          video.id,
          video.label, 
          video.creationTime,
          SSEntityE.entity, 
          null,
          true);
        
        SSServCaller.tagsAddAtCreationTime(
          authorUri, 
          video.id,
          video.keywords,
          SSSpaceE.sharedSpace, 
          video.creationTime, 
          true);
        
        final List<String> categoryLabels = new ArrayList<>();
        
        for(String annotation : video.annotations){
          
          if(!SSCategoryLabel.isCategoryLabel(annotation)){
            continue;
          }
          
          categoryLabels.add(annotation);
        }
        
        SSServCaller.categoriesAddAtCreationTime(
          authorUri, 
          video.id,
          categoryLabels,
          SSSpaceE.sharedSpace, 
          video.creationTime,
          false,
          true);
      }
      
      System.out.println();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public Boolean dataImportUserResourceTagFromWikipedia(final SSServPar parA) throws Exception {
    
    final SSDataImportUserResourceTagFromWikipediaPar par           = new SSDataImportUserResourceTagFromWikipediaPar(parA);
    int                                               counter       = 1;
    int                                               tagCounter    = 0;
    BufferedReader                                    lineReader    = null;
    List<String>                                      tagList;
    FileInputStream                                   dataImportFileIn;
    String                                            line;
    List<String>                                      lineSplit;
    String                                            userLabel;
    SSUri                                             resource;
    SSUri                                             user;
    String                                            tags;
    String                                            categories;
    Long                                              timestamp;
    
    try{
      
      SSServCaller.tagsRemove(null, null, null, SSSpaceE.sharedSpace, par.shouldCommit);
      
      dataImportFileIn = SSFileU.openFileForRead   (SSFileU.dirWorkingDataCsv() + ((SSDataImportConf)conf).fileName);
      lineReader       = new BufferedReader        (new InputStreamReader(dataImportFileIn));
      line             = lineReader.readLine();
      
      while(line != null){
        
        line       = SSStrU.removeDoubleQuotes(line); //        line       = SSStrU.replaceAll(line, SSStrU.dot,     SSStrU.empty);
        line       = SSStrU.replaceAll        (line, SSStrU.percent, SSStrU.empty);
        lineSplit  = SSStrU.split             (line, SSStrU.semiColon);
        
        if(
          lineSplit == null ||
          lineSplit.size() < 5){
          
          line = lineReader.readLine();
          continue;
        }
        
        categories = lineSplit.get(4);
        
//        if(!categories.contains("health")){
//          line = lineReader.readLine();
//          continue;
//        }
        
        try{
          resource  = SSUri.get(lineSplit.get(1));
        }catch(Exception error){
          line = lineReader.readLine();
          continue;
        }
        
        userLabel   = lineSplit.get   (0);
        timestamp   = Long.parseLong  (lineSplit.get(2)) * 1000;
        tags        = lineSplit.get   (3);
        
        user = 
          SSServCaller.authRegisterUser(
            par.user, 
            SSLabel.get(userLabel), 
            "1234", 
            false);
        
        tagList     = SSStrU.splitDistinctWithoutEmptyAndNull(tags, SSStrU.comma);
        tagCounter += tagList.size();

        SSServCaller.tagsAddAtCreationTime(
          user, 
          resource,
          tagList, 
          SSSpaceE.sharedSpace, 
          timestamp, 
          par.shouldCommit);

        SSLogU.info("line " + counter++ + " " + tagCounter + " time : " + new Date().getTime() + " user: " + user.toString() + " tags: " + tags);

        line = lineReader.readLine();
      }

      return true;

    }catch(SSSQLDeadLockErr deadLockErr){
      
      if(dbSQL.rollBack(parA)){
        return dataImportUserResourceTagFromWikipedia(parA);
      }else{
        SSServErrReg.regErrThrow(deadLockErr);
        return null;
      }
      
    }catch(Exception error){
      dbSQL.rollBack(parA);
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      
      if(lineReader != null){
        lineReader.close();
      }
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
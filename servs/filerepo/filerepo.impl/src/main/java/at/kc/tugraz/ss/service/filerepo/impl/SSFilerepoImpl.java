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
package at.kc.tugraz.ss.service.filerepo.impl;

import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntitiesGetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUpdatePar;
import at.tugraz.sss.servs.file.datatype.par.SSEntityFileAddPar;
import at.tugraz.sss.servs.file.datatype.par.SSEntityFilesGetPar;
import at.tugraz.sss.serv.SSFileExtE;
import at.tugraz.sss.serv.SSMimeTypeE;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSSocketCon;
import at.tugraz.sss.serv.SSEntityE;
import at.kc.tugraz.ss.service.filerepo.api.*;
import at.kc.tugraz.ss.service.filerepo.conf.*;
import at.kc.tugraz.ss.service.filerepo.datatypes.*;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSEntityHandlerImplI;
import at.tugraz.sss.util.SSServCallerU;
import at.kc.tugraz.ss.service.filerepo.datatypes.pars.SSFileDownloadPar;
import at.kc.tugraz.ss.service.filerepo.datatypes.pars.SSFileReplacePar;
import at.kc.tugraz.ss.service.filerepo.datatypes.pars.SSFileUploadPar;
import at.kc.tugraz.ss.service.filerepo.impl.fct.SSFileSQLFct;
import at.tugraz.sss.serv.SSCircleContentChangedPar;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSEntityCopiedPar;
import at.tugraz.sss.serv.SSEntityCopyPar;
import at.tugraz.sss.serv.SSEntityDescriberPar;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.caller.SSServCaller;
import java.util.*;
import java.util.List;

public class SSFilerepoImpl 
extends SSServImplWithDBA
implements 
  SSFileRepoClientI, 
  SSFileRepoServerI, 
  SSEntityHandlerImplI{

  private final SSFileSQLFct sqlFct;
  
  public SSFilerepoImpl(
    final SSFileRepoConf conf) throws Exception{

    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());
    
    sqlFct = new SSFileSQLFct   (this);
  }
  
@Override
  public void copyEntity(
    final SSEntity                  entity,
    final SSEntityCopyPar           par) throws Exception{
    
  }
  
  @Override
  public void entityCopied(final SSEntityCopiedPar par) throws Exception{
    
  }
  
  @Override
  public List<SSUri> getSubEntities(
    final SSUri         user,
    final SSUri         entity,
    final SSEntityE     type) throws Exception{

    return new ArrayList<>();
  }
  
  @Override
  public List<SSUri> getParentEntities(
    final SSUri         user,
    final SSUri         entity,
    final SSEntityE     type) throws Exception{
    
    return new ArrayList<>();
  }
  
  @Override
  public void circleContentChanged(final SSCircleContentChangedPar par) throws Exception{
    
  }
  
  @Override
  public SSEntity getUserEntity(
    final SSEntity             entity, 
    final SSEntityDescriberPar par) throws Exception{
    
    try{
      
      if(par.setFiles){
        
        final List<SSUri> files =
          filesGet(
            new SSEntityFilesGetPar(
              null, 
              null, 
              par.user, 
              entity.id, 
              par.withUserRestriction));

        if(!files.isEmpty()){
          entity.file = files.get(0);
        }
      }
      
      switch(entity.type){
        
        case file:{
          
          final SSFileExtE  fileExt  = SSFileExtE.ext(SSStrU.removeTrailingSlash(entity));
          final SSMimeTypeE mimeType = SSMimeTypeE.mimeTypeForFileExt (fileExt);
          
          final SSFile file = SSFile.get(entity.id, fileExt, mimeType);
          
          return SSFile.get(
              file,
              entity);
        }
      }
      
      return entity;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void fileDownload(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    final SSFileDownloadPar par = (SSFileDownloadPar) parA.getFromJSON(SSFileDownloadPar.class);
    
    par.sSCon = sSCon;
    
    fileDownload(par);
  }

  @Override
  public void fileDownload(final SSFileDownloadPar par) throws Exception{
    
    try{
      
      SSServCallerU.canUserReadEntity(par.user, par.file);
      
      new Thread(new SSFileDownloader((SSFileRepoConf)conf, par, this)).start();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  @Override
  public void fileReplace(final SSSocketCon sSCon, final SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    final SSFileReplacePar par = (SSFileReplacePar) parA.getFromJSON(SSFileReplacePar.class);
    
    par.sSCon = sSCon;
    
    fileReplace(par);
  }
  
  @Override
  public void fileReplace(final SSFileReplacePar par) throws Exception{
    
    try{
      
      SSServCallerU.canUserReadEntity(par.user, par.file);
      
      new Thread(new SSFileReplacer((SSFileRepoConf)conf, par, this)).start();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  @Override
  public void fileUpload(final SSSocketCon sSCon, final SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    final SSFileUploadPar par = (SSFileUploadPar) parA.getFromJSON(SSFileUploadPar.class);
    
    par.sSCon = sSCon;
    
    fileUpload(par);
  }
  
  @Override
  public void fileUpload(final SSFileUploadPar par) throws Exception{
    
    try{
      new Thread(new SSFileUploader((SSFileRepoConf) conf, par)).start();
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  @Override
  public SSUri fileAdd(final SSEntityFileAddPar par) throws Exception{
    
    try{
      
     if(par.file == null){
        par.file = SSServCaller.vocURICreate();
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityUpdate(
        new SSEntityUpdatePar(
          null, 
          null, 
          par.user, 
          par.file,  //entity
          SSEntityE.file,  //type
          null, //label, 
          null, //description, 
          null, //entitiesToAttach,
          null, //creationTime, 
          null, //read, 
          null, //setPublic, 
          par.withUserRestriction, //withUserRestriction
          false)); //shouldCommit)
      
      sqlFct.addFile(par.file);
      
      if(par.entity != null){
        
        ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityUpdate(
          new SSEntityUpdatePar(
            null,
            null,
            par.user,
            par.entity,  //entity
            null,  //type
            null, //label,
            null, //description,
            SSUri.asListWithoutNullAndEmpty(par.file), //entitiesToAttach,
            null, //creationTime,
            null, //read,
            null, //setPublic,
            par.withUserRestriction, //withUserRestriction
            false)); //shouldCommit)
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return par.file;
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return fileAdd(par);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(par.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public List<SSUri> filesGet(final SSEntityFilesGetPar par) throws Exception{
    
    try{
    
      final List<SSUri> files = new ArrayList<>();
      
      if(par.entity == null){
        throw new SSErr(SSErrE.parameterMissing);
      }
      
      if(par.withUserRestriction){
        SSServCallerU.canUserReadEntity(par.user, par.entity);
      }
      
      files.addAll(sqlFct.getFiles(par.entity));
      
      if(!par.withUserRestriction){
        return files;
      }
      
      return SSUri.getFromEntitites(
        ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entitiesGet(
          new SSEntitiesGetPar(
            null,
            null,
            par.user,
            files,  //entities
            null, //types,
            null, //descPar,
            par.withUserRestriction)));// withUserRestriction
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}
  
//  public static SSUri getFileDefaultUri(
//     String fileId) throws Exception{
//    
//    if(SSObjectUtils.isNull (fileId)){
//      throw new MalformedURLException("Cannot get default file uri for null id");
//    }
//   
//    return SSUri.get(SSVocServ.inst().serv().getUriDefaultFile() + fileId);
//  }
//  public static SSUri getFileDefaultUri(
//     SSUri fileUri) throws Exception{
//    
//    String fileAsString;
//    
//    if(SSObjectUtils.isNull (fileUri)){
//      return fileUri;
//    }
//    
//    fileAsString = SSStrU.removeTrailingSlash(fileUri.toString());
//    
//    return SSUri.get(SSVocServ.inst().serv().getUriDefaultFile() + fileAsString.substring(fileAsString.lastIndexOf(strU.slash) + 1));
//  }
//  public static SSUri getFileUriInDomain(
//     SSUri fileUri) throws Exception{
//    
//    return SSUri.get(getFileUri() + getIdFromFileUri(fileUri));
//  }
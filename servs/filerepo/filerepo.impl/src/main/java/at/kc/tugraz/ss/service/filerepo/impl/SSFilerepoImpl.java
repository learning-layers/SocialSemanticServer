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
package at.kc.tugraz.ss.service.filerepo.impl;

import at.tugraz.sss.serv.SSFileExtE;
import at.tugraz.sss.serv.SSFileU;
import at.tugraz.sss.serv.SSLogU;
import at.tugraz.sss.serv.SSMimeTypeE;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSSocketCon;
import at.kc.tugraz.ss.conf.conf.SSCoreConf;
import at.tugraz.sss.serv.SSEntityE;
import at.kc.tugraz.ss.service.filerepo.api.*;
import at.kc.tugraz.ss.service.filerepo.conf.*;
import at.kc.tugraz.ss.service.filerepo.datatypes.*;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSEntityDescriberI;
import at.tugraz.sss.serv.SSEntityHandlerImplI;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.tugraz.sss.util.SSServCallerU;
import at.kc.tugraz.ss.service.filerepo.datatypes.pars.SSFileDownloadPar;
import at.kc.tugraz.ss.service.filerepo.datatypes.pars.SSFileIDFromURIPar;
import at.kc.tugraz.ss.service.filerepo.datatypes.pars.SSFileReplacePar;
import at.kc.tugraz.ss.service.filerepo.datatypes.pars.SSFileThumbBase64GetPar;
import at.kc.tugraz.ss.service.filerepo.datatypes.pars.SSFileUploadPar;
import at.kc.tugraz.ss.service.filerepo.impl.fct.activity.SSFileActivityFct;
import at.tugraz.sss.serv.SSCircleContentChangedPar;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSEntityDescriberPar;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.tugraz.sss.serv.SSServPar;
import java.util.*;
import java.util.List;

public class SSFilerepoImpl 
extends SSServImplWithDBA
implements 
  SSFileRepoClientI, 
  SSFileRepoServerI, 
  SSEntityHandlerImplI, 
  SSEntityDescriberI{

  public SSFilerepoImpl(
    final SSFileRepoConf conf) throws Exception{

    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());
  }
  
  @Override
  public void copyEntity(
    final SSUri        user,
    final List<SSUri>  users,
    final SSUri        entity,
    final List<SSUri>  entitiesToExclude,
    final SSEntityE    entityType) throws Exception{
    
  }
  
  @Override
  public List<SSUri> getSubEntities(
    final SSUri         user,
    final SSUri         entity,
    final SSEntityE     type) throws Exception{

    return null;
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
    
    for(SSEntity entityToAdd: par.entitiesToAdd){
      
      switch(entityToAdd.type){
        
        case file:{
          
          SSFileActivityFct.shareFileWithUser(
            par.user,
            entityToAdd.id,
            par.usersToPushEntitiesTo);
          
          break;
        }
      }
    }
  }
  
  @Override
  public void removeDirectlyAdjoinedEntitiesForUser(
    final SSUri       userUri,
    final SSEntityE   entityType,
    final SSUri       entityUri,
    final Boolean     removeUserTags,
    final Boolean     removeUserRatings,
    final Boolean     removeFromUserColls,
    final Boolean     removeUserLocations) throws Exception{
  }
  
  @Override
  public SSEntity getUserEntity(
    final SSEntity             entity, 
    final SSEntityDescriberPar par) throws Exception{
    
    try{
      
      switch(entity.type){
        
        case file:{
          
          final SSFileExtE  fileExt  = SSFileExtE.ext(SSStrU.removeTrailingSlash(entity));
          final SSMimeTypeE mimeType = SSMimeTypeE.mimeTypeForFileExt (fileExt);
          
          if(par.setThumb){
            
            entity.thumb =
              SSServCaller.fileThumbBase64Get(
                par.user,
                entity.id);
          }
          
          return 
            SSFile.get(
              entity,
              fileExt,
              mimeType);
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
      new Thread(new SSFileUploader((SSFileRepoConf) conf, par, this)).start();
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  @Override
  public String fileIDFromURI(final SSFileIDFromURIPar par) throws Exception{

    try{
      String result = SSStrU.removeTrailingSlash(SSStrU.toStr(par.file));
      
      result = result.substring(result.lastIndexOf(SSStrU.slash) + 1);

      return result;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public String fileThumbBase64Get(final SSFileThumbBase64GetPar par) throws Exception{
    
    try{
      
      final List<SSUri>  thumbUris = SSServCaller.entityThumbsGet(par.user, par.file);
      
      if(thumbUris.isEmpty()){
        SSLogU.warn("thumb couldnt be retrieved from file " + par.file);
        return null;
      }
      
      final String pngFilePath = 
        SSCoreConf.instGet().getSss().getLocalWorkPath() + fileIDFromURI(new SSFileIDFromURIPar(null, null, par.user, thumbUris.get(0)));
      
      return SSFileU.readPNGToBase64Str(pngFilePath);
      
    }catch(Exception error){
      SSLogU.warn("base 64 file thumb couldnt be retrieved");
      SSServErrReg.reset();
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
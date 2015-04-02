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
import at.kc.tugraz.ss.service.filerepo.datatypes.pars.SSFileSetReaderOrWriterPar;
import at.kc.tugraz.ss.service.filerepo.datatypes.pars.SSFileUserFileWritesPar;
import at.kc.tugraz.ss.service.filerepo.datatypes.pars.SSFileRemoveReaderOrWriterPar;
import at.kc.tugraz.ss.service.filerepo.datatypes.pars.SSFileWritingMinutesLeftPar;
import at.kc.tugraz.ss.service.filerepo.datatypes.pars.SSFileCanWritePar;
import at.tugraz.sss.serv.SSSocketCon;
import at.kc.tugraz.ss.conf.conf.SSCoreConf;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityDescGetPar;
import at.tugraz.sss.serv.SSEntityE;
import at.kc.tugraz.ss.service.filerepo.api.*;
import at.kc.tugraz.ss.service.filerepo.conf.*;
import at.kc.tugraz.ss.service.filerepo.datatypes.*;
import at.kc.tugraz.ss.service.filerepo.datatypes.rets.SSFileCanWriteRet;
import at.kc.tugraz.ss.service.filerepo.datatypes.rets.SSFileGetEditingFilesRet;
import at.kc.tugraz.ss.service.filerepo.datatypes.rets.SSFileWritingMinutesLeftRet;
import at.kc.tugraz.ss.service.filerepo.datatypes.rets.SSFileRemoveReaderOrWriterRet;
import at.kc.tugraz.ss.service.filerepo.datatypes.rets.SSFileSetReaderOrWriterRet;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSEntityDescriberI;
import at.tugraz.sss.serv.SSEntityHandlerImplI;
import at.tugraz.sss.serv.SSServImplMiscA;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.tugraz.sss.serv.caller.SSServCallerU;
import at.kc.tugraz.ss.service.filerepo.datatypes.pars.SSFileDownloadPar;
import at.kc.tugraz.ss.service.filerepo.datatypes.pars.SSFileIDFromURIPar;
import at.kc.tugraz.ss.service.filerepo.datatypes.pars.SSFileExtGetPar;
import at.kc.tugraz.ss.service.filerepo.datatypes.pars.SSFileThumbBase64GetPar;
import at.kc.tugraz.ss.service.filerepo.datatypes.rets.SSFileExtGetRet;
import at.kc.tugraz.ss.service.filerepo.impl.fct.SSFileFct;
import at.kc.tugraz.ss.service.filerepo.impl.fct.activity.SSFileRepoActivityFct;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServParI;
import java.util.*;
import java.util.List;

public class SSFilerepoImpl 
extends SSServImplMiscA
implements 
  SSFileRepoClientI, 
  SSFileRepoServerI, 
  SSEntityHandlerImplI, 
  SSEntityDescriberI{

  private final Map<String, SSFileRepoFileAccessProperty> fileAccessProps;

  public SSFilerepoImpl(
    final SSFileRepoConf                           conf, 
    final Map<String, SSFileRepoFileAccessProperty> fileAccessProps) throws Exception{

    super(conf);

    this.fileAccessProps = fileAccessProps;
  }
  
  @Override
  public Boolean copyUserEntity(
    final SSUri        user,
    final List<SSUri>  users,
    final SSUri        entity,
    final List<SSUri>  entitiesToExclude,
    final SSEntityE    entityType) throws Exception{
    
    return false;
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
  public Boolean setUserEntityPublic(
    final SSUri          userUri,
    final SSUri          entityUri, 
    final SSEntityE      entityType,
    final SSUri          publicCircleUri) throws Exception{

    return false;
  }
  
  @Override
  public void shareUserEntity(
    final SSUri          user, 
    final List<SSUri>    usersToShareWith,
    final SSUri          entity, 
    final SSUri          circleUri,
    final SSEntityE      entityType,
    final Boolean        saveActivity) throws Exception{
    
    switch(entityType){
      case file: 
        SSFileRepoActivityFct.shareFileWithUser(user, entity, usersToShareWith, saveActivity);
    }
  }
  
  @Override
  public void shareUserEntityWithCircle(
    final SSUri        userUri,
    final SSUri        circleUri,
    final SSUri        entityUri,
    final SSEntityE    entityType) throws Exception{
    
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
    final SSUri              user,
    final SSEntity           entity) throws Exception{
    
    switch(entity.type){
      case file:
//        return SSServCaller.videoUserGet(user, entity.id);
    }
    
    return entity;
  }
  
  @Override
  public SSEntity getDescForEntity(
    final SSServParI parA,
    final SSEntity      desc) throws Exception{

    final SSEntityDescGetPar par = (SSEntityDescGetPar) parA;
    
    switch(desc.type){
      case file:{
        
        final SSFileExtE  fileExt  = SSServCaller.fileExtGet        (par.user, par.entity);
        final SSMimeTypeE mimeType = SSMimeTypeE.mimeTypeForFileExt (fileExt);
        
        if(par.getThumb){
          
          desc.thumb =
            SSServCaller.fileThumbBase64Get(
              par.user,
              par.entity);
        }
        
        return SSFile.get(
          desc,
          fileExt,
          mimeType);
      }
      
      default: return desc;
    }
  }
  
  @Override
  public void fileCanWrite(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    sSCon.writeRetFullToClient(fileCanWrite(parA), parA.op);
  }

  @Override
  public void fileSetReaderOrWriter(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    sSCon.writeRetFullToClient(fileSetReaderOrWriter(parA), parA.op);
  }

  @Override
  public void fileRemoveReaderOrWriter(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    sSCon.writeRetFullToClient(fileRemoveReaderOrWriter(parA), parA.op);
  }

  @Override
  public void fileWritingMinutesLeft(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    sSCon.writeRetFullToClient(fileWritingMinutesLeft(parA), parA.op);
  }

  @Override
  public void fileUserFileWrites(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    sSCon.writeRetFullToClient(fileUserFileWrites(parA), parA.op);
  }

  @Override
  public void fileDownload(final SSSocketCon sSCon, final SSServPar parA) throws Exception{
    
    SSServCallerU.checkKey(parA);
    
    try{
      
      final SSFileDownloadPar par = new SSFileDownloadPar(parA);

      SSServCallerU.canUserReadEntity(par.user, par.file);
      
      new Thread(new SSFileDownloader((SSFileRepoConf)conf, sSCon, par)).start();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  @Override
  public void fileReplace(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    new Thread(new SSFileReplacer((SSFileRepoConf)conf, sSCon, parA, fileAccessProps)).start();
  }

  @Override
  public void fileUpload(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    new Thread(new SSFileUploader((SSFileRepoConf)conf, sSCon, parA)).start();
  }

  @Override
  public void fileExtGet(SSSocketCon sSCon, SSServPar parA) throws Exception{
        
    SSServCallerU.checkKey(parA);

    sSCon.writeRetFullToClient(new SSFileExtGetRet(fileExtGet(parA), parA.op), parA.op);
  }

  /* SSFileRepoServerI */

  @Override
  public String fileExtGet(final SSServPar parA) throws Exception{

    final SSFileExtGetPar par = new SSFileExtGetPar(parA);
    String                result;
    
    try{
      
      result = SSServCaller.fileIDFromURI(par.user, par.file);
      
      if(
        result                         == null ||
        result.lastIndexOf(SSStrU.dot) == -1){
        throw new Exception("file id from uri not found");
      }
      
      return result.substring(result.lastIndexOf(SSStrU.dot) + 1, result.length());
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public void fileUpdateWritingMinutes(SSServPar parI){

    synchronized(fileAccessProps){

      for(SSFileRepoFileAccessProperty fileAccessProperty : fileAccessProps.values()){
        fileAccessProperty.updateWritingMinutes();
      }
    }
  }

  @Override
  public SSFileGetEditingFilesRet fileUserFileWrites(SSServPar parI) throws Exception{

    SSFileUserFileWritesPar par = new SSFileUserFileWritesPar(parI);
    final SSFileGetEditingFilesRet result;
    List<SSUri> fileUris = new ArrayList<>();

    SSFileFct.getEditingFileUris(fileAccessProps, par.user, fileUris);

    result = 
      new SSFileGetEditingFilesRet(
        par.op, 
        SSStrU.distinctWithoutEmptyAndNull(fileUris), 
        null);

    for(String fileUri : result.files){
      result.labels.add(SSStrU.toStr(SSServCaller.entityGet(SSUri.get(fileUri)).label));
    }

    return result;
  }

  @Override
  public SSFileCanWriteRet fileCanWrite(SSServPar parI) throws Exception{

    SSFileCanWritePar par = new SSFileCanWritePar(parI);
    SSFileCanWriteRet result = new SSFileCanWriteRet(SSStrU.toStr(par.file), par.op);

    result.canWrite = SSFileFct.canWrite(fileAccessProps, par.user, par.file);

    return result;
  }

  @Override
  public SSFileSetReaderOrWriterRet fileSetReaderOrWriter(SSServPar parI) throws Exception{

    SSFileSetReaderOrWriterPar par = new SSFileSetReaderOrWriterPar(parI);

    SSFileSetReaderOrWriterRet result = new SSFileSetReaderOrWriterRet(SSStrU.toStr(par.file), par.op);

    result.worked = SSFileFct.setReaderOrWriter(fileAccessProps, par.user, par.file, par.write);

    return result;
  }

  @Override
  public SSFileRemoveReaderOrWriterRet fileRemoveReaderOrWriter(SSServPar parI) throws Exception{

    SSFileRemoveReaderOrWriterPar par = new SSFileRemoveReaderOrWriterPar(parI);

    SSFileRemoveReaderOrWriterRet result = new SSFileRemoveReaderOrWriterRet(SSStrU.toStr(par.file), par.op);

    result.worked = SSFileFct.removeReaderOrWriter(fileAccessProps, par.user, par.file, par.write);

    return result;
  }

  @Override
  public SSFileWritingMinutesLeftRet fileWritingMinutesLeft(SSServPar parI) throws Exception{

    SSFileWritingMinutesLeftPar par = new SSFileWritingMinutesLeftPar(parI);

    SSFileWritingMinutesLeftRet result = new SSFileWritingMinutesLeftRet(par.file, par.op);

    result.writingMinutesLeft = SSFileFct.getWritingMinutesLeft(fileAccessProps, par.user, par.file);

    return result;
  }

  @Override
  public String fileIDFromURI(final SSServPar parA) throws Exception{

    final SSFileIDFromURIPar par = new SSFileIDFromURIPar(parA);
    String result;

    try{
      result = SSStrU.removeTrailingSlash(SSStrU.toStr(par.file));
      result = result.substring(result.lastIndexOf(SSStrU.slash) + 1);

      return result;
    }catch(Exception error){
      SSServErrReg.regErrThrow(new Exception("given file uri not valid"));
      return null;
    }
  }
  
  @Override
  public String fileThumbBase64Get(final SSServPar parA) throws Exception{
    
    try{
      
      final SSFileThumbBase64GetPar par = new SSFileThumbBase64GetPar(parA);
      final List<SSUri>             thumbUris = SSServCaller.entityThumbsGet(par.user, par.file);
      
      if(thumbUris.isEmpty()){
        SSLogU.warn("thumb couldnt be retrieved from file " + par.file);
        return null;
      }
      
      final String pngFilePath = SSCoreConf.instGet().getSs().getLocalWorkPath() + SSServCaller.fileIDFromURI (par.user, thumbUris.get(0));
      
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
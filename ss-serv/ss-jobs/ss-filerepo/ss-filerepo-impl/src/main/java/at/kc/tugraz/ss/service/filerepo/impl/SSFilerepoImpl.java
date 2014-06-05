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

import at.kc.tugraz.socialserver.utils.SSMimeTypeU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.service.filerepo.datatypes.pars.SSFileSetReaderOrWriterPar;
import at.kc.tugraz.ss.service.filerepo.datatypes.pars.SSFileUserFileWritesPar;
import at.kc.tugraz.ss.service.filerepo.datatypes.pars.SSFileRemoveReaderOrWriterPar;
import at.kc.tugraz.ss.service.filerepo.datatypes.pars.SSFileWritingMinutesLeftPar;
import at.kc.tugraz.ss.service.filerepo.datatypes.pars.SSFileCanWritePar;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityA;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.service.filerepo.api.*;
import at.kc.tugraz.ss.service.filerepo.conf.*;
import at.kc.tugraz.ss.service.filerepo.datatypes.*;
import at.kc.tugraz.ss.service.filerepo.datatypes.pars.SSFileUriFromIDPar;
import at.kc.tugraz.ss.service.filerepo.datatypes.rets.SSFileCanWriteRet;
import at.kc.tugraz.ss.service.filerepo.datatypes.rets.SSFileGetEditingFilesRet;
import at.kc.tugraz.ss.service.filerepo.datatypes.rets.SSFileWritingMinutesLeftRet;
import at.kc.tugraz.ss.service.filerepo.datatypes.rets.SSFileRemoveReaderOrWriterRet;
import at.kc.tugraz.ss.service.filerepo.datatypes.rets.SSFileSetReaderOrWriterRet;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityDescA;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSEntityDesc;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSEntityHandlerImplI;
import at.kc.tugraz.ss.serv.serv.api.SSServImplMiscA;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.service.filerepo.datatypes.pars.SSFileCreateUriPar;
import at.kc.tugraz.ss.service.filerepo.datatypes.pars.SSFileIDFromURIPar;
import at.kc.tugraz.ss.service.filerepo.datatypes.pars.SSFileExtGetPar;
import at.kc.tugraz.ss.service.filerepo.datatypes.rets.SSFileExtGetRet;
import at.kc.tugraz.ss.service.filerepo.impl.fct.SSFileFct;
import java.util.*;

public class SSFilerepoImpl extends SSServImplMiscA implements SSFileRepoClientI, SSFileRepoServerI, SSEntityHandlerImplI{

//  private final String uriDefaultFile = SSStrU.PREFIX_HTTP + "at.tug.kc.socialServer" + SSStrU.slash + SSEntityEnum.file + SSStrU.slash;
//  
//  @Override
//  public String vocUriDefaultFile() throws Exception{
//    return uriDefaultFile;
//  }
  private final SSFilerepoFct                             fct;
  private final Map<String, SSFileRepoFileAccessProperty> fileAccessProps;

  public SSFilerepoImpl(
    final SSFileRepoConf                           conf, 
    final Map<String, SSFileRepoFileAccessProperty> fileAccessProps) throws Exception{

    super(conf);

    this.fileAccessProps = fileAccessProps;
    
    this.fct = new SSFilerepoFct();
  }
  
  /* SSEntityHandlerImplI */
  @Override
  public List<SSUri> searchWithKeywordWithin(
    final SSUri         userUri,
    final SSUri         entityUri,
    final String        keyword,
    final SSEntityE     entityType) throws Exception{

    return null;
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
  public Boolean shareUserEntity(
    final SSUri          userUri, 
    final List<SSUri>    userUrisToShareWith,
    final SSUri          entityUri, 
    final SSUri          circleUri,
    final SSEntityE      entityType) throws Exception{
    
    try{
      return false;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public Boolean addEntityToCircle(
    final SSUri        userUri, 
    final SSUri        circleUri, 
    final SSUri        entityUri, 
    final SSEntityE entityType) throws Exception{
    
    return false;
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
  public SSEntityDescA getDescForEntity(
    final SSEntityE       entityType,
    final SSUri           userUri,
    final SSUri           entityUri,
    final SSLabel         label,
    final Long            creationTime,
    final List<String>    tags,
    final SSEntityA       overallRating,
    final List<SSUri>     discUris,
    final SSUri           author) throws Exception{

    if(!SSEntityE.equals(entityType, SSEntityE.file)){
      return SSEntityDesc.get(entityUri, label, creationTime, tags, overallRating, discUris, author);
    }

    final String fileExt = SSServCaller.fileExtGet(userUri, entityUri);
    
    return SSFileDesc.get(
      entityUri,
      label,
      creationTime,
      tags,
      overallRating,
      discUris,
      author,
      fileExt,
      SSMimeTypeU.mimeTypeForFileExt(fileExt));
  }

  /* SSFileRepoClientI */
  @Override
  public void fileCanWrite(SSSocketCon sSCon, SSServPar par) throws Exception{

    SSServCaller.checkKey(par);

    sSCon.writeRetFullToClient(fileCanWrite(par));
  }

  @Override
  public void fileSetReaderOrWriter(SSSocketCon sSCon, SSServPar par) throws Exception{

    SSServCaller.checkKey(par);

    sSCon.writeRetFullToClient(fileSetReaderOrWriter(par));
  }

  @Override
  public void fileRemoveReaderOrWriter(SSSocketCon sSCon, SSServPar par) throws Exception{

    SSServCaller.checkKey(par);

    sSCon.writeRetFullToClient(fileRemoveReaderOrWriter(par));
  }

  @Override
  public void fileWritingMinutesLeft(SSSocketCon sSCon, SSServPar par) throws Exception{

    SSServCaller.checkKey(par);

    sSCon.writeRetFullToClient(fileWritingMinutesLeft(par));
  }

  @Override
  public void fileUserFileWrites(SSSocketCon sSCon, SSServPar par) throws Exception{

    SSServCaller.checkKey(par);

    sSCon.writeRetFullToClient(fileUserFileWrites(par));
  }

  @Override
  public void fileDownload(SSSocketCon sSCon, SSServPar par) throws Exception{

    SSServCaller.checkKey(par);

    new Thread(new SSFileDownloader((SSFileRepoConf)conf, sSCon, par)).start();
  }

  @Override
  public void fileReplace(SSSocketCon sSCon, SSServPar par) throws Exception{

    SSServCaller.checkKey(par);

    new Thread(new SSFileReplacer((SSFileRepoConf)conf, sSCon, par, fileAccessProps)).start();
  }

  @Override
  public void fileUpload(SSSocketCon sSCon, SSServPar par) throws Exception{

    SSServCaller.checkKey(par);

    new Thread(new SSFileUploader((SSFileRepoConf)conf, sSCon, par)).start();
  }

  @Override
  public void fileThumbGet(SSSocketCon sSCon, SSServPar par) throws Exception{

    SSServCaller.checkKey(par);

    new Thread(new SSFileThumbGetter((SSFileRepoConf)conf, sSCon, par)).start();
  }
  
  @Override
  public void fileExtGet(SSSocketCon sSCon, SSServPar par) throws Exception{
        
    SSServCaller.checkKey(par);

    sSCon.writeRetFullToClient(new SSFileExtGetRet(fileExtGet(par), par.op));
  }

  /* SSFileRepoServerI */
  @Override
  public SSUri fileCreateUri(SSServPar parA) throws Exception{

    SSFileCreateUriPar par = new SSFileCreateUriPar(parA);

    return fct.createFileUri(par.fileExt);
  }

  @Override
  public String fileExtGet(final SSServPar parA) throws Exception{

    final SSFileExtGetPar par = new SSFileExtGetPar(parA);
    String                result;
    
    try{
      
      result = SSServCaller.fileIDFromURI(par.user, par.file);
      result = SSStrU.subString(result, SSStrU.lastIndexOf(result, SSStrU.dot) + 1, SSStrU.length(result));
      
      return result;
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
    List<SSUri> fileUris = new ArrayList<SSUri>();

    SSFileFct.getEditingFileUris(fileAccessProps, par.user, fileUris);

    result = new SSFileGetEditingFilesRet(par.op, SSUri.toDistinctStringArray(fileUris), null);

    for(String fileUri : result.files){
      result.labels.add(SSLabel.toStr(SSServCaller.entityGet(SSUri.get(fileUri)).label));
    }

    return result;
  }

  @Override
  public SSFileCanWriteRet fileCanWrite(SSServPar parI) throws Exception{

    SSFileCanWritePar par = new SSFileCanWritePar(parI);
    SSFileCanWriteRet result = new SSFileCanWriteRet(SSStrU.toString(par.file), par.op);

    result.canWrite = SSFileFct.canWrite(fileAccessProps, par.user, par.file);

    return result;
  }

  @Override
  public SSFileSetReaderOrWriterRet fileSetReaderOrWriter(SSServPar parI) throws Exception{

    SSFileSetReaderOrWriterPar par = new SSFileSetReaderOrWriterPar(parI);

    SSFileSetReaderOrWriterRet result = new SSFileSetReaderOrWriterRet(SSStrU.toString(par.file), par.op);

    result.worked = SSFileFct.setReaderOrWriter(fileAccessProps, par.user, par.file, par.write);

    return result;
  }

  @Override
  public SSFileRemoveReaderOrWriterRet fileRemoveReaderOrWriter(SSServPar parI) throws Exception{

    SSFileRemoveReaderOrWriterPar par = new SSFileRemoveReaderOrWriterPar(parI);

    SSFileRemoveReaderOrWriterRet result = new SSFileRemoveReaderOrWriterRet(SSStrU.toString(par.file), par.op);

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
  public SSUri fileUriFromID(SSServPar parA) throws Exception{

    SSFileUriFromIDPar par = new SSFileUriFromIDPar(parA);

    if(SSStrU.isEmpty(par.id)){
      return null;
    }

    return SSUri.get(fct.objFile() + par.id);
  }

  @Override
  public String fileIDFromURI(final SSServPar parA) throws Exception{

    final SSFileIDFromURIPar par = new SSFileIDFromURIPar(parA);
    String result;

    try{
      result = SSStrU.removeTrailingSlash(SSStrU.toString(par.file));
      result = result.substring(result.lastIndexOf(SSStrU.slash) + 1);

      return result;
    }catch(Exception error){
      SSServErrReg.regErrThrow(new Exception("given file uri not valid"));
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
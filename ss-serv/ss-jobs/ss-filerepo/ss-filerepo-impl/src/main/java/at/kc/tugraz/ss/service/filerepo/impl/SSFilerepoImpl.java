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

import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.service.filerepo.datatypes.pars.SSFileSetReaderOrWriterPar;
import at.kc.tugraz.ss.service.filerepo.datatypes.pars.SSFileUserFileWritesPar;
import at.kc.tugraz.ss.service.filerepo.datatypes.pars.SSFileRemoveReaderOrWriterPar;
import at.kc.tugraz.ss.service.filerepo.datatypes.pars.SSFileWritingMinutesLeftPar;
import at.kc.tugraz.ss.service.filerepo.datatypes.pars.SSFileCanWritePar;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityEnum;
import at.kc.tugraz.ss.service.filerepo.api.*;
import at.kc.tugraz.ss.service.filerepo.conf.*;
import at.kc.tugraz.ss.service.filerepo.datatypes.*;
import at.kc.tugraz.ss.service.filerepo.datatypes.pars.SSFileUriFromID;
import at.kc.tugraz.ss.service.filerepo.datatypes.rets.SSFileCanWriteRet;
import at.kc.tugraz.ss.service.filerepo.datatypes.rets.SSFileGetEditingFilesRet;
import at.kc.tugraz.ss.service.filerepo.datatypes.rets.SSFileWritingMinutesLeftRet;
import at.kc.tugraz.ss.service.filerepo.datatypes.rets.SSFileRemoveReaderOrWriterRet;
import at.kc.tugraz.ss.service.filerepo.datatypes.rets.SSFileSetReaderOrWriterRet;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityDescA;
import at.kc.tugraz.ss.datatypes.datatypes.SSLabelStr;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSEntityDesc;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserDirectlyAdjoinedEntitiesRemovePar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSEntityHandlerImplI;
import at.kc.tugraz.ss.serv.serv.api.SSServImplMiscA;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.service.filerepo.datatypes.pars.SSFileCreateUriPar;
import at.kc.tugraz.ss.service.filerepo.datatypes.pars.SSFileIDFromURIPar;
import at.kc.tugraz.ss.service.filerepo.datatypes.pars.SSFileExtGetPar;
import at.kc.tugraz.ss.service.filerepo.datatypes.rets.SSFileExtGetRet;
import at.kc.tugraz.ss.service.filerepo.impl.fct.SSFileFct;
import at.kc.tugraz.ss.service.rating.datatypes.SSRatingOverall;
import at.kc.tugraz.ss.service.tag.datatypes.SSTag;
import java.lang.reflect.Method;
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
  public void removeDirectlyAdjoinedEntitiesForUser(
    final SSEntityEnum                                  entityType,
    final SSEntityUserDirectlyAdjoinedEntitiesRemovePar par,
    final Boolean                                       shouldCommit) throws Exception{
  }
  
  @Override
  public SSEntityDescA getDescForEntity(
    final SSEntityEnum    entityType,
    final SSUri           userUri,
    final SSUri           entityUri,
    final SSLabelStr      label,
    final Long            creationTime,
    final List<SSTag>     tags,
    final SSRatingOverall overallRating,
    final List<SSUri>     discUris,
    final SSUri           author) throws Exception{

    if(!SSEntityEnum.equals(entityType, SSEntityEnum.file)){
      return SSEntityDesc.get(entityUri, label, creationTime, tags, overallRating, discUris, author);
    }

    return SSFileDesc.get(
      entityUri,
      label,
      creationTime,
      tags,
      overallRating,
      discUris,
      author);
  }

  /**
   * ****
   * SSServRegisterableImplI *****
   */
  @Override
  public List<SSMethU> publishClientOps() throws Exception{

    List<SSMethU> clientOps = new ArrayList<SSMethU>();

    Method[] methods = SSFileRepoClientI.class.getMethods();

    for(Method method : methods){
      clientOps.add(SSMethU.get(method.getName()));
    }

    return clientOps;
  }

  @Override
  public List<SSMethU> publishServerOps() throws Exception{

    List<SSMethU> serverOps = new ArrayList<SSMethU>();

    Method[] methods = SSFileRepoServerI.class.getMethods();

    for(Method method : methods){
      serverOps.add(SSMethU.get(method.getName()));
    }

    return serverOps;
  }

  @Override
  public void handleClientOp(SSSocketCon sSCon, SSServPar par) throws Exception{
    SSFileRepoClientI.class.getMethod(SSMethU.toStr(par.op), SSSocketCon.class, SSServPar.class).invoke(this, sSCon, par);
  }

  @Override
  public Object handleServerOp(SSServPar par) throws Exception{
    return SSFileRepoServerI.class.getMethod(SSMethU.toStr(par.op), SSServPar.class).invoke(this, par);
  }

  /**
   * ****
   * SSFileRepoClientI *****
   */
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

  /**
   * SSFileRepoServerI 
   */
  @Override
  public SSUri fileCreateUri(SSServPar parA) throws Exception{

    SSFileCreateUriPar par = new SSFileCreateUriPar(parA);

    return fct.createFileUri(par.fileExtension);
  }

  @Override
  public String fileExtGet(final SSServPar parA) throws Exception{

    final SSFileExtGetPar par = new SSFileExtGetPar(parA);
    String                result;
    
    try{
      
      result = SSServCaller.fileIDFromURI(par.user, par.fileUri);
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

    for(String fileUri : result.fileUris){
      result.fileNames.add(SSStrU.toString(SSServCaller.entityLabelGet(SSUri.get(fileUri))));
    }

    return result;
  }

  @Override
  public SSFileCanWriteRet fileCanWrite(SSServPar parI) throws Exception{

    SSFileCanWritePar par = new SSFileCanWritePar(parI);
    SSFileCanWriteRet result = new SSFileCanWriteRet(SSStrU.toString(par.uri), par.op);

    result.canWrite = SSFileFct.canWrite(fileAccessProps, par.user, par.uri);

    return result;
  }

  @Override
  public SSFileSetReaderOrWriterRet fileSetReaderOrWriter(SSServPar parI) throws Exception{

    SSFileSetReaderOrWriterPar par = new SSFileSetReaderOrWriterPar(parI);

    SSFileSetReaderOrWriterRet result = new SSFileSetReaderOrWriterRet(SSStrU.toString(par.uri), par.op);

    result.worked = SSFileFct.setReaderOrWriter(fileAccessProps, par.user, par.uri, par.write);

    return result;
  }

  @Override
  public SSFileRemoveReaderOrWriterRet fileRemoveReaderOrWriter(SSServPar parI) throws Exception{

    SSFileRemoveReaderOrWriterPar par = new SSFileRemoveReaderOrWriterPar(parI);

    SSFileRemoveReaderOrWriterRet result = new SSFileRemoveReaderOrWriterRet(SSStrU.toString(par.uri), par.op);

    result.wasSuccessful = SSFileFct.removeReaderOrWriter(fileAccessProps, par.user, par.uri, par.write);

    return result;
  }

  @Override
  public SSFileWritingMinutesLeftRet fileWritingMinutesLeft(SSServPar parI) throws Exception{

    SSFileWritingMinutesLeftPar par = new SSFileWritingMinutesLeftPar(parI);

    SSFileWritingMinutesLeftRet result = new SSFileWritingMinutesLeftRet(par.uri, par.op);

    result.writingMinutesLeft = SSFileFct.getWritingMinutesLeft(fileAccessProps, par.user, par.uri);

    return result;
  }

  @Override
  public SSUri fileUriFromID(SSServPar parA) throws Exception{

    SSFileUriFromID par = new SSFileUriFromID(parA);

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
      result = SSStrU.removeTrailingSlash(SSStrU.toString(par.fileUri));
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
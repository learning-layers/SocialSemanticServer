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
 package at.kc.tugraz.ss.service.filerepo.api;

import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.service.filerepo.datatypes.rets.SSFileCanWriteRet;
import at.kc.tugraz.ss.service.filerepo.datatypes.rets.SSFileGetEditingFilesRet;
import at.kc.tugraz.ss.service.filerepo.datatypes.rets.SSFileWritingMinutesLeftRet;
import at.kc.tugraz.ss.service.filerepo.datatypes.rets.SSFileRemoveReaderOrWriterRet;
import at.kc.tugraz.ss.service.filerepo.datatypes.rets.SSFileSetReaderOrWriterRet;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;

public interface SSFileRepoServerI{

  public SSUri                                     fileCreateUri            (final SSServPar parA) throws Exception;
  public String                                    fileExtGet               (final SSServPar parA) throws Exception;
  public void                                      fileUpdateWritingMinutes (final SSServPar parA) throws Exception;
  public SSFileCanWriteRet                         fileCanWrite             (final SSServPar parA) throws Exception;
  public SSFileSetReaderOrWriterRet                fileSetReaderOrWriter    (final SSServPar parA) throws Exception;
  public SSFileRemoveReaderOrWriterRet             fileRemoveReaderOrWriter (final SSServPar parA) throws Exception;
  public SSFileWritingMinutesLeftRet               fileWritingMinutesLeft   (final SSServPar parA) throws Exception;
  public SSUri                                     fileUriFromID            (final SSServPar parA) throws Exception;
  public String                                    fileIDFromURI            (final SSServPar parA) throws Exception;
  public SSFileGetEditingFilesRet                  fileUserFileWrites       (final SSServPar parA) throws Exception;
}


//  public boolean uploadFile() throws Exception;
  
//  public void doUploadFile(
//    String      fileName,
//    int         fileLength,
//    String      fileExtension,
//    InputStream fileUploadInputStream) throws Exception;
//  
//  public byte[] downloadFile(
//    SSUri file) throws Exception;

//  public boolean isFileSharedForUser(
//    SSUri        fileUri,
//    List<SSColl> userCollections) throws Exception;
//  
//  public boolean isFilePrivateForUser(
//    SSUri        fileUri,
//    List<SSColl> userCollections) throws Exception;

//  public SSFileDownloadRetObj downloadFileFromWeb(
//    SSUri   fileUri) throws Exception;
//  
//  public boolean doDownloadFileFromWeb(
//    SSFileDownloadRetObj       downloadFileFromWebObject,
//    DataOutputStream           dataOutputStream) throws Exception;

//  public SSFileUploadRetObj uploadFileFromWeb(
//    String  fileExtension) throws Exception;
//  
//  public boolean doUploadFileFromWeb(
//    SSFileUploadRetObj       uploadFileFromWebObject,
//    DataInputStream          dataInputStream) throws Exception;
  
  //  public void fileUpload(
//    Socket       clientSocket, 
//    List<String> jsonPars) throws Exception;
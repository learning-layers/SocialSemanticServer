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

import at.kc.tugraz.socialserver.utils.SSFileExtU;
import at.kc.tugraz.socialserver.utils.SSFileU;
import at.kc.tugraz.socialserver.utils.SSIDU;
import at.kc.tugraz.socialserver.utils.SSLogU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.conf.conf.SSCoreConf;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import java.util.List;

public class SSFilerepoFct{
  
  public SSFilerepoFct(){}
  
  //TODO dtheiler: remove replication of this method in SSEvernoteImpl
  public static String getThumbBase64(
    final SSUri  user,
    final SSUri  file) throws Exception{
    
    try{
      
      final List<SSUri> thumbUris = SSServCaller.entityThumbsGet(user, file);
      
      if(thumbUris.isEmpty()){
        SSLogU.warn("thumb couldnt be retrieved from file " + file);
        return null;
      }
      final String pngFilePath = SSCoreConf.instGet().getSsConf().getLocalWorkPath() + SSServCaller.fileIDFromURI (user, thumbUris.get(0));
      
      return SSFileU.readPNGToBase64Str(pngFilePath);
        
//    try{
//      final String localWorkPath    = SSCoreConf.instGet().getSsConf().getLocalWorkPath();
//      final String fileId           = SSServCaller.fileIDFromURI      (user, file);
//      final String filePath         = localWorkPath + fileId;
//      final SSUri  pngUri           = SSServCaller.fileCreateUri      (user, SSFileExtU.png);
//      final String pngId            = SSServCaller.fileIDFromURI      (user, pngUri);
//      final SSUri  pdfUri           = SSServCaller.fileCreateUri      (user, SSFileExtU.pdf);
//      final String pdfId            = SSServCaller.fileIDFromURI      (user, pdfUri);
//      final String pngFilePath      = localWorkPath + pngId;
//      final String pdfFilePath      = localWorkPath + pdfId;
//
//      if(SSFileExtU.imageFileExts.contains(fileExt)){
//
//        SSFileU.scalePNGAndWrite(ImageIO.read(new File(filePath)), new File(pngFilePath));
//
//        return SSFileU.readPNGToBase64Str(pngFilePath);
//      }
//
//      if(SSStrU.equals(SSFileExtU.pdf, fileExt)){
//
//        SSFileU.writePNGFromPDF(filePath, pngFilePath);
//
//        return SSFileU.readPNGToBase64Str(pngFilePath);
//      }
//
//      if(SSStrU.equals(SSFileExtU.doc, fileExt)){
//
//        SSFileU.writePDFFromDoc (filePath,    pdfFilePath);
//        SSFileU.writePNGFromPDF (pdfFilePath, pngFilePath);
//
//        return SSFileU.readPNGToBase64Str(pngFilePath);
//      }
//

    }catch(Exception error){
      SSLogU.warn("base 64 file thumb couldnt be retrieved");
      return null;
    }
  }
  
  public SSUri createFileUri(String fileExtension) throws Exception{
    return SSUri.get(SSIDU.uniqueID(objFile().toString(), SSStrU.dot + fileExtension));
  }
  
  public SSUri objFile() throws Exception{
    return SSUri.get(SSServCaller.vocURIPrefixGet(), SSEntityE.file.toString());
  }
}

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
 package at.kc.tugraz.ss.serv.job.file.sys.local.impl;

import at.kc.tugraz.socialserver.utils.SSFileExtU;
import at.kc.tugraz.socialserver.utils.SSFileU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.serv.serv.api.SSServConfA;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.job.file.sys.local.api.SSFileSysLocalServerI;
import at.kc.tugraz.ss.serv.job.file.sys.local.api.SSFileSysLocalClientI;
import at.kc.tugraz.ss.serv.job.file.sys.local.conf.SSFileSysLocalConf;
import at.kc.tugraz.ss.serv.job.file.sys.local.datatypes.pars.SSFileSysLocalAddTextToFilesNamesAtBeginInDirPar;
import at.kc.tugraz.ss.serv.job.file.sys.local.datatypes.pars.SSFileSysLocalFormatFileNamesInDirPar;
import at.kc.tugraz.ss.serv.serv.api.SSServImplMiscA;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SSFileSysLocalImpl extends SSServImplMiscA implements SSFileSysLocalClientI, SSFileSysLocalServerI{
  
  public SSFileSysLocalImpl(final SSServConfA conf) throws Exception{
    super(conf);
  }
  
  /****** SSFileSysLocalClientI ******/
  
//  @Override
//  public void fileSysLocalRenameFilesInDir(SSSocketCon sSCon, SSServPar par) throws Exception {
//    
//    checkKey(par);
//    
//    sSCon.writeRetFullToClient(FileSysLocalRenameFilesInDirRet.get(labelSet(sSCon.sSRequ)));
//  }
  
  /****** SSFileSysLocalServerI ******/
  
  @Override
  public void fileSysLocalFormatAudioAndVideoFileNamesInDir(SSServPar parI) throws Exception {
    
    SSFileSysLocalFormatFileNamesInDirPar par = new SSFileSysLocalFormatFileNamesInDirPar(parI);
    
    formatAudioAndVideoFileNamesInDir(SSFileU.filesForDirPath(((SSFileSysLocalConf)conf).dirPath));
  }
  
  @Override
  public void fileSysLocalAddTextToFilesNamesAtBeginInDir(SSServPar parI) throws Exception {
    
    SSFileSysLocalAddTextToFilesNamesAtBeginInDirPar par = new SSFileSysLocalAddTextToFilesNamesAtBeginInDirPar(parI);
    
    addTextToFilesNamesAtBeginInDir(SSFileU.filesForDirPath(((SSFileSysLocalConf)conf).dirPath), ((SSFileSysLocalConf)conf).text);
  }
  
  private void addTextToFilesNamesAtBeginInDir(File[] files, String textToAddAtBegin) throws IOException{
    
    Path   pathToFile;
    String fileName;
    
    for(File file : files){
      
      if(file.isDirectory()){
        return;
      }else{
        
        pathToFile  = file.toPath();
        fileName    = pathToFile.getFileName().toString();
        fileName    = textToAddAtBegin + fileName;
        
        try{
          Files.move(pathToFile, pathToFile.resolveSibling(fileName));
        }catch(FileAlreadyExistsException error){
          System.out.println("file " + pathToFile.resolveSibling(fileName) + " already exists!");
        }
      }
    }
  }
  
  private void formatAudioAndVideoFileNamesInDir(File[] files) throws Exception {
    
    for(File file : files){
      
      if(file.isDirectory()){
        formatAudioAndVideoFileNamesInDir(file.listFiles());
      }else{
        formatAudioAndVideoFileName(file);
      }
    }
  }
  
  private void formatAudioAndVideoFileName(File file) throws IOException{
    
    Path   pathToFile  = file.toPath();
    String fileName    = pathToFile.getFileName().toString().toLowerCase();
    String fileExt     = SSFileExtU.ext(fileName);
    
    if(SSFileExtU.isNotAudioOrVideoFileExt(fileExt)){
      return;
    }
    
    fileName = SSStrU.replaceBlanksSpecialCharactersDoubleDots(fileName, SSStrU.underline);

    try{
      Files.move(pathToFile, pathToFile.resolveSibling(fileName));
    }catch(FileAlreadyExistsException error){
      System.out.println("file " + pathToFile.resolveSibling(fileName) + " already exists!");
    }
  }
}
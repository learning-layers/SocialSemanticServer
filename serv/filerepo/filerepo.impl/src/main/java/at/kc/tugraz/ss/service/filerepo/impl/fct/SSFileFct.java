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
package at.kc.tugraz.ss.service.filerepo.impl.fct;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.service.filerepo.datatypes.SSFileRepoFileAccessProperty;
import java.util.List;
import java.util.Map;

public class SSFileFct {
  
  public static Boolean canWrite(
    final Map<String, SSFileRepoFileAccessProperty> fileAccessProps,
    final SSUri user,
    final SSUri file) throws Exception{
    
    //TODO dtheiler: change synchronized blocks in this class to 
    //http://stackoverflow.com/questions/8229942/java-class-to-allow-multiple-threads-to-read-or-one-to-modify-at-the-same-time
    synchronized(fileAccessProps){
      
      if(fileAccessProps.containsKey(file.toString()) == false){
        return true;
      }
      
      return fileAccessProps.get(file.toString()).canWrite(user);
    }
  }
  
  public static Boolean setReaderOrWriter(
    final Map<String, SSFileRepoFileAccessProperty> fileAccessProps,
    final SSUri user,
    final SSUri file,
    final Boolean write) throws Exception{
    
    synchronized(fileAccessProps){
      
      if(fileAccessProps.containsKey(file.toString()) == false){
        
        fileAccessProps.put(file.toString(), new SSFileRepoFileAccessProperty(file));
      }
      
      if(write){
        return fileAccessProps.get(file.toString()).addWriter(user);
      }else{
        return fileAccessProps.get(file.toString()).addReader(user);
      }
    }
  }
  
  public static Boolean removeReaderOrWriter(
    final Map<String, SSFileRepoFileAccessProperty> fileAccessProps,
    final SSUri user,
    final SSUri file,
    final Boolean write) throws Exception{
    
    synchronized(fileAccessProps){
      
      if(fileAccessProps.containsKey(file.toString()) == false){
        return true;
      }
      
      if(write == true){
        return fileAccessProps.get(file.toString()).removeWriter(user);
      }else{
        return fileAccessProps.get(file.toString()).removeReader(user);
      }
    }
  }
  
  public static Integer getWritingMinutesLeft(
    final Map<String, SSFileRepoFileAccessProperty> fileAccessProps,
    final SSUri user,
    final SSUri file) throws Exception{
    
    synchronized(fileAccessProps){
      
      if(fileAccessProps.containsKey(file.toString()) == false){
        
        return -1;
      }
      
      return fileAccessProps.get(file.toString()).getWritingMinutesLeft(user);
    }
  }
  
  public static void getEditingFileUris(
    final Map<String, SSFileRepoFileAccessProperty> fileAccessProps,
    final SSUri user,
    final List<SSUri> files){
    
    synchronized(fileAccessProps){
      
      for(SSFileRepoFileAccessProperty fileAccessProperty : fileAccessProps.values()){
        
        if(fileAccessProperty.file == null){
          continue;
        }
        
        if(SSStrU.equals(fileAccessProperty.getWriter(), user)){
          files.add(fileAccessProperty.file);
        }
      }
    }
  }
}
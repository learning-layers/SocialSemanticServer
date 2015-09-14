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
package at.kc.tugraz.ss.service.filerepo.datatypes;

import at.tugraz.sss.serv.SSFileExtE;
import at.tugraz.sss.serv.SSMimeTypeE;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSUri;

public class SSFile extends SSEntity{
  
  public SSFileExtE  fileExt      = null;
  public SSMimeTypeE mimeType     = null;
  public SSUri       downloadLink = null;
  
  public String getMimeType(){
    return SSStrU.toStr(mimeType);
  }
  
  public String getDownloadLink(){
    return SSStrU.removeTrailingSlash(downloadLink);
  }
  
  @Override
  public Object jsonLDDesc(){
    throw new UnsupportedOperationException();
  }
  
  public static SSFile get(
    final SSFile           file,
    final SSEntity         entity) throws Exception{
    
    return new SSFile(file, entity);
  }
  
  protected SSFile(
    final SSFile           file,
    final SSEntity         entity) throws Exception{
    
    super(file, entity);
    
    if(file.fileExt != null){
      this.fileExt = file.fileExt;
    }else{
      
      if(entity instanceof SSFile){
        this.fileExt = ((SSFile) entity).fileExt;
      }
    }
    
    if(file.mimeType != null){
      this.mimeType = file.mimeType;
    }else{
      
      if(entity instanceof SSFile){
        this.mimeType = ((SSFile) entity).mimeType;
      }
    }
    
    if(file.downloadLink != null){
      this.downloadLink = file.downloadLink;
    }else{
      
      if(entity instanceof SSFile){
        this.downloadLink = ((SSFile) entity).downloadLink;
      }
    }
  }
  
  public static SSFile get(
    final SSUri        id, 
    final SSEntityE    type,
    final SSFileExtE   fileExt,
    final SSMimeTypeE  mimeType, 
    final SSUri        downloadLink) throws Exception{
    
    return new SSFile(id, type, fileExt, mimeType, downloadLink);
  }
  
  public SSFile(
    final SSUri        id,
    final SSEntityE    type,
    final SSFileExtE   fileExt,
    final SSMimeTypeE  mimeType, 
    final SSUri        downloadLink) throws Exception{
    
    super(id, type);
    
    this.fileExt      = fileExt;
    this.mimeType     = mimeType;
    this.downloadLink = downloadLink;
  }
}
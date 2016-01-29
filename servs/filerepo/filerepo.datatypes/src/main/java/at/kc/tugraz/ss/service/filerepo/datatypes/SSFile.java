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

import at.tugraz.sss.serv.util.SSFileExtE;
import at.tugraz.sss.serv.util.SSMimeTypeE;
import at.tugraz.sss.serv.datatype.SSEntity;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.*;
import io.swagger.annotations.*;

@ApiModel
public class SSFile extends SSEntity{
  
  @ApiModelProperty
  public SSFileExtE  fileExt      = null;
  
  public void setFileExt(final String fileExt) throws SSErr{
    this.fileExt = SSFileExtE.get(fileExt);
  }
  
  public String getFileExt(){
    return SSStrU.toStr(fileExt);
  }

  @ApiModelProperty
  public SSMimeTypeE mimeType     = null;
  
  public void setMimeType(final String mimeType) throws SSErr{
    this.mimeType = SSMimeTypeE.get(mimeType);
  }
  
  public String getMimeType(){
    return SSStrU.toStr(mimeType);
  }
  
  @ApiModelProperty
  public SSUri       downloadLink = null;
  
   public void setDownloadLink(final String downloadLink) throws SSErr{
    this.downloadLink = SSUri.get(downloadLink);
  }
   
  public String getDownloadLink(){
    return SSStrU.removeTrailingSlash(downloadLink);
  }
  
   public static SSFile get(
    final SSUri        id, 
    final SSEntityE    type,
    final SSFileExtE   fileExt,
    final SSMimeTypeE  mimeType, 
    final SSUri        downloadLink) throws SSErr{
    
    return new SSFile(id, type, fileExt, mimeType, downloadLink);
  }
   
  public static SSFile get(
    final SSFile           file,
    final SSEntity         entity) throws SSErr{
    
    return new SSFile(file, entity);
  }
  
  public SSFile(){/* Do nothing because of only JSON Jackson needs this */ }
  
  protected SSFile(
    final SSFile           file,
    final SSEntity         entity) throws SSErr{
    
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
  
  private SSFile(
    final SSUri        id,
    final SSEntityE    type,
    final SSFileExtE   fileExt,
    final SSMimeTypeE  mimeType, 
    final SSUri        downloadLink) throws SSErr{
    
    super(id, type);
    
    this.fileExt      = fileExt;
    this.mimeType     = mimeType;
    this.downloadLink = downloadLink;
  }
}
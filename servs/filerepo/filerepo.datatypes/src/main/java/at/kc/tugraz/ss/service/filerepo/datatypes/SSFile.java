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
import at.tugraz.sss.serv.SSUri;
import com.wordnik.swagger.annotations.ApiModelProperty;
import java.util.Map;

public class SSFile extends SSEntity{
  
  @ApiModelProperty(
    required = false,
    value = "file extension")
  public SSFileExtE fileExt  = null;
  
  @ApiModelProperty(
    required = false,
    value = "mime type")
  public SSMimeTypeE mimeType = null;
  
  public static SSFile get(
    final SSFile           file,
    final SSEntity         entity) throws Exception{
    
    return new SSFile(file, entity);
  }
  
  protected SSFile(
    final SSFile           file,
    final SSEntity         entity) throws Exception{
    
    super(file, entity);
    
    this.fileExt  = file.fileExt;
    this.mimeType = file.mimeType;
  }
  
  public static SSFile get(
    final SSUri        id, 
    final SSFileExtE   fileExt,
    final SSMimeTypeE  mimeType) throws Exception{
    
    return new SSFile(id, fileExt, mimeType);
  }
  
  public SSFile(
    final SSUri        id,
    final SSFileExtE   fileExt,
    final SSMimeTypeE  mimeType) throws Exception{
    
    super(id, SSEntityE.file);
    
    this.fileExt  = fileExt;
    this.mimeType = mimeType;
  }
  
  
  @Override
  public Object jsonLDDesc(){
    
    final Map<String, Object> ld = (Map<String, Object>) super.jsonLDDesc();
    
//    ld.put(SSVarU.mimeType,      SSVarU.xsd + SSStrU.colon + SSStrU.valueString);
//    ld.put(SSVarU.fileExt,       SSVarU.sss + SSStrU.colon + SSFileExtE.class.getName());
    
    return ld;
  }
}
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
package at.tugraz.sss.servs.file.datatype;

import at.tugraz.sss.servs.util.SSVarNames;
import at.tugraz.sss.servs.util.SSStrU;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.entity.datatype.SSLabel;
import at.tugraz.sss.servs.entity.datatype.SSServRetI; 
import javax.ws.rs.core.*;

public class SSFileDownloadRet extends SSServRetI{
  
  public  SSUri           file         = null;
  public  SSLabel         label        = null;
  public  StreamingOutput outputStream = null;

  public String getFile(){
    return SSStrU.removeTrailingSlash(file);
  }
   
  public String getLabel(){
    return SSStrU.toStr(label);
  }
  
  public SSFileDownloadRet(
    final SSUri           file,
    final SSLabel         label,
    final StreamingOutput outputStream){
    
    super(SSVarNames.fileDownload);
    
    this.file         = file;
    this.label        = label;
    this.outputStream = outputStream;
  }
}
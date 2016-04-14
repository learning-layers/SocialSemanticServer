 /**
  * Code contributed to the Learning Layers project
  * http://www.learning-layers.eu
  * Development is partly funded by the FP7 Programme of the European Commission under
  * Grant Agreement FP7-ICT-318209.
  * Copyright (c) 2015, Graz University of Technology - KTI (Knowledge Technologies Institute).
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

/**
 * @author Dieter Theiler
 */

package at.tugraz.sss.servs.file.datatype;

import at.tugraz.sss.servs.util.SSVarNames;
import at.tugraz.sss.servs.util.SSStrU;
import at.tugraz.sss.servs.entity.datatype.SSClientE;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.entity.datatype.SSTextComment;
import at.tugraz.sss.servs.entity.datatype.SSLabel;
import at.tugraz.sss.servs.util.SSMimeTypeE;
import at.tugraz.sss.servs.entity.datatype.SSServPar;
import java.io.*;
import java.net.Socket;

public class SSFileUploadPar extends SSServPar{
  
  public SSLabel       label       = null;
  public SSTextComment description = null;
  public SSMimeTypeE   mimeType    = null;
  public SSUri         circle      = null;
  
  public InputStream  fileInputStream = null;
  public SSClientE    clientType      = SSClientE.socket;
  
  public String getLabel(){
    return SSStrU.toStr(label);
  }
  
  public void setLabel(final String label) throws SSErr{
    this.label = SSLabel.get(label);
  }
  
  public String getDescription(){
    return SSStrU.toStr(description);
  }
  
  public void setDescription(final String description) throws SSErr{
    this.description = SSTextComment.get(description);
  }
  
  public String getMimeType(){
    return SSStrU.toStr(mimeType);
  }
  
  public void setMimeType(final String mimeType) throws SSErr{
    this.mimeType = SSMimeTypeE.get(mimeType);
  }
  
  public String getCircle(){
    return SSStrU.removeTrailingSlash(circle);
  }
  
  public void setCircle(final String circle) throws SSErr{
    this.circle = SSUri.get(circle);
  }
  
  public SSFileUploadPar(){/* Do nothing because of only JSON Jackson needs this */ }
  
  public SSFileUploadPar(
    final SSServPar     servPar,
    final SSUri         user,
    final SSMimeTypeE   mimeType,
    final SSLabel       label,
    final SSTextComment description,
    final SSUri         circle,
    final Socket        clientSocket,
    final InputStream   fileInputStream,
    final SSClientE     clientType,
    final boolean       shouldCommit){
    
    super (SSVarNames.fileUpload, null, user, servPar.sqlCon);
    
    this.mimeType        = mimeType;
    this.label           = label;
    this.description     = description;
    this.circle          = circle;
    this.clientSocket    = clientSocket;
    this.fileInputStream = fileInputStream;
    this.clientType      = clientType;
    this.shouldCommit    = shouldCommit;
  }
}
/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2015, Graz University of Technology - KTI (Knowledge Technologies Institute).
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
package at.tugraz.sss.servs.file.datatype.par;

import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.util.SSFileExtE;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.par.SSServPar;
import at.tugraz.sss.serv.util.*;
import java.sql.*;

public class SSEntityFileAddPar extends SSServPar{
  
  public byte[]     fileBytes                    = null;
  public Integer    fileLength                   = null;
  public SSFileExtE fileExt                      = null;
  public SSUri      file                         = null;
  public SSEntityE  type                         = null;
  public SSLabel    label                        = null;
  public SSUri      entity                       = null;
  public boolean    createThumb                  = false;
  public SSUri      entityToAddThumbTo           = null;
  public boolean    removeExistingFilesForEntity = false;

  public String getFileExt() {
    return SSStrU.toStr(fileExt);
  }

  public void setFileExt(final String fileExt) throws SSErr {
    this.fileExt = SSFileExtE.get(fileExt);
  }
  
  public String getFile(){
    return SSStrU.removeTrailingSlash(file);
  }

  public void setFile(final String file) throws SSErr{
    this.file = SSUri.get(file);
  }
  
  public String getType(){
    return SSStrU.toStr(type);
  }

  public void setType(final String type) throws SSErr{
    this.type = SSEntityE.get(type);
  }
  
  public String getLabel(){
    return SSStrU.toStr(label);
  }

  public void setLabel(final String label) throws SSErr{
    this.label = SSLabel.get(label);
  }

  public String getEntity(){
    return SSStrU.removeTrailingSlash(entity);
  }

  public void setEntity(final String entity) throws SSErr{
   this.entity = SSUri.get(entity);
  }
  
  public String getEntityToAddThumbTo(){
    return SSStrU.removeTrailingSlash(entityToAddThumbTo);
  }

  public void setEntityToAddThumbTo(final String entityToAddThumbTo) throws SSErr{
   this.entity = SSUri.get(entityToAddThumbTo);
  }
  
  public SSEntityFileAddPar(){} 
  
  public SSEntityFileAddPar(
    final SSServPar servPar,
    final SSUri      user,
    final byte[]     fileBytes, 
    final Integer    fileLength,
    final SSFileExtE fileExt,
    final SSUri      file,
    final SSEntityE  type,
    final SSLabel    label,
    final SSUri      entity,
    final boolean    createThumb,
    final SSUri      entityToAddThumbTo, 
    final boolean    removeExistingFilesForEntity,
    final boolean    withUserRestriction, 
    final boolean    shouldCommit) {
    
    super(SSVarNames.fileAdd, null, user, servPar.sqlCon);
    
    this.fileBytes                    = fileBytes;
    this.fileLength                   = fileLength;
    this.fileExt                      = fileExt;
    this.file                         = file;
    this.type                         = type;
    this.label                        = label;
    this.entity                       = entity;
    this.createThumb                  = createThumb;
    this.entityToAddThumbTo           = entityToAddThumbTo;
    this.removeExistingFilesForEntity = removeExistingFilesForEntity;
    this.withUserRestriction          = withUserRestriction;
    this.shouldCommit                 = shouldCommit;
  }
}
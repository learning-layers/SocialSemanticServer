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
package at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par;

import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.SSEntity;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.enums.*;
import io.swagger.annotations.*;

@ApiModel
public class SSEvernoteResource extends SSEntity{

  @ApiModelProperty
  public SSUri  note     = null;
  
  public void setNote(final String note) throws SSErr{
    this.note = SSUri.get(note);
  }
  
  public String getNote(){
    return SSStrU.removeTrailingSlash(note);
  }

  public static SSEvernoteResource get(
    final SSEvernoteResource      resource,
    final SSEntity                entity) throws SSErr{
    
    return new SSEvernoteResource(resource, entity);
  }
  
  public static SSEvernoteResource get(
    final SSUri           id,
    final SSUri           note) throws SSErr{
    
    return new SSEvernoteResource(id, note);
  }
  
  public SSEvernoteResource(){/* Do nothing because of only JSON Jackson needs this */ }
  
  protected SSEvernoteResource(
    final SSEvernoteResource      resource,
    final SSEntity                entity) throws SSErr{

    super(resource, entity);
    
    if(resource.note != null){
      this.note     = resource.note;
    }else{
      
      if(entity instanceof SSEvernoteResource){
        this.note = ((SSEvernoteResource)entity).note;
      }
    }
  }
  
  protected SSEvernoteResource(
    final SSUri       id,
    final SSUri       note) throws SSErr{

    super(id, SSEntityE.evernoteResource);
    
    this.id       = id;
    this.note     = note;
  }
}

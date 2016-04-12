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
package at.tugraz.sss.servs.disc.datatype;

import at.tugraz.sss.servs.util.SSStrU;
import at.tugraz.sss.servs.entity.datatype.SSEntityE;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.entity.datatype.SSTextComment;
import at.tugraz.sss.servs.entity.datatype.SSEntity;
import io.swagger.annotations.*;

@ApiModel
public class SSDiscEntry extends SSEntity{

  @ApiModelProperty
  public  int             pos = -1;
  
  @ApiModelProperty
  public  SSTextComment       content = null;
  
  public void setContent(final String content) throws SSErr{
    this.content = SSTextComment.get(content);
  }
  
  public String getContent(){
    return SSStrU.toStr(content);
  }
  
  @ApiModelProperty
  public  boolean             accepted = false;
  
  public static SSDiscEntry get(
    final SSUri                  id,
    final SSEntityE              type,
    final int                    pos,
    final SSTextComment          content,
    final boolean                accepted) throws SSErr{
    
    return new SSDiscEntry(
      id,
      type,
      pos,
      content,
      accepted);
  }
  
  public static SSDiscEntry get(
    final SSDiscEntry            entry,
    final SSEntity               entity) throws SSErr{
    
    return new SSDiscEntry(
      entry, 
      entity);
  }
  
  protected SSDiscEntry(
    final SSDiscEntry            discEntry,
    final SSEntity               entity) throws SSErr{
    
    super(discEntry, entity);
    
    if(discEntry.pos != -1){
      this.pos = discEntry.pos;
    }else{
     
      if(entity instanceof SSDiscEntry){
        this.pos = ((SSDiscEntry) discEntry).pos;
      }
    }
    
    if(discEntry.content != null){
      this.content = discEntry.content;
    }else{
      
      if(entity instanceof SSDiscEntry){
        this.content = ((SSDiscEntry) discEntry).content;
      }
    }
    
    if(discEntry.accepted){
      this.accepted = discEntry.accepted;
    }else{
      
      if(entity instanceof SSDiscEntry){
        this.accepted = ((SSDiscEntry) discEntry).accepted;
      }
    }
  }
  
  protected SSDiscEntry(
    final SSUri                  id,
    final SSEntityE              type,
    final int                    pos,
    final SSTextComment          content, 
    final boolean                accepted) throws SSErr{
    
    super(id, type);
    
    this.pos      = pos;
    this.content  = content;
    this.accepted = accepted;
  }
}
/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2016, Graz University of Technology - KTI (Knowledge Technologies Institute).
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
package at.kc.tugraz.sss.comment.datatypes;

import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.serv.util.*;
import io.swagger.annotations.*;
import java.util.*;

@ApiModel
public class SSComment extends SSEntity{
  
  public SSTextComment comment = null;
  
  public void setComment(final String comment) throws SSErr{
    this.comment = SSTextComment.get(comment);
  }
  
  public String getComment(){
    return SSStrU.toStr(comment);
  }
  
  public SSComment(){/* Do nothing because of only JSON Jackson needs this */ }
  
  public static SSComment get(
    final SSComment     comment, 
    final SSEntity      commentEntity) throws SSErr{
    
    return new SSComment(comment, commentEntity);
  }
  
  public static SSComment get(
    final SSUri         id, 
    final SSTextComment comment) throws SSErr{
    
    return new SSComment(id, comment);
  }
  
  protected SSComment(
    final SSComment   comment, 
    final SSEntity    entity) throws SSErr{
    
    super(comment, entity);
    
    if(comment.comment != null){
      this.comment = comment.comment;
    }else{
      
      if(entity instanceof SSComment){
        this.comment = ((SSComment) entity).comment;
      }
    }
  }
  
  protected SSComment(
    final SSUri            id, 
    final SSTextComment    comment) throws SSErr{
    
    super(id, SSEntityE.comment);
    
    this.comment = comment;
  }
  
  public static List<SSTextComment> getTextsFromComments(final List<SSComment> comments) throws SSErr{    
    
    try{
      
      final List<SSTextComment> texts = new ArrayList<>();
      
      if(comments == null){
        return texts;
      }
      
      for(SSComment comment : comments){
        texts.add(comment.comment);
      }
      
      return texts;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}
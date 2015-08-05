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
package at.kc.tugraz.sss.comment.datatypes.par;

import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSTextComment;
import at.tugraz.sss.serv.SSUri;
import java.util.ArrayList;
import java.util.List;

public class SSCommentsAddPar extends SSServPar{
  
  public SSUri               entity   = null;
  public List<SSTextComment> comments = new ArrayList<>();
  
  public String getEntity(){
    return SSStrU.removeTrailingSlash(entity);
  }
  
  public void setEntity(final String entity) throws Exception{
    this.entity = SSUri.get(entity);
  }
  
  public List<String> getComments(){
    return SSStrU.toStr(comments);
  }
  
  public void setComments(final List<String> comments) throws Exception{
    this.comments = SSTextComment.get(comments);
  }
  
  public SSCommentsAddPar(){}
  
  public SSCommentsAddPar(
    final SSServOpE            op,
    final String               key,
    final SSUri                user,
    final SSUri                entity,
    final List<SSTextComment>  comments,
    final Boolean              withUserRestriction,
    final Boolean              shouldCommit){
    
    super(op, key, user);
    
    this.entity = entity;
    
    if(comments != null){
      this.comments.addAll(comments);
    }
    
    this.withUserRestriction = withUserRestriction;
    this.shouldCommit        = shouldCommit;
  }
}
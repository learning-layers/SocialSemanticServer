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
package at.tugraz.sss.servs.entity.datatypes.par;

import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSTextComment;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSServOpE;
import java.util.ArrayList;
import java.util.List;

public class SSEntitySharePar extends SSServPar{
  
  public SSUri         entity          = null;
  public List<SSUri>   users           = new ArrayList<>();
  public List<SSUri>   circles         = new ArrayList<>();
  public Boolean       setPublic       = false;
  public SSTextComment comment         = null;
  
  public void setEntity(final String entity) throws Exception{
    this.entity = SSUri.get(entity);
  }
    
  public void setUsers(final List<String> users) throws Exception{
    this.users = SSUri.get(users);
  }
    
  public void setCircles(final List<String> circles) throws Exception{
    this.circles = SSUri.get(circles);
  }

  public void getComment(final String comment) throws Exception{
    this.comment = SSTextComment.get(comment);
  }
  
  public String getEntity(){
    return SSStrU.removeTrailingSlash(entity);
  }
  
  public List<String> getUsers() throws Exception{
    return SSStrU.removeTrailingSlash(users);
  }
  
  public List<String> getCircles() throws Exception{
    return SSStrU.removeTrailingSlash(circles);
  }
  
  public String getComment(){
    return SSStrU.toStr(comment);
  }

  public SSEntitySharePar(){}
    
  public SSEntitySharePar(
    final SSUri         user,
    final SSUri         entity,
    final List<SSUri>   users,
    final List<SSUri>   circles,
    final Boolean       setPublic,
    final SSTextComment comment, 
    final Boolean       withUserRestriction, 
    final Boolean       shouldCommit){
    
    super(SSServOpE.entityShare, null, user);
    
    this.entity       = entity;
    
    SSUri.addDistinctWithoutNull(this.users,   users);
    SSUri.addDistinctWithoutNull(this.circles, circles);
    
    this.setPublic           = setPublic;
    this.comment             = comment;
    this.withUserRestriction = withUserRestriction;
    this.shouldCommit        = shouldCommit;
  }
}
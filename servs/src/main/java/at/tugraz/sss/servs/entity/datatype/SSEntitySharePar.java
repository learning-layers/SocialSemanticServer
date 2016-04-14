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

package at.tugraz.sss.servs.entity.datatype;

import at.tugraz.sss.servs.util.SSVarNames;
import at.tugraz.sss.servs.util.SSStrU;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSTextComment;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import java.sql.*;

import java.util.ArrayList;
import java.util.List;

public class SSEntitySharePar extends SSServPar{
  
  public SSUri         entity          = null;
  public List<SSUri>   users           = new ArrayList<>();
  public List<SSUri>   circles         = new ArrayList<>();
  public boolean       setPublic       = false;
  public SSTextComment comment         = null;
  
  public void setEntity(final String entity) throws SSErr{
    this.entity = SSUri.get(entity);
  }
    
  public void setUsers(final List<String> users) throws SSErr{
    this.users = SSUri.get(users);
  }
    
  public void setCircles(final List<String> circles) throws SSErr{
    this.circles = SSUri.get(circles);
  }

  public void getComment(final String comment) throws SSErr{
    this.comment = SSTextComment.get(comment);
  }
  
  public String getEntity(){
    return SSStrU.removeTrailingSlash(entity);
  }
  
  public List<String> getUsers() throws SSErr{
    return SSStrU.removeTrailingSlash(users);
  }
  
  public List<String> getCircles() throws SSErr{
    return SSStrU.removeTrailingSlash(circles);
  }
  
  public String getComment(){
    return SSStrU.toStr(comment);
  }

  public SSEntitySharePar(){/* Do nothing because of only JSON Jackson needs this */ }
    
  public SSEntitySharePar(
    final SSServPar servPar,
    final SSUri         user,
    final SSUri         entity,
    final List<SSUri>   users,
    final List<SSUri>   circles,
    final boolean       setPublic,
    final SSTextComment comment, 
    final boolean       withUserRestriction, 
    final boolean       shouldCommit){
    
    super(SSVarNames.entityShare, null, user, servPar.sqlCon);
    
    this.entity       = entity;
    
    SSUri.addDistinctWithoutNull(this.users,   users);
    SSUri.addDistinctWithoutNull(this.circles, circles);
    
    this.setPublic           = setPublic;
    this.comment             = comment;
    this.withUserRestriction = withUserRestriction;
    this.shouldCommit        = shouldCommit;
  }
}
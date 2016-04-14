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

package at.tugraz.sss.servs.tag.datatype;

import at.tugraz.sss.servs.util.SSVarNames;
import at.tugraz.sss.servs.util.SSStrU;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.entity.datatype.SSSpaceE;
import at.tugraz.sss.servs.entity.datatype.SSServPar; 
import at.tugraz.sss.servs.tag.datatype.SSTagLabel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SSTagFrequsGetPar extends SSServPar{

  public SSUri              forUser              = null;
  public List<SSUri>        entities             = new ArrayList<>();
  public List<SSTagLabel>   labels               = new ArrayList<>();
  public List<SSSpaceE>     spaces               = new ArrayList<>();
  public List<SSUri>        circles              = new ArrayList<>();
  public Long               startTime            = null;
  public boolean            useUsersEntities     = false;

  public String getForUser(){
    return SSStrU.removeTrailingSlash(forUser);
  }
    
  public void setForUser(final String forUser) throws SSErr{
    this.forUser = SSUri.get(forUser);
  } 

  public List<String> getEntities() throws SSErr{
    return SSStrU.removeTrailingSlash(entities);
  }
  
  public void setEntities(final List<String> entities) throws SSErr{
    this.entities = SSUri.get(entities);
  }

  public List<String> getLabels() throws SSErr{
    return SSStrU.toStr(labels);
  }
  
  public void setLabels(final List<String> labels) throws SSErr{
    this.labels = SSTagLabel.get(labels);
  }
  
  public List<String> getSpaces(){
    return SSStrU.toStr(spaces);
  }
  
  public void setSpaces(final List<String> spaces) throws SSErr{
    this.spaces = SSSpaceE.get(spaces);
  }

  public List<String> getCircles() throws SSErr{
    return SSStrU.removeTrailingSlash(circles);
  }
  
  public void setCircles(final List<String> circles) throws SSErr{
    this.circles = SSUri.get(circles);
  }
  
  public SSTagFrequsGetPar(){/* Do nothing because of only JSON Jackson needs this */ }
   
  public SSTagFrequsGetPar(
    final SSServPar servPar,
    final SSUri              user, 
    final SSUri              forUser, 
    final List<SSUri>        entities, 
    final List<SSTagLabel>   labels, 
    final List<SSSpaceE>     spaces, 
    final List<SSUri>        circles, 
    final Long               startTime,
    final boolean            useUsersEntities,
    final boolean            withUserRestriction){
    
    super(SSVarNames.tagFrequsGet, null, user, servPar.sqlCon);
    
    this.forUser = forUser;
    
    SSUri.addDistinctWithoutNull      (this.entities, entities);
    SSTagLabel.addDistinctNotEmpty    (this.labels,   labels);
    SSSpaceE.addDistinctWithoutNull   (this.spaces,   spaces);
    SSUri.addDistinctWithoutNull      (this.circles,  circles);
    
    this.startTime            = startTime;
    this.useUsersEntities     = useUsersEntities;
    this.withUserRestriction  = withUserRestriction;
  }
}
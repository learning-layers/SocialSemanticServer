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
package at.kc.tugraz.ss.service.tag.datatypes.pars;

import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSSpaceE;
import at.tugraz.sss.serv.SSServPar;
import at.kc.tugraz.ss.service.tag.datatypes.SSTagLabel;
import java.util.ArrayList;
import java.util.List;

public class SSTagFrequsGetPar extends SSServPar{

  public SSUri              forUser              = null;
  public List<SSUri>        entities             = new ArrayList<>();
  public List<SSTagLabel>   labels               = new ArrayList<>();
  public List<SSSpaceE>     spaces               = new ArrayList<>();
  public List<SSUri>        circles              = new ArrayList<>();
  public Long               startTime            = null;
  public Boolean            useUsersEntities     = false;

  public String getForUser(){
    return SSStrU.removeTrailingSlash(forUser);
  }
    
  public void setForUser(final String forUser) throws Exception{
    this.forUser = SSUri.get(forUser);
  } 

  public List<String> getEntities() throws Exception{
    return SSStrU.removeTrailingSlash(entities);
  }
  
  public void setEntities(final List<String> entities) throws Exception{
    this.entities = SSUri.get(entities);
  }

  public List<String> getLabels() throws Exception{
    return SSStrU.toStr(labels);
  }
  
  public void setLabels(final List<String> labels) throws Exception{
    this.labels = SSTagLabel.get(labels);
  }
  
  public List<String> getSpaces(){
    return SSStrU.toStr(spaces);
  }
  
  public void setSpaces(final List<String> spaces) throws Exception{
    this.spaces = SSSpaceE.get(spaces);
  }

  public List<String> getCircles() throws Exception{
    return SSStrU.removeTrailingSlash(circles);
  }
  
  public void setCircles(final List<String> circles) throws Exception{
    this.circles = SSUri.get(circles);
  }
  
  public SSTagFrequsGetPar(){}
   
  public SSTagFrequsGetPar(
    final SSUri              user, 
    final SSUri              forUser, 
    final List<SSUri>        entities, 
    final List<SSTagLabel>   labels, 
    final List<SSSpaceE>     spaces, 
    final List<SSUri>        circles, 
    final Long               startTime,
    final Boolean            useUsersEntities,
    final Boolean            withUserRestriction){
    
    super(SSServOpE.tagFrequsGet, null, user);
    
    this.forUser = forUser;
    
    SSUri.addDistinctWithoutNull      (this.entities, entities);
    SSTagLabel.addDistinctWithoutNull (this.labels,   labels);
    SSSpaceE.addDistinctWithoutNull   (this.spaces,   spaces);
    SSUri.addDistinctWithoutNull      (this.circles,  circles);
    
    this.startTime            = startTime;
    this.useUsersEntities     = useUsersEntities;
    this.withUserRestriction  = withUserRestriction;
  }
}
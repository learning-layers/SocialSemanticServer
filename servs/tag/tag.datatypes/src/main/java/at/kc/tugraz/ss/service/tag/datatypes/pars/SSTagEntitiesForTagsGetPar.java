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
import at.kc.tugraz.ss.service.tag.datatypes.SSTagLabel;
import at.tugraz.sss.serv.SSServPar;
import java.util.ArrayList;
import java.util.List;

public class SSTagEntitiesForTagsGetPar extends SSServPar{

  public SSUri             forUser   = null;
  public List<SSTagLabel>  labels    = new ArrayList<>();
  public SSSpaceE          space     = null;
  public Long              startTime = null;

  public void setForUser(final String forUser) throws Exception{
    this.forUser = SSUri.get(forUser);
  }

  public void setLabels(final List<String> labels) throws Exception{
    this.labels = SSTagLabel.get(labels);
  }

  public void setSpace(final String space) throws Exception{
    this.space = SSSpaceE.get(space);
  }
  
  public String getForUser(){
    return SSStrU.removeTrailingSlash(forUser);
  }

  public List<String> getLabels() throws Exception{
    return SSStrU.toStr(labels);
  }

  public String getSpace(){
    return SSStrU.toStr(space);
  }
  
  public SSTagEntitiesForTagsGetPar(){}
    
  public SSTagEntitiesForTagsGetPar(
    final SSServOpE        op,
    final String           key,
    final SSUri            user,
    final SSUri            forUser,
    final List<SSTagLabel> labels,
    final SSSpaceE         space,
    final Long             startTime,
    final Boolean          withUserRestriction){
    
    super(op, key, user);
    
    this.forUser = forUser;

    SSTagLabel.addDistinctWithoutNull(this.labels, labels);
    
    this.space               = space;
    this.startTime           = startTime;
    this.withUserRestriction = withUserRestriction;
  }
}
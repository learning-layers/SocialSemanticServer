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

import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSSpaceE;
import at.tugraz.sss.serv.SSServPar;
import at.kc.tugraz.ss.service.tag.datatypes.SSTagLabel;
import java.util.ArrayList;
import java.util.List;
import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSStrU;

public class SSTagsAddPar extends SSServPar{
  
  public List<SSUri>       entities     = new ArrayList<>();
  public List<SSTagLabel>  labels       = new ArrayList<>();
  public SSSpaceE          space        = null;
  public SSUri             circle       = null;
  public Long              creationTime = null;

  public List<String> getEntities() {
    return SSStrU.removeTrailingSlash(entities);
  }

  public void setEntities(final List<String> entities) throws Exception {
    this.entities = SSUri.get(entities);
  }

  public List<String> getLabels(){
    return SSStrU.toStr(labels);
  }

  public void setLabels(final List<String> labels) throws Exception{
    this.labels = SSTagLabel.get(labels);
  }

  public String getSpace(){
    return SSStrU.toStr(space);
  }

  public void setSpace(final String space) throws Exception{
    this.space = SSSpaceE.get(space);
  }
  
  public String getCircle(){
    return SSStrU.removeTrailingSlash(circle);
  }
  
  public void setCircle(final String circle) throws Exception{
    this.circle = SSUri.get(circle);
  }
  
  public SSTagsAddPar(){}
  
  public SSTagsAddPar(
    final SSUri              user,
    final List<SSTagLabel>   labels,
    final List<SSUri>        entities,
    final SSSpaceE           space,
    final SSUri              circle,
    final Long               creationTime,
    final Boolean            withUserRestriction,
    final Boolean            shouldCommit){
    
    super(SSServOpE.tagsAdd, null, user);
    
    SSTagLabel.addDistinctWithoutNull (this.labels,   labels);
    SSUri.addDistinctWithoutNull      (this.entities, entities);

    this.space               = space;
    this.circle              = circle;
    this.creationTime        = creationTime;
    this.withUserRestriction = withUserRestriction;
    this.shouldCommit        = shouldCommit;
  }
}
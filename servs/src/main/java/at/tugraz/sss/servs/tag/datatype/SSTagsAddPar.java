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
package at.tugraz.sss.servs.tag.datatype;

import at.tugraz.sss.servs.util.SSVarNames;
import at.tugraz.sss.servs.util.SSStrU;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.entity.datatype.SSSpaceE;
import at.tugraz.sss.servs.entity.datatype.SSServPar;
import at.tugraz.sss.servs.tag.datatype.SSTagLabel;
import java.util.ArrayList;
import java.util.List;

import java.sql.*;

public class SSTagsAddPar extends SSServPar{
  
  public List<SSUri>       entities     = new ArrayList<>();
  public List<SSTagLabel>  labels       = new ArrayList<>();
  public SSSpaceE          space        = null;
  public SSUri             circle       = null;
  public Long              creationTime = null;

  public List<String> getEntities() {
    return SSStrU.removeTrailingSlash(entities);
  }

  public void setEntities(final List<String> entities) throws SSErr {
    this.entities = SSUri.get(entities);
  }

  public List<String> getLabels(){
    return SSStrU.toStr(labels);
  }

  public void setLabels(final List<String> labels) throws SSErr{
    this.labels = SSTagLabel.get(labels);
  }

  public String getSpace(){
    return SSStrU.toStr(space);
  }

  public void setSpace(final String space) throws SSErr{
    this.space = SSSpaceE.get(space);
  }
  
  public String getCircle(){
    return SSStrU.removeTrailingSlash(circle);
  }
  
  public void setCircle(final String circle) throws SSErr{
    this.circle = SSUri.get(circle);
  }
  
  public SSTagsAddPar(){/* Do nothing because of only JSON Jackson needs this */ }
  
  public SSTagsAddPar(
    final SSServPar servPar,
    final SSUri              user,
    final List<SSTagLabel>   labels,
    final List<SSUri>        entities,
    final SSSpaceE           space,
    final SSUri              circle,
    final Long               creationTime,
    final boolean            withUserRestriction,
    final boolean            shouldCommit){
    
    super(SSVarNames.tagsAdd, null, user, servPar.sqlCon);
    
    SSTagLabel.addDistinctNotEmpty (this.labels,   labels);
    SSUri.addDistinctWithoutNull   (this.entities, entities);

    this.space               = space;
    this.circle              = circle;
    this.creationTime        = creationTime;
    this.withUserRestriction = withUserRestriction;
    this.shouldCommit        = shouldCommit;
  }
}
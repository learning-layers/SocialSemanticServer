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
package at.kc.tugraz.ss.service.disc.datatypes.pars;

import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.par.SSServPar; 
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.SSTextComment;
import java.util.ArrayList;
import java.util.List;

public class SSDiscUpdatePar extends SSServPar{
  
  public SSUri               disc                   = null;
  public SSLabel             label                  = null;
  public SSTextComment       content                = null;
  public List<SSUri>         entitiesToRemove       = new ArrayList<>();
  public List<SSUri>         entitiesToAttach       = new ArrayList<>();
  public List<SSLabel>       entityLabels           = new ArrayList<>();
  public Boolean             read                   = null;
  
  public String getDisc() {
    return SSStrU.removeTrailingSlash(disc);
  }

  public void setDisc(String disc) throws Exception {
    this.disc = SSUri.get(disc);
  }
  
  public String getLabel() {
    return SSStrU.toStr(label);
  }

  public void setLabel(String label) throws Exception {
    this.label = SSLabel.get(label);
  }
  
  public String getContent() {
    return SSStrU.toStr(content);
  }

  public void setContent(String content) throws Exception {
    this.content = SSTextComment.get(content);
  }

  public List<String> getEntitiesToRemove() {
    return SSStrU.removeTrailingSlash(entitiesToRemove);
  }

  public void setEntitiesToRemove(List<String> entitiesToRemove) throws Exception {
    this.entitiesToRemove = SSUri.get(entitiesToRemove);
  }

  public List<String> getEntitiesToAttach() {
    return SSStrU.removeTrailingSlash(entitiesToAttach);
  }

  public void setEntitiesToAttach(List<String> entitiesToAttach) throws Exception {
    this.entitiesToAttach = SSUri.get(entitiesToAttach);
  }
  
  public SSDiscUpdatePar(){}
  
  public SSDiscUpdatePar(
    final SSUri               user,
    final SSUri               disc,
    final SSLabel             label,
    final SSTextComment       content,
    final List<SSUri>         entitiesToRemove,
    final List<SSUri>         entitiesToAttach,
    final List<SSLabel>       entityLabels,
    final Boolean             read,
    final boolean             withUserRestriction,
    final boolean             shouldCommit){
    
    super(SSVarNames.discUpdate, null, user);
    
    this.disc           = disc;
    this.label          = label;
    this.content        = content;
    
    SSUri.addDistinctWithoutNull   (this.entitiesToRemove, entitiesToRemove);
    SSUri.addDistinctWithoutNull   (this.entitiesToAttach, entitiesToAttach);
    SSLabel.addDistinctNotNull     (this.entityLabels,     entityLabels);
      
    this.read                = read;
    this.withUserRestriction = withUserRestriction;
    this.shouldCommit        = shouldCommit;
  }
}

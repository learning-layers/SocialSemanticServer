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
package at.tugraz.sss.servs.livingdocument.datatype.par;

import at.tugraz.sss.serv.datatype.*;

import at.tugraz.sss.serv.datatype.par.SSServPar; 
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.SSTextComment;
import at.tugraz.sss.serv.datatype.*;
import java.sql.*;

public class SSLivingDocUpdatePar extends SSServPar{
  
  public SSUri           livingDoc            = null;
  public SSLabel         label                = null;
  public SSTextComment   description          = null;
  public SSUri           discussion           = null;
  
  public void setLivingDoc(final String livingDoc) throws SSErr{
    this.livingDoc = SSUri.get(livingDoc);
  }
  
  public String getLivingDoc(){
    return SSStrU.removeTrailingSlash(livingDoc);
  }

  public String getLabel() {
    return SSStrU.toStr(label);
  }

  public void setLabel(final String label) throws SSErr {
    this.label = SSLabel.get(label);
  }

  public String getDescription() {
    return SSStrU.toStr(description);
  }

  public void setDescription(final String description) throws SSErr {
    this.description = SSTextComment.get(description);
  }
  
  public void setDiscussion(final String discussion) throws SSErr{
    this.discussion = SSUri.get(discussion);
  }
  
  public String getDiscussion(){
    return SSStrU.removeTrailingSlash(discussion);
  }
  
  public SSLivingDocUpdatePar(){/* Do nothing because of only JSON Jackson needs this */ }
    
  public SSLivingDocUpdatePar(
    final SSServPar servPar,
    final SSUri         user,
    final SSUri         livingDoc, 
    final SSLabel       label, 
    final SSTextComment description, 
    final SSUri         discussion, 
    final boolean       withUserRestriction,
    final boolean       shouldCommit){
    
    super(SSVarNames.livingDocUpdate, null, user, servPar.sqlCon);
    
    this.livingDoc            = livingDoc;
    this.label                = label;
    this.description          = description;
    this.discussion           = discussion;
    this.withUserRestriction  = withUserRestriction;
    this.shouldCommit         = shouldCommit;
  }
}
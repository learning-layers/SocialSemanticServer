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

import at.tugraz.sss.serv.datatype.par.SSServPar; import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.SSTextComment;
import at.tugraz.sss.serv.datatype.*;

public class SSLivingDocAddPar extends SSServPar{
  
  public SSUri           uri                  = null;
  public SSLabel         label                = null;
  public SSTextComment   description          = null;
  public SSUri           discussion           = null;
  
  public void setUri(final String uri) throws Exception{
    this.uri = SSUri.get(uri);
  }
  
  public String getUri(){
    return SSStrU.removeTrailingSlash(uri);
  }

  public String getLabel() {
    return SSStrU.toStr(label);
  }

  public void setLabel(final String label) throws Exception {
    this.label = SSLabel.get(label);
  }

  public String getDescription() {
    return SSStrU.toStr(description);
  }

  public void setDescription(final String description) throws Exception {
    this.description = SSTextComment.get(description);
  }
  
  public void setDiscussion(final String discussion) throws Exception{
    this.discussion = SSUri.get(discussion);
  }
  
  public String getDiscussion(){
    return SSStrU.removeTrailingSlash(discussion);
  }
  
  public SSLivingDocAddPar(){}
    
  public SSLivingDocAddPar(
    final SSUri         user,
    final SSUri         uri, 
    final SSLabel       label, 
    final SSTextComment description, 
    final SSUri         discussion, 
    final boolean       withUserRestriction,
    final boolean       shouldCommit){
    
    super(SSVarNames.livingDocAdd, null, user);
    
    this.uri                  = uri;
    this.label                = label;
    this.description          = description;
    this.discussion           = discussion;
    this.withUserRestriction  = withUserRestriction;
    this.shouldCommit         = shouldCommit;
  }
}
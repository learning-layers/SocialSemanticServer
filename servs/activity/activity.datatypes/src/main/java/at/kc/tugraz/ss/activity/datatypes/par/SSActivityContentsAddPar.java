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
package at.kc.tugraz.ss.activity.datatypes.par;

import at.kc.tugraz.ss.activity.datatypes.SSActivityContent;
import at.kc.tugraz.ss.activity.datatypes.enums.SSActivityContentE;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSStrU;
import java.util.ArrayList;
import java.util.List;

public class SSActivityContentsAddPar extends SSServPar{
  
  public SSUri                   activity         = null;
  public SSActivityContentE      contentType      = null;
  public List<SSActivityContent> contents         = new ArrayList<>();

  public String getActivity(){
    return SSStrU.removeTrailingSlash(activity);
  }

  public void setActivity(final String activity) throws Exception{
    this.activity = SSUri.get(activity);
  }

  public String getContentType(){
    return SSStrU.toStr(contentType);
  }

  public void setContentType(final String contentType) throws Exception{
    this.contentType = SSActivityContentE.get(contentType);
  }

  public List<String> getContents(){
    return SSStrU.toStr(contents);
  }

  public void setContents(final List<String> contents) throws Exception{
    this.contents = SSActivityContent.get(contents);
  }
  
  public SSActivityContentsAddPar(
    final SSServOpE               op,
    final String                  key,
    final SSUri                   user,
    final SSUri                   activity,
    final SSActivityContentE      contentType,
    final List<SSActivityContent> contents,
    final Boolean                 shouldCommit){
    
    super(op, key, user);
    
    this.activity      = activity;
    this.contentType   = contentType;
    
    if(contents != null){
      this.contents.addAll(contents);
    }
    
    this.shouldCommit = shouldCommit;
  }
}

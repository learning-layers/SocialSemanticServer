/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2014, Graz University of Technology - KTI (Knowledge Technologies Institute).
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
package at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par;

import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSVarNames;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntityE;
import com.wordnik.swagger.annotations.ApiModelProperty;
import java.util.Map;

public class SSEvernoteNote extends SSEntity{
  
  @ApiModelProperty(
    required = false,
    value = "notebook containing this note")
  public SSUri notebook = null;
  
  public static SSEvernoteNote get(
    final SSEntity entity,
    final SSUri    notebook) throws Exception{
    
    return new SSEvernoteNote(entity, notebook);
  }
  
  public static SSEvernoteNote get(
    final SSUri id,
    final SSUri notebook) throws Exception{
    
    return new SSEvernoteNote(id, notebook);
  }
  
  protected SSEvernoteNote(
    final SSEntity entity,
    final SSUri    notebook) throws Exception{

    super(entity);
    
    this.notebook = notebook;
  }
  
  protected SSEvernoteNote(
    final SSUri id,
    final SSUri notebook) throws Exception{

    super(id, SSEntityE.evernoteNote);
    
    this.notebook = notebook;
  }

  @Override
  public Object jsonLDDesc(){
    
    final Map<String, Object> ld = (Map<String, Object>) super.jsonLDDesc();
    
    ld.put(SSVarNames.notebook, SSVarNames.sss + SSStrU.colon + SSUri.class.getName());
    
    return ld;
  }
  
  /* json getters */
  public String getNotebook(){
    return SSStrU.removeTrailingSlash(notebook);
  }
}
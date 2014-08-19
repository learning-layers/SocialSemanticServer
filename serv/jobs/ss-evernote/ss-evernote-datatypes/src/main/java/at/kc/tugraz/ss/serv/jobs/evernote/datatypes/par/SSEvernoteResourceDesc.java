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

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityDescA;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import java.util.Map;

public class SSEvernoteResourceDesc extends SSEntityDescA{
  
  public SSUri  note     = null;
  public String fileExt  = null;
  public String mimeType = null;

  public static SSEvernoteResourceDesc get(
    final SSEntityDescA   entityDesc,
    final SSUri           note,
    final String          fileExt,
    final String          mimeType) throws Exception{
    
    return new SSEvernoteResourceDesc(entityDesc, note, fileExt, mimeType);
  }
  
  private SSEvernoteResourceDesc(
    final SSEntityDescA    entityDesc,
    final SSUri            note,
    final String           fileExt,
    final String           mimeType) throws Exception{
    
    super(entityDesc);
    
    this.note     = note;
    this.fileExt  = fileExt;
    this.mimeType = mimeType;
  }
  
  @Override
  public Object jsonLDDesc(){
    
    final Map<String, Object> ld = (Map<String, Object>) super.jsonLDDesc();
    
    ld.put(SSVarU.note,      SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.mimeType,  SSVarU.xsd + SSStrU.colon + SSStrU.valueString);
    ld.put(SSVarU.fileExt,   SSVarU.xsd + SSStrU.colon + SSStrU.valueString);
    
    return ld;
  }
  
  /* json getters */
  
  public String getNote(){
    return SSStrU.removeTrailingSlash(note);
  }
}
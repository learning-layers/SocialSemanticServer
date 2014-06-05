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
package at.kc.tugraz.ss.service.filerepo.datatypes;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityA;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityDescA;
import java.util.List;
import java.util.Map;

public class SSFileDesc extends SSEntityDescA{
  
  public String fileExt  = null;
  public String mimeType = null;
  
  private SSFileDesc(
    final SSUri            entityUri,
    final SSLabel          entityLabel, 
    final Long             creationTime,
    final List<String>     tags, 
    final SSEntityA        overallRating,
    final List<SSUri>      discs,
    final SSUri            author,
    final String           fileExt,
    final String           mimeType) throws Exception{
    
    super(entityUri, entityLabel, creationTime, SSEntityE.file, SSEntityE.fileDesc, author, overallRating, tags, discs);
    
    this.fileExt  = fileExt;
    this.mimeType = mimeType;
  }
  
  public static SSFileDesc get(
    final SSUri           entityUri,
    final SSLabel         entityLabel,
    final Long            entityCreationTime,
    final List<String>    tags, 
    final SSEntityA       overallRating,
    final List<SSUri>     discs,
    final SSUri           author,
    final String          fileExt,
    final String          mimeType) throws Exception{
    
    return new SSFileDesc(entityUri, entityLabel, entityCreationTime, tags, overallRating, discs, author, fileExt, mimeType);
  }
  
  @Override
  public Object jsonLDDesc(){
    
    final Map<String, Object> ld = (Map<String, Object>) super.jsonLDDesc();
    
    ld.put(SSVarU.mimeType,      SSVarU.xsd + SSStrU.colon + SSStrU.valueString);
    ld.put(SSVarU.fileExt,       SSVarU.xsd + SSStrU.colon + SSStrU.valueString);
    
    return ld;
  }
}

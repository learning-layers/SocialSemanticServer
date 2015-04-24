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
package at.kc.tugraz.ss.serv.job.i5cloud.datatypes;

import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSVarNames;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSLabel;
import at.tugraz.sss.serv.SSJSONLDU;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSi5CloudAchsoVideo extends SSEntity{
  
  public SSLabel      authorLabel    = null;
  public List<String> keywords       = new ArrayList<>();
  public List<String> annotations    = new ArrayList<>();
  
  public static SSi5CloudAchsoVideo get(
    final SSUri        id,
    final SSLabel      label,
    final SSLabel      authorLabel, 
    final List<String> keywords,
    final List<String> annotations) throws Exception{
    
    return new SSi5CloudAchsoVideo(
      id, 
      label,
      authorLabel,
      keywords, 
      annotations);
  }
  
  protected SSi5CloudAchsoVideo(
    final SSUri        id,
    final SSLabel      label,
    final SSLabel      authorLabel, 
    final List<String> keywords,
    final List<String> annotations) throws Exception{
    
    super(id, SSEntityE.video, label);
    
    this.authorLabel  = authorLabel;
      
    if(keywords != null){
      this.keywords.addAll(keywords);
    }
    
    if(annotations != null){
      this.annotations.addAll(annotations);
    }
  }

  @Override
  public Object jsonLDDesc(){
    
    final Map<String, Object> ld             = (Map<String, Object>) super.jsonLDDesc();
    final Map<String, Object> keywordsObj    = new HashMap<>();
    final Map<String, Object> annotationsObj = new HashMap<>();
    
    keywordsObj.put(SSJSONLDU.id,        SSVarNames.xsd + SSStrU.colon + SSStrU.valueString);
    keywordsObj.put(SSJSONLDU.container, SSJSONLDU.set);
    
    ld.put(SSVarNames.keywords, keywordsObj);
    
    annotationsObj.put(SSJSONLDU.id,        SSVarNames.xsd + SSStrU.colon + SSStrU.valueString);
    annotationsObj.put(SSJSONLDU.container, SSJSONLDU.set);
    
    ld.put(SSVarNames.annotations, annotationsObj);
    
    ld.put(SSVarNames.authorLabel, SSVarNames.sss + SSStrU.colon + SSLabel.class.getName());
    
    return ld;
  }
  
  /* json getters */
  public String getAuthorLabel(){
    return SSStrU.toStr(authorLabel);
  }
}

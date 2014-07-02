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
package at.kc.tugraz.ss.datatypes.datatypes.entity;

import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.SSTextComment;
import at.kc.tugraz.ss.serv.jsonld.util.SSJSONLDU;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class SSEntityDescA extends SSEntityA{

  public SSUri            entity          = null;
	public SSEntityE        type            = null;
  public SSLabel          label           = null;
	public Long             creationTime    = null;
  public SSUri            author          = null;
  public SSEntityA        overallRating   = null;
  public List<String>     tags            = new ArrayList<>();
  public List<SSEntityA>  discs           = new ArrayList<>();
  public List<SSEntityA>  uEs             = new ArrayList<>();
  public String           thumb           = null;
  public SSTextComment    description     = null;

  protected SSEntityDescA(
    final SSEntityDescA entityDesc) throws Exception{
    
    super(entityDesc.entity);
    
    this.entity          = entityDesc.entity;
    this.type            = entityDesc.type;
    this.label           = entityDesc.label;
    this.creationTime    = entityDesc.creationTime;
    this.author          = entityDesc.author;
    this.overallRating   = entityDesc.overallRating;
    this.tags            = entityDesc.tags;
    this.discs           = entityDesc.discs;
    this.uEs             = entityDesc.uEs;
    this.thumb           = entityDesc.thumb;
    this.description     = entityDesc.description;
  }
  
  protected SSEntityDescA(
    final SSUri           entity, 
    final SSLabel         label, 
    final Long            creationTime, 
    final SSEntityE       type, 
    final SSUri           author,
    final SSEntityA       overallRating,
    final List<String>    tags, 
    final List<SSEntityA> discs,
    final List<SSEntityA> uEs,
    final String          thumb,
    final SSTextComment   description) throws Exception{
    
    super(entity);
    
    this.entity         = entity;
    this.type           = type;
    this.label          = label;
    this.creationTime   = creationTime;
    this.author         = author;
    this.overallRating  = overallRating;
    this.thumb          = thumb;
    this.description    = description;
    
    if(tags != null){
      this.tags.addAll(tags);
    }
    
    if(discs != null){
      this.discs.addAll(discs);
    }
    
    if(uEs != null){
      this.uEs.addAll(uEs);
    }
  }
  
  @Override
  public Object jsonLDDesc(){
    
    final Map<String, Object> ld       = new HashMap<>();
    final Map<String, Object> tagsObj  = new HashMap<>();
    final Map<String, Object> discsObj = new HashMap<>();
    final Map<String, Object> uEsObj   = new HashMap<>();
    
    tagsObj.put(SSJSONLDU.id,        SSVarU.xsd + SSStrU.colon + SSStrU.valueString);
    tagsObj.put(SSJSONLDU.container, SSJSONLDU.set);

    ld.put(SSVarU.tags,      tagsObj);
    
    discsObj.put(SSJSONLDU.id,        SSVarU.sss + SSStrU.colon + SSEntityA.class.getName());
    discsObj.put(SSJSONLDU.container, SSJSONLDU.set);

    ld.put(SSVarU.discs,      discsObj);
    
    uEsObj.put(SSJSONLDU.id,        SSVarU.sss + SSStrU.colon + SSEntityA.class.getName());
    uEsObj.put(SSJSONLDU.container, SSJSONLDU.set);

    ld.put(SSVarU.uEs,      uEsObj);
    
    ld.put(SSVarU.entity,         SSVarU.sss  + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.label,          SSVarU.sss  + SSStrU.colon + SSLabel.class.getName());
    ld.put(SSVarU.creationTime,   SSVarU.xsd  + SSStrU.colon + SSStrU.valueLong);
    ld.put(SSVarU.entity,         SSVarU.sss  + SSStrU.colon + SSEntityE.class.getName());
    ld.put(SSVarU.author,         SSVarU.sss  + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.overallRating,  SSVarU.sss  + SSStrU.colon + SSEntityA.class.getName());
    ld.put(SSVarU.thumb,          SSVarU.xsd  + SSStrU.colon + SSStrU.valueString);
    ld.put(SSVarU.description,    SSVarU.sss  + SSStrU.colon + SSTextComment.class.getName());
    
    return ld;
  }
  
  /* json getters */
  
  public String getEntity() throws Exception{
    return SSStrU.removeTrailingSlash(entity);
  }

  public String getLabel() {
    return SSStrU.toStr(label);
  }

  public Long getCreationTime() {
    return creationTime;
  }

  public String getType() {
    return SSStrU.toStr(type);
  }

  public String getAuthor() throws Exception {
    return SSStrU.removeTrailingSlash(author);
  }
  
  public List<String> getTags(){
    return tags;
  }

  public SSEntityA getOverallRating(){
    return overallRating;
  }

  public List<String> getDiscs() throws Exception{
    return SSStrU.removeTrailingSlash(discs);
  }
  
  public List<SSEntityA> getuEs() throws Exception{
    return uEs;
  }
  
  public String getThumb(){
    return thumb;
  }
  
  public String getDescription(){
    return SSStrU.toStr(description);
  }
}
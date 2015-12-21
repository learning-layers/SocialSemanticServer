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
package at.tugraz.sss.adapter.rest.v2.disc;

import at.kc.tugraz.ss.conf.conf.SSVocConf;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.SSTextComment;
import at.tugraz.sss.serv.datatype.*;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@ApiModel(value = "discEntryAdd request parameter")
public class SSDiscEntryAddRESTAPIV2Par{
  
  @ApiModelProperty(
    required = false,
    value = "discussion name (optional in case of an existing discussion)")
  public SSLabel                label          = null;
  
  @XmlElement
  public void setLabel(final String label) throws Exception{
    this.label = SSLabel.get(label); 
  }
  
  @ApiModelProperty(
    required = false,
    value = "describes the discussion in more detail (optional, except in case of a new discussion of type qa)")
  public SSTextComment          description    = null;

  @XmlElement
  public void setDescription(final String description) throws Exception{
    this.description = SSTextComment.get(description);
  }
  
  @ApiModelProperty(
    required = false,
    value = "entities to be attached either to corresponding discussion if new discussion to be added or to respective entry in the other case")
  public List<SSUri>            entities       = null;
  
  @XmlElement
  public void setEntities(final List<String> entities) throws Exception{
    this.entities = SSUri.get(entities, SSVocConf.sssUri);
  }
  
  @ApiModelProperty(
    required = false,
    value = "labels for the entities to be attached")
  public List<SSLabel>            entityLabels       = null;
  
  @XmlElement
  public void setEntityLabels(final List<String> entityLabels) throws Exception{
   this.entityLabels = SSLabel.get(entityLabels);
  }

  @ApiModelProperty(
    required = false,
    value = "users to share this discussion with upon creation of a new discussion (optional, though works only for a new discussion)")
  public List<SSUri>            users          = null;
  
  @XmlElement
  public void setUsers(final List<String> users) throws Exception{
    this.users = SSUri.get(users, SSVocConf.sssUri);
  }
 
  @ApiModelProperty(
    required = false,
    value = "circles to share this discussion with upon creation of a new discussion (optional, though works only for a new discussion)")
  public List<SSUri>            circles          = null;
  
  @XmlElement
  public void setCircles(final List<String> circles) throws Exception{
    this.circles = SSUri.get(circles, SSVocConf.sssUri);
  }
  
  @ApiModelProperty(
    required = false,
    value = "text for the comment / answer / opinion (optional in case of a new discussion)")
  public SSTextComment          entry    = null;

  @XmlElement
  public void setEntry(final String entry) throws Exception{
    this.entry = SSTextComment.get(entry);
  }
      
  @ApiModelProperty(
    required = false,
    value = "discussion to add an entry for (optional in case of a new discussion)")
  public SSUri                disc          = null;
  
  @XmlElement
  public void setDisc(final String disc) throws Exception{
    this.disc = SSUri.get(disc, SSVocConf.sssUri);
  }
  
  @ApiModelProperty(
    required = false,
    value = "entities to start a discussion for (optional)")
  public List<SSUri>                targets          = null;
  
  @XmlElement
  public void setTargets(final List<String> targets) throws Exception{
    this.targets = SSUri.get(targets, SSVocConf.sssUri);
  }
  
  @ApiModelProperty(
    required = false,
    value = "discussion type: disc, qa or chat (optional in case of an existing discussion)")
  public SSEntityE                type          = null;
  
  @XmlElement
  public void setType(final String type) throws Exception{
    this.type = SSEntityE.get(type);
  }
  
  @XmlElement
  @ApiModelProperty(
    required = false,
    value = "whether a new disc should be created")
  public Boolean          addNewDisc    = null;
}
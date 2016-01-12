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
package at.tugraz.sss.adapter.rest.v3.video;

import at.tugraz.sss.serv.datatype.SSTextComment;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.conf.SSConf;
import at.kc.tugraz.sss.video.datatypes.SSVideoE;
import io.swagger.annotations.*;

@ApiModel
public class SSVideoAddRESTPar{
  
  @ApiModelProperty(
    required = false,
    value = "video's uuid (if provided used within id if link is not set)")
  public String                uuid        = null;
  
  @ApiModelProperty(
    required = false,
    value = "video's link (if provided used as id)")
  public SSUri                link        = null;
  
  public void setLink(final String link) throws SSErr{
    this.link = SSUri.get(link, SSConf.sssUri);
  }
  
  @ApiModelProperty(
    required = false,
    value = "video type, e.g., achso")
  public SSVideoE               type = null;
  
  public void setType(final String type) throws SSErr{
    this.type = SSVideoE.get(type);
  }
  
  @ApiModelProperty(
    required = false,
    value = "entity for which to attach this video")
  public SSUri                forEntity        = null;
  
  public void setForEntity(final String forEntity) throws SSErr{
    this.forEntity = SSUri.get(forEntity, SSConf.sssUri);
  }
  
  @ApiModelProperty(
    required = false,
    value = "video's genre")
  public String                genre        = null;
  
  @ApiModelProperty(
    required = false,
    value = "name")
  public SSLabel               label        = null;
  
  public void setLabel(final String label) throws SSErr{
    this.label = SSLabel.get(label);
  }
  
  @ApiModelProperty(
    required = false,
    value = "description")
  public SSTextComment               description        = null;
  
  public void setDescription(final String description) throws SSErr{
    this.description = SSTextComment.get(description);
  }
  
  @ApiModelProperty(
    required = false,
    value = "creation time of the video")
  public Long               creationTime        = null;
  
  @ApiModelProperty(
    required = false,
    value = "latitude")
  public Double               latitude = null;
  
  @ApiModelProperty(
    required = false,
    value = "longitude")
  public Double               longitude = null;
  
  @ApiModelProperty(
    required = false,
    value = "accuracy")
  public Float               accuracy = null;
  
  public SSVideoAddRESTPar(){}
}
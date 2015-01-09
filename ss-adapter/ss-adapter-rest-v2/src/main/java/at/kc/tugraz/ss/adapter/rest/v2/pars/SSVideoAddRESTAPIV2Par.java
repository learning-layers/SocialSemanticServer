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
package at.kc.tugraz.ss.adapter.rest.v2.pars;

import at.kc.tugraz.ss.datatypes.datatypes.SSTextComment;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@ApiModel(value = "videoAdd request parameter")
public class SSVideoAddRESTAPIV2Par{
  
  @XmlElement
  @ApiModelProperty(
    required = false,
    value = "video's uuid (if provided used within id if link is not set)")
  public String                uuid        = null;
  
  @ApiModelProperty(
    required = false,
    value = "video's link (if provided used as id)")
  public SSUri                link        = null;
  
  @XmlElement
  public void setLink(final String link) throws Exception{
    try{ this.link = SSUri.get(link, SSVocConf.sssUri); }catch(Exception error){}
  }
  
  @ApiModelProperty(
    required = false,
    value = "entity for which to attach this video")
  public SSUri                forEntity        = null;
  
  @XmlElement
  public void setForEntity(final String forEntity) throws Exception{
    try{ this.forEntity = SSUri.get(forEntity, SSVocConf.sssUri); }catch(Exception error){}
  }
  
  @XmlElement
  @ApiModelProperty(
    required = false,
    value = "video's genre")
  public String                genre        = null;
  
  
  @ApiModelProperty(
    required = false,
    value = "name")
  public SSLabel               label        = null;
  
  @XmlElement
  public void setLabel(final String label) throws Exception{
    try{ this.label = SSLabel.get(label); }catch(Exception error){}
  }
  
  @ApiModelProperty(
    required = false,
    value = "description")
  public SSTextComment               description        = null;
  
  @XmlElement
  public void setDescription(final String description) throws Exception{
    try{ this.description = SSTextComment.get(description); }catch(Exception error){}
  }
  
  @XmlElement
  @ApiModelProperty(
    required = false,
    value = "creation time of the video")
  public Long               creationTime        = null;
  
  @XmlElement
  @ApiModelProperty(
    required = false,
    value = "latitude")
  public Double               latitude = null;
  
  @XmlElement
  @ApiModelProperty(
    required = false,
    value = "longitude")
  public Double               longitude = null;
  
  @XmlElement
  @ApiModelProperty(
    required = false,
    value = "accuracy")
  public Float               accuracy = null;

  public SSVideoAddRESTAPIV2Par(){}
}
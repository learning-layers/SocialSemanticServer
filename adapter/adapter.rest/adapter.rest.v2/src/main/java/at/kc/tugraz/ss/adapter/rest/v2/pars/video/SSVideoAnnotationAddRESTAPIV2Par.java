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
package at.kc.tugraz.ss.adapter.rest.v2.pars.video;

import at.tugraz.sss.serv.SSTextComment;
import at.tugraz.sss.serv.SSLabel;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@ApiModel(value = "videoAnnotationAdd request parameter")
public class SSVideoAnnotationAddRESTAPIV2Par{
  
  @XmlElement
  @ApiModelProperty(
    required = true,
    value = "time point the annotation is attached to the video")
  public Long               timePoint        = null;
  
  @XmlElement
  @ApiModelProperty(
    required = true,
    value = "x coordinate the annotation is attached to the video")
  public Float               x        = null;
  
  @XmlElement
  @ApiModelProperty(
    required = true,
    value = "y coordinate the annotation is attached to the video")
  public Float               y        = null;
  
  @ApiModelProperty(
    required = true,
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
  
  public SSVideoAnnotationAddRESTAPIV2Par(){}
}
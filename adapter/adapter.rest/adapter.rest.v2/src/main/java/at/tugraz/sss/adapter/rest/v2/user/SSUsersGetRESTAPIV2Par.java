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
package at.tugraz.sss.adapter.rest.v2.user;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@ApiModel(value = "users get request parameter")
public class SSUsersGetRESTAPIV2Par{
  
  @XmlElement
  @ApiModelProperty(
    required = false, 
    value = "")
  public boolean               setFriends      = false;
  
  @XmlElement
  @ApiModelProperty(
    required = false, 
    value = "")
  public boolean               setProfilePicture      = false;
  
  @XmlElement
  @ApiModelProperty(
    required = false, 
    value = "")
  public boolean               setThumb      = false;
  
  @XmlElement
  @ApiModelProperty(
    required = false, 
    value = "")
  public boolean               setMessages      = false;
  
  @XmlElement
  @ApiModelProperty(
    required = false, 
    value = "")
  public boolean               setActivities      = false;
  
  @XmlElement
  @ApiModelProperty(
    required = false, 
    value = "")
  public boolean               setCircles      = false;
  
  @XmlElement
  @ApiModelProperty(
    required = false,
    value = "")
  public boolean               setDiscs      = false;
  
  @XmlElement
  @ApiModelProperty(
    required = false,
    value = "")
  public boolean               setColls      = false;
  
  @XmlElement
  @ApiModelProperty(
    required = false,
    value = "")
  public boolean               setTags      = false;
  
  public SSUsersGetRESTAPIV2Par(){}
}
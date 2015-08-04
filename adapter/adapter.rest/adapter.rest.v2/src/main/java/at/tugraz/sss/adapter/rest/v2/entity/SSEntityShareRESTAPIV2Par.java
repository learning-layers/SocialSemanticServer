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
package at.tugraz.sss.adapter.rest.v2.entity;

import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.tugraz.sss.serv.SSTextComment;
import at.tugraz.sss.serv.SSUri;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@ApiModel(value = "entityShare request parameter")
public class SSEntityShareRESTAPIV2Par{

  @ApiModelProperty(
    required = false,
    value = "users to share with")
  public List<SSUri> users = null;
  
  @XmlElement
  public void setUsers(final List<String> users) throws Exception{
    this.users = SSUri.get(users, SSVocConf.sssUri);
  }
  
  @ApiModelProperty(
    required = false,
    value = "circles to share with")
  public List<SSUri> circles = null;
  
  @XmlElement
  public void setCircles(final List<String> circles) throws Exception{
    this.circles = SSUri.get(circles, SSVocConf.sssUri);
  }
  
  @ApiModelProperty(
    required = false,
    value = "set the entity public")
  public Boolean setPublic = null;
  
  @ApiModelProperty(
    required = false,
    value = "comment to add the share process")
  public SSTextComment comment = null;
  
  @XmlElement
  public void setComment(final String comment) throws Exception{
    this.comment = SSTextComment.get(comment);
  }
  
  public SSEntityShareRESTAPIV2Par(){}
}
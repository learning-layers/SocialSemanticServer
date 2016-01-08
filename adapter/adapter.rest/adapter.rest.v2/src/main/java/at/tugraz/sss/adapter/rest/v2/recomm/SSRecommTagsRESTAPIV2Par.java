/**
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
package at.tugraz.sss.adapter.rest.v2.recomm;

import at.tugraz.sss.conf.SSConf;
import at.tugraz.sss.serv.datatype.*;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@ApiModel(value = "recommTags request parameter")
public class SSRecommTagsRESTAPIV2Par{
  
  @XmlElement
  @ApiModelProperty(
    required = false,
    value = "realm the user wants to query")
  public String         realm    = null;
  
  @ApiModelProperty( 
    required = false, 
    value = "user to be considered to retrieve recommendations for")
  public SSUri         forUser    = null;
  
  @XmlElement
  public void setForUser(final String forUser) throws Exception{
    this.forUser = SSUri.get(forUser, SSConf.sssUri);
  }
  
  @ApiModelProperty( 
    required = false, 
    value = "resource to be considered to retrieve recommendations for")
  public SSUri         entity     = null;
  
  @XmlElement
  public void setEntity(final String entity) throws Exception{
    this.entity = SSUri.get(entity, SSConf.sssUri);
  }
  
  @XmlElement
  @ApiModelProperty( 
    required = false, 
    value = "additional information to be taken into account")
  public List<String>  categories = null;
  
  @XmlElement
  @ApiModelProperty( 
    required = false, 
    value = "number of tags to be returned")
  public Integer       maxTags    = 10;
  
  @XmlElement
  @ApiModelProperty(
    required = false,
    value = "whether own tags should be included in the result")
  public boolean includeOwn = false;
  
  @XmlElement
  @ApiModelProperty(
    required = false,
    value = "whether access rights shall be ignored, as data from external tool is used")
  public boolean ignoreAccessRights = false;
  
  public SSRecommTagsRESTAPIV2Par(){}
}

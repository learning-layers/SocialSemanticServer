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

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@ApiModel(value = "discGet request parameter")
public class SSDiscGetRESTAPIV2Par{
  
  @XmlElement
  @ApiModelProperty( 
    required = false, 
    value = "whether comments of threads and entries shall be retrieved")
  public Boolean    setComments       = false;
  
  @XmlElement
  @ApiModelProperty( 
    required = false, 
    value = "whether likes for entries shall be set")
  public Boolean    setLikes       = false;
  
  @XmlElement
  @ApiModelProperty( 
    required = false, 
    value = "whether the types of circles the discussion is in shall be set")
  public Boolean    setCircleTypes   = false;
  
  @XmlElement
  @ApiModelProperty( 
    required = false, 
    value = "whether the entries of the discussion shall be returned")
  public Boolean    setEntries   = false;
}
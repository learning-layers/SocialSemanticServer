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

import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSUri;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@ApiModel(value = "entities accessible get request parameter")
public class SSEntitiesAccessibleGetRESTAPIV2Par{
  
  @ApiModelProperty( 
    required = false, 
    value = "")
  public List<SSEntityE>            types         = null;

  @XmlElement
  public void setTypes(final List<String> types) throws Exception{
     this.types = SSEntityE.get(types); 
  }
  
  @ApiModelProperty( 
    required = false, 
    value = "")
  public List<SSUri>            authors         = null;

  @XmlElement
  public void setAuthors(final List<String> authors) throws Exception{
     this.authors = SSUri.get(authors); 
  }
  
  @XmlElement
  @ApiModelProperty(
    required = false,
    value = "unique identifier for the pages of a previous query")
  public String              pagesID             = null;
  
  @XmlElement
  @ApiModelProperty(
    required = false,
    value = "number of the page to be requested from a previous query")
  public Integer             pageNumber             = null;
  
  public SSEntitiesAccessibleGetRESTAPIV2Par(){}
}
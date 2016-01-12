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
package at.tugraz.sss.adapter.rest.v3.entity;

import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.datatype.*;
import io.swagger.annotations.*;
import java.util.List;

@ApiModel
public class SSEntitiesAccessibleGetRESTPar{
  
  @ApiModelProperty( 
    required = false, 
    value = "")
  public List<SSEntityE>            types         = null;

  
  public void setTypes(final List<String> types) throws SSErr{
     this.types = SSEntityE.get(types); 
  }
  
  @ApiModelProperty( 
    required = false, 
    value = "")
  public List<SSUri>            authors         = null;

  
  public void setAuthors(final List<String> authors) throws SSErr{
     this.authors = SSUri.get(authors); 
  }
  
  
  @ApiModelProperty(
    required = false,
    value = "unique identifier for the pages of a previous query")
  public String              pagesID             = null;
  
  
  @ApiModelProperty(
    required = false,
    value = "number of the page to be requested from a previous query")
  public Integer             pageNumber             = null;
  
  
  @ApiModelProperty(
    required = false,
    value = "start timestamp")
  public Long            startTime      = null;
  
  
  @ApiModelProperty(
    required = false,
    value = "end timestamp")
  public Long            endTime        = null;
  
  public SSEntitiesAccessibleGetRESTPar(){}
}
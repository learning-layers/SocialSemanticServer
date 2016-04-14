/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2015, Graz University of Technology - KTI (Knowledge Technologies Institute).
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

/**
 * @author Dieter Theiler
 */

package at.tugraz.sss.servs.entity.datatype;

import at.tugraz.sss.servs.util.SSStrU;
import io.swagger.annotations.*;
import java.util.List;

@ApiModel
public class SSEntitiesAccessibleGetRESTPar{
  
  @ApiModelProperty( 
    required = false)
  public List<SSEntityE>            types         = null;

  public void setTypes(final List<String> types) throws SSErr{
     this.types = SSEntityE.get(types); 
  }
  
  @ApiModelProperty( 
    required = false)
  public List<SSUri>            authors         = null;

  public void setAuthors(final List<String> authors) throws SSErr{
     this.authors = SSUri.get(authors); 
  }
  
  public List<String> getAuthors(){
     return SSStrU.removeTrailingSlash(authors);
  }

  @ApiModelProperty(
    required = false)
  public int              pageSize             = 10;
  
  @ApiModelProperty(
    required = false,
    value = "unique identifier for the pages of a previous query")
  public String              pagesID             = null;
  
  @ApiModelProperty(
    required = false,
    value = "number of the page to be requested from a previous query")
  public int             pageNumber             = 1;
  
  @ApiModelProperty(
    required = false,
    value = "start timestamp")
  public Long            startTime      = null;
  
  @ApiModelProperty(
    required = false,
    value = "end timestamp")
  public Long            endTime        = null;
 
  @ApiModelProperty(
    required = false,
    value = "whether tags for entitis shall be set")
  public boolean            setTags  = false;
  
  @ApiModelProperty(
    required = false,
    value = "whether flags for entitis shall be set")
  public boolean            setFlags  = false;
  
  public SSEntitiesAccessibleGetRESTPar(){/* Do nothing because of only JSON Jackson needs this */ }
}
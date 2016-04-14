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

import io.swagger.annotations.*;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.*;

@ApiModel
public class SSQueryResultPage {

  @ApiModelProperty
  public String     pagesID     = null;
  
  @ApiModelProperty
  public int        pageNumber  = 0;
  
  @ApiModelProperty
  public int        pagesCount  = 0;
  
  @ApiModelProperty
  public List<SSEntity> entities    = new ArrayList<>();
  
   public SSQueryResultPage(
    final List<SSEntity> entities){
    
    this.pagesID     = "1";
    this.pageNumber  = 1;
    this.pagesCount  = 1;
    
    SSEntity.addEntitiesDistinctWithoutNull(this.entities, entities);
  }
   
  public SSQueryResultPage(){/* Do nothing because of only JSON Jackson needs this */ }
   
  public SSQueryResultPage(
    final String         pagesID,
    final int            pageNumber,
    final int            pagesCount,
    final List<SSEntity> entities){
    
    this.pagesID     = pagesID;
    this.pageNumber  = pageNumber;
    this.pagesCount  = pagesCount;
    
    SSEntity.addEntitiesDistinctWithoutNull(this.entities, entities);
  }
}
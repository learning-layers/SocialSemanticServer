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

package at.tugraz.sss.servs.flag.datatype;

import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.conf.SSConf;
import at.tugraz.sss.servs.flag.datatype.*;
import io.swagger.annotations.*;
import java.util.List;

@ApiModel
public class SSFlagsGetRESTPar{
  
  @ApiModelProperty( 
    value = "entities", 
    required = false )
  public List<SSUri>   entities       = null;
  
  
  public void setEntities(final List<String> entities) throws SSErr{
    this.entities = SSUri.get(entities, SSConf.sssUri);
  }
  
  @ApiModelProperty(
    value = "types", 
    required = false )
  public List<SSFlagE> types          = null;
  
  
  public void setTypes(final List<String> types) throws SSErr{
    this.types = SSFlagE.get(types);
  }
  
  
  @ApiModelProperty( 
    value = "startTime", 
    required = false )
  public Long          startTime      = null;
  
  
  @ApiModelProperty( 
    value = "endTime", 
    required = false )
  public Long          endTime        = null;
  
  public SSFlagsGetRESTPar(){/* Do nothing because of only JSON Jackson needs this */ }
}
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
package at.tugraz.sss.adapter.rest.v3.recomm;

import at.tugraz.sss.serv.conf.SSConf;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.enums.*;
import io.swagger.annotations.*;
import java.util.List;

@ApiModel
public class SSRecommResourcesRESTPar{
  
  @ApiModelProperty(
    required = false,
    value = "realm the user wants to query")
  public String         realm    = null;
  
  @ApiModelProperty( 
    required = false, 
    value = "user to be considered to retrieve recommendations for")
  public SSUri         forUser    = null;
  
  
  public void setForUser(final String forUser) throws SSErr{
    this.forUser = SSUri.get(forUser, SSConf.sssUri);
  }
  
  @ApiModelProperty( 
    required = false, 
    value = "resource to be considered to retrieve recommendations for")
  public SSUri         entity     = null;
  
  
  public void setEntity(final String entity) throws SSErr{
    this.entity = SSUri.get(entity, SSConf.sssUri);
  }
  
  @ApiModelProperty( 
    required = false, 
    value = "additional information to be taken into account")
  public List<String>  categories = null;
  
  
  @ApiModelProperty( 
    required = false, 
    value = "number of resources to be returned")
  public Integer       maxResources    = 10;
    
  
  @ApiModelProperty( 
    required = false, 
    value = "whether circle types (i.e. priv, group, pub) for recommended entities shall be set")
  public boolean       setCircleTypes    = false;
  
  @ApiModelProperty( 
    required = false, 
    value = "entity types to be recommended")
  public List<SSEntityE> typesToRecommOnly = null;

  
  public void setTypesToRecommOnly(final List<String> typesToRecommOnly) throws SSErr{
    this.typesToRecommOnly = SSEntityE.get(typesToRecommOnly);
  }
  
  
  @ApiModelProperty( 
    required = false, 
    value = "whether own entities should be included in the result")
  public boolean includeOwn = true;
    
  public SSRecommResourcesRESTPar(){/* Do nothing because of only JSON Jackson needs this */ }
}

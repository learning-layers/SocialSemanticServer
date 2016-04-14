/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2014, Graz University of Technology - KTI (Knowledge Technologies Institute).
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

package at.tugraz.sss.servs.location.datatype;

import at.tugraz.sss.servs.entity.datatype.SSEntityE;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.entity.datatype.SSEntity;
import io.swagger.annotations.*;

@ApiModel
public class SSLocation extends SSEntity{

  @ApiModelProperty
  public Double latitude    = null;
  
  @ApiModelProperty
  public Double longitude   = null;
  
  @ApiModelProperty
  public Float  accuracy    = null;
  
  public static SSLocation get(
    final SSLocation  location,
    final SSEntity    entity) throws SSErr{
    
    return new SSLocation(location, entity);
  }
  
  public static SSLocation get(
    final SSUri  id, 
    final Double latitude,
    final Double longitude,
    final Float  accuracy) throws SSErr{
    
    return new SSLocation(id, latitude, longitude, accuracy);
  }
  
  public SSLocation(){/* Do nothing because of only JSON Jackson needs this */ }
  
  protected SSLocation(
    final SSLocation  location,
    final SSEntity    entity) throws SSErr{
    
    super(location, entity);
    
    this.latitude  = location.latitude;
    this.longitude = location.longitude;
    this.accuracy  = location.accuracy;
  }
  
  protected SSLocation(
    final SSUri  id, 
    final Double latitude,
    final Double longitude,
    final Float  accuracy) throws SSErr{
    
    super(id, SSEntityE.location);
    
    this.latitude  = latitude;
    this.longitude = longitude;
    this.accuracy  = accuracy;
  }
}

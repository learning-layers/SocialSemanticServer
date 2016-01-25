/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2014, Graz University of Technology - KTI (Knowledge Technologies Institute).
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
package at.tugraz.sss.servs.location.datatype;

import at.tugraz.sss.serv.datatype.SSEntity;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.datatype.*;
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
    final SSEntity    entity) throws Exception{
    
    return new SSLocation(location, entity);
  }
  
  public static SSLocation get(
    final SSUri  id, 
    final Double latitude,
    final Double longitude,
    final Float  accuracy) throws Exception{
    
    return new SSLocation(id, latitude, longitude, accuracy);
  }
  
  public SSLocation(){}
  
  protected SSLocation(
    final SSLocation  location,
    final SSEntity    entity) throws Exception{
    
    super(location, entity);
    
    this.latitude  = location.latitude;
    this.longitude = location.longitude;
    this.accuracy  = location.accuracy;
  }
  
  protected SSLocation(
    final SSUri  id, 
    final Double latitude,
    final Double longitude,
    final Float  accuracy) throws Exception{
    
    super(id, SSEntityE.location);
    
    this.latitude  = latitude;
    this.longitude = longitude;
    this.accuracy  = accuracy;
  }
}

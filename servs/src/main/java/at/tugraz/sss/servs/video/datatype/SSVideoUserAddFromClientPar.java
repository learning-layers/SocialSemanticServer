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

package at.tugraz.sss.servs.video.datatype;

import at.tugraz.sss.servs.entity.datatype.SSServPar;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.entity.datatype.SSLabel;
import at.tugraz.sss.servs.entity.datatype.SSTextComment;

public class SSVideoUserAddFromClientPar extends SSVideoUserAddPar{
  
  public Double                latitude         = null;
  public Double                longitude        = null;
  public Float                 accuracy         = null;

  public SSVideoUserAddFromClientPar(){/* Do nothing because of only JSON Jackson needs this */ }
  
  public SSVideoUserAddFromClientPar(
    final SSServPar servPar,
    final String         uuid,
    final SSUri          link,
    final SSVideoE       type, 
    final SSUri          forEntity,
    final String         genre,
    final SSLabel        label,
    final SSTextComment  description,
    final Long           creationTime,
    final Double         latitude,
    final Double         longitude,
    final Float          accuracy){
    
    super(
      servPar,
      null,
      uuid,
      link,
      type,
      forEntity,
      genre,
      label,
      description,
      creationTime,
      null, //file
      true,
      true);
    
    this.latitude  = latitude;
    this.longitude = longitude;
    this.accuracy  = accuracy;
  }
}
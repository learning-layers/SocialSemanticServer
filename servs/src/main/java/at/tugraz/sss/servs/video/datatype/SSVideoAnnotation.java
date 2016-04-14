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

package at.tugraz.sss.servs.video.datatype;

import at.tugraz.sss.servs.entity.datatype.SSEntityE;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.entity.datatype.SSEntity;
import io.swagger.annotations.*;

@ApiModel
public class SSVideoAnnotation extends SSEntity{

  @ApiModelProperty
  public Long  timePoint = null;
  
  @ApiModelProperty
  public Float x         = null;
  
  @ApiModelProperty
  public Float y         = null;
  
  public static SSVideoAnnotation get(
    final SSVideoAnnotation     annotation,
    final SSEntity              entity) throws SSErr{
    
    return new SSVideoAnnotation(annotation, entity);
  }
  
  public static SSVideoAnnotation get(
    final SSUri           id,
    final Long            timePoint,
    final Float           x, 
    final Float           y) throws SSErr{
    
    return new SSVideoAnnotation(
      id,
      timePoint,
      x,
      y);
  }
  
  public SSVideoAnnotation(){/* Do nothing because of only JSON Jackson needs this */ }
  
  protected SSVideoAnnotation(
    final SSVideoAnnotation   annotation,
    final SSEntity            entity) throws SSErr{
    
    super(annotation, entity);
    
    this.timePoint       = annotation.timePoint;
    this.x               = annotation.x;
    this.y               = annotation.y;
  }
  
  protected SSVideoAnnotation(
    final SSUri           id,
    final Long            timePoint,
    final Float           x, 
    final Float           y) throws SSErr{
    
    super(id, SSEntityE.videoAnnotation);
    
    this.timePoint       = timePoint;
    this.x               = x;
    this.y               = y;
  }
}
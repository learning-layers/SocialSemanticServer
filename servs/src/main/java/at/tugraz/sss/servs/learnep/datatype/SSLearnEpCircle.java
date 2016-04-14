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

package at.tugraz.sss.servs.learnep.datatype;

import at.tugraz.sss.servs.entity.datatype.SSEntityE;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.entity.datatype.SSLabel;
import at.tugraz.sss.servs.entity.datatype.SSEntity;
import at.tugraz.sss.servs.entity.datatype.SSTextComment;
import io.swagger.annotations.*;

@ApiModel
public class SSLearnEpCircle extends SSEntity{

  @ApiModelProperty
  public Float         xLabel           = null;
  
  @ApiModelProperty
  public Float         yLabel           = null;
  
  @ApiModelProperty
  public Float         xR               = null;
  
  @ApiModelProperty
  public Float         yR               = null;
  
  @ApiModelProperty
  public Float         xC               = null;
  
  @ApiModelProperty
  public Float         yC               = null;
  
  public static SSLearnEpCircle get(
    final SSLearnEpCircle learnEpCircle, 
    final SSEntity        entity) throws SSErr{
    
    return new SSLearnEpCircle(learnEpCircle, entity);
  }
  
  public static SSLearnEpCircle get(
    final SSUri                  id, 
    final SSLabel                label, 
    final SSTextComment          description, 
    final Long                   creationTime, 
    final SSEntity               author,
    final Float                  xLabel, 
    final Float                  yLabel, 
    final Float                  xR, 
    final Float                  yR, 
    final Float                  xC, 
    final Float                  yC) throws SSErr{
    
    return new SSLearnEpCircle(
      id,
      label,
      description,
      creationTime,
      author,
      xLabel, 
      yLabel, 
      xR, 
      yR, 
      xC, 
      yC);
  }
  
  public SSLearnEpCircle(){/* Do nothing because of only JSON Jackson needs this */ }
  
  protected SSLearnEpCircle(
    final SSLearnEpCircle learnEpCircle,
    final SSEntity        entity) throws SSErr{
    
    super(learnEpCircle, entity);
    
    if(learnEpCircle.xLabel != null){
      this.xLabel             = learnEpCircle.xLabel;
    }else{
      
      if(entity instanceof SSLearnEpCircle){
        this.xLabel = ((SSLearnEpCircle) entity).xLabel;
      }
    }
    
    if(learnEpCircle.yLabel != null){
      this.yLabel            = learnEpCircle.yLabel;
    }else{
      
      if(entity instanceof SSLearnEpCircle){
        this.yLabel = ((SSLearnEpCircle) entity).yLabel;
      }
    }
    
    if(learnEpCircle.xR != null){
      this.xR            = learnEpCircle.xR;
    }else{
      
      if(entity instanceof SSLearnEpCircle){
        this.xR = ((SSLearnEpCircle) entity).xR;
      }
    }
    
    if(learnEpCircle.yR != null){
      this.yR            = learnEpCircle.yR;
    }else{
      
      if(entity instanceof SSLearnEpCircle){
        this.yR = ((SSLearnEpCircle) entity).yR;
      }
    }
    
    if(learnEpCircle.xC != null){
      this.xC            = learnEpCircle.xC;
    }else{
      
      if(entity instanceof SSLearnEpCircle){
        this.xC = ((SSLearnEpCircle) entity).xC;
      }
    }
    
    if(learnEpCircle.yC != null){
      this.yC            = learnEpCircle.yC;
    }else{
      
      if(entity instanceof SSLearnEpCircle){
        this.yC = ((SSLearnEpCircle) entity).yC;
      }
    }
  }
  
  protected SSLearnEpCircle(
    final SSUri         id, 
    final SSLabel       label,
    final SSTextComment description,
    final Long          creationTime,
    final SSEntity      author,
    final Float         xLabel, 
    final Float         yLabel, 
    final Float         xR, 
    final Float         yR, 
    final Float         xC, 
    final Float         yC) throws SSErr{
    
    super(
      id, 
      SSEntityE.learnEpCircle,
      label, 
      description, 
      creationTime, 
      author);
    
    this.xLabel              = xLabel;
    this.yLabel              = yLabel;
    this.xR                  = xR;
    this.yR                  = yR;
    this.xC                  = xC;
    this.yC                  = yC;
  }
}